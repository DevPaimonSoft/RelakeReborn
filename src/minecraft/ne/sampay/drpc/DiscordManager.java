package ne.sampay.drpc;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import ne.sampay.drpc.utils.*;
import su.relake.compiler.sdk.classes.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages Discord Rich Presence integration, handling initialization, updates, and shutdown.
 */
@Getter
@Log4j2
public class DiscordManager {

    private final DiscordDaemonThread discordDaemonThread;
    private boolean running = false;
    private DiscordUser discordUser;

    // Hardcoded configuration
    private static final String APPLICATION_ID = "1380189622327574630";
    private static final String LARGE_IMAGE_URL = "https://images.steamusercontent.com/ugc/449611652050198394/003B0F458420C44A75D10CBDC94A9C0B964C06F7/?imw=5000&imh=5000&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=false";
    private static final String DISCORD_URL = "https://discord.gg/QK2UtECBF4";
    private static final String PURCHASE_URL = "https://relakeclient.fun/";
    private static final String APP_VERSION = "Beta 0.1";

    public DiscordManager() {
        this.discordDaemonThread = new DiscordDaemonThread(this);
    }

    /**
     * Initializes Discord Rich Presence with predefined settings.
     *
     * @return this instance for method chaining
     */
    public DiscordManager init() {
        String details = Profile.getUsername() + " - " + Profile.getUid();
        DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder("Relake " + APP_VERSION)
                .setDetails(details)
                .setBigImage(LARGE_IMAGE_URL, details);

        List<RPCButton> buttons = new ArrayList<>();
        buttons.add(RPCButton.create("Purchase", PURCHASE_URL));
        buttons.add(RPCButton.create("Discord", DISCORD_URL));
        builder.setButtons(buttons);

        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder()
                .setReadyEventHandler(user -> {
                    running = true;
                    discordUser = user;
                    log.info("Discord Rich Presence initialized for user: {}", user.username);
                })
                .setErroredEventHandler((errorCode, message) -> log.error("Discord error [{}]: {}", errorCode, message))
                .setDisconnectedEventHandler((errorCode, message) -> {
                    log.warn("Discord disconnected [{}]: {}", errorCode, message);
                    stopRPC();
                })
                .build();

        DiscordRPC.discordInitialize(APPLICATION_ID, handlers, true);
        DiscordRPC.discordUpdatePresence(builder.build());
        discordDaemonThread.start();

        return this;
    }

    /**
     * Updates the Discord Rich Presence with new details.
     *
     * @param details the new details to display
     */

    public void updatePresence(String details) {
        if (!running) {
            log.warn("Cannot update presence: Discord RPC is not running.");
            return;
        }
        DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder("Relake " + APP_VERSION)
                .setDetails(details)
                .setBigImage(LARGE_IMAGE_URL, details);
        List<RPCButton> buttons = new ArrayList<>();
        buttons.add(RPCButton.create("Purchase", PURCHASE_URL));
        buttons.add(RPCButton.create("Discord", DISCORD_URL));
        builder.setButtons(buttons);
        DiscordRPC.discordUpdatePresence(builder.build());
        log.info("Updated Discord Rich Presence with details: {}", details);
    }

    /**
     * Stops the Discord Rich Presence and cleans up resources.
     */

    public void stopRPC() {
        if (!running) {
            return;
        }
        DiscordRPC.discordShutdown();
        discordDaemonThread.shutdown();
        running = false;
        log.info("Discord Rich Presence stopped.");
    }
}