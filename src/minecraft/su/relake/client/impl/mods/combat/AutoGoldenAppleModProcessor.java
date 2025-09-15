package su.relake.client.impl.mods.combat;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import su.relake.client.api.context.implement.UpdateEvent;
import su.relake.client.api.mod.ModBase;
import su.relake.compiler.sdk.annotations.VMProtect;
import su.relake.compiler.sdk.enums.VMProtectType;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutoGoldenAppleModProcessor extends ModBase {

    boolean active;




    @Override
    @VMProtect(type = VMProtectType.VIRTUALIZATION)
    public void initialize() {
        this.setName("Auto Golden Apple");
        this.setDescRU("описание");
        this.setDescENG("decription");
        this.setModuleCategory("Combat");

    }

    @EventHandler

    public void onUpdateContext(UpdateEvent updateContext) {
        if (mc.player.getOffhandItem().getItem() == Items.GOLDEN_APPLE && mc.player.getHealth() <= 16) {
            active = true;
            if (!mc.player.isUsingItem()) {
                mc.player.startUsingItem(InteractionHand.OFF_HAND);
                mc.gameMode.useItem(mc.player, InteractionHand.OFF_HAND);
                mc.options.keyUse.setDown(true);
                mc.player.startUsingItem(InteractionHand.OFF_HAND);
            }
        } else if (active && mc.player.isUsingItem()) {
            mc.gameMode.releaseUsingItem(mc.player);
            if (!(mc.mouseHandler.isRightPressed() && mc.screen == null)) mc.options.keyUse.setDown(false);
            active = false;
        }
    }


}
