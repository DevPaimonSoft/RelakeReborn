package su.relake.client.api.utils.render.util;

import lombok.experimental.UtilityClass;

import java.awt.*;
import java.util.Random;

@UtilityClass
public class ColorUtil {
    private final Random RANDOM = new Random();

    public final int RED = getColor(255, 0, 0);
    public final int GREEN = getColor(0, 255, 0);
    public final int BLUE = getColor(0, 0, 255);
    public final int YELLOW = getColor(255, 255, 0);
    public final int AQUA = getColor(0, 255, 255);
    public final int PINK = getColor(255, 0, 255);
    public final int WHITE = getColor(255);
    public final int BLACK = getColor(0);
    public final int TRANSPARENT = getColor(0, 0);

    public int red(int c) {
        return (c >> 16) & 0xFF;
    }

    public int green(int c) {
        return (c >> 8) & 0xFF;
    }

    public int blue(int c) {
        return c & 0xFF;
    }

    public int alpha(int c) {
        return (c >> 24) & 0xFF;
    }

    public float redf(int c) {
        return red(c) / 255.0f;
    }

    public float greenf(int c) {
        return green(c) / 255.0f;
    }

    public float bluef(int c) {
        return blue(c) / 255.0f;
    }

    public float alphaf(int c) {
        return alpha(c) / 255.0f;
    }

    public int[] getRGBA(int c) {
        return new int[]{red(c), green(c), blue(c), alpha(c)};
    }

    public int[] getRGB(int c) {
        return new int[]{red(c), green(c), blue(c)};
    }

    public float[] getRGBAf(int c) {
        return new float[]{redf(c), greenf(c), bluef(c), alphaf(c)};
    }

    public float[] getRGBf(int c) {
        return new float[]{redf(c), greenf(c), bluef(c)};
    }

    public int getColor(float red, float green, float blue, float alpha) {
        return getColor(Math.round(red * 255), Math.round(green * 255), Math.round(blue * 255), Math.round(alpha * 255));
    }

    public int getColor(int red, int green, int blue, float alpha) {
        return getColor(red, green, blue, Math.round(alpha * 255));
    }

    public int getColor(float red, float green, float blue) {
        return getColor(red, green, blue, 1F);
    }

    public int getColor(int brightness, int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }

    public int getColor(int brightness, float alpha) {
        return getColor(brightness, Math.round(alpha * 255));
    }

    public int getColor(int brightness) {
        return getColor(brightness, brightness, brightness);
    }

    public int replAlpha(int color, float alpha) {
        return getColor(red(color), green(color), blue(color), alpha);
    }

    public int multAlpha(int color, float percent01) {
        return getColor(red(color), green(color), blue(color), alphaf(color) * percent01);
    }

    public int multDark(int color, float percent01) {
        final float percent = Mathf.clamp01(percent01);
        return getColor(
                redf(color) * percent,
                greenf(color) * percent,
                bluef(color) * percent,
                alphaf(color)
        );
    }

    public int multBright(int color, float percent01) {
        final float percent = 1F + Mathf.clamp01(percent01);
        return getColor(
                redf(color) * percent,
                greenf(color) * percent,
                bluef(color) * percent,
                alphaf(color)
        );
    }


    public int overCol(int color1, int color2, float percent01) {
        final float percent = Mathf.clamp01(percent01);
        return getColor(
                Mathf.lerp(red(color1), red(color2), percent),
                Mathf.lerp(green(color1), green(color2), percent),
                Mathf.lerp(blue(color1), blue(color2), percent),
                Mathf.lerp(alpha(color1), alpha(color2), percent)
        );
    }

    public int overCol(int color1, int color2) {
        return overCol(color1, color2, 0.5F);
    }

    public int randomColor() {
        return Color.HSBtoRGB(RANDOM.nextFloat(), 0.5F + (RANDOM.nextFloat() * 0.5F), 1F);
    }

    public int fade(int speed, int index, int first, int second) {
        int angle = (int) ((System.currentTimeMillis() / speed + index) % 360);
        angle = angle >= 180 ? 360 - angle : angle;
        return overCol(first, second, angle / 180f);
    }

    public int getColor(int red, int green, int blue, int alpha) {
        return computeColor(red, green, blue, alpha);
    }

    public int getColor(int red, int green, int blue) {
        return getColor(red, green, blue, 255);
    }

    public int hex(String hex) {
        if (hex == null || hex.isEmpty()) {
            throw new IllegalArgumentException("Hex color string cannot be null or empty");
        }

        hex = hex.replace("#", "");
        if (hex.length() == 6) {
            int red = Integer.parseInt(hex.substring(0, 2), 16);
            int green = Integer.parseInt(hex.substring(2, 4), 16);
            int blue = Integer.parseInt(hex.substring(4, 6), 16);
            return getColor(red, green, blue);
        } else if (hex.length() == 8) {
            int alpha = Integer.parseInt(hex.substring(0, 2), 16);
            int red = Integer.parseInt(hex.substring(2, 4), 16);
            int green = Integer.parseInt(hex.substring(4, 6), 16);
            int blue = Integer.parseInt(hex.substring(6, 8), 16);
            return getColor(red, green, blue, alpha);
        } else {
            return 0;
        }
    }

    public int[] genGradientForText(int color1, int color2, int length) {
        int[] gradient = new int[length];
        for (int i = 0; i < length; i++) {
            float percent = (float) i / (length - 1);
            gradient[i] = overCol(color1, color2, percent);
        }
        return gradient;
    }

    private int computeColor(int red, int green, int blue, int alpha) {
        return ((Mathf.clamp(0, 255, alpha) << 24) |
                (Mathf.clamp(0, 255, red) << 16) |
                (Mathf.clamp(0, 255, green) << 8) |
                Mathf.clamp(0, 255, blue));
    }

    public int[] extractRGBA(int color) {
        return new int[]{(color >> 16 & 0xFF), (color >> 8 & 0xFF), (color & 0xFF), (color >> 24 & 0xFF)};
    }

    public float[] extractRGBAf(int color) {
        return new float[]{(color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, (color >> 24 & 0xFF) / 255.0F};
    }
    public float alpha = -1;
    public int rgba(int r, int g, int b, int a) {
        a = (int) ((alpha == -1.0F || a == 0) ? (float) a : alpha);
        return a << 24 | r << 16 | g << 8 | b;
    }

    public int rgba(double r, double g, double b, double a) {
        return rgba((int) r, (int) g, (int) b, (int) a);
    }

    public int rgb(double r, double g, double b) {
        return rgba((int) r, (int) g, (int) b, 255);
    }

    public int rgb(int r, int g, int b) {
        return rgba(r, g, b, 255);
    }

}