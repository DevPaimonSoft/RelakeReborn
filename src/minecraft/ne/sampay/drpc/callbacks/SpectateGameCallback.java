package ne.sampay.drpc.callbacks;

import com.sun.jna.Callback;

/**
 * Callback interface for when a player spectates a game.
 */
public interface SpectateGameCallback extends Callback {
	/**
	 * Called when a player spectates a game.
	 *
	 * @param spectateSecret Unique string for spectating the game
	 */
	void apply(String spectateSecret);
}