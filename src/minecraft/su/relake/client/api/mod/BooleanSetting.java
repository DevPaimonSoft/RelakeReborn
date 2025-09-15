package su.relake.client.api.mod;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name) {
        super(name, false);
    }

    public BooleanSetting setValue(boolean value) {
        this.value = value;
        return this;
    }
}