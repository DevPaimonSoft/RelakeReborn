package su.relake.client.impl.mods.combat;

import lombok.Getter;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import su.relake.client.api.context.implement.MotionEvent;
import su.relake.client.api.context.implement.UpdateEvent;
import su.relake.client.api.mod.*;
import su.relake.client.api.mod.*;
import su.relake.client.api.utils.AuraUtil;
import su.relake.client.api.utils.Rotation;
import su.relake.client.api.utils.RotationComp;
import su.relake.client.api.utils.math.common.MouseUtil;
import su.relake.client.api.utils.math.timer.TimerUtil;
import su.relake.compiler.sdk.annotations.VMProtect;
import su.relake.compiler.sdk.enums.VMProtectType;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class KillAuraProcessor extends ModBase {

    @Getter
    private LivingEntity target;
    @Getter
    private Vec2 rotateVector = new Vec2(0, 0);
    @Getter
    private float lastYaw, lastPitch;
    private boolean lookingAtHitbox = false;
    private Vec3 lastHandledTargetPointVector;
    private Vec3 prevHandledTargetPointVector;
    private final TimerUtil attackTimer = new TimerUtil();
    private final TimerUtil cpsTimer = new TimerUtil();
    private int count = 0;

    private ModeSetting rotationModeImpl;
    private ModeSetting rangeModeImpl;
    private SliderSetting attackRange;
    private SliderSetting preRange;
    private SliderSetting fov;
    private MultiSelectSetting targets;
    private MultiSelectSetting options;
    private BooleanSetting cpsBypass;
    private BooleanSetting postMissSpamming;
    private BooleanSetting renderBoxPoint;


    @VMProtect(type = VMProtectType.VIRTUALIZATION)
    public void initialize() {
        this.setName("KillAura");
        this.setDescRU("Автоматически атакует ближайшие цели с настраиваемыми ротациями и радиусом.");
        this.setDescENG("Automatically attacks nearby targets with customizable rotations and range.");
        this.setModuleCategory("COMBAT");

        rotationModeImpl = new ModeSetting("Ротация головы")
                .setValue("None", "FunTime");
        rangeModeImpl = new ModeSetting("Мод радиуса атаки")
                .setValue("Сильный античит", "NCP или аналоги", "Античит AAC", "Античит Matrix", "Слабый античит", "Свои настройки");

        attackRange = new SliderSetting("Дистанция атаки")
                .setIncrement(0.1f)
                .range(2f, 6f)
                .setValue(4.2f)
                .setVisible(() -> rangeModeImpl.isSelected("Свои настройки"));

        preRange = new SliderSetting("Доп. дистанция")
                .setIncrement(0.1f)
                .range(0f, 3f)
                .setValue(1.0f)
                .setVisible(() -> rangeModeImpl.isSelected("Свои настройки"));

        fov = new SliderSetting("FOV")
                .setIncrement(1f)
                .range(30f, 180f)
                .setValue(90f);

        targets = new MultiSelectSetting("Таргеты")
                .setValue("Игроки", "Мобы", "Животные", "Невидимки", "Голые");

        options = new MultiSelectSetting("Опции")
                .setValue("РэйКаст", "Не бить через стены", "Коррекция движения", "Только криты", "Ломать щит");
        options.toggleOption("Коррекция движения");
        cpsBypass = new BooleanSetting("Обход CPS")
                .setValue(true);

        postMissSpamming = new BooleanSetting("Закликать после мисса")
                .setValue(false);

        renderBoxPoint = new BooleanSetting("Показать точку ротации")
                .setValue(true);

        this.registerComponent(rotationModeImpl, rangeModeImpl, attackRange, preRange, fov, targets, options, cpsBypass, postMissSpamming, renderBoxPoint);
    }

    @EventHandler

    public void onUpdate(UpdateEvent event) {
        updateTarget();
        if (target == null) {
            reset();
            return;
        }

        double[] ranges = getRanges();
        if (isValidTarget(target) && isInRange(target, ranges[2])) {
            if (!rotationModeImpl.isSelected("None")) {
                updateRotation(ranges[2]);
            }
            if (shouldAttack() && attackTimer.hasReached(100) && canPerformCrit() && isRotationValidForAttack(ranges[2])) {
                performAttack();
                attackTimer.reset();
            }
        } else {
            reset();
        }
    }


    private boolean isRotationValidForAttack(double maxRange) {
        if (lookingAtHitbox) {
            return true;
        }
        Vec3 targetPos = getTargetPoint(target);
        float[] targetRotation = calcAngle(targetPos);
        float yawDelta = Mth.wrapDegrees(targetRotation[0] - rotateVector.x);
        float pitchDelta = Mth.wrapDegrees(targetRotation[1] - rotateVector.y);
        float angleDeviation = (float) Math.sqrt(yawDelta * yawDelta + pitchDelta * pitchDelta);
        return angleDeviation <= fov.getValue() || MouseUtil.checkRtx(rotateVector.x, rotateVector.y, (float) maxRange, 0.5f);
    }

    @EventHandler

    public void onMotion(MotionEvent event) {
        if (target == null || rotationModeImpl.isSelected("None")) {
            return;
        }
        event.setYaw(rotateVector.x);
        event.setPitch(rotateVector.y);
        mc.player.yHeadRot = rotateVector.x;
        mc.player.yBodyRot = rotateVector.x;
    }
//
//    @Subscribe
//
//    public void onWorldRender(WorldRenderEvent event) {
//        if (!renderBoxPoint.getValue() || lastHandledTargetPointVector == null || target == null) {
//            return;
//        }
//
//        Vec3 renderPoint = prevHandledTargetPointVector == null ? lastHandledTargetPointVector : lerp(prevHandledTargetPointVector, lastHandledTargetPointVector, event.getPartialTicks());
//        float cooledPCWave = (float) (attackTimer.getTime() / 500.0);
//        cooledPCWave = (cooledPCWave > 0.5F ? 1.0F - cooledPCWave : cooledPCWave) * 2.0F;
//        final float scale = 0.05F + 0.025F * cooledPCWave;
//        final int colorOutline = 0xFF00FF00, colorFill = 0x8000FF00;
//        final AABB aabb = new AABB(renderPoint, renderPoint).inflate(scale / 2.0);
//    }


    private Vec3 lerp(Vec3 oldVec, Vec3 vec, float partialTicks) {
        return new Vec3(
                Mth.lerp(partialTicks, oldVec.x, vec.x),
                Mth.lerp(partialTicks, oldVec.y, vec.y),
                Mth.lerp(partialTicks, oldVec.z, vec.z)
        );
    }


    private double[] getRanges() {
        return switch (rangeModeImpl.getName()) {
            case "Сильный античит" -> new double[]{3.05, 0.4, 3.45};
            case "NCP или аналоги" -> new double[]{4.05, 2.7, 6.75};
            case "Античит AAC" -> new double[]{3.56, 1.15, 4.71};
            case "Античит Matrix" -> new double[]{3.3, 0.65, 3.95};
            case "Слабый античит" -> new double[]{6.0, 0.0, 6.0};
            case "Свои настройки" ->
                    new double[]{attackRange.getValue(), preRange.getValue(), attackRange.getValue() + preRange.getValue()};
            default -> new double[]{4.2, 1.0, 5.2};
        };
    }


    private void updateTarget() {
        List<LivingEntity> entities = new ArrayList<>();
        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity instanceof LivingEntity living && isValidTarget(living)) {
                entities.add(living);
            }
        }

        if (entities.isEmpty()) {
            target = null;
            return;
        }

        entities.sort(Comparator.comparingDouble(e -> mc.player.distanceTo(e)));
        target = entities.get(0);
    }


    private boolean isValidTarget(LivingEntity entity) {
        if (entity == null || entity == mc.player || !entity.isAlive() || entity.isInvulnerable()) {
            return false;
        }

        // if (Relake.getInstance().getFriendManager().isFriend(entity.getName().getString())) return false;

        double[] ranges = getRanges();
        if (mc.player.distanceTo(entity) > ranges[2]) {
            return false;
        }
        if (entity instanceof Player && !targets.isSelected("Игроки")) {
            return false;
        }
        if (entity instanceof net.minecraft.world.entity.monster.Monster && !targets.isSelected("Мобы")) {
            return false;
        }
        if (entity instanceof net.minecraft.world.entity.animal.Animal && !targets.isSelected("Животные")) {
            return false;
        }
        if (entity.isInvisible() && !targets.isSelected("Невидимки")) {
            return false;
        }
        if (entity instanceof Player && entity.getArmorValue() == 0 && !targets.isSelected("Голые")) {
            return false;
        }
        return !options.isSelected("Не бить через стены") || canSeeThroughWall(entity);
    }


    private boolean canSeeThroughWall(Entity entity) {
        HitResult result = mc.level.clip(new ClipContext(mc.player.getEyePosition(0.0F), entity.getEyePosition(0.0F), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mc.player));
        return result.getType() == HitResult.Type.MISS;
    }


    private boolean isInRange(Entity entity, double maxRange) {
        if (options.isSelected("РэйКаст")) {
            float[] rotation;
            float halfBox = (float) (entity.getBoundingBox().getXsize() / 2f);
            for (float x = -halfBox; x <= halfBox; x += 0.15f) {
                for (float z = -halfBox; z <= halfBox; z += 0.15f) {
                    for (float y = 0.05f; y <= entity.getBoundingBox().getYsize(); y += 0.25f) {
                        Vec3 point = new Vec3(entity.getX() + x, entity.getY() + y, entity.getZ() + z);
                        if (squaredDistanceFromEyes(point) > maxRange * maxRange) {
                            continue;
                        }
                        rotation = calcAngle(point);
                        if (MouseUtil.checkRtx(rotation[0], rotation[1], (float) maxRange, 0)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return squaredDistanceFromEyes(entity.getPosition(0F).add(0, entity.getEyeHeight(entity.getPose()), 0)) <= maxRange * maxRange;
    }


    private boolean shouldAttack() {
        if (cpsBypass.getValue() && !cpsTimer.hasReached(50)) {
            return false;
        }
        float attackStrength = mc.player.getAttackStrengthScale(1.0f);
        return attackStrength >= 1f;
    }


    private boolean canPerformCrit() {
        if (!options.isSelected("Только криты")) {
            return true;
        }
        return !mc.player.onGround() && mc.player.fallDistance > 0 && !mc.player.isAutoJumpEnabled() &&
                !mc.player.isInWater() && !mc.player.isInLava() && !mc.player.onClimbable() && mc.player.getDeltaMovement().y < 0;
    }


    private void performAttack() {
        if (target instanceof Player && options.isSelected("Ломать щит") && target.isBlocking()) {
            // TODO: Implement shield-breaking logic (e.g., switch to axe)
        }
        mc.player.setSprinting(false);
        mc.gameMode.attack(mc.player, target);
        mc.player.swing(InteractionHand.MAIN_HAND);
        mc.player.setSprinting(true);
        lookingAtHitbox = false;
        cpsTimer.reset();
        if (postMissSpamming.getValue() && !lookingAtHitbox) {
            mc.player.setSprinting(false);
            mc.gameMode.attack(mc.player, target);
            mc.player.swing(InteractionHand.MAIN_HAND);
            mc.player.setSprinting(true);
        }
        count = (count + 1) % 2; // Update count for FunTime rotation
    }


    private void updateRotation(double maxRange) {
        if (target == null || rotationModeImpl.isSelected("None")) {
            return;
        }

        Vec3 targetPos = getTargetPoint(target);
        double maxHeight = (AuraUtil.getStrictDistance(target) / attackRange.getValue());
        Vec3 vec = targetPos
                .add(0, Mth.clamp(mc.player.getEyePosition(0.0F).y - target.getY(), 0, maxHeight), 0)
                .subtract(mc.player.getEyePosition(0.0F))
                .normalize();

        float rawYaw = (float) Math.toDegrees(Math.atan2(-vec.x, vec.z));
        float rawPitch = (float) Mth.clamp(-Math.toDegrees(Math.atan2(vec.y, Math.hypot(vec.x, vec.z))), -90F, 90F);

        float speed = new SecureRandom().nextBoolean() ? randomLerp(0.3F, 0.4F) : randomLerp(0.5F, 0.6F);

        float cos = (float) Math.cos(System.currentTimeMillis() / 100D);
        float sin = (float) Math.sin(System.currentTimeMillis() / 100D);
        float yaw = (float) Math.ceil(randomLerp(6F, 12) * cos + (1F - cooldownFromLastSwing()) * (randomLerp(60, 90) * (count == 0 ? 1 : -1)));
        float pitch = (float) Math.ceil(randomLerp(6F, 12) * sin + (1F - cooldownFromLastSwing()) * (randomLerp(15, 45) * (count == 0 ? 1 : -1)));

        rotateVector = new Vec2(wrapLerp(speed, Mth.wrapDegrees(rotateVector.x), Mth.wrapDegrees(rawYaw + yaw)), wrapLerp(speed / 2F, rotateVector.y, Mth.clamp(rawPitch + pitch, -90F, 90F)));

        Rotation rotation = new Rotation(mc.player.yRot + (float) Math.ceil(Mth.wrapDegrees(rotateVector.x) - Mth.wrapDegrees(mc.player.yRot)), mc.player.xRot + (float) Math.ceil(Mth.wrapDegrees(rotateVector.y) - Mth.wrapDegrees(mc.player.xRot)));

        float fovValue = (float) AuraUtil.calculateFOVFromCamera(target);
        float baseFov = this.fov.getValue();

        boolean toFast = cooldownFromLastSwing() > 0.5F;
        if (Math.abs(fovValue) < baseFov) {
            RotationComp.update(rotation, toFast && rayTrace() ? new Random().nextFloat() : 3F, 10F, 3F, 3F, 1, 5, false);
        }

        lookingAtHitbox = MouseUtil.checkRtx(rotateVector.x, rotateVector.y, (float) maxRange, 0.5f);
        lastYaw = Mth.wrapDegrees(rotateVector.x - mc.player.yRot);
        lastPitch = Mth.wrapDegrees(rotateVector.y - mc.player.xRot);

        prevHandledTargetPointVector = lastHandledTargetPointVector;
        lastHandledTargetPointVector = targetPos;

        if (options.isSelected("Коррекция движения")) {
            mc.player.yHeadRot = rotateVector.x;
            mc.player.xRot = rotateVector.y;
        }
    }


    private Vec3 getTargetPoint(LivingEntity entity) {
        double lengthY = entity.getBoundingBox().getYsize();
        return entity.getPosition(0F).add(0, lengthY * 0.5, 0);
    }


    private float[] calcAngle(Vec3 to) {
        double diffX = to.x - mc.player.getEyePosition().x;
        double diffY = (to.y - mc.player.getEyePosition().y) * -1F;
        double diffZ = to.z - mc.player.getEyePosition().z;
        double dist = Mth.sqrt((float) (diffX * diffX + diffZ * diffZ));
        return new float[]{
                (float) Mth.wrapDegrees(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0),
                (float) Mth.wrapDegrees(Math.toDegrees(Math.atan2(diffY, dist)))
        };
    }


    private float squaredDistanceFromEyes(@NotNull Vec3 vec) {
        double d0 = vec.x - mc.player.getX();
        double d1 = vec.z - mc.player.getZ();
        double d2 = vec.y - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        return (float) (d0 * d0 + d1 * d1 + d2 * d2);
    }


    private void reset() {
        target = null;
        if (mc.player == null) return;
        rotateVector = new Vec2(mc.player.yRot, mc.player.xRot);
        lastHandledTargetPointVector = null;
        prevHandledTargetPointVector = null;
        attackTimer.reset();
        cpsTimer.reset();
        count = 0;
        RotationComp.getInstance().stopRotation();
        if (options.isSelected("Коррекция движения")) {
            mc.player.yHeadRot = Integer.MIN_VALUE;
            mc.player.xRot = Integer.MIN_VALUE;
        }
    }

    @Override

    public void onEnable() {
        super.onEnable();
        reset();
    }

    @Override

    public void onDisable() {
        super.onDisable();
        reset();
    }


    private static float randomLerp(float min, float max) {
        return min + (float) Math.random() * (max - min);
    }


    private static float wrapLerp(float speed, float from, float to) {
        float delta = Mth.wrapDegrees(to - from);
        return from + delta * speed;
    }


    private float cooldownFromLastSwing() {
        return mc.player.getAttackStrengthScale(1.0f);
    }


    private boolean rayTrace() {
        if (target == null) return false;
        HitResult result = mc.level.clip(new ClipContext(mc.player.getEyePosition(0.0F), target.getEyePosition(0.0F), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mc.player));
        return result.getType() == HitResult.Type.MISS;
    }
}
