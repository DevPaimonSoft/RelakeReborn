package su.relake.client.api.context.implement;

import lombok.AllArgsConstructor;
import lombok.Data;
import su.relake.client.api.context.EventBase;

@Data
@AllArgsConstructor
public class MotionEvent extends EventBase {
    private double x, y, z;
    private float yaw, pitch;
    private boolean onGround;

    Runnable postMotion;
}
