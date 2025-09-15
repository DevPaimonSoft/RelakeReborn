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

public final class BooleanSettingRenderer {

    public static void drawSwitch(GuiGraphics g, boolean value, int x, int y, int w, int h) {
        float r = h / 2f;
        int outer = value ? ColorUtil.rgb(124, 128, 208) : ColorUtil.rgb(54, 56, 72);
        int inner = value ? ColorUtil.rgb(111, 115, 198) : ColorUtil.rgb(36, 37, 46);

        RenderUtil.drawRoundedRectangle(g.pose(), x, y, w, h, outer, r);
        RenderUtil.drawRoundedRectangle(g.pose(), x + 1, y + 1, w - 2, h - 2, inner, r);

        int knob = h - 4;
        int kx = value ? x + w - knob - 2 : x + 2;
        int ky = y + 2;
        RenderUtil.drawRoundedRectangle(g.pose(), kx - 1, ky - 1, knob + 2, knob + 2, ColorUtil.rgb(195, 198, 255), knob / 2f + 1);
        RenderUtil.drawRoundedRectangle(g.pose(), kx, ky, knob, knob, ColorUtil.rgb(255, 255, 255), knob / 2f);
    }

    public static int render(GuiGraphics g, String name, boolean value,
                             int sx, int sy, int sw, List<ClickableArea> clicks, Runnable onToggle) {
        Matrix4f mat = g.pose().last().pose();

        Builder.text()
                .font(Fonts.COMFORTAA_BOLD)
                .text(name)
                .color(ColorUtil.getColor(220, 225, 240, 255))
                .size(5.5f)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build()
                .render(mat, sx, sy);

        int toggleW = 24;
        int toggleH = 12;
        int tx = sx + sw - toggleW;
        int ty = sy - 2;

        drawSwitch(g, value, tx, ty, toggleW, toggleH);

        if (clicks != null && onToggle != null) {
            clicks.add(new ClickableArea(tx, ty, toggleW, toggleH, onToggle));
        }

        return 14;
    }
}


