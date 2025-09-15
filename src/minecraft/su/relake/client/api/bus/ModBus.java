package su.relake.client.api.bus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import meteordevelopment.orbit.EventHandler;
import su.relake.client.Main;
import su.relake.client.api.context.implement.KeyPressEvent;
import su.relake.client.api.mod.ModBase;
import su.relake.client.impl.mods.combat.AutoGoldenAppleModProcessor;
import su.relake.client.impl.mods.combat.KillAuraProcessor;
import su.relake.client.impl.mods.combat.TrigerBotProcessor;
import su.relake.client.impl.mods.combat.VelocityModProcessor;
import su.relake.client.impl.mods.movement.AutoSprintProcessor;
import su.relake.client.impl.mods.movement.InvMoveModProcessor;
import su.relake.client.impl.mods.movement.NoSlowModProcessor;
import su.relake.client.impl.mods.movement.SpeedModProcessor;
import su.relake.client.impl.mods.player.FreeCameraMod;
import su.relake.client.impl.mods.render.*;
import su.relake.client.impl.mods.render.*;
import su.relake.client.impl.mods.utils.ItemScrollerModProcessor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModBus {

    private static final ModBus INSTANCE = new ModBus();
    private static boolean initialized = false;

    List<ModBase> mods;

    public ItemScrollerModProcessor itemScrollerModProcessor;

    public ItemPhysicModProcessor itemPhysicModProcessor;

    public SwingAnimationModProcessor swingAnimationModProcessor;

    public FreeCameraMod freeCameraMod;

    public NoSlowModProcessor noSlowModProcessor;

    private ModBus() {
        mods = new CopyOnWriteArrayList<>();

        Main.getInstance()
                .getEventHandler()
                .subscribe(this);
    }

    public static ModBus getInstance() {
        return INSTANCE;
    }

    public void processModInitialization() {
        if (initialized) return;
        mods.clear();
        mods.add(new KillAuraProcessor());
        mods.add(new TrigerBotProcessor());
        mods.add(new Test());
        mods.add(new AutoSprintProcessor());
        mods.add(new HudProcessor());
        mods.add(new InvMoveModProcessor());
        mods.add(new GuiModProcessor());
        mods.add(new AutoGoldenAppleModProcessor());
        mods.add(new SpeedModProcessor());
        mods.add(itemScrollerModProcessor = new ItemScrollerModProcessor());
        mods.add(new FullBrightModProcessor());
        mods.add(itemPhysicModProcessor = new ItemPhysicModProcessor());
        mods.add(swingAnimationModProcessor = new SwingAnimationModProcessor());
        mods.add(new VelocityModProcessor());
        mods.add(freeCameraMod = new FreeCameraMod());
        mods.add(noSlowModProcessor = new NoSlowModProcessor());

        mods.forEach(ModBase::initialize);

        mods = mods.stream().sorted((f1, f2) -> -Float.compare(f1.getName().length(), f2.getName().length())).toList();

        initialized = true;
    }

    @EventHandler
    public void onKeyPressContextInterceptor(KeyPressEvent keyPressContext) {
        int glfwKeyIntId = keyPressContext.getId();

        mods.forEach(modBase -> {
            if (modBase.getBind() == glfwKeyIntId) {
                modBase.setToggled(!modBase.isState());
            }
        });
    }
}