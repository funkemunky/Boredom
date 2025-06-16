package cc.funkemunky.test.listeners;

import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.MathUtils;
import cc.funkemunky.test.user.User;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Material;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Init
public class ChestStealListeners implements Listener {

    private static final List<ItemStack> itemsToPutInChest = Arrays.asList(
            new ItemStack(Material.DIAMOND_CHESTPLATE, 1), new ItemStack(Material.IRON_LEGGINGS, 1),
            new ItemStack(Material.STONE_SWORD), new ItemStack(Material.COOKED_CHICKEN, 1),
            new ItemStack(Material.COOKED_BEEF, 1));

    @EventHandler
    public void onEvent(InventoryOpenEvent event) {
        if(!(event.getPlayer() instanceof Player player)) return;

        if(StrikePracticePlugin.INSTANCE != null && StrikePracticePlugin.notInTestMap(player)) return;
        if(event.getInventory().getHolder() instanceof DoubleChest chest) {
            User user = User.getUser(event.getPlayer().getUniqueId());

            if(user == null) return;

            ItemStack[] stack = new ItemStack[chest.getInventory().getSize()];

            for (int i = 0; i < stack.length; i++) {
                stack[i] = itemsToPutInChest
                        .get(ThreadLocalRandom.current().nextInt(0, itemsToPutInChest.size() - 1));
            }

            user.previousInventoryContents = event.getPlayer().getInventory().getContents();
            chest.getInventory().setContents(stack);
            event.getPlayer().getInventory().clear();
            user.inventoryStart = System.currentTimeMillis();
        }
    }

    @EventHandler
    public void onEvent(InventoryCloseEvent event) {
        if(!(event.getPlayer() instanceof Player player)) return;

        if(StrikePracticePlugin.INSTANCE != null && StrikePracticePlugin.notInTestMap(player)) return;

        User user = User.getUser(event.getPlayer().getUniqueId());

        if(user != null && user.inventoryStart != 0) {
            long delta = System.currentTimeMillis() - user.inventoryStart;

            String formatted = delta < TimeUnit.MINUTES.toMillis(1)
                    ? MathUtils.round(delta / 1000., 1) + " seconds"
                    : DurationFormatUtils
                    .formatDurationWords(delta, true, true);

            event.getPlayer().sendMessage(Color.translate("&7Your cheststeal took &e"
                    + formatted + " &7to collect all the items."));
            player.getInventory().clear();
            player.getInventory().setContents(user.previousInventoryContents);
            player.updateInventory();
            user.inventoryStart = 0;
            user.previousInventoryContents = null;
        }
    }
}
