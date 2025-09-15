package su.relake.client.api.mod;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MultiSelectSetting extends Setting<Set<String>> {
    private String[] options;

    public MultiSelectSetting(String name) {
        super(name, new HashSet<>());
        this.options = new String[0];
    }

    public MultiSelectSetting setValue(String... options) {
        this.options = options;
        this.value = new HashSet<>();
        return this;
    }

    @Override
    public void setValue(Set<String> value) {
        super.setValue(value);
    }

    public String[] getOptions() {
        return options;
    }

    public boolean isSelected(String option) {
        return value.contains(option);
    }

    public void toggleOption(String option) {
        if (Arrays.asList(options).contains(option)) {
            if (value.contains(option)) {
                value.remove(option);
            } else {
                value.add(option);
            }
        }
    }
}