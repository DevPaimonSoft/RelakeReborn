package su.relake.client.api.utils.render.builders.impl;

import su.relake.client.api.utils.render.builders.AbstractBuilder;
import su.relake.client.api.utils.render.builders.states.QuadColor;
import su.relake.client.api.utils.render.builders.states.QuadRadius;
import su.relake.client.api.utils.render.builders.states.QuadSize;
import su.relake.client.api.utils.render.renderers.impl.BuiltBorder;

public final class BorderBuilder extends AbstractBuilder<BuiltBorder> {

    private QuadSize size;
    private QuadRadius radius;
    private QuadColor color;
    private float thickness;
    private float internalSmoothness, externalSmoothness;

    public BorderBuilder size(QuadSize size) {
        this.size = size;
        return this;
    }

    public BorderBuilder radius(QuadRadius radius) {
        this.radius = radius;
        return this;
    }

    public BorderBuilder color(QuadColor color) {
        this.color = color;
        return this;
    }

    public BorderBuilder thickness(float thickness) {
        this.thickness = thickness;
        return this;
    }

    public BorderBuilder smoothness(float internalSmoothness, float externalSmoothness) {
        this.internalSmoothness = internalSmoothness;
        this.externalSmoothness = externalSmoothness;
        return this;
    }

    @Override
    protected BuiltBorder _build() {
        return new BuiltBorder(
                this.size,
                this.radius,
                this.color,
                this.thickness,
                this.internalSmoothness, this.externalSmoothness
        );
    }

    @Override
    protected void reset() {
        this.size = QuadSize.NONE;
        this.radius = QuadRadius.NO_ROUND;
        this.color = QuadColor.TRANSPARENT;
        this.thickness = 0.0f;
        this.internalSmoothness = 1.0f;
        this.externalSmoothness = 1.0f;
    }

}