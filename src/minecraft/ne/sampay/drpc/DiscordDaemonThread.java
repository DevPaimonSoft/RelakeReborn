package ne.sampay.drpc;

import lombok.extern.log4j.Log4j2;
import ne.sampay.drpc.utils.DiscordRPC;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Manages periodic Discord RPC callbacks in a separate thread.
 */
@Log4j2
public class DiscordDaemonThread {

    private final DiscordManager discordManager;
    private final ExecutorService executor;
    private volatile boolean running = true;

    public DiscordDaemonThread(DiscordManager discordManager) {
        this.discordManager = discordManager;
        this.executor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r, "Discord-RPC");
            thread.setDaemon(true);
            return thread;
        });
    }

    /**
     * Starts the daemon thread to handle Discord RPC callbacks.
     */
    public void start() {
        executor.submit(() -> {
            try {
                while (running && discordManager.isRunning()) {
                    DiscordRPC.discordRunCallbacks();
                    Thread.sleep(5000); // Run every 5 seconds
                }
            } catch (InterruptedException e) {
                log.info("Discord daemon thread interrupted.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("Error in Discord daemon thread: {}", e.getMessage());
                discordManager.stopRPC();
            }
        });
        log.info("Discord daemon thread started.");
    }

    /**
     * Shuts down the daemon thread gracefully.
     */
    public void shutdown() {
        running = false;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                log.warn("Discord daemon thread did not terminate gracefully.");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            log.error("Interrupted during daemon thread shutdown: {}", e.getMessage());
        }
    }
}