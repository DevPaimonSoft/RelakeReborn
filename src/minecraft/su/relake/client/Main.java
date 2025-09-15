package su.relake.client;

import lombok.Getter;
import meteordevelopment.orbit.EventBus;
import ne.sampay.drpc.DiscordManager;
import su.relake.client.api.bus.ModBus;
import su.relake.client.api.config.ConfigsStorage;

import java.lang.invoke.MethodHandles;

@Getter
public class Main {

    @Getter
    private static Main instance;

    private EventBus eventHandler;
    private DiscordManager discordManager;
    private ModBus modBus;
    private ConfigsStorage configsStorage;


    public Main() {

        instance = this;

        init();

    }


    public void init() {
        eventHandler = new EventBus();


        System.out.println(this.getClass().getPackageName());

        eventHandler.registerLambdaFactory(this.getClass().getPackageName().contains("su") ? "su" : "yumesoft", (method, clazz) -> (MethodHandles.Lookup) method.invoke(null, clazz, MethodHandles.lookup()));

        modBus = ModBus.getInstance();
        configsStorage = new ConfigsStorage();

        modBus.processModInitialization();
        discordManager = new DiscordManager();
        discordManager.init();

//        commands.add(new ConfigsCommand(configsStorage, prefix, logger));

        try {
            configsStorage.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
