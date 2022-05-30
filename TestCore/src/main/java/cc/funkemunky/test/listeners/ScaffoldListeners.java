package cc.funkemunky.test.listeners;

import cc.funkemunky.api.utils.*;
import cc.funkemunky.api.utils.world.types.SimpleCollisionBox;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Init
public class ScaffoldListeners implements Listener {
    //135 108 387 107 125 355

    private static SimpleCollisionBox placeArea = new SimpleCollisionBox(
            100,107,353, 135.99,125,387.99);

    private static Map<Block, Long> blocksPlaced = new ConcurrentHashMap<>();
    private static Map<UUID, ItemStack[]> playerInventory = new HashMap<>();
    private static World world;

    @ConfigSetting(path = "build", name = "one")
    private Vector firstLoc = new Vector(100,107,353);

    @ConfigSetting(path = "build", name = "two")
    private Vector secondLoc = new Vector(135.99,125,387.99);

    public ScaffoldListeners() {
        world = Bukkit.getWorld("world");
        for (Block block : Helper.getBlocksNearby2(world, placeArea, Materials.SOLID)) {
            if(!block.getType().equals(Material.BRICK)) continue;

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

        placeArea = new SimpleCollisionBox(firstLoc, secondLoc);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEvent(BlockPlaceEvent event) {
        SimpleCollisionBox playerBox = new SimpleCollisionBox(event.getPlayer().getLocation().toVector(),
                0.05, 1.8);
        if(event.getBlockPlaced() != null
                && playerBox.isIntersected(placeArea)
                && event.getBlockPlaced().getType().equals(Material.BRICK)) {
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
                event.getPlayer().getInventory().addItem(new ItemStack(Material.BRICK, 5));
            }
        } else if(playerInventory.containsKey(event.getPlayer().getUniqueId())) {
            event.getPlayer().getInventory().setContents(playerInventory.get(event.getPlayer().getUniqueId()));
            event.getPlayer().updateInventory();
            playerInventory.remove(event.getPlayer().getUniqueId());
        }
    }

    public static void reset() {
        for (Block block : Helper.getBlocksNearby2(world, placeArea, Materials.SOLID)) {
            if(!block.getType().equals(Material.BRICK)) continue;
            block.setType(Material.AIR);
        }
        blocksPlaced.clear();
        playerInventory.keySet().stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(player -> {
                    player.getInventory().setContents(playerInventory.get(player.getUniqueId()));
                    player.updateInventory();
                    playerInventory.remove(player.getUniqueId());
                });

    }
}
