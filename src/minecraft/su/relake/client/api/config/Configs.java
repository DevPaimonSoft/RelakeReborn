package su.relake.client.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import su.relake.client.Main;
import su.relake.client.api.mod.*;
import su.relake.client.api.utils.render.util.IMinecraft;

import java.io.File;
import java.util.function.Consumer;

public class Configs implements IMinecraft {
    private final File file;
    private final String name;

    public Configs(String name) {
        this.name = name;
        this.file = new File(new File(Minecraft.getInstance().gameDirectory, "\\relake\\configs"), name + ".cfg");
    }

    public void loadConfig(JsonObject jsonObject) {
        if (jsonObject == null) {
            return;
        }

        if (jsonObject.has("functions")) {
            loadFunctionSettings(jsonObject.getAsJsonObject("functions"));
        }
    }

    private void loadFunctionSettings(JsonObject functionsObject) {
        Main.getInstance().getModBus().getMods().forEach(f -> {
            JsonObject moduleObject = functionsObject.getAsJsonObject(f.getName().toLowerCase());
            if (moduleObject == null) {
                return;
            }

            f.setToggled(false);
            loadSettingFromJson(moduleObject, "bind", value -> f.setBind(value.getAsInt()));
            loadSettingFromJson(moduleObject, "enable", value -> f.setToggled(value.getAsBoolean()));
            f.getSettings().forEach(setting -> loadIndividualSetting(moduleObject, setting));
        });
    }

    private void loadIndividualSetting(JsonObject moduleObject, Setting<?> configSetting) {
        JsonElement settingElement = moduleObject.get(configSetting.getName());

        if (settingElement == null || settingElement.isJsonNull()) {
            return;
        }

        if (configSetting instanceof SliderSetting) {
            ((SliderSetting) configSetting).setValue(settingElement.getAsFloat());
        }
        if (configSetting instanceof BooleanSetting) {
            ((BooleanSetting) configSetting).setValue(settingElement.getAsBoolean());
        }
        if (configSetting instanceof ColorPickerSetting) {
            ((ColorPickerSetting) configSetting).setValue(settingElement.getAsInt());
        }
        if (configSetting instanceof ModeSetting) {
            ((ModeSetting) configSetting).setValue(settingElement.getAsString());
        }
//        if (configSetting instanceof KeyBindSetting) {
//            ((KeyBindSetting) configSetting).setValues(settingElement.getAsInt());
//        }
        if (configSetting instanceof InputSetting) {
            ((InputSetting) configSetting).setValue(settingElement.getAsString());
        }
        if (configSetting instanceof MultiSelectSetting) {
            loadModeListSetting((MultiSelectSetting) configSetting, moduleObject);
        }
    }

    private void loadModeListSetting(MultiSelectSetting setting, JsonObject moduleObject) {
        JsonObject elements = moduleObject.getAsJsonObject(setting.getName());
//        setting.getOptions().forEach(option -> {
//            JsonElement optionElement = elements.get(option);
//            if (optionElement != null && !optionElement.isJsonNull()) {
//
//                if (optionElement.getAsBoolean()) {
//
//
//
//                    option.setValues(optionElement.getAsBoolean());
//
//                }
//            }
//        });
    }

    private void loadSettingFromJson(JsonObject jsonObject, String key, Consumer<JsonElement> consumer) {
        JsonElement element = jsonObject.get(key);
        if (element != null && !element.isJsonNull()) {
            consumer.accept(element);
        }
    }

    public JsonElement saveConfig() {
        JsonObject functionsObject = new JsonObject();
        JsonObject stylesObject = new JsonObject();

        saveFunctionSettings(functionsObject);

        JsonObject newObject = new JsonObject();
        newObject.add("functions", functionsObject);
        newObject.add("styles", stylesObject);

        return newObject;
    }

    private void saveFunctionSettings(JsonObject functionsObject) {
        Main.getInstance().getModBus().getMods().forEach(module -> {
            JsonObject moduleObject = new JsonObject();

            moduleObject.addProperty("bind", module.getBind());
            moduleObject.addProperty("enable", module.isState());

            module.getSettings().forEach(setting -> saveIndividualSetting(moduleObject, setting));

            functionsObject.add(module.getName().toLowerCase(), moduleObject);
        });
    }

    private void saveIndividualSetting(JsonObject moduleObject, Setting<?> configSetting) {
        if (configSetting instanceof BooleanSetting) {
            moduleObject.addProperty(configSetting.getName(), ((BooleanSetting) configSetting).getValue());
        }
        if (configSetting instanceof SliderSetting) {
            moduleObject.addProperty(configSetting.getName(), ((SliderSetting) configSetting).getValue());
        }
        if (configSetting instanceof ModeSetting) {
            moduleObject.addProperty(configSetting.getName(), ((ModeSetting) configSetting).getValue());
        }
        if (configSetting instanceof ColorPickerSetting) {
            moduleObject.addProperty(configSetting.getName(), ((ColorPickerSetting) configSetting).getValue());
        }
//        if (configSetting instanceof KeyBindSetting) {
//            moduleObject.addProperty(configSetting.getName(), ((KeyBindSetting) configSetting).getValue());
//        }
        if (configSetting instanceof InputSetting) {
            moduleObject.addProperty(configSetting.getName(), ((InputSetting) configSetting).getValue());
        }
        if (configSetting instanceof MultiSelectSetting) {
//            saveModeListSetting(moduleObject, (MultiSelectSetting) configSetting);
        }
    }

//    private void saveModeListSetting(JsonObject moduleObject, ListSetting setting) {
//        JsonObject elements = new JsonObject();
//        setting.get().forEach(option -> elements.addProperty(option.name, option.get()));
//        moduleObject.add(setting.name, elements);
//    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }
}