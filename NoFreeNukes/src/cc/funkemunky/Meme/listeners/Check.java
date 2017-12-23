package cc.funkemunky.Meme.listeners;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import cc.funkemunky.Meme.utils.Utils;

public class Check implements Listener {
	
	@EventHandler
	public void checkFreecam(PlayerInteractEvent e) {
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK) {
			return;
		}
		 boolean isValid = false;
	      Player player = e.getPlayer();
	      Location scanLocation = e.getClickedBlock().getRelative(e.getBlockFace()).getLocation();
	      double x = scanLocation.getX();
	      double y = scanLocation.getY();
	      double z = scanLocation.getZ();
	      for (double sX = x; sX < x + 2.0D; sX += 1.0D) {
	        for (double sY = y; sY < y + 2.0D; sY += 1.0D) {
	          for (double sZ = z; sZ < z + 2.0D; sZ += 1.0D)
	          {
	            Location relative = new Location(scanLocation.getWorld(), sX, sY, sZ);
	            List<Location> blocks = Utils.rayTrace(player.getLocation(), relative);
	            boolean valid = true;
	            for (Location l : blocks) {
	              if (!Utils.checkPhase(l.getBlock().getType())) {
	                valid = false;
	              }
	            }
	            if (valid) {
	              isValid = true;
	            }
	          }
	        }
	      }
	      if ((!isValid) && 
	        (!player.getPlayer().getItemInHand().getType().equals(Material.ENDER_PEARL))) {
	        e.setCancelled(true);
	      }
	}


}
