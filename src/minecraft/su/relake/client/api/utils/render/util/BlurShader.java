package su.relake.client.api.utils.render.util;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CompiledShaderProgram;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.client.renderer.ShaderProgram;
import su.relake.client.api.utils.render.providers.ResourceProvider;

import java.awt.*;

public enum BlurShader implements IMinecraft {
    INSTANCE;
    private static final ShaderProgram KAWASE_DOWN_SHADER = new ShaderProgram(
            ResourceProvider.getShaderIdentifier("kawase_down"),
            DefaultVertexFormat.POSITION_TEX_COLOR,
            ShaderDefines.EMPTY
    );

    private static final ShaderProgram KAWASE_UP_SHADER = new ShaderProgram(
            ResourceProvider.getShaderIdentifier("kawase_up"),
            DefaultVertexFormat.POSITION_TEX_COLOR,
            ShaderDefines.EMPTY
    );

    private static final RenderTarget MAIN_FBO = Minecraft.getInstance().getMainRenderTarget();

    public static final Supplier<CustomRenderTarget> CACHE = Suppliers.memoize(() -> new CustomRenderTarget(false).setLinear());
    public static final Supplier<CustomRenderTarget> BUFFER = Suppliers.memoize(() -> new CustomRenderTarget(false).setLinear());


    public void update() {
        CustomRenderTarget cache = CACHE.get();
        CustomRenderTarget buffer = BUFFER.get();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        CompiledShaderProgram shaderdown = RenderSystem.setShader(KAWASE_DOWN_SHADER);
        setupUniforms(shaderdown);

        cache.setup();
        MAIN_FBO.bindRead();
        RenderSystem.setShaderTexture(0, MAIN_FBO.getColorTextureId());
        drawQuad(0, 0, mw.getGuiScaledWidth(), mw.getGuiScaledHeight());
        cache.stop();

        CustomRenderTarget[] buffers = {cache, buffer};

        final int steps = 3;
        for (int i = 1; i < steps; ++i) {
            int step = i % 2;
            buffers[step].setup();
            buffers[(step + 1) % 2].bindRead();
            RenderSystem.setShaderTexture(0, buffers[(step + 1) % 2].getColorTextureId());
            drawQuad(0, 0, mw.getGuiScaledWidth(), mw.getGuiScaledHeight());
            buffers[(step + 1) % 2].unbindRead();
            buffers[step].stop();
        }

        CompiledShaderProgram shaderup = RenderSystem.setShader(KAWASE_UP_SHADER);
        setupUniforms(shaderup);

        for (int i = 0; i < steps; ++i) {
            int step = i % 2;
            buffers[(step + 1) % 2].setup();
            buffers[step].bindRead();
            RenderSystem.setShaderTexture(0, buffers[step].getColorTextureId());
            drawQuad(0, 0, mw.getGuiScaledWidth(), mw.getGuiScaledHeight());
            buffers[step].unbindRead();
            buffers[step].stop();
        }

        MAIN_FBO.unbindRead();

        MAIN_FBO.bindWrite(false);
        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.disableBlend();
    }


    private void setupUniforms(CompiledShaderProgram shader) {
        shader.getUniform("Offset").set(2F);
        shader.getUniform("Resolution").set(1f / mw.getWidth(), 1f / mw.getHeight());
        shader.getUniform("Saturation").set(1.1f);
        shader.getUniform("TintIntensity").set(0.05f);
        shader.getUniform("TintColor").set(ColorUtil.getRGBf(new Color(0, 0, 0, 100).getRGB()));
    }


    private void drawQuad(float x, float y, float width, float height) {
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        int color = ColorUtil.WHITE;
        builder.addVertex(x, y, 0F).setUv(0, 1).setColor(color);
        builder.addVertex(x, y + height, 0F).setUv(0, 0).setColor(color);
        builder.addVertex(x + width, y + height, 0F).setUv(1, 0).setColor(color);
        builder.addVertex(x + width, y, 0F).setUv(1, 1).setColor(color);

        BufferUploader.drawWithShader(builder.build());
    }


    public static int getTexture() {
        return BUFFER.get().getColorTextureId();
    }
}
