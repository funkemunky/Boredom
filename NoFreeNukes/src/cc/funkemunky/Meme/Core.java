package cc.funkemunky.Meme;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import cc.funkemunky.Meme.commands.NoFreeNukesCmd;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import cc.funkemunky.Meme.listeners.Check;

public class Core extends JavaPlugin {

	private static Core core;
	public List<Player> hasAlertsOn;

	public static Core getCore() {
		return core;
	}

	public void sendAlert(String string) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player.hasPermission("nofreenukes.alerts") && hasAlertsOn.contains(player)) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', string));
			}
		}
	}

	public void onEnable() {
		core = this;
		hasAlertsOn = new ArrayList<>();

		getServer().getLogger().log(Level.INFO, "Successfully enabled NoFreeNukes v" + getDescription().getVersion() +  ", the plugin that prevents "
				+ "freecams and nukers from giving you grief.");
		saveDefaultConfig();

		//Commands and Listeners
		getServer().getPluginManager().registerEvents(new Check(), this);

		getCommand("nofreenukes").setExecutor(new NoFreeNukesCmd());
	}
	
	

}
