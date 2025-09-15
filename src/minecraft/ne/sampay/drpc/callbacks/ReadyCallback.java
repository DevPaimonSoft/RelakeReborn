package ne.sampay.drpc.callbacks;

import com.sun.jna.Callback;
import ne.sampay.drpc.utils.DiscordUser;

/**
 * Callback interface for when the Discord connection is established.
 */
public interface ReadyCallback extends Callback {
	/**
	 * Called when the Discord connection is ready.
	 *
	 * @param user Discord user information
	 */
	void apply(DiscordUser user);
}