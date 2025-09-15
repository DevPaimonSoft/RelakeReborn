package su.relake.client.api.utils.math.common;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import su.relake.client.api.utils.render.util.IMinecraft;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

public class MouseUtil implements IMinecraft {

    public static boolean isHovered(double x, double y, double mouseX, double mouseY, double w, double h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    public static Vector2i getMouse(double mouseX, double mouseY) {
        return new Vector2i((int) (mouseX * Minecraft.getInstance().getWindow().getGuiScale() / 2), (int) (mouseY * Minecraft.getInstance().getWindow().getGuiScale() / 2));
    }

    public static Entity getMouseOver(Entity target,
                                      float yaw,
                                      float pitch,
                                      double distance) {
        HitResult objectMouseOver;
        Entity entity = mc.getCameraEntity();
        if (entity != null && mc.level != null) {

            objectMouseOver = null;
            boolean flag = distance > 3;

            Vec3 startVec = entity.getEyePosition(1);
            Vec3 directionVec = getVectorForRotation(pitch, yaw);
            Vec3 endVec = startVec.add(
                    directionVec.x * distance,
                    directionVec.y * distance,
                    directionVec.z * distance
            );

            AABB axisalignedbb = target.getBoundingBox().inflate(target.getPickRadius());

            EntityHitResult entityraytraceresult = rayTraceEntities(entity,
                    startVec,
                    endVec,
                    axisalignedbb,
                    (p_lambda$getMouseOver$0_0_) ->
                            !p_lambda$getMouseOver$0_0_.isSpectator()
                                    && p_lambda$getMouseOver$0_0_.canBeCollidedWith(), distance
            );

            if (entityraytraceresult != null) {

                if (flag && startVec.distanceTo(startVec) > distance) {

                    objectMouseOver = BlockHitResult.miss(startVec, null, new BlockPos(new Vec3i((int) startVec.x, (int) startVec.y, (int) startVec.z)));
                }
                if ((distance < distance || objectMouseOver == null)) {

                    objectMouseOver = entityraytraceresult;
                }
            }
            if (objectMouseOver == null) {

                return null;
            }
            if (objectMouseOver instanceof EntityHitResult obj) {

                return obj.getEntity();
            }
        }
        return null;
    }

    public static Vec3 getVectorForRotation(float pitch, float yaw) {
        float yawRadians = -yaw * ((float) Math.PI / 180) - (float) Math.PI;
        float pitchRadians = -pitch * ((float) Math.PI / 180);

        float cosYaw = Mth.cos(yawRadians);
        float sinYaw = Mth.sin(yawRadians);
        float cosPitch = -Mth.cos(pitchRadians);
        float sinPitch = Mth.sin(pitchRadians);

        return new Vec3(sinYaw * cosPitch, sinPitch, cosYaw * cosPitch);
    }

    public static EntityHitResult rayTraceEntities(Entity shooter,
                                                   Vec3 startVec,
                                                   Vec3 endVec,
                                                   AABB boundingBox,
                                                   Predicate<Entity> filter,
                                                   double distance) {
        Level world = shooter.level();
        double closestDistance = distance;
        Entity entity = null;
        Vec3 closestHitVec = null;

        for (Entity entity1 : world.getEntities(shooter, boundingBox, filter)) {

            AABB axisalignedbb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
            Optional<Vec3> optional = axisalignedbb.clip(startVec, endVec);

            if (axisalignedbb.contains(startVec)) {

                if (closestDistance >= 0.0D) {
                    entity = entity1;
                    closestHitVec = startVec;
                    closestDistance = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vector3d1 = optional.get();
                double d3 = startVec.distanceTo(optional.get());

                if (d3 < closestDistance || closestDistance == 0.0D) {

                    boolean flag1 = false;
                    if (!flag1) {

                        if (closestDistance == 0.0D) {
                            entity = entity1;
                            closestHitVec = vector3d1;
                        }
                    } else {

                        entity = entity1;
                        closestHitVec = vector3d1;
                        closestDistance = d3;
                    }

                }
            }
        }

        return entity == null ? null : new EntityHitResult(entity, closestHitVec);
    }

    public static Entity getRtxTarget(float yaw, float pitch, float distance, boolean ignoreWalls) {
        Entity targetedEntity = null;
        HitResult result = ignoreWalls ? null : rayTrace(distance, yaw, pitch);
        Vec3 vec3d = mc.player.getPosition(1F).add(0, mc.player.getEyeHeight(mc.player.getPose()), 0);
        double distancePow2 = Math.pow(distance, 2);
        if (result != null) distancePow2 = result.getLocation().distanceToSqr(vec3d);
        Vec3 vec3d2 = getRotationVector(pitch, yaw);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * distance, vec3d2.y * distance, vec3d2.z * distance);
        AABB box = mc.player.getBoundingBox().expandTowards(vec3d2.scale(distance)).inflate(1.0, 1.0, 1.0);
        EntityHitResult entityHitResult = raycast(mc.player, vec3d, vec3d3, box, (entity) -> !entity.isSpectator() && entity.isPickable(), distancePow2);
        if (entityHitResult != null) {
            Entity entity2 = entityHitResult.getEntity();
            Vec3 vec3d4 = entityHitResult.getLocation();
            double g = vec3d.distanceToSqr(vec3d4);
            if (g < distancePow2 || result == null) {
                if (entity2 instanceof LivingEntity) {
                    targetedEntity = entity2;
                    return targetedEntity;
                }
            }
        }
        return targetedEntity;
    }

    public static HitResult rayTrace(double dst, float yaw, float pitch) {
        Vec3 vec3d = mc.player.getEyePosition(Minecraft.getInstance().getFrameTimeNs());
        Vec3 vec3d2 = getRotationVector(pitch, yaw);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * dst, vec3d2.y * dst, vec3d2.z * dst);
        return mc.level.clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, mc.player));
    }

    private static @NotNull Vec3 getRotationVector(float yaw, float pitch) {
        return new Vec3(Mth.sin(-pitch * 0.017453292F) * Mth.cos(yaw * 0.017453292F), -Mth.sin(yaw * 0.017453292F), Mth.cos(-pitch * 0.017453292F) * Mth.cos(yaw * 0.017453292F));
    }

    public static boolean checkRtx(float yaw, float pitch, float distance, float wallDistance) {
        Entity targetedEntity;
        HitResult result = rayTrace(distance, yaw, pitch);
        Vec3 eyePos = mc.player.getPosition(1F).add(0, mc.player.getEyeHeight(mc.player.getPose()), 0);
        double distancePow2 = Math.pow(distance, 2);
        distancePow2 = eyePos.distanceToSqr(result.getLocation());
        Vec3 vec3d2 = getRotationVector(pitch, yaw);
        Vec3 vec3d3 = eyePos.add(vec3d2.x * distance, vec3d2.y * distance, vec3d2.z * distance);
        AABB box = mc.player.getBoundingBox().expandTowards(vec3d2.scale(distance)).inflate(1.0, 1.0, 1.0);
        EntityHitResult entityHitResult = raycast(mc.player, eyePos, vec3d3, box, (entity) -> !entity.isSpectator() && entity.isPickable(), distancePow2);
        if (entityHitResult != null) {
            if (entityHitResult.getEntity() instanceof FireworkRocketEntity)
                return false;
            Entity entity2 = entityHitResult.getEntity();
            if (eyePos.distanceToSqr(entityHitResult.getLocation()) <= Math.pow(wallDistance, 2) || eyePos.distanceToSqr(entityHitResult.getLocation()) < distancePow2) {
                targetedEntity = entity2;
          //      return targetedEntity == Relake.getInstance().getModuleInstance().getModule(KillAura.class).getTarget() || Relake.getInstance().getModuleInstance().getModule(KillAura.class).getTarget() == null;
            }
        }
        return false;
    }

    public static boolean checkRtx2(float yaw, float pitch, float distance, float wallDistance) {
        HitResult result = rayTrace(distance, yaw, pitch);
        Vec3 startPoint = mc.player.getPosition(1F).add(0, mc.player.getEyeHeight(mc.player.getPose()), 0);
        double distancePow2 = Math.pow(distance, 2);

        if (result != null)
            distancePow2 = startPoint.distanceToSqr(result.getLocation());

        Vec3 rotationVector = getRotationVector(pitch, yaw).scale(distance);
        Vec3 endPoint = startPoint.add(rotationVector);

        AABB entityArea = mc.player.getBoundingBox().expandTowards(rotationVector).inflate(1.0, 1.0, 1.0);

        EntityHitResult ehr;

        double maxDistance = Math.max(distancePow2, Math.pow(wallDistance, 2));

//        if (Relake.getInstance().getModuleInstance().getModule(KillAura.class).getTarget() != null)
//            ehr = ProjectileUtil.getEntityHitResult(mc.player, startPoint, endPoint, entityArea, e -> !e.isSpectator() && e.isPickable() && e == Relake.getInstance().getModuleInstance().getModule(KillAura.class).getTarget(), maxDistance);
//        else
//            ehr = ProjectileUtil.getEntityHitResult(mc.player, startPoint, endPoint, entityArea, e -> !e.isSpectator() && e.isPickable(), maxDistance);
//
//        if (ehr != null) {
//            boolean allowedWallDistance = startPoint.distanceToSqr(ehr.getLocation()) <= Math.pow(wallDistance, 2);
//            boolean wallMissing = result == null;
//            boolean wallBehindEntity = startPoint.distanceToSqr(ehr.getLocation()) < distancePow2;
//            boolean allowWallHit = wallMissing || allowedWallDistance || wallBehindEntity;
//
//            if (allowWallHit && startPoint.distanceToSqr(ehr.getLocation()) <= Math.pow(distance, 2))
//                return ehr.getEntity() == Relake.getInstance().getModuleInstance().getModule(KillAura.class).getTarget() || Relake.getInstance().getModuleInstance().getModule(KillAura.class).getTarget() == null;
//        }

        return false;
    }

    @Nullable
    public static EntityHitResult raycast(Entity entity, Vec3 min, Vec3 max, AABB box, Predicate<Entity> predicate, double maxDistance) {
        Level world = entity.level();
        double d = maxDistance;
        Entity entity2 = null;
        Vec3 vec3d = null;
        Iterator var12 = world.getEntities(entity, box, predicate).iterator();

        while(true) {
            while(var12.hasNext()) {
                Entity entity3 = (Entity)var12.next();
                AABB box2 = entity3.getBoundingBox().inflate(0);

                Optional<Vec3> optional = box2.clip(min, max);
                if (box2.contains(min)) {
                    if (d >= 0.0) {
                        entity2 = entity3;
                        vec3d = (Vec3)optional.orElse(min);
                        d = 0.0;
                    }
                } else if (optional.isPresent()) {
                    Vec3 vec3d2 = (Vec3)optional.get();
                    double e = min.distanceToSqr(vec3d2);
                    if (e < d || d == 0.0) {
                        if (entity3.getRootVehicle() == entity.getRootVehicle()) {
                            if (d == 0.0) {
                                entity2 = entity3;
                                vec3d = vec3d2;
                            }
                        } else {
                            entity2 = entity3;
                            vec3d = vec3d2;
                            d = e;
                        }
                    }
                }
            }

            if (entity2 == null) {
                return null;
            }

            return new EntityHitResult(entity2, vec3d);
        }
    }

}
