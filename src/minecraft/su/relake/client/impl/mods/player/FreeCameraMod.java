package su.relake.client.impl.mods.player;

import com.mojang.authlib.GameProfile;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import su.relake.client.api.context.implement.MotionEvent;
import su.relake.client.api.context.implement.PacketEvent;
import su.relake.client.api.context.implement.UpdateEvent;
import su.relake.client.api.mod.ModBase;
import su.relake.client.api.mod.SliderSetting;
import su.relake.compiler.sdk.annotations.Compile;
import su.relake.client.api.utils.BaseMinecraftInterface;
import su.relake.client.api.utils.movement.MoveUtils;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class FreeCameraMod extends ModBase {

    AbstractClientPlayer fakePlayer;

    SliderSetting speed;
    SliderSetting speedY;

    @Compile
    public void initialize() {
        this.setName("Free Camera");
        this.setDescRU("Включает звуки");
        this.setDescENG("Enable sounds");
        this.setModuleCategory("Player");

        speed = new SliderSetting("Speed").range(0.01f, 10.0f).setValue(0.5f);
        speedY = new SliderSetting("Speed").range(0.01f, 10.0f).setValue(0.5f);

        registerComponent(speed,
                speedY
        );
    }

    @EventHandler
    @Compile
    public void onMotion(MotionEvent e) {
        e.cancel();//нахуй с пляжа сука
//        if (mc.player == null || mc.level == null) {
//            switchState(false, true);
//
//            return;
//        }
//
//        if (mc.player.hurtTime > 0 /*&& disableOnDamage.getValue()*/) {
//            switchState(false, true);
//
//            return;
//        }
//
//        if (fakePlayer != null) {
//            e.setX(fakePlayer.getX());
//            e.setY(fakePlayer.getY());
//            e.setZ(fakePlayer.getZ());
//            e.setPitch(fakePlayer.getXRot());//new Vector2f(fakePlayer.rotationYaw, fakePlayer.rotationPitch))
//            e.setY(fakePlayer.getYRot());
//            e.setOnGround(fakePlayer.onGround());
//        }
    }

//    @EventHandler
//    public void onEventLivingUpdate(LivingUpdateEvent livingUpdateEvent) {
//        livingUpdateEvent.setClip(true);
//    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (BaseMinecraftInterface.mc.player == null || BaseMinecraftInterface.mc.level == null) {
            setToggled(false);

            return;
        }

        if (BaseMinecraftInterface.mc.player != null) {
            if (BaseMinecraftInterface.mc.player.getHealth() <= 0)
                setToggled(false);

            System.out.println("wa");

            MoveUtils.setMotion(speed.getValue(), 0);

            BaseMinecraftInterface.mc.player.getDeltaMovement().y = 0;

            if (BaseMinecraftInterface.mc.options.keyJump.isDown()) {
                BaseMinecraftInterface.mc.player.getDeltaMovement().y = speedY.getValue();
            }

            if (BaseMinecraftInterface.mc.options.keyShift.isDown()) {
                BaseMinecraftInterface.mc.player.getDeltaMovement().y = -speedY.getValue();
            }
        }
    }

    @EventHandler
    public void onPacket(PacketEvent e) {
        if (BaseMinecraftInterface.mc.player == null || BaseMinecraftInterface.mc.level == null) {
            setToggled(false);

            return;
        }

        if (fakePlayer != null) {
//            if (!bypass.getValue())
//                return;

            if (BaseMinecraftInterface.mc.player != null && BaseMinecraftInterface.mc.level != null) {
                if (e.getPacket() instanceof ServerboundPlayerActionPacket
                        || e.getPacket() instanceof ServerboundUseItemOnPacket
//                        || e.getPacket() instanceof CPlayerTryUseItemPacket
                        || e.getPacket() instanceof ClientboundAnimatePacket) {

                    e.cancel();
                }

                if (e.getPacket() instanceof ServerboundMovePlayerPacket.Rot eve) {
                    if (fakePlayer != null) {
                        fakePlayer.setYRot(eve.yRot);
                        fakePlayer.setXRot(eve.xRot);

                        fakePlayer.setPos(eve.x, eve.y, eve.z);
                    }

                    e.cancel();
                }

                if (e.getPacket() instanceof ClientboundRespawnPacket) {
                    setToggled(false);
                }
            }
        } else {
            setToggled(false);
        }
    }

    @Override
    public void onEnable() {
        if (BaseMinecraftInterface.mc.player != null) {
            fakePlayer = new AbstractClientPlayer(BaseMinecraftInterface.mc.level, new GameProfile(UUID.randomUUID(), BaseMinecraftInterface.mc.player.getGameProfile().getName()));
            fakePlayer.setDeltaMovement(BaseMinecraftInterface.mc.player.getDeltaMovement());
            fakePlayer.setPos(BaseMinecraftInterface.mc.player.position().x, BaseMinecraftInterface.mc.player.position().y, BaseMinecraftInterface.mc.player.position().z);
            fakePlayer.yHeadRot = BaseMinecraftInterface.mc.player.yHeadRot;
            fakePlayer.setXRot(BaseMinecraftInterface.mc.player.getXRot());
            fakePlayer.setYRot(BaseMinecraftInterface.mc.player.getYRot());
            fakePlayer.setYHeadRot(BaseMinecraftInterface.mc.player.yHeadRot);
//            fakePlayer.rotationYawOffset = mc.player.rotationYawOffset;
            fakePlayer.setOnGround(BaseMinecraftInterface.mc.player.onGround());
            fakePlayer.setInvisible(false);

            BaseMinecraftInterface.mc.level.addEntity(0x4884358, fakePlayer);
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (BaseMinecraftInterface.mc.player != null) {
            if (fakePlayer != null) {
                BaseMinecraftInterface.mc.player.setDeltaMovement(Vec3.ZERO);
                BaseMinecraftInterface.mc.player.setPos(fakePlayer.position().x, fakePlayer.position().y, fakePlayer.position().z);
                fakePlayer.setXRot(BaseMinecraftInterface.mc.player.getXRot());
                fakePlayer.setYRot(BaseMinecraftInterface.mc.player.getYRot());
                BaseMinecraftInterface.mc.player.setOnGround(fakePlayer.onGround());

                BaseMinecraftInterface.mc.level.removeEntity(fakePlayer.getId(), Entity.RemovalReason.DISCARDED);

                fakePlayer = null;

            }

        }
    }
}