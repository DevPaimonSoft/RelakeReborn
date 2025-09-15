package net.minecraft.client.resources.model;

import net.minecraft.resources.ResourceLocation;

public interface ResolvableModel {
    void resolveDependencies(ResolvableModel.Resolver pResolver);

        public interface Resolver {
        UnbakedModel resolve(ResourceLocation pModel);
    }
}