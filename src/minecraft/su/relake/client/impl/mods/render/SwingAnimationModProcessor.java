package su.relake.client.impl.mods.render;

import su.relake.client.api.mod.ModBase;
import su.relake.client.api.mod.ModeSetting;
import su.relake.client.api.mod.SliderSetting;

public class SwingAnimationModProcessor extends ModBase {
    public ModeSetting mode;
    public SliderSetting handScale;

    @Override

    public void initialize() {
        this.setName("Swing Animations");
        this.setDescRU("Подсвечивает игроков и вагонетки");
        this.setDescENG("Highlights players and minecarts");
        this.setModuleCategory("Render");
        mode = new ModeSetting("Mode").setValue("1", "2", "3", "4");
        mode.setValue("1");
        handScale = new SliderSetting("Hand Scale").setIncrement(0.01f).range(0.1f, 1f).setValue(0.6f);

        registerComponent(
                mode,
                handScale
        );
    }
}
