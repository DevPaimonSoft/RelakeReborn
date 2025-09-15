package su.relake.compiler.sdk.classes;

public class Profile {

    private static String username = System.getenv("username");

    private static int uid = 1;

    private static String expire = "2038-06-06";

    private static String role = "Разработчик";

    private static String client = "dimasclient.fun";

    private static String hwid = "nolik";

    public static String getUsername() {
        return username;
    }

    public static int getUid() {
        return uid;
    }

    public static String getExpire() {
        return expire;
    }

    public static String getRole() {
        return role;
    }

    public static String getClient() {
        return client;
    }

    public static String getHwid() {
        return hwid;
    }

}
