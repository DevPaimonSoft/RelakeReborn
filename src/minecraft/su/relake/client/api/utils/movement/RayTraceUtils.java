package su.relake.client.api.utils.movement;


import lombok.experimental.UtilityClass;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import su.relake.client.api.utils.render.util.IMinecraft;


@UtilityClass
public class RayTraceUtils implements IMinecraft {

    public boolean rayTraceSingleEntity(float yaw, float pitch, double distance, Entity entity) {
        Vec3 eyeVec = mc.player.getEyePosition(1.0F);
        Vec3 lookVec = mc.player.calculateViewVector(pitch, yaw);
        Vec3 extendedVec = eyeVec.add(lookVec.scale(distance));

        AABB AABB = entity.getBoundingBox();

        return AABB.contains(eyeVec) || AABB.clip(eyeVec, extendedVec).isPresent();
    }

    public boolean isHitBoxNotVisible(final Vec3 vec3d) {
        final ClipContext rayTraceContext = new ClipContext(
                mc.player.getEyePosition(1F),
                vec3d,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                mc.player
        );
        final BlockHitResult blockHitResult = mc.level.clip(rayTraceContext);
        return blockHitResult.getType() == BlockHitResult.Type.MISS;
    }
}
