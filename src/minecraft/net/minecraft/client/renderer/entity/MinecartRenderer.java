package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class MinecartRenderer extends AbstractMinecartRenderer<AbstractMinecart, MinecartRenderState> {
    public MinecartRenderer(EntityRendererProvider.Context pContext, ModelLayerLocation pLayer) {
        super(pContext, pLayer);
    }

    public MinecartRenderState createRenderState() {
        return new MinecartRenderState();
    }
}