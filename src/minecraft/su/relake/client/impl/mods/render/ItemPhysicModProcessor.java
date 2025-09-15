package su.relake.client.impl.mods.render;

import su.relake.client.api.mod.ModBase;
import su.relake.compiler.sdk.annotations.Compile;

public class ItemPhysicModProcessor extends ModBase {
    @Override
    @Compile
    public void initialize() {
        this.setName("Item Physic");
        this.setDescRU("описание");
        this.setDescENG("decription");
        this.setModuleCategory("Render");
    }
}
