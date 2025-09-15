package su.relake.client.impl.ui.menu;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import su.relake.client.api.utils.render.RenderUtil;
import su.relake.client.api.utils.render.util.ColorUtil;
import su.relake.client.api.utils.render.builders.Builder;
import su.relake.client.api.utils.render.msdf.Fonts;
import su.relake.client.api.utils.render.providers.TextAlignment;
import su.relake.client.api.mod.ModType;
import su.relake.client.impl.ui.menu.module.CategoryModulesPanel;

@FieldDefaults(level = AccessLevel.PUBLIC)
public class MenuShell extends Screen {

        private ModType selectedCategory = ModType.COMBAT;
        private final CategoryModulesPanel modulesPanel = new CategoryModulesPanel();

        public MenuShell() {
            super(Component.empty());
        }

        @Override
        protected void init() {
            super.init();
        }

        @Override
        public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            int containerWidth = 400;
            int containerHeight = 200;
            int x = (this.width - containerWidth) / 2;
            int y = (this.height - containerHeight) / 2;
            float cornerRadius = 8f;
            int backgroundColor = ColorUtil.rgb(24, 25, 33);
            int outlineColor = ColorUtil.rgb(41, 43, 56);
            RenderUtil.drawRoundedRectangleWithGlow(
                pGuiGraphics.pose(), x, y, containerWidth, containerHeight, outlineColor, cornerRadius, 2f
            );
            RenderUtil.drawRoundedRectangle(
                pGuiGraphics.pose(), x + 1, y + 1, containerWidth - 2, containerHeight - 2, backgroundColor, cornerRadius
            );

            float dividerPercentFromLeft = 0.20f;
            int columnsDividerX = x + 1 + Math.round(containerWidth * dividerPercentFromLeft);
            int columnsDividerTop = y + 1;
            int columnsDividerHeight = containerHeight - 2;
            RenderUtil.drawRoundedRectangleWithGlow(
                pGuiGraphics.pose(), columnsDividerX, columnsDividerTop, 2f, columnsDividerHeight, ColorUtil.rgb(90, 92, 110), 0.0F, 4f
            );

            int topDividerY = Math.max(y + 1, y + 36 - Math.round(containerHeight * 0.05f));
            int topDividerX = columnsDividerX;
            int topDividerWidth = (x + containerWidth - 1) - topDividerX;
            RenderUtil.drawRoundedRectangleWithGlow(
                pGuiGraphics.pose(), topDividerX, topDividerY, topDividerWidth, 2f, ColorUtil.rgb(90, 92, 110), 0.0F, 3f
            );

            String contentTitle = selectedCategory.getName();
            float contentTitleX = columnsDividerX + 4.5f;
            float contentTitleY = y + 13.5f;
            Builder.text()
                    .font(Fonts.COMFORTAA_BOLD)
                    .text(contentTitle)
                    .color(ColorUtil.getColor(230, 235, 250, 255))
                    .size(8f)
                    .alignment(TextAlignment.LEFT)
                    .thickness(0.1f)
                    .build()
                    .render(pGuiGraphics.pose().last().pose(), contentTitleX, contentTitleY);

            int rightBoxX = columnsDividerX + 16;
            int rightBoxY = topDividerY + 10;
            int rightBoxW = x + containerWidth - 16 - rightBoxX;
            int rightBoxH = y + containerHeight - 14 - rightBoxY;
            modulesPanel.setBounds(rightBoxX, rightBoxY, rightBoxW, rightBoxH);
            modulesPanel.setCategory(selectedCategory);
            modulesPanel.render(pGuiGraphics);

            int leftPadding = 6;
            int leftAreaX = x + leftPadding;
            int leftAreaRight = columnsDividerX - leftPadding;
            int leftAreaWidth = Math.max(0, leftAreaRight - leftAreaX);

            float headerIconSize = 9f;
            double headerIconX = leftAreaX;
            double headerIconY = y + 16 - headerIconSize / 2.0;
            Builder.text()
                    .font(Fonts.ICONS_WEXSIDE)
                    .text("W")
                    .color(ColorUtil.getColor(140, 150, 255, 255))
                    .size(headerIconSize)
                    .alignment(TextAlignment.LEFT)
                    .thickness(0.1f)
                    .build()
                    .render(pGuiGraphics.pose().last().pose(), headerIconX, headerIconY);

            Builder.text()
                    .font(Fonts.COMFORTAA_BOLD)
                    .text("Relake")
                    .color(ColorUtil.getColor(220, 225, 240, 255))
                    .size(7f)
                    .alignment(TextAlignment.LEFT)
                    .thickness(0.1f)
                    .build()
                    .render(pGuiGraphics.pose().last().pose(), headerIconX + headerIconSize + 8, y + 12);

            float categoriesStartY = topDividerY + 17.8f;
            int itemHeight = 18;
            int itemRadius = 8;

