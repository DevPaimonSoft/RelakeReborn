package su.relake.client.impl.mods.render;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import su.relake.client.api.context.implement.RenderEvent;
import su.relake.client.api.mod.ModBase;
import su.relake.client.api.mod.MultiSelectSetting;
import su.relake.client.api.utils.render.builders.Builder;
import su.relake.client.api.utils.render.builders.states.QuadColor;
import su.relake.client.api.utils.render.builders.states.QuadRadius;
import su.relake.client.api.utils.render.builders.states.QuadSize;
import su.relake.client.api.utils.render.msdf.Fonts;
import su.relake.client.api.utils.render.providers.TextAlignment;
import su.relake.client.api.utils.render.util.ColorUtil;
import su.relake.compiler.sdk.annotations.Compile;

import java.awt.*;

public class HudProcessor extends ModBase {
    private static final float PADDING = 2f;
    private static final float HEIGHT = 12f;
    private static final float RADIUS = 3f;
    private static final float ICON_SIZE = 6f;

    private static final Color ICON_COLOR = new Color(215, 150, 225, 255);
    private static final Color TEXT_COLOR = new Color(255, 255, 255, 216);
    private static final Color PINK_COLOR = new Color(215, 150, 225, 255);


    private MultiSelectSetting hudElements;

    @Compile
    public void initialize() {
        this.setName("HUD");
        this.setDescRU("Красивый HUD");
        this.setDescENG("Beautiful HUD");
        this.setModuleCategory("Render");

        hudElements = new MultiSelectSetting("Watermark Элементы")
                .setValue("Логотип", "Имя игрока", "Координаты", "BPS", "FPS", "Сервер");

        hudElements.toggleOption("Логотип");
        hudElements.toggleOption("Имя игрока");
        hudElements.toggleOption("Координаты");
        hudElements.toggleOption("BPS");
        hudElements.toggleOption("FPS");
        hudElements.toggleOption("Сервер");
        settings.add(hudElements);
    }

    @Compile
    @EventHandler
    public void onRender(RenderEvent event) {
        GuiGraphics g = event.getGraphics();
        Minecraft mc = Minecraft.getInstance();

        float startX = 8f, startY = 8f;
        float x = startX, y = startY;
        float blockWidth;
        float spacing = 2f;
        int elementsPerRow = 3;
        int currentElementInRow = 0;

        String playerName = "User";
        String coords = mc.player != null
                ? String.format("%d %d %d",
                (int) mc.player.getX(),
                (int) mc.player.getY(),
                (int) mc.player.getZ()
        ) : "0 0 0";
        double dx = mc.player != null ? mc.player.getX() - mc.player.oldPosition().x : 0;
        double dz = mc.player != null ? mc.player.getZ() - mc.player.oldPosition().z : 0;
        double bps = Math.sqrt(dx*dx + dz*dz) * 20;
        int fps = mc.getFps();

        String serverIP = mc.getCurrentServer() != null
                ? mc.getCurrentServer().ip
                : "localhost";

        if (hudElements.isSelected("Логотип")) {
            if (currentElementInRow >= elementsPerRow) {
                x = startX;
                y += HEIGHT + spacing;
                currentElementInRow = 0;
            }
            renderHudBlock(g, "R", x, y, 15, HEIGHT, ICON_COLOR, ColorUtil.getColor(27, 28, 37, 98), RADIUS);
            x += 18 + 1;
            currentElementInRow++;
        }

        if (hudElements.isSelected("Имя игрока")) {
            if (currentElementInRow >= elementsPerRow) {
                x = startX;
                y += HEIGHT + spacing;
                currentElementInRow = 0;
            }
            blockWidth = getTextWidth(playerName, 5) + ICON_SIZE + 6 + 2;
            renderHudBlock(g, "A", playerName, x, y, blockWidth, HEIGHT, ICON_COLOR, ColorUtil.getColor(27, 28, 37, 98), RADIUS);
            x += blockWidth + spacing + 2;
            currentElementInRow++;
        }

        if (hudElements.isSelected("Координаты")) {
            if (currentElementInRow >= elementsPerRow) {
                x = startX;
                y += HEIGHT + spacing;
                currentElementInRow = 0;
            }
            blockWidth = getTextWidth(coords, 5) + ICON_SIZE + 10 + 2;
            renderHudBlock(g,"B", coords, x, y, blockWidth, HEIGHT, ICON_COLOR, ColorUtil.getColor(27, 28, 37, 98), RADIUS);
            x += blockWidth + spacing + 2;
            currentElementInRow++;
        }

        if (hudElements.isSelected("BPS")) {
            if (currentElementInRow >= elementsPerRow) {
                x = startX;
                y += HEIGHT + spacing;
                currentElementInRow = 0;
            }
            blockWidth = getTextWidth(String.format("%.1fBPS", bps), 5) + ICON_SIZE + 6 + 2 ;
            renderHudBlock(g,"E", String.format("%.1f", bps), "BPS", x, y, blockWidth, HEIGHT, ICON_COLOR, PINK_COLOR, ColorUtil.getColor(27, 28, 37, 98), RADIUS);
            x += blockWidth + spacing + 2;
            currentElementInRow++;
        }

        if (hudElements.isSelected("FPS")) {
            if (currentElementInRow >= elementsPerRow) {
                x = startX;
                y += HEIGHT + spacing;
                currentElementInRow = 0;
            }
            blockWidth = getTextWidth(String.format("%dFPS", fps), 5) + ICON_SIZE + 6 + 2;
            renderHudBlock(g, "C", String.valueOf(fps), "FPS", x, y, blockWidth, HEIGHT, ICON_COLOR, PINK_COLOR, ColorUtil.getColor(27, 28, 37, 98), RADIUS);
            x += blockWidth + spacing + 2;
            currentElementInRow++;
        }

        if (hudElements.isSelected("Сервер")) {
            if (currentElementInRow >= elementsPerRow) {
                x = startX;
                y += HEIGHT + spacing;
                currentElementInRow = 0;
            }
            blockWidth = getTextWidth(serverIP, 5) + ICON_SIZE + 8 + 2;
            renderHudBlock(g, "D", serverIP, x, y, blockWidth, HEIGHT, ICON_COLOR, ColorUtil.getColor(27, 28, 37, 98), RADIUS);
            x += blockWidth + spacing + 2;
            currentElementInRow++;
        }
    }

