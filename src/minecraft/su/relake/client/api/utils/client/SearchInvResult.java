package su.relake.client.api.utils.client;

import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.relake.client.api.utils.render.util.IMinecraft;

public record SearchInvResult(int slot, boolean found, ItemStack stack) implements IMinecraft {
    private static final SearchInvResult NOT_FOUND_RESULT = new SearchInvResult(-1, false, null);

    public static SearchInvResult notFound() {
        return NOT_FOUND_RESULT;
    }

    public static @NotNull SearchInvResult inOffhand(ItemStack stack) {
        return new SearchInvResult(999, true, stack);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isHolding() {
        if (mc.player == null) return false;

        return mc.player.getInventory().selected == slot;
    }

    public boolean isInHotBar() {
        return slot < 9;
    }

    public void switchTo() {
        if (found && isInHotBar())
            switchTo(slot);
    }
    public static void switchTo(int slot) {
        if (mc.player == null || mc.player.connection == null) return;
        if (mc.player.getInventory().selected == slot) return;
        mc.player.getInventory().selected = slot;
        mc.player.connection.send(new ServerboundSetCarriedItemPacket(slot));
    }

}
