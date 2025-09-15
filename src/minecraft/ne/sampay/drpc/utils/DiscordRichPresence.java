package ne.sampay.drpc.utils;

import com.sun.jna.Structure;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a Discord Rich Presence structure for JNA.
 */
@Getter
public class DiscordRichPresence extends Structure {

	public String state;
	public String details;
	public long startTimestamp;
	public long endTimestamp;
	public String largeImageKey;
	public String largeImageText;
	public String smallImageKey;
	public String smallImageText;
	public String partyId;
	public int partySize;
	public int partyMax;
	@Deprecated
	public String matchSecret;
	public String spectateSecret;
	public String joinSecret;
	@Deprecated
	public int instance;
	public String button_url_1;
	public String button_label_1;
	public String button_url_2;
	public String button_label_2;

	@Override
	public List<String> getFieldOrder() {
		return Arrays.asList("state", "details", "startTimestamp", "endTimestamp", "largeImageKey", "largeImageText",
				"smallImageKey", "smallImageText", "partyId", "partySize", "partyMax", "matchSecret", "joinSecret",
				"spectateSecret", "button_label_1", "button_url_1", "button_label_2", "button_url_2", "instance");
	}

	public static class Builder {
		private final DiscordRichPresence presence;

		public Builder(String state) {
			presence = new DiscordRichPresence();
			presence.state = state;
		}

		public Builder setDetails(String details) {
			presence.details = details;
			return this;
		}

		public Builder setStartTimestamps(long start) {
			presence.startTimestamp = start;
			return this;
		}

		public Builder setEndTimestamp(long end) {
			presence.endTimestamp = end;
			return this;
		}

		public Builder setBigImage(String key, String text) {
			if (text != null && !text.isEmpty() && key == null) {
				throw new IllegalArgumentException("Image key must not be null when assigning hover text.");
			}
			presence.largeImageKey = key;
			presence.largeImageText = text;
			return this;
		}

		public Builder setSmallImage(String key, String text) {
			if (text != null && !text.isEmpty() && key == null) {
				throw new IllegalArgumentException("Image key must not be null when assigning hover text.");
			}
			presence.smallImageKey = key;
			presence.smallImageText = text;
			return this;
		}

		public Builder setParty(String partyId, int size, int max) {
			presence.partyId = partyId;
			presence.partySize = size;
			presence.partyMax = max;
			return this;
		}

		@Deprecated
		public Builder setSecrets(String match, String join, String spectate) {
			presence.matchSecret = match;
			presence.joinSecret = join;
			presence.spectateSecret = spectate;
			return this;
		}

		public Builder setSecrets(String join, String spectate) {
			presence.joinSecret = join;
			presence.spectateSecret = spectate;
			return this;
		}

		@Deprecated
		public Builder setInstance(boolean instance) {
			presence.instance = instance ? 1 : 0;
			return this;
		}

		public Builder setButtons(List<RPCButton> buttons) {
			if (buttons != null && !buttons.isEmpty()) {
				int buttonCount = Math.min(buttons.size(), 2);
				presence.button_label_1 = buttons.get(0).getLabel();
				presence.button_url_1 = buttons.get(0).getUrl();
				if (buttonCount == 2) {
					presence.button_label_2 = buttons.get(1).getLabel();
					presence.button_url_2 = buttons.get(1).getUrl();
				}
			}
			return this;
		}

		public DiscordRichPresence build() {
			return presence;
		}
	}
}