    private void renderHudBlock(GuiGraphics g, String icon, String text, float x, float y, float w, float h, Color iconColor, int bgColor, float radius) {
        final var bg = Builder.rectangle()
                .size(new QuadSize(w + 5, h))
                .radius(new QuadRadius(radius))
                .color(new QuadColor(bgColor))
                .smoothness(1f)
                .build();
        final var bg1 = Builder.blur()
                .color(QuadColor.WHITE)
                .size(new QuadSize(w+ 5, h))
                .smoothness(0.5f)
                .radius(new QuadRadius(radius))
                .build();
        final var bg2 = Builder.rectangle()
                .size(new QuadSize(8.7f, h))
                .radius(new QuadRadius(3))
                .color(new QuadColor(ColorUtil.getColor(215, 150, 225, 30)))
                .smoothness(1f)
                .build();
        bg1.render(g.pose().last().pose(), x, y);
        bg.render(g.pose().last().pose(), x, y);
        bg2.render(g.pose().last().pose(), x, y);
        final var text2 = Builder.text()
                .font(Fonts.WT)
                .text(icon)
                .color(iconColor)
                .size(ICON_SIZE - 1)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build();
        text2.render(g.pose().last().pose(),  x + 12 - 10 - 0.6f,  y + (h-ICON_SIZE)/2);
        final var text1 = Builder.text()
                .font(Fonts.COMFORTAA_BOLD)
                .text(text)
                .color(TEXT_COLOR)
                .size(5f)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build();
        text1.render(g.pose().last().pose(),  x + ICON_SIZE + 6 - 3 + 4,  y + h/2 - 2 - 0.5);

    }
    private void renderHudBlock(GuiGraphics g, String icon, float x, float y, float w, float h, Color iconColor, int bgColor, float radius) {
        final var bg = Builder.rectangle()
                .size(new QuadSize(w, h))
                .radius(new QuadRadius(radius))
                .color(new QuadColor(bgColor))
                .smoothness(1f)
                .build();
        final var bg1 = Builder.blur()
                .color(QuadColor.WHITE)
                .size(new QuadSize(w, h))
                .smoothness(0.5f)
                .radius(new QuadRadius(radius))
                .build();
        final var bg2 = Builder.rectangle()
                .size(new QuadSize(8.5f, h))
                .radius(new QuadRadius(3))
                .color(new QuadColor(ColorUtil.getColor(215, 150, 225, 30)))
                .smoothness(1f)
                .build();
        bg1.render(g.pose().last().pose(), x, y);
        bg.render(g.pose().last().pose(), x, y);


        final var text2 = Builder.text()
                .font(Fonts.WT)
                .text(icon)
                .color(iconColor)
                .size(ICON_SIZE + 2)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build();
        text2.render(g.pose().last().pose(),  x + 5 - 2,  y + (h-ICON_SIZE)/2 - 2);
    }
    private void renderHudBlock(GuiGraphics g, String icon, String main, String suffix, float x, float y, float w, float h, Color iconColor, Color suffixColor, int bgColor, float radius) {
        final var bg = Builder.rectangle()
                .size(new QuadSize(w, h))
                .radius(new QuadRadius(radius))
                .color(new QuadColor(bgColor))
                .smoothness(1f)
                .build();
        final var bg1 = Builder.blur()
                .color(QuadColor.WHITE)
                .size(new QuadSize(w, h))
                .smoothness(0.5f)
                .radius(new QuadRadius(radius))
                .build();
        final var bg2 = Builder.rectangle()
                .size(new QuadSize(8.5f, h))
                .radius(new QuadRadius(3))
                .color(new QuadColor(ColorUtil.getColor(215, 150, 225, 30)))
                .smoothness(1f)
                .build();
        bg1.render(g.pose().last().pose(), x, y);
        bg.render(g.pose().last().pose(), x, y);
        bg2.render(g.pose().last().pose(), x, y);
        final var icons = Builder.text()
                .font(Fonts.WT)
                .text(icon)
                .color(iconColor)
                .size(ICON_SIZE -1)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build();
        icons.render(g.pose().last().pose(),  x + 12 - 10 - 0.9, y + (h-ICON_SIZE)/2);
        final var text1 = Builder.text()
                .font(Fonts.COMFORTAA_BOLD)
                .text(main)
                .color(TEXT_COLOR)
                .size(5)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build();
        text1.render(g.pose().last().pose(),  x + ICON_SIZE + 6 - 3 + 1,  y + h/2 - 2 - 0.5f);
        float mainWidth = getTextWidth(main, 5f);
        final var text2 = Builder.text()
                .font(Fonts.COMFORTAA_BOLD)
                .text(suffix)
                .color(suffixColor)
                .size(5)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build();
        text2.render(g.pose().last().pose(),  x + ICON_SIZE + 6 + mainWidth + 1 - 3 + 2.5f,  y + h/2 - 2 - 0.5);


    }
    private float getTextWidth(String text, float size) {
        return Fonts.COMFORTAA_BOLD.getWidth(text, size);
    }
}