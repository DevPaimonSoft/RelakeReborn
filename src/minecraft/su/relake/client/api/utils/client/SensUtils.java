package su.relake.client.api.utils.client;


import su.relake.client.api.utils.render.util.IMinecraft;

public class SensUtils implements IMinecraft {

    public static float getSensitivity(float rot) {
        return getDeltaMouse(rot) * getGCDValue();
    }

    public static float getGCDValue() {
        return (float) (getGCD() * 0.15);
    }

    public static float getGCD() {
        float f1;
        return (f1 = (float) (mc.options.mouseWheelSensitivity().get() * 0.6 + 0.2)) * f1 * f1 * 8;
    }

    public static float getDeltaMouse(float delta) {
        return Math.round(delta / getGCDValue());
    }

    public static float applyMinimalThreshold(float delta, float threshold) {
        return Math.abs(delta) < threshold ? 0 : delta;
    }

}