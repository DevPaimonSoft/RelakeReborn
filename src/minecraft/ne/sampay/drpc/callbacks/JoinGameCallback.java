package ne.sampay.drpc.callbacks;

import com.sun.jna.Callback;

/**
 * Callback interface for when a player joins a game.
 */
public interface JoinGameCallback extends Callback {
	/**
	 * Called when a player joins a game.
	 *
	 * @param joinSecret Unique string for joining the game
	 */
	void apply(String joinSecret);
}