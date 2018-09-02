package cc.funkemunky.Meme;

import lombok.Getter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import cc.funkemunky.Meme.listeners.AllListeners;

@Getter
public class Tab extends JavaPlugin{

	public static Tab instance
			;
	private Permission permission;

	public void onEnable() {
		instance = this;

		Bukkit.getPluginManager().registerEvents(new AllListeners(), this);

		setupPermissions();
		saveDefaultConfig();
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}
}