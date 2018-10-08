package cc.funkemunky.Meme.listeners;

import cc.funkemunky.Meme.utils.Color;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ChestStealer implements Listener {

    private List<Material> items;
    private Map<Player, Long> openTime;
    public ChestStealer() {
        items = Arrays.asList(Material.DIAMOND_AXE, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_APPLE, Material.COOKED_BEEF, Material.ARROW, Material.BOW);
        openTime = new WeakHashMap<>();
    }
    @EventHandler
    public void inventoryOpen(InventoryOpenEvent event) {
        if(event.getInventory().getType().equals(InventoryType.CHEST)) {
            Random random = new Random();
            for (int i = 0; i < event.getInventory().getContents().length; i++) {
                Material itemMaterial = items.get(random.nextInt(items.size() - 1));

                event.getInventory().setItem(i, new ItemStack(itemMaterial, 1));
            }
            openTime.put((Player) event.getPlayer(), System.currentTimeMillis());
        }
    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent event) {
        if(event.getInventory().getType().equals(InventoryType.CHEST)) {
            long elapsed = System.currentTimeMillis() - openTime.getOrDefault((Player) event.getPlayer(), System.currentTimeMillis());

            event.getPlayer().sendMessage(Color.Gray + "Your chest-steal time is: " + Color.Aqua + DurationFormatUtils.formatDurationWords(elapsed, true, true));
            event.getPlayer().getInventory().clear();
        }
    }
}
