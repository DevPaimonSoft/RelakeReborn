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

public final class DropdownRenderer {

    public static final class State {
        public boolean expanded;
    }

    public static int renderHeader(GuiGraphics g, String title, int sx, int sy, int sw, State state, List<ClickableArea> clicks) {
        Matrix4f mat = g.pose().last().pose();

        int h = 22;
        int radius = 8;
        int bg = ColorUtil.rgb(36, 37, 46);
        int outline = ColorUtil.rgb(55, 57, 72);

        RenderUtil.drawRoundedRectangleWithGlow(g.pose(), sx, sy, sw, h, outline, radius, 2f);
        RenderUtil.drawRoundedRectangle(g.pose(), sx + 1, sy + 1, sw - 2, h - 2, bg, radius);

        Builder.text()
                .font(Fonts.COMFORTAA_BOLD)
                .text(title)
                .color(ColorUtil.getColor(230, 235, 250, 255))
                .size(7f)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build()
                .render(mat, sx + 12, sy + 5);

        String chevron = state.expanded ? "Y" : "X";
        Builder.text()
                .font(Fonts.ICONS_WEXSIDE)
                .text(chevron)
                .color(ColorUtil.getColor(220, 225, 240, 255))
                .size(7f)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build()
                .render(mat, sx + sw - 16, sy + 5);

        if (clicks != null) clicks.add(new ClickableArea(sx, sy, sw, h, () -> state.expanded = !state.expanded));
        return h;
    }

    public static int renderMulti(GuiGraphics g, String[] options, java.util.Set<String> selected,
                                  int sx, int sy, int sw, List<ClickableArea> clicks, java.util.function.Consumer<String> onToggle) {
        Matrix4f mat = g.pose().last().pose();

        int totalH = options.length * 18;
        int radius = 6;
        int bg = ColorUtil.rgb(32, 33, 42);
        int outline = ColorUtil.rgb(45, 47, 60);
        
        RenderUtil.drawRoundedRectangleWithGlow(g.pose(), sx, sy, sw, totalH, outline, radius, 2f);
        RenderUtil.drawRoundedRectangle(g.pose(), sx + 1, sy + 1, sw - 2, totalH - 2, bg, radius);

        int y = sy + 4;
        int itemH = 18;
        int box = 12;
        for (String opt : options) {
            boolean isOn = selected.contains(opt);
            int bx = sx + 10;
            int by = y + 3;

            int bOuter = isOn ? ColorUtil.rgb(124, 128, 208) : ColorUtil.rgb(54, 56, 72);
            int bInner = isOn ? ColorUtil.rgb(111, 115, 198) : ColorUtil.rgb(36, 37, 46);
            RenderUtil.drawRoundedRectangle(g.pose(), bx, by, box, box, bOuter, 3f);
            RenderUtil.drawRoundedRectangle(g.pose(), bx + 1, by + 1, box - 2, box - 2, bInner, 3f);
            if (isOn) {
                Builder.text().font(Fonts.ICONS_WEXSIDE).text("V").color(ColorUtil.getColor(255,255,255,255)).size(5f).alignment(TextAlignment.LEFT).thickness(0.1f).build().render(mat, bx + 2, by + 2);
            }

            Builder.text()
                    .font(Fonts.COMFORTAA_BOLD)
                    .text(opt)
                    .color(ColorUtil.getColor(230, 235, 250, isOn ? 255 : 200))
                .size(6.5f)
                    .alignment(TextAlignment.LEFT)
                    .thickness(0.1f)
                    .build()
                .render(mat, bx + box + 8, y + 2);

            if (clicks != null) clicks.add(new ClickableArea(bx, by, sw - (bx - sx) - 10, itemH, () -> onToggle.accept(opt)));
            y += itemH;
        }
        return totalH;
    }

