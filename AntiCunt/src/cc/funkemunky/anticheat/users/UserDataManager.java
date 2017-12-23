package cc.funkemunky.anticheat.users;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UserDataManager {
	
	private List<UserData> users;
	
	public UserDataManager() {
		users = new ArrayList<UserData>();
	}
	
	public UserData getUser(UUID uuid) {
		for(UserData user : users) {
			if(user.getUUID() == uuid) {
				return user;
			}
		}
		return null;
	}
	
	public void createUser(Player player) {
		users.add(new UserData(player));
	}
	
	public void deleteUser(UserData user) {
		users.remove(user);
	}
	
	public void loadOnlinePlayers() {
		for(Player player : Bukkit.getOnlinePlayers()) {
		      createUser(player);
		}
	}

}
