package cc.funkemunky.ComboMode.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import cc.funkemunky.ComboMode.Core;

public class JoinListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(Core.getInstance().isComboMode()) {
			Player player = e.getPlayer();
			player.setMaximumNoDamageTicks(1);
		}
	}

}
