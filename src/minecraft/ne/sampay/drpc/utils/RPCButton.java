package ne.sampay.drpc.utils;

import lombok.Getter;

import java.io.Serializable;

/**
 * Represents a button for Discord Rich Presence.
 */
@Getter
public class RPCButton implements Serializable {
    private final String url;
    private final String label;

    /**
     * Creates a new RPC button with a label (max 31 chars) and URL.
     *
     * @param label Button label
     * @param url   Button URL
     * @return new RPCButton instance
     */
    public static RPCButton create(String label, String url) {
        if (label == null || label.isEmpty()) {
            throw new IllegalArgumentException("Button label cannot be null or empty.");
        }
        label = label.substring(0, Math.min(label.length(), 31));
        return new RPCButton(label, url);
    }

    private RPCButton(String label, String url) {
        this.label = label;
        this.url = url;
    }
}