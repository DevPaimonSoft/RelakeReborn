package su.relake.client.impl.mods.movement;

import com.google.common.eventbus.Subscribe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import su.relake.client.api.context.implement.MotionEvent;
import su.relake.client.api.mod.ModBase;
import su.relake.compiler.sdk.annotations.Compile;
import su.relake.compiler.sdk.annotations.VMProtect;
import su.relake.compiler.sdk.enums.VMProtectType;

public class SpeedModProcessor extends ModBase {

    boolean state;


    @Override

    @VMProtect(type = VMProtectType.VIRTUALIZATION)
    public void initialize() {
        this.setName("Speed");
        this.setDescRU("описание");
        this.setDescENG("description");
        this.setModuleCategory("Move");
    }

    @Compile
    @Subscribe
    public void onMotion(MotionEvent e) {
        BlockPos pos = new BlockPos((int) mc.player.getX(), (int) (mc.player.getY() - 1), (int) mc.player.getZ());
        if (mc.player.onGround() && !mc.player.input.jumping) {
            mc.player.connection.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, pos, Direction.UP));
            mc.player.connection.send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY));

            mc.player.setDeltaMovement(
                    mc.player.getMotionDirection().getStepX() * 0.35,
                    mc.player.getDeltaMovement().y * 0.34,
                    mc.player.getMotionDirection().getStepZ() * 0.35
            );
            state = true;
        } else if (state) {
            mc.player.connection.send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY));
            state = false;
        }
    }

}
