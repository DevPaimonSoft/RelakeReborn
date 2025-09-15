package su.relake.client.api.context.implement;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import su.relake.client.api.context.EventBase;

@Data
@RequiredArgsConstructor
public class RenderEvent extends EventBase {
    private final GuiGraphics graphics;
    private final DeltaTracker deltaTracker;
}