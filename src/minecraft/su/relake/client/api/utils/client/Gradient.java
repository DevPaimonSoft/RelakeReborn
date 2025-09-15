package su.relake.client.api.utils.client;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import su.relake.client.api.utils.render.util.IMinecraft;

import java.awt.*;

public class Gradient implements IMinecraft {

    public static MutableComponent gradient(String message, int colorStart, int colorEnd, int speed) {
        MutableComponent text = Component.empty();
        int length = message.length();
        long time = System.currentTimeMillis();

        for (int i = 0; i < length; i++) {
            float progress = (float) (Math.sin(time * speed / 1000.0 + i * 0.5) * 0.5 + 0.5);
            int red = (int) (getRed(colorStart) * (1 - progress) + getRed(colorEnd) * progress);
            int green = (int) (getGreen(colorStart) * (1 - progress) + getGreen(colorEnd) * progress);
            int blue = (int) (getBlue(colorStart) * (1 - progress) + getBlue(colorEnd) * progress);
            int color = new Color(red, green, blue).getRGB();
            text.append(Component.literal(String.valueOf(message.charAt(i)))
                    .setStyle(Style.EMPTY.withColor(net.minecraft.network.chat.TextColor.fromRgb(color))));
        }
        return text;
    }

    private static int getRed(int color) {
        return (color >> 16) & 0xFF;
    }

    private static int getGreen(int color) {
        return (color >> 8) & 0xFF;
    }

    private static int getBlue(int color) {
        return color & 0xFF;
    }
}
