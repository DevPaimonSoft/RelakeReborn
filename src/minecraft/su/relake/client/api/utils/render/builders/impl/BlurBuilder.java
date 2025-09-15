package su.relake.client.api.utils.render.builders.impl;

import su.relake.client.api.utils.render.builders.AbstractBuilder;
import su.relake.client.api.utils.render.builders.states.QuadColor;
import su.relake.client.api.utils.render.builders.states.QuadRadius;
import su.relake.client.api.utils.render.builders.states.QuadSize;
import su.relake.client.api.utils.render.renderers.impl.BuiltBlur;

public final class BlurBuilder extends AbstractBuilder<BuiltBlur> {

    private QuadSize size;
    private QuadRadius radius;
    private QuadColor color;
    private float smoothness;

    public BlurBuilder size(QuadSize size) {
        this.size = size;
        return this;
    }

    public BlurBuilder radius(QuadRadius radius) {
        this.radius = radius;
        return this;
    }

    public BlurBuilder color(QuadColor color) {
        this.color = color;
        return this;
    }

    public BlurBuilder smoothness(float smoothness) {
        this.smoothness = smoothness;
        return this;
    }


    @Override

    protected BuiltBlur _build() {
        return new BuiltBlur(
                this.size,
                this.radius,
                this.color,
                this.smoothness
        );
    }

    @Override

    protected void reset() {
        this.size = QuadSize.NONE;
        this.radius = QuadRadius.NO_ROUND;
        this.color = QuadColor.WHITE;
        this.smoothness = 1.0f;
    }
}
