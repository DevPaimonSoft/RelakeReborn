package ne.sampay.drpc.utils;

import com.sun.jna.Structure;
import ne.sampay.drpc.callbacks.*;

import java.util.Arrays;
import java.util.List;

/**
 * Holds references to all registered Discord event handlers.
 */
public class DiscordEventHandlers extends Structure {

	public ReadyCallback ready;
	public DisconnectedCallback disconnected;
	public ErroredCallback errored;
	public JoinGameCallback joinGame;
	public SpectateGameCallback spectateGame;
	public JoinRequestCallback joinRequest;

	@Override
	public List<String> getFieldOrder() {
		return Arrays.asList("ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest");
	}

	public static class Builder {
		private final DiscordEventHandlers handlers;

		public Builder() {
			handlers = new DiscordEventHandlers();
		}

		public Builder setReadyEventHandler(ReadyCallback ready) {
			handlers.ready = ready;
			return this;
		}

		public Builder setDisconnectedEventHandler(DisconnectedCallback disconnected) {
			handlers.disconnected = disconnected;
			return this;
		}

		public Builder setErroredEventHandler(ErroredCallback errored) {
			handlers.errored = errored;
			return this;
		}

		public Builder setJoinGameEventHandler(JoinGameCallback joinGame) {
			handlers.joinGame = joinGame;
			return this;
		}

		public Builder setSpectateGameEventHandler(SpectateGameCallback spectateGame) {
			handlers.spectateGame = spectateGame;
			return this;
		}

		public Builder setJoinRequestEventHandler(JoinRequestCallback joinRequest) {
			handlers.joinRequest = joinRequest;
			return this;
		}

		public DiscordEventHandlers build() {
			return handlers;
		}
	}
}