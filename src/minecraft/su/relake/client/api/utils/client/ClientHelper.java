package su.relake.client.api.utils.client;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import su.relake.client.api.utils.render.util.IMinecraft;

import java.util.UUID;

public class ClientHelper implements IMinecraft {
    private static boolean pvpMode;
    private static UUID uuid;

    public static void updateBossInfo(UUID bossUUID, Component bossName, boolean isRemove) {
        if (!isRemove) {
            if (StringUtil.stripColor(bossName.getString()).toLowerCase().contains("pvp")) {
                pvpMode = true;
                uuid = bossUUID;
            }
        } else {
            if (bossUUID.equals(uuid)) {
                pvpMode = false;
            }
        }
    }

    public static String getIP() {
        if (mc.level == null) {
            return "mainmenu";
        }
        if (mc.isLocalServer()) {
            return "local";
        }
        if (mc.getCurrentServer() != null) {
            return mc.getCurrentServer().ip.toLowerCase();
        }
        return "";
    }

    public static boolean isConnectedToServer(String ip) {
        return mc.getCurrentServer() != null && mc.getCurrentServer().ip != null && mc.getCurrentServer().ip.contains(ip);
    }

    public static boolean isPvP() {
        return pvpMode;
    }

    public static String getPlayerPing() {
        if (mc.isLocalServer()) {
            return "local";
        } else if (mc.player != null && mc.getConnection() != null) {
            PlayerInfo info = mc.getConnection().getPlayerInfo(mc.player.getUUID());
            if (info != null) {
                return info.getLatency() + "";
            }
        }
        return "N/A";
    }

//    public static float[] getHealthFromScoreboard(LivingEntity target) {
//        float currentHealth = target.getHealth();
//        float maxHealth = target.getMaxHealth();
//
//        if (target instanceof Player player) {
//            Scoreboard scoreboard = player.getScoreboard();
//
//            Optional<Objective> objectiveOptional = scoreboard.getObjectives().stream()
//                    .filter(obj -> obj.getDisplayName().getString().contains("Здоровья"))
//                    .findFirst();
//
//            if (objectiveOptional.isPresent()) {
//                Objective objective = objectiveOptional.get();
//                currentHealth = scoreboard.getOrCreatePlayerScore(player, objective).get();
//                maxHealth = 20;
//            }
//        }
//        return new float[]{currentHealth, maxHealth};
//    }
}
