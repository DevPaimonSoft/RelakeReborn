package su.relake.client.api.utils.animation.extended.impl;


import su.relake.client.api.utils.animation.extended.Animation;
import su.relake.client.api.utils.animation.extended.Direction;

public class ElasticAnimation extends Animation {

    private final float easeAmount;
    private final float smooth;
    private final boolean reallyElastic;

    public ElasticAnimation(int ms, double endPoint, float elasticity, float smooth, boolean moreElasticity) {
        super(ms, endPoint);
        this.easeAmount = elasticity;
        this.smooth = smooth;
        this.reallyElastic = moreElasticity;
    }

    public ElasticAnimation(int ms, double endPoint, float elasticity, float smooth, boolean moreElasticity, Direction direction) {
        super(ms, endPoint, direction);
        this.easeAmount = elasticity;
        this.smooth = smooth;
        this.reallyElastic = moreElasticity;
    }

    @Override
    protected double getEquation(double x) {
        x = Math.pow(x, smooth);
        double elasticity = easeAmount * 0.1f;
        return Math.pow(2, -10 * (reallyElastic ? Math.sqrt(x) : x)) * Math.sin((x - (elasticity / 4)) * ((2 * Math.PI) / elasticity)) + 1;
    }
}
