package su.relake.client.api.utils.client.project;//package ru.kotopushka.client.api.utils.client.project;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.world.phys.Vec2;
//import net.minecraft.world.phys.Vec3;
//import org.joml.Quaternionf;
//import org.joml.Vector2f;
//import ru.kotopushka.relake.client.core.interfaces.IMinecraft;
//import ru.kotopushka.relake.client.core.utils.vector.floats.Vector3f;
//
//public class Projection implements IMinecraft {
//    public static Vector2f project(double x, double y, double z, float ticks) {
//        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
//        Quaternionf cameraRotation = new Quaternionf(mc.gameRenderer.getMainCamera().rotation().x(), mc.gameRenderer.getMainCamera().rotation().y(), mc.gameRenderer.getMainCamera().rotation().z(), mc.gameRenderer.getMainCamera().rotation().w());
//        cameraRotation.conjugate();
//
//        Vector3f result3f = new Vector3f((float) (cameraPos.x - x ), (float) (cameraPos.y - y), (float) (cameraPos.z - z));
//        result3f.transform(cameraRotation);
//
//        PoseStack poseStack = new PoseStack();
//
//        double fov = mc.gameRenderer.getFov(mc.gameRenderer.getMainCamera(), ticks, true);
//        poseStack.mulPose(mc.gameRenderer.getProjectionMatrix(fov));
//        return calculateScreenPosition(result3f, fov);
//    }
//
//    public static Vec2 project2(double x, double y, double z) {
//        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
//        Quaternionf cameraRotation = new Quaternionf(mc.gameRenderer.getMainCamera().rotation().x(), mc.gameRenderer.getMainCamera().rotation().y(), mc.gameRenderer.getMainCamera().rotation().z(), mc.gameRenderer.getMainCamera().rotation().w());
//        cameraRotation.conjugate();
//
//        Vector3f result3f = new Vector3f((float) (cameraPos.x - x ), (float) (cameraPos.y - y), (float) (cameraPos.z - z));
//        result3f.transform(cameraRotation);
//
//        PoseStack poseStack = new PoseStack();
//
//        double fov = mc.gameRenderer.getFov(mc.gameRenderer.getMainCamera(), mc.getFrameTimeNs(), true);
//        poseStack.mulPose(mc.gameRenderer.getProjectionMatrix(fov));
//        return calculateScreenPosition2(result3f, fov);
//    }
//
//    private static Vector2f calculateScreenPosition(Vector3f result3f, double fov) {
//        float halfHeight = mc.getWindow().getGuiScaledHeight() / 2.0F;
//        float scaleFactor = halfHeight / (result3f.z * (float) Math.tan(Math.toRadians(fov / 2.0F)));
//        if (result3f.z < 0.0F) {
//            return new Vector2f(-result3f.x * scaleFactor + mc.getWindow().getGuiScaledWidth() / 2.0F, mc.getWindow().getGuiScaledHeight() / 2.0F - result3f.y * scaleFactor);
//        }
//        return new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
//    }
//
//    private static Vec2 calculateScreenPosition2(Vector3f result3f, double fov) {
//        float halfHeight = mc.getWindow().getGuiScaledHeight() / 2.0F;
//        float scaleFactor = halfHeight / (result3f.z * (float) Math.tan(Math.toRadians(fov / 2.0F)));
//        if (result3f.z < 0.0F) {
//            return new Vec2(-result3f.x * scaleFactor + mc.getWindow().getGuiScaledWidth() / 2.0F, mc.getWindow().getGuiScaledHeight() / 2.0F - result3f.y * scaleFactor);
//        }
//        return new Vec2(Float.MAX_VALUE, Float.MAX_VALUE);
//    }
//}
//
