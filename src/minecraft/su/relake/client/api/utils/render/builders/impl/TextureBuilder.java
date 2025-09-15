package su.relake.client.api.utils.render.builders.impl;

import net.minecraft.client.renderer.texture.AbstractTexture;
import su.relake.client.api.utils.render.builders.AbstractBuilder;
import su.relake.client.api.utils.render.builders.states.QuadColor;
import su.relake.client.api.utils.render.builders.states.QuadRadius;
import su.relake.client.api.utils.render.builders.states.QuadSize;
import su.relake.client.api.utils.render.renderers.impl.BuiltTexture;

public final class TextureBuilder extends AbstractBuilder<BuiltTexture> {

    private QuadSize size;
    private QuadRadius radius;
    private QuadColor color;
    private float smoothness;
    private float u, v;
    private float texWidth, texHeight;
    private int textureId;

    public TextureBuilder size(QuadSize size) {
        this.size = size;
        return this;
    }

    public TextureBuilder radius(QuadRadius radius) {
        this.radius = radius;
        return this;
    }

    public TextureBuilder color(QuadColor color) {
        this.color = color;
        return this;
    }

    public TextureBuilder smoothness(float smoothness) {
        this.smoothness = smoothness;
        return this;
    }

    public TextureBuilder texture(float u, float v, float texWidth, float texHeight, AbstractTexture texture) {
        return texture(u, v, texWidth, texHeight, texture.getId());
    }

    public TextureBuilder texture(float u, float v, float texWidth, float texHeight, int textureId) {
        this.u = u;
        this.v = v;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.textureId = textureId;
        return this;
    }

    @Override
    protected BuiltTexture _build() {
        return new BuiltTexture(
                this.size,
                this.radius,
                this.color,
                this.smoothness,
                this.u, this.v,
                this.texWidth, this.texHeight,
                this.textureId
        );
    }

    @Override
    protected void reset() {
        this.size = QuadSize.NONE;
        this.radius = QuadRadius.NO_ROUND;
        this.color = QuadColor.WHITE;
        this.smoothness = 1.0f;
        this.u = 0.0f;
        this.v = 0.0f;
        this.texWidth = 0.0f;
        this.texHeight = 0.0f;
        this.textureId = 0;
    }

}