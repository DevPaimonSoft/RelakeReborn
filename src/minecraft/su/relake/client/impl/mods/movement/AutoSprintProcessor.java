package su.relake.client.impl.mods.movement;

import meteordevelopment.orbit.EventHandler;
import su.relake.client.api.context.implement.UpdateEvent;
import su.relake.client.api.mod.BooleanSetting;
import su.relake.client.api.mod.ModBase;
import su.relake.client.api.utils.movement.MoveUtils;
import su.relake.compiler.sdk.annotations.Compile;
import su.relake.compiler.sdk.annotations.VMProtect;
import su.relake.compiler.sdk.enums.VMProtectType;

public class AutoSprintProcessor extends ModBase {
    public BooleanSetting keepSprint = new BooleanSetting("KeepSprint").setValue(true);

    @Compile
    @VMProtect(type = VMProtectType.VIRTUALIZATION)
    public void initialize() {
        this.setName("AutoSprint");
        this.setDescRU("описание");
        this.setDescENG("description");
        this.setModuleCategory("Move");

        registerComponent(keepSprint);
    }

    @Compile
    @EventHandler
    @VMProtect(type = VMProtectType.MUTATION)
    public void onUpdate(UpdateEvent eventUpdate) {
        if (mc.player != null && mc.level != null) {
            mc.player.setSprinting(
                    mc.player.getFoodData().getFoodLevel() > 6
                            && !mc.player.horizontalCollision
                            && !(mc.player.input.forwardImpulse < 0)
                            && !mc.player.isShiftKeyDown()
                            && MoveUtils.isMoving()
            );
        }
    }
}
