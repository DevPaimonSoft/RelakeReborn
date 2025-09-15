package su.relake.client.api.utils.render.builders;

import su.relake.client.api.utils.render.builders.impl.*;
import su.relake.client.api.utils.render.builders.impl.*;

public final class Builder {

    private static final RectangleBuilder RECTANGLE_BUILDER = new RectangleBuilder();
    private static final BorderBuilder BORDER_BUILDER = new BorderBuilder();
    private static final TextureBuilder TEXTURE_BUILDER = new TextureBuilder();
    private static final TextBuilder TEXT_BUILDER = new TextBuilder();
    private static final BlurBuilder BLUR_BUILDER = new BlurBuilder();
    private static final SquircleBuilder SQUIRCLE_BUILDER = new SquircleBuilder();
    private static final ShadowBuilder SHADOW_BUILDER = new ShadowBuilder();

    public static RectangleBuilder rectangle() {
        return RECTANGLE_BUILDER;
    }

    public static BorderBuilder border() {
        return BORDER_BUILDER;
    }

    public static TextureBuilder texture() {
        return TEXTURE_BUILDER;
    }

    public static TextBuilder text() {
        return TEXT_BUILDER;
    }

    public static BlurBuilder blur() {
        return BLUR_BUILDER;
    }

    public static SquircleBuilder squircle() {
        return SQUIRCLE_BUILDER;
    }

    public static ShadowBuilder shadow() {
        return SHADOW_BUILDER;
    }
}