    public static int renderMode(GuiGraphics g, String[] options, String value,
                                 int sx, int sy, int sw, List<ClickableArea> clicks, java.util.function.Consumer<String> onPick) {
        Matrix4f mat = g.pose().last().pose();
        
        int totalH = options.length * 18;
        int radius = 6;
        int bg = ColorUtil.rgb(32, 33, 42);
        int outline = ColorUtil.rgb(45, 47, 60);
        
        RenderUtil.drawRoundedRectangleWithGlow(g.pose(), sx, sy, sw, totalH, outline, radius, 2f);
        RenderUtil.drawRoundedRectangle(g.pose(), sx + 1, sy + 1, sw - 2, totalH - 2, bg, radius);

        int y = sy + 4;
        int itemH = 18;
        for (String opt : options) {
            boolean isOn = value != null && value.equals(opt);
            int dot = 10;
            int dx = sx + 10;
            int dy = y + 4;
            int col = isOn ? ColorUtil.rgb(124, 128, 208) : ColorUtil.rgb(60, 62, 78);
            RenderUtil.drawRoundedRectangle(g.pose(), dx, dy, dot, dot, col, dot / 2f);
            Builder.text()
                    .font(Fonts.COMFORTAA_BOLD)
                    .text(opt)
                    .color(ColorUtil.getColor(230, 235, 250, isOn ? 255 : 200))
                    .size(6.5f)
                    .alignment(TextAlignment.LEFT)
                    .thickness(0.1f)
                    .build()
                    .render(mat, dx + dot + 8, y + 2);
            if (clicks != null) clicks.add(new ClickableArea(dx, dy, sw - (dx - sx) - 10, itemH, () -> onPick.accept(opt)));
            y += itemH;
        }
        return totalH;
    }

    public static int renderExpandingMulti(GuiGraphics g, String title, String[] options, java.util.Set<String> selected,
                                           int sx, int sy, int sw, State state, List<ClickableArea> clicks, java.util.function.Consumer<String> onToggle) {
        Matrix4f mat = g.pose().last().pose();
        
        int headerH = 22;
        int listH = state.expanded ? (options.length * 18 + 8) : 0;
        int totalH = headerH + listH;
        int radius = 8;
        int bg = ColorUtil.rgb(36, 37, 46);
        int outline = ColorUtil.rgb(55, 57, 72);
        
        RenderUtil.drawRoundedRectangleWithGlow(g.pose(), sx, sy, sw, totalH, outline, radius, 2f);
        RenderUtil.drawRoundedRectangle(g.pose(), sx + 1, sy + 1, sw - 2, totalH - 2, bg, radius);

        Builder.text()
                .font(Fonts.COMFORTAA_BOLD)
                .text(title)
                .color(ColorUtil.getColor(230, 235, 250, 255))
                .size(7f)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build()
                .render(mat, sx + 12, sy + 5);

        String chevron = state.expanded ? "Y" : "X";
        Builder.text()
                .font(Fonts.ICONS_WEXSIDE)
                .text(chevron)
                .color(ColorUtil.getColor(220, 225, 240, 255))
                .size(7f)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build()
                .render(mat, sx + sw - 16, sy + 5);

        if (clicks != null) clicks.add(new ClickableArea(sx, sy, sw, headerH, () -> state.expanded = !state.expanded));

        if (state.expanded) {
            int y = sy + headerH + 4;
            int itemH = 18;
            int box = 12;
            for (String opt : options) {
                boolean isOn = selected.contains(opt);
                int bx = sx + 10;
                int by = y + 3;

                int bOuter = isOn ? ColorUtil.rgb(124, 128, 208) : ColorUtil.rgb(54, 56, 72);
                int bInner = isOn ? ColorUtil.rgb(111, 115, 198) : ColorUtil.rgb(36, 37, 46);
                RenderUtil.drawRoundedRectangle(g.pose(), bx, by, box, box, bOuter, 3f);
                RenderUtil.drawRoundedRectangle(g.pose(), bx + 1, by + 1, box - 2, box - 2, bInner, 3f);
                if (isOn) {
                    Builder.text().font(Fonts.ICONS_WEXSIDE).text("V").color(ColorUtil.getColor(255,255,255,255)).size(5f).alignment(TextAlignment.LEFT).thickness(0.1f).build().render(mat, bx + 2, by + 2);
                }

                Builder.text()
                        .font(Fonts.COMFORTAA_BOLD)
                        .text(opt)
                        .color(ColorUtil.getColor(230, 235, 250, isOn ? 255 : 200))
                        .size(6.5f)
                        .alignment(TextAlignment.LEFT)
                        .thickness(0.1f)
                        .build()
                        .render(mat, bx + box + 8, y + 6);

                if (clicks != null) clicks.add(new ClickableArea(bx, by, sw - (bx - sx) - 10, itemH, () -> onToggle.accept(opt)));
                y += itemH;
            }
        }
        
        return totalH;
    }

