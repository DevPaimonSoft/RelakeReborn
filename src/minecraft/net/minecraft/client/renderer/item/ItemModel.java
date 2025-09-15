package net.minecraft.client.renderer.item;

import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public interface ItemModel {
    void update(
        ItemStackRenderState pRenderState,
        ItemStack pStack,
        ItemModelResolver pItemModelResolver,
        ItemDisplayContext pDisplayContext,
        @Nullable ClientLevel pLevel,
        @Nullable LivingEntity pEntity,
        int pSeed
    );

        public static record BakingContext(ModelBaker blockModelBaker, EntityModelSet entityModelSet, ItemModel missingItemModel) {
        public BakedModel bake(ResourceLocation pLocation) {
            return this.blockModelBaker().bake(pLocation, BlockModelRotation.X0_Y0);
        }
    }

        public interface Unbaked extends ResolvableModel {
        MapCodec<? extends ItemModel.Unbaked> type();

        ItemModel bake(ItemModel.BakingContext pContext);
    }
}