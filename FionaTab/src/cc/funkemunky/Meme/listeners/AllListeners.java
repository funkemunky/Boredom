package cc.funkemunky.Meme.listeners;

import java.util.*;

import cc.funkemunky.Meme.utils.Color;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import cc.funkemunky.Meme.Tab;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class AllListeners implements Listener {

	private Map<Entity, Location> entities;

	public AllListeners() {
		entities = new HashMap<>();
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlockPlaced();
		
		if(block.getType().equals(Material.BRICK)) {
			player.getInventory().addItem(new ItemStack(Material.BRICK, 1));
			new BukkitRunnable() {
				public void run() {
					block.setType(Material.AIR);
				}
			}.runTaskLater(Tab.getInstance(), 120L);
		}
	}

	@EventHandler
	public void death(EntityDeathEvent e) {
		e.getDrops().clear();
		e.setDroppedExp(0);
	}

	@EventHandler
	public void onColorSign(SignChangeEvent event) {
		int i = 0;
		for(String line : event.getLines()) {
			event.setLine(i, Color.translate(line));
			i++;
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location one = new Location(player.getWorld(), 107, 107, 355);
		Location two = new Location(player.getWorld(), 135, 200, 387);
		
		if(isInRect(event.getTo(), one, two) && !isInRect(event.getFrom(), one, two)) {
			player.getInventory().clear();
			player.getInventory().addItem(new ItemStack(Material.BRICK, 10));
		} else if(!isInRect(event.getTo(), one, two) && isInRect(event.getFrom(), one, two)) {
			player.getInventory().clear();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();

		PermissionUser user = PermissionsEx.getUser(player);

		if(user.getPrefix(player.getWorld().getName()).contains("Owner")) {
			player.setPlayerListName(Color.Dark_Red + Color.Italics + player.getName());
		} else if(user.getPrefix(player.getWorld().getName()).contains("Developer")
				|| user.getPrefix(player.getWorld().getName()).contains("Admin")) {
			player.setPlayerListName(Color.Red + Color.Italics + player.getName());
		} else if(user.getPrefix(player.getWorld().getName()).contains("Test")) {
			player.setPlayerListName(Color.Gold + player.getName());
		}
	}

	@EventHandler
	public void damage(EntityDamageByEntityEvent e) {
		if(e.getEntity().getType() == EntityType.SHEEP && entities.containsKey(e.getEntity())) {
			Sheep sheep = (Sheep) e.getEntity();

			e.setCancelled(true);
			entities.remove(e.getEntity());
			sheep.setHealth(0);
		}
	}

	@EventHandler
	public void entitySpawn(EntitySpawnEvent e) {
		if(e.getEntityType() == EntityType.SHEEP) {
			Sheep sheep = (Sheep) e.getEntity();
			sheep.setColor(DyeColor.BLUE);
			entities.put(e.getEntity(), e.getEntity().getLocation());
		}
	}

	@EventHandler
	public void entityDeath(EntityDeathEvent e) {
		if(e.getEntityType() == EntityType.SHEEP && entities.containsKey(e.getEntity())) {
			entities.remove(e.getEntity());
		}
	}
	@EventHandler
	public void entityTarget(EntityTargetEvent e) {
		e.setCancelled(true);
	}
	
	private boolean isInRect(Location player, Location loc1, Location loc2)
	{
	    double[] dim = new double[2];
	 
	    dim[0] = loc1.getX();
	    dim[1] = loc2.getX();
	    Arrays.sort(dim);
	    if(player.getX() > dim[1] || player.getX() < dim[0])
	        return false;
	 
	    dim[0] = loc1.getZ();
	    dim[1] = loc2.getZ();
	    Arrays.sort(dim);
	    if(player.getZ() > dim[1] || player.getZ() < dim[0])
	        return false;
	 
	    dim[0] = loc1.getY();
	    dim[1] = loc2.getY();
	    Arrays.sort(dim);
		return !(player.getY() > dim[1]) && !(player.getY() < dim[0]);
	}

}
