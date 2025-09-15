package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;

public interface VillagerLikeModel {
    void hatVisible(boolean pHatVisible);

    void translateToArms(PoseStack pPoseStack);
}