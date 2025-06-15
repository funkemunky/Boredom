package cc.funkemunky.test.listeners;

import cc.funkemunky.api.utils.*;
import cc.funkemunky.api.utils.world.types.SimpleCollisionBox;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Init
public class ScaffoldListeners implements Listener {
    //135 108 387 107 125 355

    private static SimpleCollisionBox placeArea = new SimpleCollisionBox(
            161,107,343, 207,153,400.99);

    private static Map<Block, Long> blocksPlaced = new ConcurrentHashMap<>();
    private static Map<UUID, ItemStack[]> playerInventory = new HashMap<>();
    private static World world;

    private static Vector firstLoc = new Vector(161,107,343);

    private static Vector secondLoc = new Vector(207,153,400.99);

    public ScaffoldListeners() {
        world = Bukkit.getWorld("world");
        placeArea = new SimpleCollisionBox(firstLoc, secondLoc);
        for (Block block : Helper.getBlocksNearby2(world, placeArea, Materials.SOLID)) {
            if(!block.getType().equals(Material.BRICKS)) continue;

            block.setType(Material.AIR);
        }
        long time = TimeUnit.SECONDS.toMillis(10);
        RunUtils.taskTimer(() -> {
            long timeStamp = System.currentTimeMillis();
            blocksPlaced.keySet().stream()
                    .filter(key -> timeStamp - blocksPlaced.get(key) > time)
                    .forEach(key -> {
                        key.setType(Material.AIR);
                        blocksPlaced.remove(key);
                    });
        }, 100L, 40L);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEvent(BlockPlaceEvent event) {
        SimpleCollisionBox playerBox = new SimpleCollisionBox(event.getPlayer().getLocation().toVector(),
                0.05, 1.8);
        if(event.getBlockPlaced() != null
                && playerBox.isIntersected(placeArea)
                && event.getBlockPlaced().getType().equals(Material.BRICKS)) {
            if(event.isCancelled()) event.setCancelled(false);
            event.getPlayer().getItemInHand().setAmount(5);
            event.getPlayer().updateInventory();
            blocksPlaced.put(event.getBlockPlaced(), System.currentTimeMillis());
        } else if(!event.getPlayer().isOp()) event.setCancelled(true);
    }

    @EventHandler
    public void onEvent(PlayerMoveEvent event) {
        SimpleCollisionBox playerBox = new SimpleCollisionBox(event.getTo().toVector(), 0.05, 1.8);

        if(playerBox.isIntersected(placeArea)) {
            if(!playerInventory.containsKey(event.getPlayer().getUniqueId())) {
                playerInventory.put(event.getPlayer().getUniqueId(), event.getPlayer().getInventory().getContents());

                event.getPlayer().getInventory().clear();
                event.getPlayer().getInventory().addItem(new ItemStack(Material.BRICKS, 5));
            }
        } else if(playerInventory.containsKey(event.getPlayer().getUniqueId())) {
            event.getPlayer().getInventory().setContents(playerInventory.get(event.getPlayer().getUniqueId()));
            event.getPlayer().updateInventory();
            playerInventory.remove(event.getPlayer().getUniqueId());
        }
    }

    public static void reset() {
        for (Block block : Helper.getBlocksNearby2(world, placeArea, Materials.SOLID)) {
            if(!block.getType().equals(Material.BRICKS)) continue;
            block.setType(Material.AIR);
        }
        blocksPlaced.clear();
        var uuids = playerInventory.keySet().stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .map(player -> {
                    player.getInventory().setContents(playerInventory.get(player.getUniqueId()));
                    player.updateInventory();
                    return player.getUniqueId();
                }).toList();

        for (UUID uuid : uuids) {
            playerInventory.remove(uuid);
        }

    }
}
