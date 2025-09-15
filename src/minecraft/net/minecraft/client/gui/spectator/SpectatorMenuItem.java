package net.minecraft.client.gui.spectator;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public interface SpectatorMenuItem {
    void selectItem(SpectatorMenu pMenu);

    Component getName();

    void renderIcon(GuiGraphics pGuiGraphics, float pBrightness, float pAlpha);

    boolean isEnabled();
}