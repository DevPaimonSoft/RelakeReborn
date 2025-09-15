package su.relake.client.api.mod;

import java.util.Arrays;

public class ModeSetting extends Setting<String> {
    private String[] modes;

    public ModeSetting(String name) {
        super(name, null);
        this.modes = new String[0];
    }

    public ModeSetting setValue(String... modes) {
        this.modes = modes;
        if (modes.length > 0 && !Arrays.asList(modes).contains(this.value)) {
            this.value = modes[0];
        }
        return this;
    }

    public String[] getModes() {
        return modes;
    }

    public boolean isSelected(String mode) {
        return value != null && value.equals(mode);
    }

    @Override
    public void setValue(String value) {
        if (Arrays.asList(modes).contains(value)) {
            this.value = value;
        }
    }
}