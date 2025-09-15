package su.relake.client.impl.ui.menu.module;

import java.util.function.Consumer;

public class SliderArea {
    public int x, y, w, h;
    public float min, max, increment;
    public Consumer<Float> onChange;

    public SliderArea(int x, int y, int w, int h, float min, float max, float increment, Consumer<Float> onChange) {
        this.x = x; this.y = y; this.w = w; this.h = h;
        this.min = min; this.max = max; this.increment = increment;
        this.onChange = onChange;
    }

    public boolean contains(double mx, double my) {
        return mx >= x && mx <= x + w && my >= y - 6 && my <= y + h + 6;
    }

    public void handle(double mx) {
        if (w <= 0 || max <= min) return;
        double t = Math.max(0.0, Math.min(1.0, (mx - x) / (double) w));
        float raw = (float) (min + t * (max - min));
        float snapped = snap(raw, increment);
        onChange.accept(snapped);
    }

    private static float snap(float value, float step) {
        if (step <= 0f) return value;
        return Math.round(value / step) * step;
    }
}


