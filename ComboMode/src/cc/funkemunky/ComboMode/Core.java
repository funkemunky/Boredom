package cc.funkemunky.ComboMode;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import cc.funkemunky.ComboMode.commands.ToggleComboCommand;
import cc.funkemunky.ComboMode.listeners.JoinListener;

public class Core extends JavaPlugin {
	
	private boolean isCombo;
	private static Core core;
	
	public static Core getInstance() {
		return core;
	}
	
	public boolean isComboMode() {
		return isCombo;
	}
	
	public void setComboMode(boolean combo) {
		isCombo = combo;
	}
	
	public void onEnable() {
		//Registration of objects.
		isCombo = false;
		core = this;
		
		//Registration of Listeners
		Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(), this);
		
		//Registration of Commands
		getCommand("togglecombo").setExecutor(new ToggleComboCommand());
	}

}
