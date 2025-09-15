package su.relake.client.api.utils.math.scale;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec2;

public class ScaleUtil {
    public static Vec2 getMouse(int mouseX, int mouseY) {
        return new Vec2((int)((double)mouseX * Minecraft.getInstance().getWindow().getGuiScale() / 2.0), (int)((double)mouseY * Minecraft.getInstance().getWindow().getGuiScale() / 2.0));
    }
}
