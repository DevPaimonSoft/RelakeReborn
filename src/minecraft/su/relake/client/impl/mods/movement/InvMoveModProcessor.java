package su.relake.client.impl.mods.movement;

import com.mojang.blaze3d.platform.InputConstants;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import su.relake.client.api.context.implement.InputEvent;
import su.relake.client.api.context.implement.PacketEvent;
import su.relake.client.api.context.implement.UpdateEvent;
import su.relake.client.api.mod.BooleanSetting;
import su.relake.client.api.mod.ModBase;
import su.relake.compiler.sdk.annotations.Compile;
import su.relake.compiler.sdk.annotations.VMProtect;
import su.relake.compiler.sdk.enums.VMProtectType;

import java.util.LinkedList;
import java.util.Queue;

public class InvMoveModProcessor extends ModBase {

    public BooleanSetting shiftInMenu;
    public BooleanSetting antiCheatBypass;

    private int stopTicksOut;
    private boolean stoppedStatus, previousStoppedStatus;
    private final Queue<ServerboundContainerClickPacket> windowClickPacketQueue = new LinkedList<>();


    @VMProtect(type = VMProtectType.MUTATION)
    public void initialize() {
        this.setName("Inventory Move");
        this.setDescRU("Позволяет передвигаться находясь в любых меню");
        this.setDescENG("Allows you to move around while in any menu");
        this.setModuleCategory("Move");

        shiftInMenu = new BooleanSetting("Shift in menu")
                .setValue(true);

        antiCheatBypass = new BooleanSetting("Anti-cheat bypass")
                .setValue(true);

        this.registerComponent(shiftInMenu, antiCheatBypass);
    }


    private boolean canStoppingOnWindowClick() {
        return antiCheatBypass.getValue() &&
                (mc.player.input.up ||
                        mc.player.input.down ||
                        mc.player.input.left ||
                        mc.player.input.right) ||
                stoppedStatus;
    }


    private void setStop(ClickType clickType) {
        if (this.isState()) {
            this.stopTicksOut = ticksWindowClickOffset(clickType == null ? ClickType.PICKUP : clickType);
        }
    }


    private int ticksWindowClickOffset(ClickType clickType) {
        return clickType.equals(ClickType.PICKUP) ? 1 : this.stopTicksOut > 1 ? 2 : 3;
    }


    private void useAccumulatedPackets() {
        if (this.windowClickPacketQueue.isEmpty()) return;
        this.windowClickPacketQueue.removeIf(packet -> {
            mc.player.connection.send(packet);
            return true;
        });
    }


    private boolean rememberCClickWindowPacket(ServerboundContainerClickPacket packet) {
        return !this.windowClickPacketQueue.contains(packet) && this.windowClickPacketQueue.add(packet);
    }


    private boolean getHasItemStackDragged() {
        if (!(mc.screen instanceof ContainerScreen screenContainer)) {
            return false;
        }
        Slot hoveredSlot = screenContainer.hoveredSlot;
        return canStoppingOnWindowClick() && hoveredSlot != null && hoveredSlot.hasItem();
    }

    @EventHandler

    public void onUpdate(UpdateEvent event) {
//System.out.print("123");

        if (this.previousStoppedStatus && this.stoppedStatus && this.stopTicksOut > 0) {
            this.useAccumulatedPackets();
        }
        this.previousStoppedStatus = this.stoppedStatus;

        if (this.getHasItemStackDragged()) {
            this.setStop(null);
        } else if (this.stopTicksOut > 0) {
            this.stopTicksOut--;
        }
        this.stoppedStatus = this.stopTicksOut > 0;

        // Обновление состояния клавиш для движения
        if (isValidScreen(mc.screen)) {
            updateKeyBindingState(new KeyMapping[]{
                    mc.options.keyUp,
                    mc.options.keyDown,
                    mc.options.keyLeft,
                    mc.options.keyRight,
                    mc.options.keyJump,
                    shiftInMenu.getValue() ? mc.options.keyShift : null
            });
        }
    }

    @EventHandler
    public void onInput(InputEvent event) {
//        if (mc.player == null || mc.level == null) return;

        // Блокируем движение только если включен античит и есть активный контейнер
        if (antiCheatBypass.getValue() && mc.screen instanceof ContainerScreen && stoppedStatus) {
            event.setForward(0);
            event.setStrafe(0);
            event.setJump(false);
        }
    }

    @EventHandler

    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof ServerboundContainerClickPacket packet) {
            if (antiCheatBypass.getValue() && packet.getSlotNum() != -1) {
                this.setStop(packet.getClickType());
                if (!this.stoppedStatus && this.rememberCClickWindowPacket(packet)) {
                    event.cancel();
                }
            }
        } else if (event.getPacket() instanceof ClientboundContainerClosePacket && antiCheatBypass.getValue()) {
            event.cancel();
        }
    }


    private void updateKeyBindingState(KeyMapping[] keyBindings) {
        for (KeyMapping keyBinding : keyBindings) {
            if (keyBinding != null) {
                keyBinding.setDown(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keyBinding.getDefaultKey().getValue()));
            }
        }
    }


    private boolean isValidScreen(net.minecraft.client.gui.screens.Screen screen) {
        return !(screen instanceof ChatScreen);
    }

    @Override
    @Compile
    public void onEnable() {
        super.onEnable();
        this.useAccumulatedPackets();
        this.windowClickPacketQueue.clear();
    }

    @Override
    @Compile
    public void onDisable() {
        super.onDisable();
        this.useAccumulatedPackets();
    }
}