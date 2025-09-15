package su.relake.client.impl.mods.render;

import org.lwjgl.glfw.GLFW;
import su.relake.client.api.mod.ModBase;
import su.relake.client.api.mod.ModType;
import su.relake.client.impl.ui.menu.MenuShell;
import su.relake.compiler.sdk.annotations.VMProtect;
import su.relake.compiler.sdk.enums.VMProtectType;

public class GuiModProcessor extends ModBase {

    @Override

    @VMProtect(type = VMProtectType.VIRTUALIZATION)
    public void initialize() {
        super.initialize("Gui", ModType.RENDER);
        this.setName("Gui");
        setBind(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override

    public void setToggled(boolean state) {
       mc.setScreen(new MenuShell());
    }
}
