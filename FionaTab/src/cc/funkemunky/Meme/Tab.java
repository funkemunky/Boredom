package cc.funkemunky.Meme;

import org.bukkit.plugin.java.JavaPlugin;

public class Tab extends JavaPlugin{

	private static Tab instance;

	public void onEnable() {
		instance = this;
		
		new me.vertises.aztec.tablist.TablistManager(this, new TabListener(this), 100L);
	}
	
	public static Tab getInstance() {
		return instance;
	}

}
