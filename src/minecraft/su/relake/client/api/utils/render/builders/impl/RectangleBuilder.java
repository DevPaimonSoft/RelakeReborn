package su.relake.client.api.utils.render.builders.impl;

import su.relake.client.api.utils.render.builders.AbstractBuilder;
import su.relake.client.api.utils.render.builders.states.QuadColor;
import su.relake.client.api.utils.render.builders.states.QuadRadius;
import su.relake.client.api.utils.render.builders.states.QuadSize;
import su.relake.client.api.utils.render.renderers.impl.BuiltRectangle;

public final class RectangleBuilder extends AbstractBuilder<BuiltRectangle> {

    private QuadSize size;
    private QuadRadius radius;
    private QuadColor color;
    private float smoothness;

    public RectangleBuilder size(QuadSize size) {
        this.size = size;
        return this;
    }

    public RectangleBuilder radius(QuadRadius radius) {
        this.radius = radius;
        return this;
    }

    public RectangleBuilder color(QuadColor color) {
        this.color = color;
        return this;
    }

    public RectangleBuilder smoothness(float smoothness) {
        this.smoothness = smoothness;
        return this;
    }

    @Override
    protected BuiltRectangle _build() {
        return new BuiltRectangle(
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
        this.color = QuadColor.TRANSPARENT;
        this.smoothness = 1.0f;
    }

}