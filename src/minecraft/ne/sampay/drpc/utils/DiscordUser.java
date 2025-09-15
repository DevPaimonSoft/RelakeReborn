package ne.sampay.drpc.utils;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a Discord user structure for JNA.
 */
public class DiscordUser extends Structure {

	public String userId;
	public String username;
	public String discriminator;
	public String avatar;

	@Override
	public List<String> getFieldOrder() {
		return Arrays.asList("userId", "username", "discriminator", "avatar");
	}
}