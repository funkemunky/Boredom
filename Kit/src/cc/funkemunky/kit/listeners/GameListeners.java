package cc.funkemunky.kit.listeners;

import cc.funkemunky.api.utils.Color;
import cc.funkemunky.kit.Kit;
import cc.funkemunky.kit.data.UserData;
import cc.funkemunky.kit.utils.LocationUtil;
import cc.funkemunky.kit.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class GameListeners implements Listener {

    @EventHandler
    public void onEvent(PlayerRespawnEvent event) {
        if(LocationUtil.getSpawnLocation() != null) {
            event.setRespawnLocation(LocationUtil.getSpawnLocation());
        }

        PlayerUtils.applyKit(event.getPlayer());
    }

    @EventHandler
    public void onEvent(PlayerDeathEvent event) {
        event.setDroppedExp(0);
        event.setDeathMessage(null);
        event.getDrops().clear();
    }

    @EventHandler
    public void onEvent(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player killer = (Player) event.getDamager(), victim = (Player) event.getEntity();

            if(victim.getHealth() - event.getFinalDamage() <= 0) {
                PlayerUtils.handleDeath(victim, killer);
                PlayerUtils.addGoldenApple(killer);
                event.setCancelled(true);

                killer.sendMessage(Color.Gray + "You have received " + Color.Gold + "5.0" + Color.Gray + " coins for killing " + Color.Yellow + victim.getName() + Color.Gray + ".");
            }
        }
    }

    @EventHandler
    public void onEvent(EntityDamageEvent event) {
        event.setCancelled(event.getCause().equals(EntityDamageEvent.DamageCause.FALL));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent(PlayerPickupItemEvent event) {
        if(event.getItem().getItemStack().getType().equals(Material.GOLD_INGOT)) {
            if(!Kit.getInstance().getDataManager().hasUserData(event.getPlayer().getUniqueId())) {
                Kit.getInstance().getDataManager().createUserData(event.getPlayer().getUniqueId());
            }
            UserData data = Kit.getInstance().getDataManager().getUserData(event.getPlayer().getUniqueId());

            data.setBalance(data.getBalance() + 1);

            event.getPlayer().sendMessage(Color.Gray + "You have received " + Color.Gold + "1.0" + Color.Gray + " coins from picking up gold.");
            event.setCancelled(true);
            event.getItem().remove();
        }
    }

    @EventHandler
    public void onEvent(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onEvent(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEvent(PlayerJoinEvent event) {
        if(LocationUtil.getSpawnLocation() != null) {
            event.getPlayer().teleport(LocationUtil.getSpawnLocation());
            PlayerUtils.applyKit(event.getPlayer());
        }
    }

}
