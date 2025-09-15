package su.relake.client.impl.ui.mainmenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import su.relake.client.api.utils.render.builders.Builder;
import su.relake.client.api.utils.render.builders.states.QuadColor;
import su.relake.client.api.utils.render.builders.states.QuadRadius;
import su.relake.client.api.utils.render.builders.states.QuadSize;
import su.relake.client.api.utils.render.msdf.Fonts;
import su.relake.client.api.utils.render.providers.TextAlignment;
import su.relake.client.api.utils.render.renderers.impl.BuiltTexture;
import su.relake.client.api.utils.render.util.ColorUtil;

import java.util.ArrayList;
import java.util.List;

import static su.relake.client.api.utils.BaseMinecraftInterface.mc;

public class MainScreen extends Screen {

    private final List<ButtonWidget> buttons = new ArrayList<>();
    private boolean isHovered = false;
    private long timeSinceLastHover = System.currentTimeMillis();
    private static final float HORIZONTAL_PADDING = 20f;
    private static final float VERTICAL_PADDING = 10f;

    public MainScreen() {
        super(Component.translatable("menu.title"));
    }

    @Override
    protected void init() {
        float buttonWidth = 40f;
        float buttonHeight = 40f;
        float buttonSpacing = 50f;
        int buttonCount = 5;
        float totalButtonAreaWidth = buttonCount * buttonWidth + (buttonCount - 1) * buttonSpacing + 2 * HORIZONTAL_PADDING;
        float x = (this.width / 2f) - totalButtonAreaWidth / 2f + HORIZONTAL_PADDING;
        float y = (this.height / 2f) - buttonHeight / 2f;

        buttons.clear();

        buttons.add(new ButtonWidget(x, y, buttonWidth, buttonHeight, "Одиночная игра", () -> {
            this.minecraft.setScreen(new SelectWorldScreen(this));
        }));

        x += buttonWidth + buttonSpacing;
        buttons.add(new ButtonWidget(x, y, buttonWidth, buttonHeight, "Сетевая игра", () -> {
            this.minecraft.setScreen(new JoinMultiplayerScreen(this));
        }));

        x += buttonWidth + buttonSpacing;
        buttons.add(new ButtonWidget(x, y, buttonWidth, buttonHeight, "Аккаунты", () -> {
            //this.minecraft.setScreen(SingletonInterceptor.getInstance().getAltManager());
        }));

        x += buttonWidth + buttonSpacing;
        buttons.add(new ButtonWidget(x, y, buttonWidth, buttonHeight, "Настройки", () -> {
            this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
        }));

        x += buttonWidth + buttonSpacing;
        buttons.add(new ButtonWidget(x, y, buttonWidth, buttonHeight, "Выйти из игры", this.minecraft::stop));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Matrix4f matrix = guiGraphics.pose().last().pose();
        AbstractTexture abstractTexture = mc.getTextureManager()
                .getTexture(ResourceLocation.fromNamespaceAndPath("relake", "main.png"));
        BuiltTexture texture = Builder.texture()
                .size(new QuadSize(this.width, this.height))
                .radius(new QuadRadius(0))
                .texture(0.125f, 0.25f, 0.125f, 0.25f, abstractTexture)
                .color(new QuadColor(ColorUtil.WHITE, ColorUtil.WHITE, ColorUtil.WHITE, ColorUtil.WHITE))
                .build();
        texture.render(matrix, 0, 0);


        float buttonWidth = 40f;
        float buttonHeight = this.height;
        float buttonSpacing = this.width;
        int buttonCount = 5;
        float totalButtonAreaWidth = buttonCount * buttonWidth + (buttonCount - 1) * buttonSpacing + 2 * HORIZONTAL_PADDING;
        float totalButtonAreaHeight = buttonHeight + 2 * VERTICAL_PADDING;
        float panelX = (this.width / 2f) - totalButtonAreaWidth / 2f;
        float panelY = (this.height / 2f) - totalButtonAreaHeight / 2f;

        Builder.blur()
                .color(QuadColor.WHITE)
                .size(new QuadSize(totalButtonAreaWidth, totalButtonAreaHeight))
                .smoothness(0.5f)
                .radius(new QuadRadius(10f))
                .build()
                .render(guiGraphics.pose().last().pose(), panelX, panelY);

        Builder.rectangle()
                .size(new QuadSize(totalButtonAreaWidth, totalButtonAreaHeight))
                .smoothness(1f)
                .radius(new QuadRadius(10f))
                .color(new QuadColor(ColorUtil.getColor(20, 20, 20, 150)))
                .build()
                .render(guiGraphics.pose().last().pose(), panelX, panelY);


        buttons.forEach(button -> button.render(guiGraphics, mouseX, mouseY, partialTicks));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        buttons.forEach(b -> b.click(mouseX, mouseY, button));
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public class ButtonWidget extends AbstractWidget {
        private final float x, y, width, height;
        private final String text;
        private final Runnable action;

        public ButtonWidget(float x, float y, float width, float height, String text, Runnable action) {
            super((int) x, (int) y, (int) width, (int) height, Component.literal(text));
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.text = text;
            this.action = action;
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            float radius = 10f;
            boolean isHovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;


            Builder.rectangle()
                    .size(new QuadSize(width, height))
                    .radius(new QuadRadius(radius))
                    .color(new QuadColor(isHovered ? ColorUtil.getColor(30, 30, 30, 200) : ColorUtil.getColor(12, 12, 22, 200)))
                    .build()
                    .render(guiGraphics.pose().last().pose(), x, y);


            String displayText = text.isEmpty() ? "" : text.substring(0, 1).toUpperCase();
            Builder.text()
                    .font(Fonts.COMFORTAA_BOLD)
                    .text(displayText)
                    .color(isHovered ? ColorUtil.getColor(255, 255, 255, 255) : ColorUtil.getColor(255, 255, 255, 200))
                    .size(12f)
                    .alignment(TextAlignment.CENTER)
                    .thickness(0.1f)
                    .build()
                    .render(guiGraphics.pose().last().pose(), x + width / 2f - Fonts.COMFORTAA_BOLD.getWidth(displayText, 12f) / 2 - 6, y + height / 2f - 6);
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput output) {
        }

        public void click(double mouseX, double mouseY, int button) {
            if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
                action.run();
            }
        }
    }
}