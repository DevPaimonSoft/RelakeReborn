package su.relake.client.api.utils.render.builders.states;

public record QuadSize(float width, float height) {

    public static final QuadSize NONE = new QuadSize(0.0f, 0.0f);

    public QuadSize(double width, double height) {
        this((float) width, (float) height);
    }

}