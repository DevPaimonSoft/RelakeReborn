package ne.sampay.drpc.callbacks;

import com.sun.jna.Callback;
import ne.sampay.drpc.utils.DiscordUser;

/**
 * Callback interface for when a join request is received.
 */
public interface JoinRequestCallback extends Callback {
	/**
	 * Called when another player requests to join a game.
	 *
	 * @param request Discord user requesting to join
	 */
	void apply(DiscordUser request);
}