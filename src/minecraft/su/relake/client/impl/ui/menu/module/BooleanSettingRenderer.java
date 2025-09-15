package su.relake.client.impl.ui.menu.module;

import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix4f;
import su.relake.client.api.utils.render.RenderUtil;
import su.relake.client.api.utils.render.builders.Builder;
import su.relake.client.api.utils.render.msdf.Fonts;
import su.relake.client.api.utils.render.providers.TextAlignment;
import su.relake.client.api.utils.render.util.ColorUtil;

public final class BooleanSettingRenderer {

    public static int render(GuiGraphics g, String name, boolean value,
                              int sx, int sy, int sw, java.util.List<ClickableArea> clicks, Runnable onToggle) {
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

        int toggleW = 22;
        int toggleH = 12;
        int tx = sx + sw - toggleW;
        int ty = sy - 2;

        int bg = value ? ColorUtil.rgb(140, 150, 255) : ColorUtil.rgb(60, 62, 78);
        RenderUtil.drawRoundedRectangleWithGlow(g.pose(), tx, ty, toggleW, toggleH, bg, 6f, 1.5f);
        RenderUtil.drawRoundedRectangle(g.pose(), tx + 1, ty + 1, toggleW - 2, toggleH - 2,
                value ? ColorUtil.rgb(120, 130, 250) : ColorUtil.rgb(30, 31, 40), 6f);

        int knobSize = toggleH - 4;
        int knobX = value ? tx + toggleW - knobSize - 2 : tx + 2;
        int knobY = ty + 2;
        RenderUtil.drawRoundedRectangleWithGlow(g.pose(), knobX, knobY, knobSize, knobSize,
                ColorUtil.rgb(240, 242, 255), 6f, 1.5f);

        if (clicks != null && onToggle != null) {
            clicks.add(new ClickableArea(tx, ty, toggleW, toggleH, onToggle));
        }

        return 14;
    }
}


