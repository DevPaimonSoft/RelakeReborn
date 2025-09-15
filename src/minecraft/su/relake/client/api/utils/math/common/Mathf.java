package su.relake.client.api.utils.math.common;

import net.minecraft.util.Mth;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public class Mathf {


    public float clamp01(float x) {
        return (float) clamp(0, 1, x);
    }

    public static double getRandom(double min, double max) {
        if (min == max) {
            return min;
        } else if (min > max) {
            final double d = min;
            min = max;
            max = d;
        }
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public double round(double target, int decimal) {
        double p = Math.pow(10, decimal);
        return Math.round(target * p) / p;
    }

    public String formatTime(long millis) {
        long hours = millis / 3600000;
        long minutes = (millis % 3600000) / 60000;
        long seconds = ((millis % 360000) % 60000) / 1000;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public float slerp(float start, float end, float t) {
        t = Math.max(0.0f, Math.min(1.0f, t));
        float startRadians = (float) Math.toRadians(start);
        float endRadians = (float) Math.toRadians(end);

        float dotProduct = (float) Math.cos(startRadians) * (float) Math.cos(endRadians) +
                (float) Math.sin(startRadians) * (float) Math.sin(endRadians);

        float angle = (float) Math.acos(dotProduct);

        if (Math.abs(angle) < 0.001f) {
            return start;
        }

        float factorStart = (float) (Math.sin((1 - t) * angle) / Math.sin(angle));
        float factorEnd = (float) (Math.sin(t * angle) / Math.sin(angle));

        float interpolatedValue = start * factorStart + end * factorEnd;
        return (float) Mth.clamp(Mth.wrapDegrees(Math.toDegrees(interpolatedValue)), start, end);
    }

    public double round(final double value, final int scale, final double inc) {
        final double halfOfInc = inc / 2.0;
        final double floored = Math.floor(value / inc) * inc;

        if (value >= floored + halfOfInc) {
            return new BigDecimal(Math.ceil(value / inc) * inc)
                    .setScale(scale, RoundingMode.HALF_UP)
                    .doubleValue();
        } else {
            return new BigDecimal(floored)
                    .setScale(scale, RoundingMode.HALF_UP)
                    .doubleValue();
        }
    }

    public double step(final double value, final double steps) {
        double a = ((Math.round(value / steps)) * steps);
        a *= 1000;
        a = (int) a;
        a /= 1000;
        return a;
    }

    public double getDistance(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        final double deltaX = x2 - x1;
        final double deltaY = y2 - y1;
        final double deltaZ = z2 - z1;
        return Mth.sqrt((float) (deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ));
    }

    public static double clamp(double min, double max, double n) {
        return Math.max(min, Math.min(max, n));
    }

    public static float limit(float current, float inputMin, float inputMax, float outputMin, float outputMax) {
        current = (float) Mathf.clamp(inputMin, inputMax, current);
        float distancePercentage = (current - inputMin) / (inputMax - inputMin);
        return Interpolator.lerp(outputMin, outputMax, distancePercentage);
    }

    public static float lerp(float min2, float max, float delta) {
        return min2 + (max - min2) * delta;
    }

}
