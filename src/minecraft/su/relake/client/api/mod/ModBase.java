package su.relake.client.api.mod;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.lwjgl.glfw.GLFW;
import su.relake.client.Main;
import su.relake.client.api.utils.BaseMinecraftInterface;
import su.relake.client.api.utils.client.SoundPlayer;

import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class ModBase implements BaseMinecraftInterface {
    String name;
    String descRu;
    String descEng;
    boolean state;
    ModType modType;
    @Getter
    protected List<Setting<?>> settings;
    private SoundPlayer soundPlayer = new SoundPlayer();
    @Setter
    int bind;

    public ModBase() {
        this.settings = new ArrayList<>();
    }

    public abstract void initialize();

    protected void initialize(String name, ModType modType) {
        this.name = name;
        this.modType = modType;
        this.bind = GLFW.GLFW_KEY_UNKNOWN;
        this.state = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescRU(String descRu) {
        this.descRu = descRu;
    }

    public void setDescENG(String descEng) {
        this.descEng = descEng;
    }

    public void setModuleCategory(String category) {
        this.modType = ModType.valueOf(category.toUpperCase());
    }

    public void registerComponent(Setting<?>... components) {
        settings.addAll(List.of(components));
    }

    public void setToggled(boolean state) {
        this.state = state;
        if (state) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void onEnable() {
        Main.getInstance()
                .getEventHandler()
                .subscribe(this);
        soundPlayer.play("enable.wav");
        soundPlayer.setVolume(0.07f);
    }

    public void onDisable() {
        Main.getInstance()
                .getEventHandler()
                .unsubscribe(this);
        soundPlayer.play("disable.wav");
        soundPlayer.setVolume(0.07f);
    }
}