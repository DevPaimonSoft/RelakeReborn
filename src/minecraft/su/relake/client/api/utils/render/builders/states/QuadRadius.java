package su.relake.client.api.utils.render.builders.states;

public record QuadRadius(float radius1, float radius2, float radius3, float radius4) {

    public static final QuadRadius NO_ROUND = new QuadRadius(0.0f);
    public static final QuadRadius DEFAULT = new QuadRadius(4.0f);

    public QuadRadius(double radius1, double radius2, double radius3, double radius4) {
        this((float) radius1, (float) radius2, (float) radius3, (float) radius4);
    }

    public QuadRadius(double radius) {
        this(radius, radius, radius, radius);
    }

    public QuadRadius(float radius) {
        this(radius, radius, radius, radius);
    }

}