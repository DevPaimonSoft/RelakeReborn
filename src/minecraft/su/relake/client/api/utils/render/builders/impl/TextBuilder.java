package su.relake.client.api.utils.render.builders.impl;

import su.relake.client.api.utils.render.builders.AbstractBuilder;
import su.relake.client.api.utils.render.msdf.MsdfFont;
import su.relake.client.api.utils.render.providers.TextAlignment;
import su.relake.client.api.utils.render.renderers.impl.BuiltText;

import java.awt.*;

public final class TextBuilder extends AbstractBuilder<BuiltText> {

    private MsdfFont font;
    private String text;
    private float size;
    private float thickness;
    private int color;
    private float smoothness;
    private float spacing;
    private int outlineColor;
    private float outlineThickness;
    private float fadeStart;
    private float fadeEnd;
    private TextAlignment alignment;

    public TextBuilder font(MsdfFont font) {
        this.font = font;
        return this;
    }

    public TextBuilder text(String text) {
        this.text = text;
        return this;
    }

    public TextBuilder size(float size) {
        this.size = size;
        return this;
    }

    public TextBuilder thickness(float thickness) {
        this.thickness = thickness;
        return this;
    }

    public TextBuilder color(Color color) {
        return this.color(color.getRGB());
    }

    public TextBuilder color(int color) {
        this.color = color;
        return this;
    }

    public TextBuilder smoothness(float smoothness) {
        this.smoothness = smoothness;
        return this;
    }

    public TextBuilder spacing(float spacing) {
        this.spacing = spacing;
        return this;
    }

    public TextBuilder outline(Color color, float thickness) {
        return this.outline(color.getRGB(), thickness);
    }

    public TextBuilder outline(int color, float thickness) {
        this.outlineColor = color;
        this.outlineThickness = thickness;
        return this;
    }

    public TextBuilder fadeStart(float fadeStart) {
        this.fadeStart = fadeStart;
        return this;
    }

    public TextBuilder fadeEnd(float fadeEnd) {
        this.fadeEnd = fadeEnd;
        return this;
    }

    public TextBuilder alignment(TextAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    @Override
    protected BuiltText _build() {
        return new BuiltText(
                this.font,
                this.text,
                this.size,
                this.thickness,
                this.color,
                this.smoothness,
                this.spacing,
                this.outlineColor,
                this.outlineThickness,
                this.fadeStart,
                this.fadeEnd,
                this.alignment
        );
    }

    @Override
    protected void reset() {
        this.font = null;
        this.text = "";
        this.size = 0.0f;
        this.thickness = 0.0f;
        this.color = -1;
        this.smoothness = 0.5f;
        this.spacing = 0.0f;
        this.outlineColor = 0;
        this.outlineThickness = 0.0f;
        this.alignment = TextAlignment.LEFT;
        this.fadeStart = 0.0f;
        this.fadeEnd = 0.0f;
    }


}