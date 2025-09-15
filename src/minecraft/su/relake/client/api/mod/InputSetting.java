package su.relake.client.api.mod;

public class InputSetting extends Setting<String> {
    private final String description;
    private final boolean onlyNumber;

    public InputSetting(String name, String defaultVal, String description, boolean onlyNumber) {
        super(name, defaultVal);
        this.description = description;
        this.onlyNumber = onlyNumber;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOnlyNumber() {
        return onlyNumber;
    }
}