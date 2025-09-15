package su.relake.client.api.utils.render.builders.impl;

import su.relake.client.api.utils.render.builders.AbstractBuilder;
import su.relake.client.api.utils.render.builders.states.QuadColor;
import su.relake.client.api.utils.render.builders.states.QuadRadius;
import su.relake.client.api.utils.render.builders.states.QuadSize;
import su.relake.client.api.utils.render.renderers.impl.BuiltSquircle;

public final class SquircleBuilder extends AbstractBuilder<BuiltSquircle> {

    private QuadSize size;
    private QuadRadius radius;
    private QuadColor color;
    private float smoothness;
    private float cornerSmoothness;
    private int colorModulator;

    public SquircleBuilder size(QuadSize size) {
        this.size = size;
        return this;
    }

    public SquircleBuilder radius(QuadRadius radius) {
        this.radius = radius;
        return this;
    }

    public SquircleBuilder color(QuadColor color) {
        this.color = color;
        return this;
    }

    public SquircleBuilder smoothness(float smoothness) {
        this.smoothness = smoothness;
        return this;
    }

    public SquircleBuilder cornerSmoothness(float cornerSmoothness) {
        this.cornerSmoothness = cornerSmoothness;
        return this;
    }

    public SquircleBuilder colorModulator(int colorModulator) {
        this.colorModulator = colorModulator;
        return this;
    }

    @Override
    protected BuiltSquircle _build() {
        return new BuiltSquircle(
                this.size,
                this.radius,
                this.color,
                this.smoothness,
                this.cornerSmoothness,
                this.colorModulator
        );
    }

    @Override
    protected void reset() {
        this.size = QuadSize.NONE;
        this.radius = QuadRadius.NO_ROUND;
        this.color = QuadColor.TRANSPARENT;
        this.smoothness = 1.0f;
        this.cornerSmoothness = 8.0f;
        this.colorModulator = -1;
    }
}
