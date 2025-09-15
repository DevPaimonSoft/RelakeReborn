package ne.sampay.drpc.utils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Java wrapper for the Discord-RPC library to manage Discord Rich Presence.
 */
@Log4j2
public final class DiscordRPC {

	static {
		loadDLL();
	}

	/**
	 * Initializes Discord RPC with the given application ID and event handlers.
	 *
	 * @param applicationId Application/Client ID
	 * @param handlers      Event handlers for Discord events
	 * @param autoRegister  Whether to auto-register the application
	 */
	public static void discordInitialize(String applicationId, DiscordEventHandlers handlers, boolean autoRegister) {
		DLL.INSTANCE.Discord_Initialize(applicationId, handlers, autoRegister ? 1 : 0, null);
		log.debug("Initialized Discord RPC with application ID: {}", applicationId);
	}

	/**
	 * Initializes Discord RPC for a Steam application.
	 *
	 * @param applicationId Application/Client ID
	 * @param handlers      Event handlers for Discord events
	 * @param autoRegister  Whether to auto-register the application
	 * @param steamId       Steam App ID
	 */
	public static void discordInitialize(String applicationId, DiscordEventHandlers handlers, boolean autoRegister, String steamId) {
		DLL.INSTANCE.Discord_Initialize(applicationId, handlers, autoRegister ? 1 : 0, steamId);
		log.debug("Initialized Discord RPC for Steam with application ID: {}, Steam ID: {}", applicationId, steamId);
	}

	/**
	 * Registers the executable of the application/game.
	 *
	 * @param applicationId Application/Client ID
	 * @param command       Launch command of the application
	 */
	public static void discordRegister(String applicationId, String command) {
		DLL.INSTANCE.Discord_Register(applicationId, command);
		log.debug("Registered application with ID: {} and command: {}", applicationId, command);
	}

	/**
	 * Registers a Steam application.
	 *
	 * @param applicationId Application/Client ID
	 * @param steamId       Steam App ID
	 */
	public static void discordRegisterSteam(String applicationId, String steamId) {
		DLL.INSTANCE.Discord_RegisterSteamGame(applicationId, steamId);
		log.debug("Registered Steam application with ID: {} and Steam ID: {}", applicationId, steamId);
	}

	/**
	 * Updates the registered event handlers.
	 *
	 * @param handlers Updated event handlers
	 */
	public static void discordUpdateEventHandlers(DiscordEventHandlers handlers) {
		DLL.INSTANCE.Discord_UpdateHandlers(handlers);
		log.debug("Updated Discord event handlers");
	}

	/**
	 * Shuts down the Discord RPC connection.
	 */
	public static void discordShutdown() {
		DLL.INSTANCE.Discord_Shutdown();
		log.debug("Discord RPC shut down");
	}

	/**
	 * Runs Discord RPC callbacks to process events.
	 */
	public static void discordRunCallbacks() {
		DLL.INSTANCE.Discord_RunCallbacks();
	}

	/**
	 * Updates the Discord Rich Presence.
	 *
	 * @param presence DiscordRichPresence instance
	 */
	public static void discordUpdatePresence(DiscordRichPresence presence) {
		DLL.INSTANCE.Discord_UpdatePresence(presence);
		log.debug("Updated Discord Rich Presence");
	}

	/**
	 * Clears the current Discord Rich Presence.
	 */
	public static void discordClearPresence() {
		DLL.INSTANCE.Discord_ClearPresence();
		log.debug("Cleared Discord Rich Presence");
	}

	/**
	 * Responds to a join/spectate request.
	 *
	 * @param userId User ID to respond to
	 * @param reply  Reply type (YES, NO, IGNORE)
	 */
	public static void discordRespond(String userId, DiscordReply reply) {
		DLL.INSTANCE.Discord_Respond(userId, reply.reply);
		log.debug("Responded to user {} with reply: {}", userId, reply);
	}

