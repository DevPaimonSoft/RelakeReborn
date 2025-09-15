package su.relake.client.impl.mods.movement;

import su.relake.client.api.mod.ModBase;

public class NoSlowModProcessor extends ModBase {
    @Override
    public void initialize() {
        setName("NoSlow");
        setDescENG("No Slow");
        setDescRU("No Slow");
        setModuleCategory("Move");
    }

}
