package su.relake.client.api.utils.render.builders.impl;

import su.relake.client.api.utils.render.builders.AbstractBuilder;
import su.relake.client.api.utils.render.builders.states.QuadColor;
import su.relake.client.api.utils.render.builders.states.QuadRadius;
import su.relake.client.api.utils.render.builders.states.QuadSize;
import su.relake.client.api.utils.render.renderers.impl.BuiltShadow;

public final class ShadowBuilder extends AbstractBuilder<BuiltShadow> {

    private QuadSize size;
    private QuadRadius radius;
    private QuadColor color;
    private float shadowSize;
    private float amount;

    public ShadowBuilder size(QuadSize size) {
        this.size = size;
        return this;
    }

    public ShadowBuilder radius(QuadRadius radius) {
        this.radius = radius;
        return this;
    }

    public ShadowBuilder color(QuadColor color) {
        this.color = color;
        return this;
    }

    public ShadowBuilder shadowSize(float shadowSize) {
        this.shadowSize = shadowSize;
        return this;
    }

    public ShadowBuilder amount(float amount) {
        this.amount = amount;
        return this;
    }

    @Override
    protected BuiltShadow _build() {
        return new BuiltShadow(
                this.size,
                this.radius,
                this.color,
                this.shadowSize,
                this.amount
        );
    }

    @Override
    protected void reset() {
        this.size = QuadSize.NONE;
        this.radius = QuadRadius.NO_ROUND;
        this.color = QuadColor.TRANSPARENT;
        this.shadowSize = 0.0f;
        this.amount = 1.0f;
    }
}