            ModType[] types = ModType.values();
            for (int i = 0; i < types.length; i++) {
                ModType type = types[i];
                float itemY = categoriesStartY + i * (itemHeight + 4.5f);
                int itemX = leftAreaX;
                int itemW = Math.max(70, Math.min(leftAreaWidth, 80));

                boolean isSelected = type == selectedCategory;
                if (isSelected) {
                    RenderUtil.drawRoundedRectangleWithGlow(
                            pGuiGraphics.pose(), itemX, itemY - 4, itemW, itemHeight, ColorUtil.rgb(55, 57, 72), itemRadius, 3f
                    );
                    RenderUtil.drawRoundedRectangle(
                            pGuiGraphics.pose(), itemX + 1, itemY - 3, itemW - 2, itemHeight - 2, ColorUtil.rgb(36, 37, 46), itemRadius
                    );
                }

                String iconGlyph;
                switch (type) {
                    case COMBAT -> iconGlyph = "A";
                    case MOVE -> iconGlyph = "B";
                    case RENDER -> iconGlyph = "C";
                    case PLAYER -> iconGlyph = "D";
                    case UTILS -> iconGlyph = "E";
                    default -> iconGlyph = "A";
                }
                float iconSize = 6f;
                double itemCenterY = (itemY - 4) + (itemHeight / 2.0);
                double iconX = itemX + 8;
                double iconY = itemCenterY - iconSize / 2.0;
                Builder.text()
                        .font(Fonts.ICONS_WEXSIDE)
                        .text(iconGlyph)
                        .color(isSelected ? ColorUtil.getColor(180, 185, 255, 255) : ColorUtil.getColor(200, 205, 220, 210))
                        .size(iconSize)
                        .alignment(TextAlignment.LEFT)
                        .thickness(0.1f)
                        .build()
                        .render(pGuiGraphics.pose().last().pose(), iconX, iconY);

                String label = type.getName();
                Builder.text()
                        .font(Fonts.COMFORTAA_BOLD)
                        .text(label)
                        .color(isSelected ? ColorUtil.getColor(255, 255, 255, 255) : ColorUtil.getColor(210, 213, 230, 235))
                        .size(6f)
                        .alignment(TextAlignment.LEFT)
                        .thickness(0.1f)
                        .build()
                        .render(pGuiGraphics.pose().last().pose(), iconX + iconSize + 10, (float)(itemCenterY - 3f));

                if (isSelected) {
                    int dotSize = 5;
                    int dotX = itemX + itemW - 12;
                    int dotY = (int)(itemCenterY - dotSize / 2.0);
                    RenderUtil.drawRoundedRectangleWithGlow(
                            pGuiGraphics.pose(), dotX, dotY, dotSize, dotSize, ColorUtil.rgb(122, 126, 255), 3f, 2f
                    );
                }
            }
        }

        @Override
        public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
            if (modulesPanel.mouseDragged(pMouseX, pMouseY, pButton)) return true;
            return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }

        @Override
        public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
            return super.mouseReleased(pMouseX, pMouseY, pButton);
        }

        @Override
        public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
            int containerWidth = 400;
            int containerHeight = 200;
            int x = (this.width - containerWidth) / 2;
            int y = (this.height - containerHeight) / 2;

            float dividerPercentFromLeft = 0.20f;
            int columnsDividerX = x + 1 + Math.round(containerWidth * dividerPercentFromLeft);
            int topDividerY = Math.max(y + 1, y + 36 - Math.round(containerHeight * 0.05f));

            int leftPadding = 6;
            int leftAreaX = x + leftPadding;
            int leftAreaRight = columnsDividerX - leftPadding;
            int leftAreaWidth = Math.max(0, leftAreaRight - leftAreaX);

            double categoriesStartY = topDividerY + 17.8;
            int itemHeight = 18;

            ModType[] types = ModType.values();
            for (int i = 0; i < types.length; i++) {
                double itemY = categoriesStartY + i * (itemHeight + 4.5);
                int itemX = leftAreaX;
                int itemW = Math.max(70, Math.min(leftAreaWidth, 80));

                if (pMouseX >= itemX && pMouseX <= itemX + itemW && pMouseY >= itemY - 4 && pMouseY <= itemY - 4 + itemHeight) {
                    selectedCategory = types[i];
                    return true;
                }
            }

            int rightBoxX = columnsDividerX + 16;
            int rightBoxY = topDividerY + 10;
            int rightBoxW = x + containerWidth - 16 - rightBoxX;
            int rightBoxH = y + containerHeight - 14 - rightBoxY;
            modulesPanel.setBounds(rightBoxX, rightBoxY, rightBoxW, rightBoxH);
            if (modulesPanel.mouseClicked(pMouseX, pMouseY)) return true;

            return super.mouseClicked(pMouseX, pMouseY, pButton);
        }
        
        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
            modulesPanel.mouseScrolled(deltaY);
            return true;
        }

}
