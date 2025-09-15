package su.relake.client.api.utils.animation.extended.impl;

import su.relake.client.api.utils.animation.extended.Animation;

public class EaseOutSine extends Animation {

    public EaseOutSine(int ms, double endPoint) {
        super(ms, endPoint);
    }

    @Override
    protected double getEquation(double x) {
        return Math.sin(x * (Math.PI / 2));
    }

    @Override
    protected boolean correctOutput() {
        return true;
    }

}
