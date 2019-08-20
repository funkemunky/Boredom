package cc.funkemunky.test.listeners;

import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.MiscUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Init
public class ChestStealListeners implements Listener {

    private static List<ItemStack> itemsToPutInChest = Arrays.asList(new ItemStack(Material.DIAMOND_CHESTPLATE, 1), new ItemStack(Material.IRON_LEGGINGS, 1), new ItemStack(Material.STONE_SWORD), new ItemStack(Material.COOKED_CHICKEN, 1), new ItemStack(Material.COOKED_BEEF, 1));
    private Map<UUID, Long> started = new HashMap<>();

    @EventHandler
    public void onEvent(InventoryOpenEvent event) {
        if(event.getInventory().getHolder() instanceof DoubleChest) {
            DoubleChest chest = (DoubleChest) event.getInventory().getHolder();

            if(chest.getInventory().getName().equals("ChestSteal Test")) {
                ItemStack[] stack = new ItemStack[chest.getInventory().getSize()];

                for (int i = 0; i < stack.length; i++) {
                    stack[i] = itemsToPutInChest.get(ThreadLocalRandom.current().nextInt(0, itemsToPutInChest.size() - 1));
                }
                chest.getInventory().setContents(stack);
                started.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
                event.getPlayer().getInventory().clear();
            }
        }
    }

    @EventHandler
    public void onEvent(InventoryCloseEvent event) {
        if(started.containsKey(event.getPlayer().getUniqueId())) {
            long delta = System.currentTimeMillis() - started.get(event.getPlayer().getUniqueId());

            String formatted = DurationFormatUtils.formatDurationWords(delta, true, true);

            event.getPlayer().sendMessage(Color.translate("&7Your cheststeal to &e" + formatted + " &7to collect all the items."));
            started.remove(event.getPlayer().getUniqueId());
            event.getPlayer().getInventory().clear();
        }
    }
}
