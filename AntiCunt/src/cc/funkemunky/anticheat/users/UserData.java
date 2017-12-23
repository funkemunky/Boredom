package cc.funkemunky.anticheat.users;

import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import cc.funkemunky.anticheat.checks.Check;
import lombok.Getter;

public class UserData {
	
	@Getter private UUID uuid;
	@Getter private Map<Check, Integer> violations;
	
	
	public UserData(Player player) {
		this.uuid = player.getUniqueId();
	}

	public UUID getUUID() {
		return uuid;
	}


	public Map<Check, Integer> getViolations() {
		return violations;
	}
}
