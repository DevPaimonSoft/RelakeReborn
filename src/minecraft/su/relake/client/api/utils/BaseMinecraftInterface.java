package su.relake.client.api.utils;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;

public interface BaseMinecraftInterface {

    Minecraft mc = Minecraft.getInstance();
    Window mw = mc.getWindow();

}
