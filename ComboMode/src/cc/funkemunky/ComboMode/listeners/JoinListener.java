package cc.funkemunky.ComboMode.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if(!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) {
			return;
		}
		if(Core.getInstance().isComboMode()) {
			Player player = (Player) e.getEntity();
			player.setNoDamageTicks(0);
		}
	}

}
