package su.relake.client.api.utils.render.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class Mathf {
    public double round(double value, int increment) {
        double multiplier = Math.pow(10, increment);
        return Math.round(value * multiplier) / multiplier;
    }

    public double step(double value, double stepSize) {
        if (stepSize <= 0) {
            throw new IllegalArgumentException("Step size must be positive");
        }
        long steps = Math.round(value / stepSize);
        return steps * stepSize;
    }

    public float step(float value, float stepSize) {
        if (stepSize <= 0) {
            throw new IllegalArgumentException("Step size must be positive");
        }
        int steps = Math.round(value / stepSize);
        return steps * stepSize;
    }

    public double clamp(double min, double max, double value) {
        return Math.max(min, Math.min(max, value));
    }

    public float clamp(float min, float max, float value) {
        return Math.max(min, Math.min(max, value));
    }

    public int clamp(int min, int max, int value) {
        return Math.max(min, Math.min(max, value));
    }

    public double clamp01(double value) {
        return clamp(0D, 1D, value);
    }

    public float clamp01(float value) {
        return clamp(0F, 1F, value);
    }

    public double clamp360(double value) {
        return clamp(0D, 360D, value);
    }

    public float clamp360(float value) {
        return clamp(0F, 360F, value);
    }

    private static void validateRange(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("Minimum value cannot be greater than maximum value.");
        }
    }

    public double random(double min, double max) {
        validateRange(min, max);
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public float random(float min, float max) {
        validateRange(min, max);
        return (float) ThreadLocalRandom.current().nextDouble(min, max);
    }

    public float deltaTime() {
        float debugFPS = Minecraft.getInstance().getFps();
        return debugFPS > 0 ? 1.0F / debugFPS : 1.0F;
    }

    public double calcDiff(double a, double b) {
        return Math.abs(a - b);
    }

    public double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        double deltaZ = z2 - z1;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }

    public double lerp(double input, double target, double step) {
        return input + step * (target - input);
    }

    public float lerp(float input, float target, double step) {
        return (float) (input + step * (target - input));
    }

    public int lerp(int input, int target, double step) {
        return (int) (input + step * (target - input));
    }
}