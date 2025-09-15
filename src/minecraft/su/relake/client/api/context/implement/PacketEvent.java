package su.relake.client.api.context.implement;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.network.protocol.Packet;
import su.relake.client.api.context.EventBase;

@Data
@AllArgsConstructor
public class PacketEvent extends EventBase {

    private Packet<?> packet;
    private Type type;
    public boolean isSend() {
        return type == Type.SEND;
    }
    public boolean isReceive() {
        return type == Type.RECEIVE;
    }
    public enum Type {
        RECEIVE, SEND
    }
}
