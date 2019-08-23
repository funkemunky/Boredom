package cc.funkemunky.test.listeners;

import cc.funkemunky.api.utils.BoundingBox;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.RunUtils;
import cc.funkemunky.test.TestCore;
import net.minecraft.server.v1_8_R3.ItemSaddle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

@Init
public class ScaffoldListeners implements Listener {
    //135 108 387 107 125 355

    private static BoundingBox placeArea = new BoundingBox(107, 108, 355, 135, 125, 387);;

    private static Map<Block, Long> blocksPlaced = new HashMap<>();
    private static Map<UUID, ItemStack[]> playerInventory = new HashMap<>();

    public ScaffoldListeners() {
        RunUtils.taskTimer(() -> {
            blocksPlaced.keySet().stream().filter(key -> System.currentTimeMillis() - blocksPlaced.get(key) > TimeUnit.SECONDS.toMillis(10)).forEach(key -> key.setType(Material.AIR));
        }, 100L, 10L);
    }

    @EventHandler
    public void onEvent(BlockPlaceEvent event) {
        if(event.getBlockPlaced() != null && event.getBlockPlaced().getType().equals(Material.BRICK)) {
            event.getPlayer().getItemInHand().setAmount(5);
            event.getPlayer().updateInventory();
            blocksPlaced.put(event.getBlockPlaced(), System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onEvent(PlayerMoveEvent event) {
        BoundingBox playerBox = new BoundingBox(event.getTo().toVector(), event.getTo().toVector()).grow(0.3f, 0, 0.3f).add(0,0,0,0,1.8f,0);

        if(playerBox.intersectsWithBox(placeArea)) {
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

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if(event.getPlugin().equals(TestCore.INSTANCE)) {
            blocksPlaced.keySet().forEach(block -> block.setType(Material.AIR));
            blocksPlaced.clear();
        }
    }
}
