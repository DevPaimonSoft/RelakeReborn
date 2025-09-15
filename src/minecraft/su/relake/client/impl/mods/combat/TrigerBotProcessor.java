package su.relake.client.impl.mods.combat;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import su.relake.client.api.context.implement.UpdateEvent;
import su.relake.client.api.mod.BooleanSetting;
import su.relake.client.api.mod.ModBase;
import su.relake.compiler.sdk.annotations.Compile;
import su.relake.compiler.sdk.annotations.VMProtect;
import su.relake.compiler.sdk.enums.VMProtectType;


public class TrigerBotProcessor extends ModBase {
    BooleanSetting onlyCriticals = new BooleanSetting("Только Криты").setValue(false);


    @Compile
    @VMProtect(type = VMProtectType.VIRTUALIZATION)
    public void initialize() {
        this.setName("TriggerBot");
        this.setDescRU("описание");
        this.setDescENG("decription");
        this.setModuleCategory("Combat");
//        this.registerComponent(onlyCriticals);
    }

    @Compile
    @EventHandler
    @VMProtect(type = VMProtectType.MUTATION)
    public void onUpdate(UpdateEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null || Minecraft.getInstance().hitResult == null) {
            return;
        }

        HitResult hitResult = Minecraft.getInstance().hitResult;

        if (!(hitResult instanceof EntityHitResult traceResult)) {
            return;
        }

        Entity entity = traceResult.getEntity();

        if (onlyCriticals.getValue() && player.fallDistance < 0.5) {
            return;
        }

        if (player.getAttackStrengthScale(1f) >= (onlyCriticals.getValue() ? 0.899f : 1f)) {
            attackEntity(entity);
        }
    }

    @Compile
    private void attackEntity(Entity entity) {
        boolean SprintStop = false;
        LocalPlayer player = Minecraft.getInstance().player;

        if (onlyCriticals.getValue() && player.isSprinting()) {
            player.setSprinting(false);
            player.connection.send(new ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
            SprintStop = true;
        }

        player.attack(entity);
        Minecraft.getInstance().gameMode.attack(player, entity);
        player.swing(InteractionHand.MAIN_HAND);

        if (SprintStop) {
            player.setSprinting(true);
            player.connection.send(new ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
        }
    }
}
