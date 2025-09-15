package ne.sampay.drpc.callbacks;

import com.sun.jna.Callback;

/**
 * Callback interface for when a Discord error occurs.
 */
public interface ErroredCallback extends Callback {
	/**
	 * Called when an error occurs in Discord RPC.
	 *
	 * @param errorCode Error code
	 * @param message   Error message
	 */
	void apply(int errorCode, String message);
}