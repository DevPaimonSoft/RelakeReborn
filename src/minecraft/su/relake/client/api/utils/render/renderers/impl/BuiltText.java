package su.relake.client.api.utils.render.renderers.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.CompiledShaderProgram;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.client.renderer.ShaderProgram;
import org.joml.Matrix4f;
import su.relake.client.api.utils.render.msdf.MsdfFont;
import su.relake.client.api.utils.render.providers.ResourceProvider;
import su.relake.client.api.utils.render.providers.TextAlignment;
import su.relake.client.api.utils.render.renderers.IRenderer;
import su.relake.client.api.utils.render.util.ColorUtil;
import su.relake.client.api.utils.render.util.IMinecraft;

import java.util.Objects;

public record BuiltText(
        MsdfFont font,
        String text,
        float size,
        float thickness,
        int color,
        float smoothness,
        float spacing,
        int outlineColor,
        float outlineThickness,
        float fadeStart,
        float fadeEnd,
        TextAlignment alignment
) implements IRenderer, IMinecraft {

    private static final ShaderProgram MSDF_FONT_SHADER_KEY = new ShaderProgram(
            ResourceProvider.getShaderIdentifier("msdf_font"),
            DefaultVertexFormat.POSITION_TEX_COLOR,
            ShaderDefines.EMPTY
    );


    @Override
    public void render(Matrix4f matrix, float x, float y, float z) {
        float finalX = switch (alignment) {
            case LEFT -> x;
            case RIGHT -> x + this.font.getWidth(this.text, this.size);
            case CENTER -> x + this.font.getWidth(this.text, this.size) / 2F;
        };

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();

        RenderSystem.setShaderTexture(0, this.font.getTextureId());

        boolean outlineEnabled = (this.outlineThickness > 0.0f);
        CompiledShaderProgram shader = RenderSystem.setShader(MSDF_FONT_SHADER_KEY);

        shader.getUniform("FadeStart").set((float) (this.fadeStart * mw.getGuiScale()));
        shader.getUniform("FadeEnd").set((float) (this.fadeEnd * mw.getGuiScale()));

        shader.getUniform("Range").set(this.font.getAtlas().range());
        shader.getUniform("Thickness").set(this.thickness);
        shader.getUniform("Smoothness").set(this.smoothness);
        shader.getUniform("Outline").set(outlineEnabled ? 1 : 0);

        if (outlineEnabled) {
            shader.getUniform("OutlineThickness").set(this.outlineThickness);
            float[] outlineComponents = ColorUtil.getRGBAf(this.outlineColor);
            shader.getUniform("OutlineColor").set(outlineComponents[0], outlineComponents[1],
                    outlineComponents[2], outlineComponents[3]);
        }

        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        this.font.applyGlyphs(matrix, builder, this.text, this.size,
                (this.thickness + this.outlineThickness * 0.5f) * 0.5f * this.size, this.spacing,
                finalX, y + this.font.getMetrics().baselineHeight() * this.size, z, this.color);

        BufferUploader.drawWithShader(Objects.requireNonNull(builder.build()));

        RenderSystem.setShaderTexture(0, 0);

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

}