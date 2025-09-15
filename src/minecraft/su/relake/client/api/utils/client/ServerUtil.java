package su.relake.client.api.utils.client;

import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import su.relake.client.api.utils.render.util.IMinecraft;

import java.util.ArrayList;
import java.util.Arrays;

@UtilityClass
public class ServerUtil implements IMinecraft {
    public int FT_ANARCHY = -1, RW_GRIEF = -1;

    private String lastIP = "mc.funtime.su";

    public String getIP() {
        if (!isInGame()) return "mainmenu";
        if (mc.isSingleplayer()) return "local";
        if (mc.getCurrentServer() != null) return lastIP = mc.getCurrentServer().ip.toLowerCase();

        return lastIP;
    }

    public boolean isSunWay() {
        String ip = getIP();
        return ip.contains("sunw");
    }

    public boolean isSaturn() {
        String ip = getIP();
        return ip.contains("saturn-x");
    }

    public boolean isSR() {
        String ip = getIP();
        return ip.contains("sunmc");
    }

    public boolean isFS() {
        String ip = getIP();
        return ip.contains("funsky");
    }

    public boolean isRW() {
        String ip = getIP();
        return ip.contains("reallyworld") || ip.contains("playrw");
    }

    public boolean isInGame() {
        return mc.level != null && mc.player != null;
    }

    public boolean isBedWars() {
        return mc.getCurrentServer() != null && (mc.getCurrentServer().ip.contains("mineblaze") || mc.getCurrentServer().ip.contains("dexland") || mc.getCurrentServer().ip.contains("masedworld") || mc.getCurrentServer().ip.contains("cheatmine"));
    }

    public String getServerName(boolean shortName) {
        String ip = getIP();
        String[] parts = ip.split("\\.");

        if (mc.isSingleplayer())
            return applyCase(ip, shortName);

        if (parts.length == 3)
            return applyCase(parts[1], shortName);

        if (parts.length == 2)
            return applyCase(parts[0], shortName);

        if (ip.contains(":"))
            return ip.split(":")[0];

        return ip;
    }

    private String applyCase(String server, boolean shortName) {
        server = server.replace("-", "");

        ArrayList<Data> datas = new ArrayList<>();
        String[] suffixes = { "legacy", "bars", "world", "best", "times", "time", "shine", "sky", "lands", "land", "trainer", "server", "blaze", "mine", "lord", "cube" , "grief", "craft", "rise", "force", "project" };

        Arrays.stream(suffixes).forEach(suffix -> datas.add(genData(suffix)));

        Arrays.stream(new Data[] {
                new Data("mc", "MC", "-MC"),
                new Data("hvh", "HVH", "-HVH"),
                new Data("pvp", "PVP", "PVP")
        }).forEach(datas::add);

        if (mc.isSingleplayer() && !shortName)
            server = "LocalHost";

        if (isSR())
            server = shortName ? "SR" : "SunRise";

        if (isSaturn())
            server = shortName ? "S-X" : "SaturnX";

        if (isSunWay())
            server = shortName ? "SW" : "SunWay";

        for (Data data : datas) {
            if (server.contains(data.orig)) {
                if (shortName) {
                    String rightPart = server.replace(data.orig, "");
                    server = server.substring(0, 1).toUpperCase() + data.small;
                } else {
                    server = server.replace(data.orig, data.big);
                    server = server.substring(0, 1).toUpperCase() + server.substring(1);
                }

                return server;
            }
        }

        server = server.substring(0, 1).toUpperCase() + server.substring(1);

        return server;
    }

    public boolean isFT() {
        String ip = getIP();
        return ip.contains("funtime") || ip.contains("ft") || ip.contains("FunTime");
    }

    public boolean isHW() {
        String ip = getIP();
        return ip.contains("holyworld") || ip.contains("hollyworld");
    }

    public boolean is(String str) {
        return getIP().contains(str);
    }

//    public boolean hasCT() {
//        return BossHealthOverlay.CT;
//    }
//
//    public int getTimeCT() {
//        return BossHealthOverlay.class;
//    }

    public int ping() {
    //    return PlayerTabOverlay.getPlayerPings() == null || !PlayerTabOverlay.getPlayerPings().containsKey(mc.player.getName().getString()) ? 0 : PlayerTabOverlay.getPlayerPings().get(mc.player.getName().getString());
 return 10;
    }


    private Data genData(String full) {
        return new Data(full, full.substring(0, 1).toUpperCase() + full.substring(1), full.substring(0, 1).toUpperCase());
    }

    @AllArgsConstructor
    class Data {
        private String orig, big, small;
    }
}