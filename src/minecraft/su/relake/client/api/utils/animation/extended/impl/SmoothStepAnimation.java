package su.relake.client.api.utils.animation.extended.impl;

import su.relake.client.api.utils.animation.extended.Animation;
import su.relake.client.api.utils.animation.extended.Direction;

public class SmoothStepAnimation extends Animation {

    public SmoothStepAnimation(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    protected double getEquation(double x) {
        return -2 * Math.pow(x, 3) + (3 * Math.pow(x, 2));
    }

}
