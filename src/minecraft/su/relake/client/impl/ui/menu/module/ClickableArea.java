package su.relake.client.impl.ui.menu.module;

public class ClickableArea {
    public final int x, y, w, h;
    public final Runnable action;

    public ClickableArea(int x, int y, int w, int h, Runnable action) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.action = action;
    }

    public boolean contains(double mx, double my) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}


