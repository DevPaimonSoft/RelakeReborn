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
import su.relake.client.api.utils.render.util.BlurShader;
import su.relake.client.api.utils.render.util.IMinecraft;

public record BuiltBlur(
        QuadSize size,
        QuadRadius radius,
        QuadColor color,
        float smoothness
) implements IRenderer, IMinecraft {
    private static final ShaderProgram TEXTURE_SHADER_KEY = new ShaderProgram(
            ResourceProvider.getShaderIdentifier("texture"),
            DefaultVertexFormat.POSITION_TEX_COLOR,
            ShaderDefines.EMPTY
    );


    @Override
    public void render(Matrix4f matrix, float x, float y, float z) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();

        RenderSystem.setShaderTexture(0, BlurShader.getTexture());

        float width = this.size.width(), height = this.size.height();
        CompiledShaderProgram shader = RenderSystem.setShader(TEXTURE_SHADER_KEY);
        shader.getUniform("Size").set(width, height);
        shader.getUniform("Radius").set(this.radius.radius1(), this.radius.radius2(),
                this.radius.radius3(), this.radius.radius4());
        shader.getUniform("Smoothness").set(this.smoothness);

        int screenWidth = mw.getGuiScaledWidth();
        int screenHeight = mw.getGuiScaledHeight();

        float u = x / screenWidth;
        float v = (screenHeight - y - height) / screenHeight;
        float texWidth = width / screenWidth;
        float texHeight = height / screenHeight;

        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        builder.addVertex(matrix, x, y, z).setUv(u, v + texHeight).setColor(this.color.color1());
        builder.addVertex(matrix, x, y + height, z).setUv(u, v).setColor(this.color.color2());
        builder.addVertex(matrix, x + width, y + height, z).setUv(u + texWidth, v).setColor(this.color.color3());
        builder.addVertex(matrix, x + width, y, z).setUv(u + texWidth, v + texHeight).setColor(this.color.color4());

        BufferUploader.drawWithShader(builder.build());

        RenderSystem.setShaderTexture(0, 0);

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

}
