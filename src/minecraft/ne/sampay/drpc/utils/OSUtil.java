package ne.sampay.drpc.utils;

import lombok.Getter;

/**
 * Utility class to determine the operating system.
 */
@Getter
public final class OSUtil {

	private final String osName = System.getProperty("os.name").toLowerCase();

	public boolean isMac() {
		return osName.startsWith("mac");
	}

	public boolean isWindows() {
		return osName.startsWith("win");
	}
}