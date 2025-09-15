package su.relake.client.api.mod;

import java.util.function.Supplier;

public abstract class Setting<T> {
    protected String name;
    protected T value;
    protected Supplier<Boolean> shown;

    public Setting(String name, T defaultVal) {
        this.name = name;
        this.value = defaultVal;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Setting<T> setShown(Supplier<Boolean> booleanSupplier) {
        this.shown = booleanSupplier;
        return this;
    }

    public boolean isShown() {
        return shown == null || shown.get();
    }
}