    public static int renderExpandingMode(GuiGraphics g, String title, String[] options, String value,
                                          int sx, int sy, int sw, State state, List<ClickableArea> clicks, java.util.function.Consumer<String> onPick) {
        Matrix4f mat = g.pose().last().pose();
        
        int headerH = 22;
        int listH = state.expanded ? (options.length * 18 + 8) : 0;
        int totalH = headerH + listH;
        int radius = 8;
        int bg = ColorUtil.rgb(36, 37, 46);
        int outline = ColorUtil.rgb(55, 57, 72);
        
        RenderUtil.drawRoundedRectangleWithGlow(g.pose(), sx, sy, sw, totalH, outline, radius, 2f);
        RenderUtil.drawRoundedRectangle(g.pose(), sx + 1, sy + 1, sw - 2, totalH - 2, bg, radius);

        Builder.text()
                .font(Fonts.COMFORTAA_BOLD)
                .text(title)
                .color(ColorUtil.getColor(230, 235, 250, 255))
                .size(7f)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build()
                .render(mat, sx + 12, sy + 5);

        String chevron = state.expanded ? "Y" : "X";
        Builder.text()
                .font(Fonts.ICONS_WEXSIDE)
                .text(chevron)
                .color(ColorUtil.getColor(220, 225, 240, 255))
                .size(7f)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build()
                .render(mat, sx + sw - 16, sy + 5);

        if (clicks != null) clicks.add(new ClickableArea(sx, sy, sw, headerH, () -> state.expanded = !state.expanded));

        if (state.expanded) {
            int y = sy + headerH + 4;
            int itemH = 18;
            int box = 12;
            for (String opt : options) {
                boolean isOn = value != null && value.equals(opt);
                int bx = sx + 10;
                int by = y + 3;

                int bOuter = isOn ? ColorUtil.rgb(124, 128, 208) : ColorUtil.rgb(54, 56, 72);
                int bInner = isOn ? ColorUtil.rgb(111, 115, 198) : ColorUtil.rgb(36, 37, 46);
                RenderUtil.drawRoundedRectangle(g.pose(), bx, by, box, box, bOuter, 3f);
                RenderUtil.drawRoundedRectangle(g.pose(), bx + 1, by + 1, box - 2, box - 2, bInner, 3f);
                if (isOn) {
                    Builder.text().font(Fonts.ICONS_WEXSIDE).text("V").color(ColorUtil.getColor(255,255,255,255)).size(5f).alignment(TextAlignment.LEFT).thickness(0.1f).build().render(mat, bx + 2, by + 2);
                }

                Builder.text()
                        .font(Fonts.COMFORTAA_BOLD)
                        .text(opt)
                        .color(ColorUtil.getColor(230, 235, 250, isOn ? 255 : 200))
                        .size(6.5f)
                        .alignment(TextAlignment.LEFT)
                        .thickness(0.1f)
                        .build()
                        .render(mat, bx + box + 8, y + 6);

                if (clicks != null) clicks.add(new ClickableArea(bx, by, sw - (bx - sx) - 10, itemH, () -> onPick.accept(opt)));
                y += itemH;
            }
        }
        
        return totalH;
    }
}


