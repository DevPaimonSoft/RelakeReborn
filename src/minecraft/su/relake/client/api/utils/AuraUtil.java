package su.relake.client.api.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class AuraUtil {
    public static double getStrictDistance(LivingEntity entity) {
        return Minecraft.getInstance().player.distanceTo(entity);
    }

    public static double calculateFOVFromCamera(Entity target) {
        Vec3 playerLook = Minecraft.getInstance().player.getViewVector(1.0F);
        Vec3 toTarget = target.getPosition(1.0F).subtract(Minecraft.getInstance().player.getEyePosition(1.0F)).normalize();
        double dot = playerLook.dot(toTarget);
        return Math.toDegrees(Math.acos(dot));
    }
}