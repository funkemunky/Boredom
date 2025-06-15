package cc.funkemunky.test.commands;

import cc.funkemunky.api.commands.ancmd.Command;
import cc.funkemunky.api.commands.ancmd.CommandAdapter;
import cc.funkemunky.api.utils.Commands;
import cc.funkemunky.api.utils.Init;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Init(commands = true)
public class ItemsCommands {

    // Test inventory items to give player. Mix of garbage, enchanted armor/wepaons/tools, nonenchanted armor/weapons/tools, and food.
    private ItemStack[] testItems = {
            new ItemStack(Material.DIRT, 64), // Random junk
            new ItemStack(Material.STONE, 32), // Random junk
            new ItemStack(Material.APPLE, 10), // Food
            new ItemStack(Material.BREAD, 5), // Food
            new ItemStack(Material.COBBLESTONE, 64), // Random junk
            new ItemStack(Material.GOLDEN_APPLE, 3), // Food
            new ItemStack(Material.DIAMOND_HELMET),
            new ItemStack(Material.DIAMOND_CHESTPLATE),
            new ItemStack(Material.DIAMOND_LEGGINGS),
            new ItemStack(Material.DIAMOND_BOOTS),
            new ItemStack(Material.DIAMOND_AXE),
            new ItemStack(Material.DIAMOND_PICKAXE),
            new ItemStack(Material.DIAMOND_SWORD),

            // Enchanted items
            createEnchantedItem(Material.DIAMOND_HELMET, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
            createEnchantedItem(Material.DIAMOND_CHESTPLATE, Enchantment.PROTECTION_ENVIRONMENTAL, 1),
            createEnchantedItem(Material.DIAMOND_LEGGINGS, Enchantment.PROTECTION_ENVIRONMENTAL, 2),
            createEnchantedItem(Material.DIAMOND_BOOTS, Enchantment.PROTECTION_ENVIRONMENTAL, 2),
            createEnchantedItem(Material.IRON_HELMET, Enchantment.PROTECTION_ENVIRONMENTAL, 2),
            createEnchantedItem(Material.IRON_CHESTPLATE, Enchantment.PROTECTION_ENVIRONMENTAL, 2),
            createEnchantedItem(Material.IRON_LEGGINGS, Enchantment.PROTECTION_ENVIRONMENTAL, 2),
            createEnchantedItem(Material.IRON_BOOTS, Enchantment.PROTECTION_ENVIRONMENTAL, 2),
            createEnchantedItem(Material.LEATHER_HELMET, Enchantment.PROTECTION_ENVIRONMENTAL, 2),
            createEnchantedItem(Material.LEATHER_CHESTPLATE, Enchantment.PROTECTION_ENVIRONMENTAL, 2),
            createEnchantedItem(Material.LEATHER_LEGGINGS, Enchantment.PROTECTION_ENVIRONMENTAL, 2),
            createEnchantedItem(Material.LEATHER_BOOTS, Enchantment.PROTECTION_ENVIRONMENTAL, 2),
            createEnchantedItem(Material.DIAMOND_AXE, Enchantment.DAMAGE_ALL, 1),
            createEnchantedItem(Material.DIAMOND_SWORD, Enchantment.DAMAGE_ALL, 1),
            createEnchantedItem(Material.DIAMOND_PICKAXE, Enchantment.DIG_SPEED, 1),
            createEnchantedItem(Material.DIAMOND_AXE, Enchantment.DAMAGE_ALL, 2),
            createEnchantedItem(Material.DIAMOND_SWORD, Enchantment.DAMAGE_ALL, 2),
            createEnchantedItem(Material.DIAMOND_PICKAXE, Enchantment.DIG_SPEED, 2),
            createEnchantedItem(Material.IRON_AXE, Enchantment.DAMAGE_ALL, 2),
            createEnchantedItem(Material.IRON_SWORD, Enchantment.DAMAGE_ALL, 2),
            createEnchantedItem(Material.IRON_PICKAXE, Enchantment.DIG_SPEED, 2),
            createEnchantedItem(Material.STONE_AXE, Enchantment.DAMAGE_ALL, 2),
            createEnchantedItem(Material.STONE_SWORD, Enchantment.DAMAGE_ALL, 2),
            createEnchantedItem(Material.STONE_PICKAXE, Enchantment.DIG_SPEED, 2),
    };

    @Command(name = "testinv", description = "Test inventory command", aliases = {"ti"}, permission = "testcore.testinv")
    public void onTestInvCommand(CommandAdapter cmd) {
        cmd.getPlayer().getInventory().addItem(testItems);
        cmd.getPlayer().sendMessage("Added test items to your inventory.");
    }



    private ItemStack createEnchantedItem(Material material, Enchantment enchantment, int level) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.addEnchant(enchantment, level, true);
            item.setItemMeta(meta);
        }
        return item;
    }
}
