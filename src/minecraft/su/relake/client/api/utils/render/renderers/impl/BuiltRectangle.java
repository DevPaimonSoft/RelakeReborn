package su.relake.client.api.utils.render.renderers.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.CompiledShaderProgram;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.client.renderer.ShaderProgram;
import org.joml.Matrix4f;
import su.relake.client.api.utils.render.builders.states.QuadColor;
import su.relake.client.api.utils.render.builders.states.QuadRadius;
import su.relake.client.api.utils.render.builders.states.QuadSize;
import su.relake.client.api.utils.render.providers.ResourceProvider;
import su.relake.client.api.utils.render.renderers.IRenderer;

public record BuiltRectangle(
        QuadSize size,
        QuadRadius radius,
        QuadColor color,
        float smoothness
) implements IRenderer {

    private static final ShaderProgram RECTANGLE_SHADER_KEY = new ShaderProgram(
            ResourceProvider.getShaderIdentifier("rectangle"),
            DefaultVertexFormat.POSITION_COLOR,
            ShaderDefines.EMPTY
    );


    @Override
    public void render(Matrix4f matrix, float x, float y, float z) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();

        float width = this.size.width(), height = this.size.height();
        CompiledShaderProgram shader = RenderSystem.setShader(RECTANGLE_SHADER_KEY);
        shader.getUniform("Size").set(width, height);
        shader.getUniform("Radius").set(this.radius.radius1(), this.radius.radius2(),
                this.radius.radius3(), this.radius.radius4());
        shader.getUniform("Smoothness").set(this.smoothness);

        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        builder.addVertex(matrix, x, y, z).setColor(this.color.color1());
        builder.addVertex(matrix, x, y + height, z).setColor(this.color.color2());
        builder.addVertex(matrix, x + width, y + height, z).setColor(this.color.color3());
        builder.addVertex(matrix, x + width, y, z).setColor(this.color.color4());

        BufferUploader.drawWithShader(builder.build());

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

}