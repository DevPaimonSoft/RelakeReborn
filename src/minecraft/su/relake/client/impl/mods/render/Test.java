package su.relake.client.impl.mods.render;

import su.relake.client.api.mod.*;
import su.relake.client.api.mod.*;

public class Test extends ModBase {
    private ModeSetting rangeModeImpl;
    private SliderSetting attackRange;
    private BooleanSetting cpsBypass;
    private InputSetting inputSetting;
    private ColorPickerSetting colorPickerSetting;
    private BindSetting bindSetting;
    private MultiSelectSetting options;

    @Override

    public void initialize() {
        super.initialize("Test", ModType.RENDER);
        //  setBind(GLFW.GLFW_KEY_RIGHT_SHIFT);
        rangeModeImpl = new ModeSetting("Ротация головы")
                .setValue("None", "FunTime");
        attackRange = new SliderSetting("Дистанция атаки")
                .setIncrement(0.1f)
                .range(2f, 6f)
                .setValue(4.2f)
                .setVisible(() -> rangeModeImpl.isSelected("Свои настройки"));
        cpsBypass = new BooleanSetting("Обход CPS")
                .setValue(true);
        inputSetting = new InputSetting("Обход CPS", "test", "sosal", false);
        colorPickerSetting = new ColorPickerSetting("colortest", 0);
        bindSetting = new BindSetting("Bind", 0);
        options = new MultiSelectSetting("Опции")
                .setValue("РэйКаст", "Не бить через стены", "Коррекция движения", "Только криты", "Ломать щит");
        options.toggleOption("Коррекция движения");
        this.registerComponent(rangeModeImpl, attackRange, options);

    }

}
