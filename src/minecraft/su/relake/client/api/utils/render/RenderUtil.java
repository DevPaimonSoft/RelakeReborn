package su.relake.client.api.utils.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.experimental.UtilityClass;
import org.joml.Matrix4f;
import su.relake.client.api.utils.render.builders.Builder;
import su.relake.client.api.utils.render.builders.states.QuadColor;
import su.relake.client.api.utils.render.builders.states.QuadRadius;
import su.relake.client.api.utils.render.builders.states.QuadSize;
import su.relake.client.api.utils.render.util.ColorUtil;
import su.relake.client.api.utils.render.util.IMinecraft;

@UtilityClass
public class RenderUtil implements IMinecraft {
    public void drawRoundedRectangle(PoseStack matrixStack, float xStart, float yStart, float xEnd, float yEnd, int color, float round) {
        float[] parsedColor = ColorUtil.extractRGBAf(color);
        if (parsedColor[3] == 0.0F) {
            return;
        }
        float deltaX = xEnd - xStart;
        float deltaY = yEnd - yStart;
//        GlStateManager.disableAlphaTest();
//        GlStateManager._enableBlend();
//        Shader.ROUNDED_RECT.use();
//        Shader.ROUNDED_RECT.initResolution(false);
//        Shader.ROUNDED_RECT.setFloatValue("color", parsedColor);
//        Shader.ROUNDED_RECT.setFloatValue("round", round);
//        Shader.ROUNDED_RECT.setFloatValue("size", deltaX * 2.0F - round / 2.0F, deltaY * 2.0F - round / 2.0F);
//        Shader.ROUNDED_RECT.setFloatValue("start", xStart * 2.0F + deltaX, yStart * 2.0F + deltaY);
//        allocateTexture(matrixStack, xStart, yStart, xEnd, yEnd);
//        Shader.ROUNDED_RECT.release();
//        GlStateManager._disableBlend();
//        GlStateManager.enableAlphaTest();
        final var a = Builder.rectangle()
                .color(new QuadColor(color))
                .radius(new QuadRadius(round))
                .size(new QuadSize(xEnd, yEnd))
                .build();
        a.render(xStart, yStart);
    }
    public void drawRectangle(PoseStack matrixStack, float xStart, float yStart, float xEnd, float yEnd, int color) {
        if (xStart < xEnd) {
            float i = xStart;
            xStart = xEnd;
            xEnd = i;
        }
        if (yStart < yEnd) {
            float j = yStart;
            yStart = yEnd;
            yEnd = j;
        }
        Matrix4f matrix = matrixStack.last().pose();
        GlStateManager.disableTexture();
        GlStateManager._enableBlend();
        GlStateManager.disableAlphaTest();
        final var bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.addVertex(matrix, xStart, yEnd, 0.0F).setColor(color);
        bufferBuilder.addVertex(matrix, xEnd, yEnd, 0.0F).setColor(color);
        bufferBuilder.addVertex(matrix, xEnd, yStart, 0.0F).setColor(color);
        bufferBuilder.addVertex(matrix, xStart, yStart, 0.0F).setColor(color);
        Tesselator.getInstance().draw(bufferBuilder);
        GlStateManager.enableAlphaTest();
        GlStateManager._disableBlend();
        GlStateManager.enableTexture();
    }

    public void drawGradientRoundedRectangle(PoseStack matrixStack, float x, float y, float xEnd, float yEnd, int color1, int color3, int color4, int color2, float round, float alpha) {
        float deltaX = xEnd - x;
        float deltaY = yEnd - y;
//        GlStateManager.disableAlphaTest();
//        GlStateManager._enableBlend();
//        Shader.GRADIENT_ROUNDED_RECT.use();
//        Shader.GRADIENT_ROUNDED_RECT.initResolution(false);
//        Shader.GRADIENT_ROUNDED_RECT.setFloatValue("color1", ColorUtility.extractRGBf(color1));
//        Shader.GRADIENT_ROUNDED_RECT.setFloatValue("color2", ColorUtility.extractRGBf(color2));
//        Shader.GRADIENT_ROUNDED_RECT.setFloatValue("color3", ColorUtility.extractRGBf(color3));
//        Shader.GRADIENT_ROUNDED_RECT.setFloatValue("color4", ColorUtility.extractRGBf(color4));
//        Shader.GRADIENT_ROUNDED_RECT.setFloatValue("round", round);
//        Shader.GRADIENT_ROUNDED_RECT.setFloatValue("alpha", alpha);
//        Shader.GRADIENT_ROUNDED_RECT.setFloatValue("size", deltaX * 2 - round / 2, deltaY * 2 - round / 2);
//        Shader.GRADIENT_ROUNDED_RECT.setFloatValue("start", x * 2 + deltaX, y * 2 + deltaY);
//        allocateTexture(matrixStack, x, y, xEnd, yEnd);
//        Shader.GRADIENT_ROUNDED_RECT.release();
//        GlStateManager._disableBlend();
//        GlStateManager.enableAlphaTest();
        final var a = Builder.rectangle()
                .color(new QuadColor(color1, color2, color3, color4))
                .size(new QuadSize(deltaX * 2 - round / 2.0F, deltaY * 2 - round / 2.0F))
                .radius(new QuadRadius(round))
                .size(new QuadSize(xEnd - x, yEnd - y))
                .build();
        a.render(x, y);
    }
    public void drawRoundedRectangleWithGlow(PoseStack matrixStack, float xStart, float yStart, float xEnd, float yEnd, int color, float round, float softness) {
        float[] parsedColor = ColorUtil.extractRGBAf(color);
        if (parsedColor[3] == 0.0F) {
            return;
        }
        float deltaX = xEnd - xStart;
        float deltaY = yEnd - yStart;
//        GlStateManager.disableAlphaTest();
//        GlStateManager._enableBlend();
//        Shader.ROUNDED_RECT_WITH_GLOW.use();
//        Shader.ROUNDED_RECT_WITH_GLOW.initResolution(false);
//        Shader.ROUNDED_RECT_WITH_GLOW.setFloatValue("color", parsedColor);
//        Shader.ROUNDED_RECT_WITH_GLOW.setFloatValue("round", round);
//        Shader.ROUNDED_RECT_WITH_GLOW.setFloatValue("alpha", ColorUtility.alpha);
//        Shader.ROUNDED_RECT_WITH_GLOW.setFloatValue("softness", softness);
//        Shader.ROUNDED_RECT_WITH_GLOW.setFloatValue("size", deltaX * 2 - round / 2.0F, deltaY * 2 - round / 2.0F);
//        Shader.ROUNDED_RECT_WITH_GLOW.setFloatValue("start", xStart * 2 + deltaX, yStart * 2 + deltaY);
//        allocateTexture(matrixStack, xStart - softness * 2, yStart - softness * 2, xEnd + softness * 2, yEnd + softness * 2);
//        Shader.ROUNDED_RECT_WITH_GLOW.release();
//        GlStateManager._disableBlend();
//        GlStateManager.enableAlphaTest();


        final var a = Builder.rectangle()
                .smoothness(softness)
                .color(new QuadColor(color))
                .radius(new QuadRadius(round))
                .size(new QuadSize(xEnd, yEnd))
                .build();
        a.render(xStart, yStart);

    }
}
