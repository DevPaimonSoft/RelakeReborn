package su.relake.client.api.utils.math.common;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3d;
import su.relake.client.api.utils.render.util.IMinecraft;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class MathUtils implements IMinecraft {

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static boolean isHoveredAndClicked(float mouseX, float mouseY, float x, float y, float width, float height, boolean isMousePressed) {
        return isHovered(mouseX, mouseY, x, y, width, height) && isMousePressed;
    }

    public static Vec2 rotationToVec(Vec3 vec) {
        Vec3 eyesPos = mc.player.getEyePosition(1.0f);
        double diffX = vec != null ? vec.x - eyesPos.x : 0;
        double diffY = vec != null ? vec.y - (mc.player.getY() + mc.player.getEyeHeight() + 0.5) : 0;
        double diffZ = vec != null ? vec.z - eyesPos.z : 0;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        yaw = mc.player.getYRot() + Mth.wrapDegrees(yaw - mc.player.getYRot());
        pitch = mc.player.getXRot() + Mth.wrapDegrees(pitch - mc.player.getXRot());
        pitch = Mth.clamp(pitch, -90.0f, 90.0f);

        return new Vec2(yaw, pitch);
    }

    public static Vec2 rotationToEntity(Entity target) {
        Vec3 vector3d = target.position().subtract(Minecraft.getInstance().player.position());
        double magnitude = Math.hypot(vector3d.x, vector3d.z);
        return new Vec2(
                (float) Math.toDegrees(Math.atan2(vector3d.z, vector3d.x)) - 90.0F,
                (float) (-Math.toDegrees(Math.atan2(vector3d.y, magnitude)))
        );
    }

    public static Vec2 rotationToVec(Vec2 rotationVector, Vec3 target) {
        double x = target.x - mc.player.getX();
        double y = target.y - mc.player.getEyePosition(1).y;
        double z = target.z - mc.player.getZ();
        double dst = Math.sqrt(x * x + z * z);
        float yaw = (float) Mth.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90);
        float pitch = (float) (-Math.toDegrees(Math.atan2(y, dst)));
        float yawDelta = Mth.wrapDegrees(yaw - rotationVector.x);
        float pitchDelta = pitch - rotationVector.y;

        if (abs(yawDelta) > 180)
            yawDelta -= signum(yawDelta) * 360;

        return new Vec2(yawDelta, pitchDelta);
    }

    public static double round(double num, double increment) {
        double v = (double) Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d0 = x1 - x2;
        double d1 = y1 - y2;
        double d2 = z1 - z2;
        return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        double x = x1 - x2;
        double y = y1 - y2;
        return Math.sqrt(x * x + y * y);
    }

    public static Vec3 interpolate(Vec3 end, Vec3 start, float multiple) {
        return new Vec3(
                interpolate(end.x, start.x, multiple),
                interpolate(end.y, start.y, multiple),
                interpolate(end.z, start.z, multiple)
        );
    }

    public static Vec3 fast(Vec3 end, Vec3 start, float multiple) {
        return new Vec3(
                fast((float) end.x, (float) start.x, multiple),
                fast((float) end.y, (float) start.y, multiple),
                fast((float) end.z, (float) start.z, multiple)
        );
    }

    public static int calc(int value) {
        Minecraft minecraft = Minecraft.getInstance();
        Window window = minecraft.getWindow();
        return (int) (value * window.getGuiScale() / 2);
    }

    public static Vector2i getMouse(int mouseX, int mouseY) {
        return new Vector2i((int) (mouseX * Minecraft.getInstance().getWindow().getGuiScale() / 2), (int) (mouseY * Minecraft.getInstance().getWindow().getGuiScale() / 2));
    }

    public static float lerp(float end, float start, float multiple) {
        return (float) (end + (start - end) * Mth.clamp(deltaTime() * multiple, 0, 1));
    }

    public static float lerp(int end, int start, double multiple) {
        return (float) (end + (start - end) * Mth.clamp(deltaTime() * multiple, 0, 1));
    }

    public static double lerp(double end, double start, double multiple) {
        return (end + (start - end) * Mth.clamp(deltaTime() * multiple, 0, 1));
    }

    public static double setMinimal(double val, double min) {
        return val < min ? min : val;
    }

    public static double setMaximal(double val, double max) {
        return val > max ? max : val;
    }

    public static float setMinimal(float val, float min) {
        return val < min ? min : val;
    }

    public static float setMaximal(float val, float max) {
        return val > max ? max : val;
    }

    private static final Random random = new Random();
    public static float random(float min, float max) {
        return (float)(Math.random() * (double)(max - min) + (double)min);
    }
    public static int clamp(final int num, final int min, final int max) {
        return (num < min) ? min : ((num > max) ? max : num);
    }
    public static Vector3d fast(Vector3d end, Vector3d start, float multiple) {
        return new Vector3d(
                fast((float) end.x(), (float) start.x(), multiple),
                fast((float) end.y(), (float) start.y(), multiple),
                fast((float) end.z(), (float) start.z(), multiple));
    }
    public static float valWave01(float value) {
        return (value > .5 ? 1 - value : value) * 2.F;
    }

    public static int getMiddle(int old, int newValue) {
        return (old + newValue) / 2;
    }

    public static float clamp(final float num, final float min, final float max) {
        return (num < min) ? min : num > max ? max : num;
    }

    public static double clamp(double min, double max, double n) {
        return Math.max(min, Math.min(max, n));
    }

    public static float limit(float current, float inputMin, float inputMax, float outputMin, float outputMax) {
        current = clamp(inputMin, inputMax, current);
        float distancePercentage = (current - inputMin) / (inputMax - inputMin);
        return lerp(outputMin, outputMax, distancePercentage);
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

    public static String getStringPercent(String text, final float percent10) {
        if (text == null || text.isEmpty()) return text;
        text = text.substring(0, (int) (MathUtils.clamp(Math.min(percent10, 1.F), 0, 1) * text.length()));
        return text;
    }

    public static float clamp01(float x) {
        return (float) clamp(0, 1, x);
    }

    public static double roundPROBLYA(float num, double increment) {
        double v = (double) Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double roundToIncrement(double num, double increment) {
        double v = (double) Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double round(double value, int increment) {
        double num = Math.pow(10, increment);
        return Math.round(value * num) / num;
    }

    public static float randomizeFloat(float min, float max) {
        return min + (max - min) * ThreadLocalRandom.current().nextFloat();
    }

    public static <T extends Number> T lerp(T input, T target, double step) {
        double start = input.doubleValue();
        double end = target.doubleValue();
        double result = start + step * (end - start);

        if (input instanceof Integer) {
            return (T) Integer.valueOf((int) Math.round(result));
        } else if (input instanceof Double) {
            return (T) Double.valueOf(result);
        } else if (input instanceof Float) {
            return (T) Float.valueOf((float) result);
        } else if (input instanceof Long) {
            return (T) Long.valueOf(Math.round(result));
        } else if (input instanceof Short) {
            return (T) Short.valueOf((short) Math.round(result));
        } else if (input instanceof Byte) {
            return (T) Byte.valueOf((byte) Math.round(result));
        } else {
            throw new IllegalArgumentException("Unsupported type: " + input.getClass().getSimpleName());
        }
    }

    public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
        return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }

    public static double getDifferenceOf(final float num1, final float num2) {
        return Math.abs(num2 - num1) > Math.abs(num1 - num2) ? Math.abs(num1 - num2) : Math.abs(num2 - num1);
    }

    public static double getDifferenceOf(final double num1, final double num2) {
        return Math.abs(num2 - num1) > Math.abs(num1 - num2) ? Math.abs(num1 - num2) : Math.abs(num2 - num1);
    }

    public static double getDifferenceOf(final int num1, final int num2) {
        return Math.abs(num2 - num1) > Math.abs(num1 - num2) ? Math.abs(num1 - num2) : Math.abs(num2 - num1);
    }

    public static double getRandomInRange(double max, double min) {
        return min + (max - min) * random.nextDouble();

    }

    public static int floor(final float value) {
        return Mth.floor(value);
    }

    public static float sin(final float value) {
        return Mth.sin(value);
    }

    public static float cos(final float value) {
        return Mth.cos(value);
    }

    public static float wrapAngle(float angle) {
        angle %= 360.0F;
        if (angle >= 180.0F)
            angle -= 360.0F;
        if (angle < -180.0F)
            angle += 360.0F;
        return angle;
    }


    public static float harp(float val, float current, float speed) {
        float emi = ((current - val) * (speed / 2)) > 0 ? Math.max((speed), Math.min(current - val, ((current - val) * (speed / 2)))) : Math.max(current - val, Math.min(-(speed / 2), ((current - val) * (speed / 2))));
        return val + emi;
    }

    private static Vector3d getVectorForRotation(float pitch, float yaw) {
        float f = Mth.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = Mth.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -Mth.cos(-pitch * 0.017453292F);
        float f3 = Mth.sin(-pitch * 0.017453292F);
        return new Vector3d((f1 * f2), f3, (f * f2));
    }

    public static double getDistanceAtVec3dToVec3d(Vector3d first, Vector3d second) {
        double dx = first.x - second.x;
        double dy = first.y - second.y;
        double dz = first.z - second.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private static Vector3d getLook(float yaw, float pitch) {
        return getVectorForRotation(pitch, yaw);
    }

    public static double easeInOutQuad(double x, int step) {
        return x < 0.5D ? 2.0D * x * x : 1.0D - Math.pow(-2.0D * x + 2.0D, (double) step) / 2.0D;
    }
    public static double easeInOutQuad(double value) {
        return value < .5D ? 2.D * value * value : 1.D - Math.pow(-2.D * value + 2.D, 2.D) / 2.D;
    }

    public static double easeInOutQuadWave(double value) {
        value = (value > .5D ? 1.D - value : value) * 2.D;
        value = value < .5D ? 2.D * value * value : 1.D - Math.pow(-2.D * value + 2.D, 2.D) / 2.D;
        value = value > 1.D ? 1.D : value < 0.D ? 0.D : value;
        return value;
    }

    public static double easeInOutExpo(double value) {
        return value < 0 ? 0 : value > 1 ? 1 : value < 0.5 ? Math.pow(2, 20 * value - 10) / 2 : (2 - Math.pow(2, -20 * value + 10)) / 2;
    }

    public static double easeInOutExpoWave(double value) {
        value = (value > .5D ? 1.D - value : value) * 2.D;
        return value < 0 ? 0 : value > 1 ? 1 : value < 0.5 ? Math.pow(2, 20 * value - 10) / 2 : (2 - Math.pow(2, -20 * value + 10)) / 2;
    }

    public static float easeInOutCubic(float x) {
        return x < 0.5 ? 4 * x * x * x : (float) (1 - Math.pow(-2 * x + 2, 3) / 2);
    }

    public static double solve01Value(double value, double allWard) {
        return value > allWard ? 1 - (allWard - value) : value;
    }

    public static double interpolate(double previous, double current, float partialTicks) {
        return current + (previous - current) * partialTicks;
    }

    public static float interpolate(float previous, float current, float partialTicks) {
        return current + (previous - current) * partialTicks;
    }

    public static Vector3d interpolate(Vector3d end, Vector3d start, float multiple) {
        return new Vector3d(
                interpolate(end.x(), start.x(), multiple),
                interpolate(end.y(), start.y(), multiple),
                interpolate(end.z(), start.z(), multiple)
        );
    }

    // ishovered
    public static boolean isInRegion(float mouseX, float mouseY, float x, float y, float width, float height) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public static int randomize(int max, int min) {
        return -min + (int)(Math.random() * (double)(max - -min + 1));
    }

    public static float fast(float end, float start, float multiple) {
        return (1 - Mth.clamp((float) (deltaTime() * multiple), 0, 1)) * end
                + Mth.clamp((float) (deltaTime() * multiple), 0, 1) * start;
    }

    public static double deltaTime() {
        return mc.getFps() > 0 ? (1.0000 / mc.getFps()) : 1;
    }

    public static boolean isHovered(float mouseX, float mouseY, float x, float y, float width, float height) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public static int randomInt(int min, int max) {
        if (min >= max) {
            System.out.println("Ну ты и дцп...");
            return -1;
        }

        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static Vector2f adjustWidthForAspectRatio(float width, float aspectWidth, float aspectHeight) {
        float aspectRatio = aspectWidth / aspectHeight;
        float height = width / aspectRatio;
        return new Vector2f(width, height);
    }


    public static void scaleFix(PoseStack matrixStack, float x, float y, float scale) {
        Minecraft mc = Minecraft.getInstance();
        double factor = mc.getWindow().getGuiScale();

        float s = (float) (scale * factor);

        matrixStack.translate(x / factor, y / factor, 0);
        matrixStack.scale(s, s, s);
        matrixStack.translate(-x / factor, -y / factor, 0);
    }

    public static float step(double value, double steps) {
        float roundedValue = (float) (Math.round(value / steps) * steps);
        return (float) (Math.round(roundedValue * 100.0) / 100.0);
    }
}
