package su.relake.client.impl.mods.combat;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import su.relake.client.api.context.implement.PacketEvent;
import su.relake.client.api.mod.ModBase;
import su.relake.client.api.mod.ModeSetting;
import su.relake.compiler.sdk.annotations.Compile;
import su.relake.compiler.sdk.annotations.Initialization;
import su.relake.compiler.sdk.annotations.VMProtect;
import su.relake.compiler.sdk.enums.VMProtectType;

public class VelocityModProcessor extends ModBase {
    private ModeSetting mode;

    @Compile
    @Initialization
    @VMProtect(type = VMProtectType.VIRTUALIZATION)
    public void initialize() {
        this.setName("Velocity");
        this.setDescRU("Блокирует или уменьшает откидывание от всяческого урона");
        this.setModuleCategory("Combat");

        mode = new ModeSetting("Обход").setValue("Ванильный", "Ванильный");

        registerComponent(mode);

    }

    @EventHandler
    public void onPacketContext(PacketEvent packetContext) {
        if (packetContext.getType() == PacketEvent.Type.RECEIVE && mode.getValue().equals("Ванильный") &&
                packetContext.getPacket() instanceof ClientboundSetEntityMotionPacket packet
                && packet.getId() == mc.player.getId())
            packetContext.cancel();
    }

}
