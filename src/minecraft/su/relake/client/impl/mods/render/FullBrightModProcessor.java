package su.relake.client.impl.mods.render;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import su.relake.client.api.mod.ModBase;
import su.relake.compiler.sdk.annotations.Compile;
import su.relake.compiler.sdk.annotations.VMProtect;
import su.relake.compiler.sdk.enums.VMProtectType;

public class FullBrightModProcessor extends ModBase {
    @Override
    @Compile
    @VMProtect(type = VMProtectType.VIRTUALIZATION)
    public void initialize() {
        this.setName("FullBright");
        this.setDescRU("описание");
        this.setDescENG("decription");
        this.setModuleCategory("Render");
    }


    @Override
    @Compile
    public void onEnable() {
        Player player = mc.player;
        if (player != null) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        }
    }

    @Override
    @Compile
    public void onDisable() {
        Player player = mc.player;
        if (player != null) {
            player.removeEffect(MobEffects.NIGHT_VISION);
        }
    }
}
