package su.relake.client.api.context.implement;


import lombok.AllArgsConstructor;
import lombok.Data;
import su.relake.client.api.context.EventBase;

@Data
@AllArgsConstructor
public class InputEvent extends EventBase {
    private float forward, strafe;
    private boolean jump, sneak;
    private double sneakSlowDownMultiplier;
}

