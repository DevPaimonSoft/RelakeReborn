package su.relake.client.api.utils.client;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.jfr.event.PacketEvent;
import su.relake.client.api.utils.render.util.IMinecraft;

@Getter
public class TPSCalc implements IMinecraft {

    private float TPS = 20;
    private float adjustTicks = 0;

    private long timestamp;

    public TPSCalc() {
      //  Relake.getInstance().getEventBus().register(this);
    }

    @Subscribe
    private void onPacket(PacketEvent e) {
//        if (e.getPacket() instanceof ClientboundSetTimePacket) {
//            updateTPS();
//        }
    }

    private void updateTPS() {
        long delay = System.nanoTime() - timestamp;

        float maxTPS = 20;
        float rawTPS = maxTPS * (1e9f / delay);

        float boundedTPS = Mth.clamp(rawTPS, 0, maxTPS);

        TPS = (float) round(boundedTPS);

        adjustTicks = boundedTPS - maxTPS;

        timestamp = System.nanoTime();
    }

    public double round(
            final double input
    ) {
        return Math.round(input * 100.0) / 100.0;
    }
    public static int getPing() {
        if (mc.getConnection() == null || mc.player == null) return 0;

        PlayerInfo playerListEntry = mc.getConnection().getPlayerInfo(mc.player.getUUID());
        if (playerListEntry == null) return 0;
        return playerListEntry.getLatency();
    }
}
