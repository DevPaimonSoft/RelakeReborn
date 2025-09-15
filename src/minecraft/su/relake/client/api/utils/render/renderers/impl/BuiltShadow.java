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

public record BuiltShadow(
        QuadSize size,
        QuadRadius radius,
        QuadColor color,
        float shadowSize,
        float amount
) implements IRenderer {

    private static final ShaderProgram SHADOW_SHADER_KEY = new ShaderProgram(
            ResourceProvider.getShaderIdentifier("shadow"),
            DefaultVertexFormat.POSITION_COLOR,
            ShaderDefines.EMPTY
    );


    @Override
    public void render(Matrix4f matrix, float x, float y, float z) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();

        float posX = x - shadowSize;
        float posY = y - shadowSize;
        float width = this.size.width() + (shadowSize * 2F), height = this.size.height() + (shadowSize * 2F);
        CompiledShaderProgram shader = RenderSystem.setShader(SHADOW_SHADER_KEY);
        shader.getUniform("Size").set(width, height);
        shader.getUniform("Radius").set(this.radius.radius1(), this.radius.radius2(),
                this.radius.radius3(), this.radius.radius4());
        shader.getUniform("softness").set(-1F, shadowSize);
        shader.getUniform("value").set(shadowSize);
        shader.getUniform("amount").set(this.amount);

        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        builder.addVertex(matrix, posX, posY, z).setColor(this.color.color1());
        builder.addVertex(matrix, posX, posY + height, z).setColor(this.color.color2());
        builder.addVertex(matrix, posX + width, posY + height, z).setColor(this.color.color3());
        builder.addVertex(matrix, posX + width, posY, z).setColor(this.color.color4());

        BufferUploader.drawWithShader(builder.build());

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

}
