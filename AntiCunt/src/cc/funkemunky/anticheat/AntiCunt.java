package cc.funkemunky.anticheat;

import org.bukkit.plugin.java.JavaPlugin;

import cc.funkemunky.anticheat.checks.Check;
import cc.funkemunky.anticheat.checks.CheckManager;
import lombok.Getter;

public class AntiCunt extends JavaPlugin {
	
	@Getter private static AntiCunt instance;
	@Getter private CheckManager checkManager;
	
	
	public void onEnable() {
		//Registering Instances
		instance = new AntiCunt();
		checkManager = new CheckManager();
		
		//Registering Commands
		
		//Register Listeners
	}

	public static AntiCunt getInstance() {
		return instance;
	}

	public CheckManager getCheckManager() {
		return checkManager;
	}
	
	public void registerListeners() {
		for(Check check : getCheckManager().getChecks()) {
			getServer().getPluginManager().registerEvents(check, this);
		}
	}
}
