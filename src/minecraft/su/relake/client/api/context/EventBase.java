package su.relake.client.api.context;

import meteordevelopment.orbit.ICancellable;
import su.relake.client.Main;

public class EventBase implements ICancellable {
    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void intercept() {
        Main.getInstance().getEventHandler().post(this);
    }

}