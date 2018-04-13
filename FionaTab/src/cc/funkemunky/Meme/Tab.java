package cc.funkemunky.Meme;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import cc.funkemunky.Meme.listeners.AllListeners;

public class Tab extends JavaPlugin{

	private static Tab instance;

	public void onEnable() {
		instance = this;

		Bukkit.getPluginManager().registerEvents(new AllListeners(), this);

		saveDefaultConfig();
	}

	
	public static Tab getInstance() {
		return instance;
	}
}