package su.relake.client.api.utils.render.util;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;

public interface IMinecraft {
    Minecraft mc = Minecraft.getInstance();
    Window mw = mc.getWindow();
}