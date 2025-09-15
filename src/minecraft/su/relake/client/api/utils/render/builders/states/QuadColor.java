package su.relake.client.api.utils.render.builders.states;

import su.relake.client.api.utils.render.util.ColorUtil;

public record QuadColor(int color1, int color2, int color3, int color4) {

    public static final QuadColor TRANSPARENT = new QuadColor(ColorUtil.TRANSPARENT);
    public static final QuadColor WHITE = new QuadColor(ColorUtil.WHITE);
    public static final QuadColor BLACK = new QuadColor(ColorUtil.BLACK);

    public QuadColor(int color) {
        this(color, color, color, color);
    }

}