	private static void loadDLL() {
		String name = System.mapLibraryName("discord-rpc");
		OSUtil osUtil = new OSUtil();
		File homeDir;
		String finalPath;
		String tempPath;
		String dir;

		if (osUtil.isMac()) {
			homeDir = new File(System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support" + File.separator);
			dir = "darwin";
			tempPath = homeDir + File.separator + "discord-rpc" + File.separator + name;
		} else if (osUtil.isWindows()) {
			homeDir = new File(System.getenv("TEMP"));
			boolean is64bit = System.getProperty("sun.arch.data.model").equals("64");
			dir = (is64bit ? "win-x64" : "win-x86");
			tempPath = homeDir + File.separator + "discord-rpc" + File.separator + name;
		} else {
			finalPath = "/linux/" + name;
			try {
				File f = File.createTempFile("drpc", name);
				try (InputStream in = DiscordRPC.class.getResourceAsStream(finalPath); OutputStream out = openOutputStream(f)) {
					if (in == null) throw new FileNotFoundException("Native Linux .so library missing. Please open an issue. https://github.com/Vatuu/discord-rpc");
					copyFile(in, out);
					f.deleteOnExit();
					System.load(f.getAbsolutePath());
					log.info("Loaded Discord RPC native library: {}", f.getAbsolutePath());
					return;
				}
			} catch (IOException e) {
				log.error("Failed to load Discord RPC library: {}", e.getMessage());
				throw new RuntimeException("Unable to load Discord RPC library", e);
			}
		}

		finalPath = "/" + dir + "/" + name;
		File f = null;
		try {
			Path tempDirectoryPath = Files.createTempDirectory("drpc");
			f = new File(tempDirectoryPath + File.separator + name);

			try (InputStream in = DiscordRPC.class.getResourceAsStream(finalPath); OutputStream out = openOutputStream(f)) {
				copyFile(in, out);
				tempDirectoryPath.toFile().deleteOnExit();
				f.deleteOnExit();
			} catch (IOException e) {
				log.error("Failed to copy Discord RPC library: {}", e.getMessage());
				throw e;
			}

			System.load(f.getAbsolutePath());
			log.info("Loaded Discord RPC native library: {}", f.getAbsolutePath());
		} catch (Exception e) {
			log.error("Failed to load Discord RPC library: {}", e.getMessage());
			throw new RuntimeException("Unable to load Discord RPC library", e);
		}
	}

	private static void copyFile(final InputStream input, final OutputStream output) throws IOException {
		byte[] buffer = new byte[1024 * 4];
		int n;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
	}

	private static FileOutputStream openOutputStream(final File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file + "' exists but is a directory");
			}
			if (!file.canWrite()) {
				throw new IOException("File '" + file + "' cannot be written to");
			}
		} else {
			final File parent = file.getParentFile();
			if (parent != null) {
				if (!parent.mkdirs() && !parent.isDirectory()) {
					throw new IOException("Directory '" + parent + "' could not be created");
				}
			}
		}
		return new FileOutputStream(file);
	}

	public enum DiscordReply {
		NO(0),
		YES(1),
		IGNORE(2);

		public final int reply;

		DiscordReply(int reply) {
			this.reply = reply;
		}
	}

	private interface DLL extends Library {
		DLL INSTANCE = Native.load("discord-rpc", DLL.class);

		void Discord_Initialize(String applicationId, DiscordEventHandlers handlers, int autoRegister, String optionalSteamId);
		void Discord_Register(String applicationId, String command);
		void Discord_RegisterSteamGame(String applicationId, String steamId);
		void Discord_UpdateHandlers(DiscordEventHandlers handlers);
		void Discord_Shutdown();
		void Discord_RunCallbacks();
		void Discord_UpdatePresence(DiscordRichPresence presence);
		void Discord_ClearPresence();
		void Discord_Respond(String userId, int reply);
	}
}