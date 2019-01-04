package cc.funkemunky.kit.listeners;

import cc.funkemunky.kit.Kit;
import cc.funkemunky.kit.utils.LocationUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.PlayerInventory;

public class ProtectionListeners implements Listener {

    @EventHandler
    public void onEvent(BlockBreakEvent event) {
        event.setCancelled(event.getPlayer().getGameMode() != GameMode.CREATIVE);
    }

    @EventHandler
    public void onEvent(BlockPlaceEvent event) {
        event.setCancelled(event.getPlayer().getGameMode() != GameMode.CREATIVE);
    }

    @EventHandler
    public void onEvent(InventoryInteractEvent event) {
        if(event.getInventory() instanceof PlayerInventory) {
            event.setCancelled(!LocationUtil.isWithinSpawn(event.getWhoClicked()) && event.getWhoClicked().getGameMode() != GameMode.CREATIVE && !event.getWhoClicked().isOp());
        }
    }

    @EventHandler
    public void onEvent(PlayerDropItemEvent event) {
        event.setCancelled(!LocationUtil.isWithinSpawn(event.getPlayer()) && event.getPlayer().getGameMode() != GameMode.CREATIVE);
    }

    @EventHandler
    public void onEvent(ExplosionPrimeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEvent(PlayerJoinEvent event) {
        if(!Kit.getInstance().getDataManager().hasUserData(event.getPlayer().getUniqueId())) {
            Kit.getInstance().getDataManager().createUserData(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onEvent(PlayerPickupItemEvent event) {
        event.setCancelled(!event.getItem().getItemStack().getType().equals(Material.GOLDEN_APPLE) && !LocationUtil.isWithinSpawn(event.getPlayer()) && event.getPlayer().getGameMode() != GameMode.CREATIVE);
    }

    @EventHandler
    public void onEvent(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if(LocationUtil.isWithinSpawn(player)) {
                event.setCancelled(true);
            }
        }
    }


}
