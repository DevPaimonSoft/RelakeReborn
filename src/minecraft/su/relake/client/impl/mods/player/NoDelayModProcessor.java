package su.relake.client.impl.mods.player;

import meteordevelopment.orbit.EventHandler;
import su.relake.client.api.context.implement.UpdateEvent;
import su.relake.client.api.mod.BooleanSetting;
import su.relake.client.api.mod.ModBase;
import su.relake.compiler.sdk.annotations.VMProtect;
import su.relake.compiler.sdk.enums.VMProtectType;

public class NoDelayModProcessor extends ModBase {

    BooleanSetting jump;

    @Override

    @VMProtect(type = VMProtectType.VIRTUALIZATION)
    public void initialize() {
        this.setName("No Delay");
        this.setDescRU("описание");
        this.setDescENG("decription");
        this.setModuleCategory("Player");

        jump = new BooleanSetting("Jump")
                .setValue(true);

        this.registerComponent(
                jump
        );
    }

    @EventHandler

    public void onUpdateContext(UpdateEvent updateContext) {
        if (jump.getValue()) mc.player.noJumpDelay = 0;
    }


}
