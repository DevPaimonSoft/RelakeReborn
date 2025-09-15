package net.minecraft.client.model.geom.builders;

import net.minecraft.client.model.geom.PartPose;

@FunctionalInterface
public interface MeshTransformer {
    static MeshTransformer scaling(float pScale) {
        float f = 24.016F * (1.0F - pScale);
        return p_368477_ -> p_368477_.transformed(p_366355_ -> p_366355_.scaled(pScale).translated(0.0F, f, 0.0F));
    }

    MeshDefinition apply(MeshDefinition pMesh);
}