package cc.funkemunky.Meme;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import cc.funkemunky.Meme.listeners.Check;

public class Core extends JavaPlugin {
	
	public void onEnable() {
		getServer().getLogger().log(Level.INFO, "Successfully enabled NoFreeNukes, the plugin that prevents "
				+ "freecams and nukers from giving you grief.");
		
		getServer().getPluginManager().registerEvents(new Check(), this);
	}
	
	

}
