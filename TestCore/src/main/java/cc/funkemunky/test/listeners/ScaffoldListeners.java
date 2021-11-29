package cc.funkemunky.test.listeners;

import cc.funkemunky.api.tinyprotocol.api.ProtocolVersion;
import cc.funkemunky.api.utils.Helper;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.Materials;
import cc.funkemunky.api.utils.RunUtils;
import cc.funkemunky.api.utils.world.BlockData;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Init
public class ScaffoldListeners implements Listener {
    //135 108 387 107 125 355

    private static SimpleCollisionBox placeArea = new SimpleCollisionBox(
            108, 108, 387,
            135, 125, 354);

    private static Map<Block, Long> blocksPlaced = new ConcurrentHashMap<>();
    private static Map<UUID, ItemStack[]> playerInventory = new HashMap<>();
    private static World world;

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
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEvent(BlockPlaceEvent event) {
        if(event.getPlayer().isOp()) return;
        if(event.getBlockPlaced() != null
                && placeArea.isIntersected(BlockData
                .getData(event.getBlockPlaced().getType()).getBox(event.getBlock(), ProtocolVersion.getGameVersion()))
                && event.getBlockPlaced().getType().equals(Material.BRICK)) {
            if(event.isCancelled()) event.setCancelled(false);
            event.getPlayer().getItemInHand().setAmount(5);
            event.getPlayer().updateInventory();
            blocksPlaced.put(event.getBlockPlaced(), System.currentTimeMillis());
        } else event.setCancelled(true);
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
