package su.relake.client.api.context.implement;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.gui.GuiGraphics;
import su.relake.client.api.context.EventBase;

@Data
@RequiredArgsConstructor
public class ModerRenderEvent extends EventBase {
    GuiGraphics guiGraphics;
    float partialTicks;

    public ModerRenderEvent(GuiGraphics poseStack, float partialTicks) {
        this.guiGraphics = poseStack;
        this.partialTicks = partialTicks;
    }

    public enum Type {
        PRE, POST, HIGH
    }
}
