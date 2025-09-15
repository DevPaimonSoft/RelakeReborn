package ne.sampay.drpc.callbacks;

import com.sun.jna.Callback;

/**
 * Callback interface for when the Discord connection is disconnected.
 */
public interface DisconnectedCallback extends Callback {
	/**
	 * Called when the Discord connection is disconnected.
	 *
	 * @param errorCode Error code
	 * @param message   Error message
	 */
	void apply(int errorCode, String message);
}