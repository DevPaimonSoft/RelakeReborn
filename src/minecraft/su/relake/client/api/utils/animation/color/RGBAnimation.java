package su.relake.client.api.utils.animation.color;



import su.relake.client.api.utils.animation.compact.CompactAnimation;
import su.relake.client.api.utils.animation.compact.Easing;

import java.awt.*;

public class RGBAnimation {

    private final RGBAnimationType type;

    private final CompactAnimation r;
    private final CompactAnimation g;
    private final CompactAnimation b;

    public RGBAnimation(RGBAnimationType type, int duration) {
        this.type = type;
        this.r = new CompactAnimation(Easing.EASE_OUT_CUBIC, duration);
        this.g = new CompactAnimation(Easing.EASE_OUT_CUBIC, duration);
        this.b = new CompactAnimation(Easing.EASE_OUT_CUBIC, duration);
    }

    public void run(Color color) {
        r.run(color.getRed());
        g.run(color.getGreen());
        b.run(color.getBlue());
    }

    public void setColor(Color color) {
        r.run(color.getRed());
        g.run(color.getGreen());
        b.run(color.getBlue());
    }

    public Color getColor() {
        if (type == RGBAnimationType.INTEGER) {
            return new Color(
                    r.getNumberValue().intValue(),
                    g.getNumberValue().intValue(),
                    b.getNumberValue().intValue()
            );
        } else {
            return new Color(
                    r.getNumberValue().floatValue() / 255.0f,
                    g.getNumberValue().floatValue() / 255.0f,
                    b.getNumberValue().floatValue() / 255.0f
            );
        }
    }
}
