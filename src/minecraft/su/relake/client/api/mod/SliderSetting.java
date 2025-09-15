package su.relake.client.api.mod;

import java.util.function.BooleanSupplier;

public class SliderSetting extends Setting<Float> {
    private float min;
    private float max;
    private float increment;
    private BooleanSupplier visibility = () -> true;

    public SliderSetting(String name) {
        super(name, 0f);
        this.min = 0f;
        this.max = 0f;
        this.increment = 1f;
    }

    public SliderSetting range(float min, float max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public SliderSetting setIncrement(float increment) {
        this.increment = increment;
        return this;
    }

    public SliderSetting setValue(float value) {
        this.value = Math.max(min, Math.min(max, value));
        return this;
    }

    public SliderSetting setVisible(BooleanSupplier visibility) {
        this.visibility = visibility;
        return this;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getIncrement() {
        return increment;
    }

    public boolean isVisible() {
        return visibility.getAsBoolean();
    }
}