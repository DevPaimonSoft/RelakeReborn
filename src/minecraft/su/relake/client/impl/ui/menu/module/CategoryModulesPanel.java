package su.relake.client.impl.ui.menu.module;

import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix4f;
import su.relake.client.Main;
import su.relake.client.api.mod.BooleanSetting;
import su.relake.client.api.mod.ModBase;
import su.relake.client.api.mod.ModType;
import su.relake.client.api.mod.Setting;
import su.relake.client.api.utils.render.RenderUtil;
import su.relake.client.api.utils.render.builders.Builder;
import su.relake.client.api.utils.render.msdf.Fonts;
import su.relake.client.api.utils.render.providers.TextAlignment;
import su.relake.client.api.utils.render.util.ColorUtil;
import su.relake.client.impl.ui.menu.settings.BooleanSettingRenderer;
import su.relake.client.impl.ui.menu.settings.SliderSettingRenderer;
import su.relake.client.impl.ui.menu.settings.DropdownRenderer;
import su.relake.client.impl.ui.menu.module.ClickableArea;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CategoryModulesPanel {

    private int x;
    private int y;
    private int width;
    private int height;
    private ModType category;
    private float scroll;
    private float targetScroll;
    private int contentHeight;

    private final List<ClickableArea> clickAreas = new ArrayList<>();
    private final List<SliderArea> sliderAreas = new ArrayList<>();
    private final Map<Setting<?>, DropdownRenderer.State> dropdownStates = new HashMap<>();

    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setCategory(ModType category) {
        this.category = category;
    }

    public void render(GuiGraphics g) {
        clickAreas.clear();
        sliderAreas.clear();

        if (category == null) return;

        List<ModBase> mods = Main.getInstance().getModBus().getMods()
                .stream()
                .filter(m -> m.getModType() == category)
                .toList();

        int gapX = 12;
        int gapY = 12;
        int padding = 10;
        float radius = 10f;
        int colW = Math.max(40, (width - gapX) / 2);


        int[] heights = new int[mods.size()];
        for (int i = 0; i < mods.size(); i++) {
            heights[i] = computeCardHeight(mods.get(i), colW, padding);
        }


        int cursor = 0;
        for (int i = 0; i < mods.size(); i += 2) {
            int h1 = heights[i];
            int h2 = (i + 1 < heights.length) ? heights[i + 1] : 0;
            int rowH = Math.max(h1, h2);
            cursor += rowH + gapY;
        }
        contentHeight = Math.max(0, cursor - gapY);


        float maxScroll = Math.max(0, contentHeight - height + 8);
        targetScroll = Math.max(0, Math.min(targetScroll, maxScroll));
        scroll += (targetScroll - scroll) * 0.2f;
        if (Math.abs(targetScroll - scroll) < 0.2f) scroll = targetScroll;

        int rowTop = (int) (y - scroll);


        int bottomExtend = (int) 12.8;
        g.enableScissor(x + 1, y + 1, x + width - 1, y + height - 1 + bottomExtend);
        for (int i = 0, row = 0; i < mods.size(); row++) {
            int h1 = heights[i];
            int h2 = (i + 1 < heights.length) ? heights[i + 1] : 0;
            int rowH = Math.max(h1, h2);

            int x1 = x;
            int x2 = x + colW + gapX;
            int yRow = rowTop;


            if (i < mods.size()) {
                if (yRow + h1 >= y && yRow <= y + height) {
                    renderModuleCard(g, mods.get(i), x1, yRow, colW, padding, radius);
                }
                i++;
            }

            if (i < mods.size()) {
                if (yRow + h2 >= y && yRow <= y + height) {
                    renderModuleCard(g, mods.get(i), x2, yRow, colW, padding, radius);
                }
                i++;
            }

            rowTop += rowH + gapY;
        }
        g.disableScissor();
    }

    private int renderModuleCard(GuiGraphics g, ModBase mod, int cardX, int cardY, int cardWidth, int padding, float radius) {
        int cardHeight = computeCardHeight(mod, cardWidth, padding);

        RenderUtil.drawRoundedRectangleWithGlow(g.pose(), cardX, cardY, cardWidth, cardHeight,
                ColorUtil.rgb(55, 57, 72), radius, 2.5f);
        RenderUtil.drawRoundedRectangle(g.pose(), cardX + 1, cardY + 1, cardWidth - 2, cardHeight - 2,
                ColorUtil.rgb(28, 29, 38), radius);

        Matrix4f mat = g.pose().last().pose();

        Builder.text()
                .font(Fonts.COMFORTAA_BOLD)
                .text(mod.getName())
                .color(ColorUtil.getColor(235, 240, 255, 255))
                .size(6.5f)
                .alignment(TextAlignment.LEFT)
                .thickness(0.1f)
                .build()
                .render(mat, cardX + padding, cardY + padding + 1);

        int titleW = (int) Fonts.COMFORTAA_BOLD.getWidth(mod.getName(), 6.5f);
        clickAreas.add(new ClickableArea(cardX + padding, cardY + padding - 2, titleW + 6, 16,
                () -> mod.setToggled(!mod.isState())));

        boolean enabled = mod.isState();
        int toggleW = 24;
        int toggleH = 12;
        int tx = cardX + cardWidth - padding - toggleW;
        int ty = cardY + padding - 2;
        su.relake.client.impl.ui.menu.settings.BooleanSettingRenderer.drawSwitch(g, enabled, tx, ty, toggleW, toggleH);
        clickAreas.add(new ClickableArea(tx, ty, toggleW, toggleH, () -> mod.setToggled(!mod.isState())));

        AtomicInteger settingsY = new AtomicInteger(cardY + padding + 22);
        int contentX = cardX + padding;
        int contentW = cardWidth - padding * 2;

        for (Setting<?> s : mod.getSettings()) {
            if (!s.isShown()) continue;
            if (s instanceof BooleanSetting bool) {
                int usedH = BooleanSettingRenderer.render(g, s.getName(), bool.getValue(), contentX, settingsY.get(), contentW, clickAreas, () -> bool.setValue(!bool.getValue()));
                settingsY.addAndGet(usedH + 6);
            } else if (s instanceof su.relake.client.api.mod.SliderSetting slider && slider.isVisible()) {
                int usedH = SliderSettingRenderer.render(g, s.getName(), slider.getValue(), slider.getMin(), slider.getMax(), slider.getIncrement(),
                        contentX, settingsY.get(), contentW, clickAreas, newVal -> slider.setValue(newVal));
                int barY = settingsY.get() + 18;
                sliderAreas.add(new SliderArea(contentX, barY, contentW, 4, slider.getMin(), slider.getMax(), slider.getIncrement(), slider::setValue));
                settingsY.addAndGet(usedH + 6);
            } else if (s instanceof su.relake.client.api.mod.MultiSelectSetting multi) {
                DropdownRenderer.State state = dropdownStates.computeIfAbsent(s, k -> new DropdownRenderer.State());
                int usedH = DropdownRenderer.renderExpandingMulti(g, s.getName(), multi.getOptions(), multi.getValue(), contentX, settingsY.get(), contentW, state, clickAreas, multi::toggleOption);
                settingsY.addAndGet(usedH + 6);
            } else if (s instanceof su.relake.client.api.mod.ModeSetting mode) {
                DropdownRenderer.State state = dropdownStates.computeIfAbsent(s, k -> new DropdownRenderer.State());
                int usedH = DropdownRenderer.renderExpandingMode(g, s.getName(), mode.getModes(), mode.getValue(), contentX, settingsY.get(), contentW, state, clickAreas, mode::setValue);
                settingsY.addAndGet(usedH + 6);
            }
        }

        return cardHeight;
    }

    private int computeCardHeight(ModBase mod, int cardWidth, int padding) {
        int baseHeaderHeight = 22;
        int settingsGap = 6;
        int settingsBlockHeight = 0;
        for (Setting<?> s : mod.getSettings()) {
            if (!s.isShown()) continue;
            if (s instanceof BooleanSetting) {
                settingsBlockHeight += 14 + settingsGap;
            } else if (s instanceof su.relake.client.api.mod.SliderSetting slider && slider.isVisible()) {
                settingsBlockHeight += 26 + settingsGap;
            } else if (s instanceof su.relake.client.api.mod.MultiSelectSetting multi) {
                DropdownRenderer.State state = dropdownStates.computeIfAbsent(s, k -> new DropdownRenderer.State());
                int headerH = 22;
                int listH = state.expanded ? (multi.getOptions().length * 18 + 8) : 0;
                settingsBlockHeight += headerH + listH + settingsGap;
            } else if (s instanceof su.relake.client.api.mod.ModeSetting mode) {
                DropdownRenderer.State state = dropdownStates.computeIfAbsent(s, k -> new DropdownRenderer.State());
                int headerH = 22;
                int listH = state.expanded ? (mode.getModes().length * 18 + 8) : 0;
                settingsBlockHeight += headerH + listH + settingsGap;
            }
        }
        if (settingsBlockHeight > 0) settingsBlockHeight -= settingsGap;
        return padding + baseHeaderHeight + settingsBlockHeight + padding;
    }

    public boolean mouseClicked(double mouseX, double mouseY) {

        for (SliderArea s : sliderAreas) {
            if (s.contains(mouseX, mouseY)) {
                s.handle(mouseX);
                return true;
            }
        }
        for (ClickableArea a : clickAreas) {
            if (a.contains(mouseX, mouseY)) {
                a.action.run();
                return true;
            }
        }
        return false;
    }

    public void mouseScrolled(double delta) {
        targetScroll -= (float) (delta * 24.0);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button) {
        for (SliderArea s : sliderAreas) {
            if (s.contains(mouseX, mouseY)) {
                s.handle(mouseX);
                return true;
            }
        }
        return false;
    }

}


