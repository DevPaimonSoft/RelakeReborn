package su.relake.client.impl.ui.menu.settings;

import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix4f;
import su.relake.client.api.utils.render.RenderUtil;
import su.relake.client.api.utils.render.builders.Builder;
import su.relake.client.api.utils.render.msdf.Fonts;
import su.relake.client.api.utils.render.providers.TextAlignment;
import su.relake.client.api.utils.render.util.ColorUtil;
import su.relake.client.impl.ui.menu.module.ClickableArea;

import java.util.List;

public final class SliderSettingRenderer {

    public static int render(GuiGraphics g, String name, float value, float min, float max, float increment,
                             int sx, int sy, int sw, List<ClickableArea> clicks, java.util.function.Consumer<Float> onChange) {
        Matrix4f mat = g.pose().last().pose();

        String valText = String.valueOf(Math.round(value));
        Builder.text()
                .font(Fonts.COMFORTAA_BOLD)
                .text(name)
                .color(ColorUtil.getColor(235, 240, 255, 255))
                .size(6.5f)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build()
                .render(mat, sx, sy);

        float valWidth = Fonts.COMFORTAA_BOLD.getWidth(valText, 6.5f);
        Builder.text()
                .font(Fonts.COMFORTAA_BOLD)
                .text(valText)
                .color(ColorUtil.getColor(235, 240, 255, 255))
                .size(6.5f)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build()
                .render(mat, sx + sw - valWidth, sy);

        int barY = sy + 18;
        int barH = 4;
        int barX = sx;
        int barW = sw;

        int leftColor = ColorUtil.rgb(124, 128, 208);
        int rightColor = ColorUtil.rgb(62, 63, 73);
        int trackBg = ColorUtil.rgb(44, 45, 56);

        RenderUtil.drawRoundedRectangle(g.pose(), barX, barY, barW, barH, trackBg, 2f);

        float t = (max > min) ? (value - min) / (max - min) : 0f;
        t = Math.max(0f, Math.min(1f, t));
        int filledW = Math.round(barW * t);
        RenderUtil.drawRoundedRectangle(g.pose(), barX, barY, filledW, barH, leftColor, 2f);
        RenderUtil.drawRoundedRectangle(g.pose(), barX + filledW, barY, barW - filledW, barH, rightColor, 2f);

        int knob = 8;
        int kx = barX + filledW - knob / 2;
        int ky = barY - (knob - barH) / 2;
        RenderUtil.drawRoundedRectangle(g.pose(), kx - 1, ky - 1, knob + 2, knob + 2, ColorUtil.rgb(195, 198, 255), knob / 2f + 1);
        RenderUtil.drawRoundedRectangle(g.pose(), kx, ky, knob, knob, ColorUtil.rgb(255, 255, 255), knob / 2f);

        if (clicks != null && onChange != null) {
            clicks.add(new ClickableArea(barX, barY - 6, barW, knob + 12, () -> {}));
        }

        return 26;
    }
}


