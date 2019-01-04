package cc.funkemunky.kit.utils;

import cc.funkemunky.api.utils.Color;
import cc.funkemunky.kit.Kit;
import cc.funkemunky.kit.data.UserData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerUtils {

    public static void applyKit(Player player) {
        player.getInventory().clear();
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
        player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
        player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS, 1));
        player.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD, 1));
        player.getInventory().setItem(1, new ItemStack(Material.FISHING_ROD, 1));
        player.getInventory().setItem(2, new ItemStack(Material.BOW, 1));
        player.getInventory().setItem(7, new ItemStack(Material.GOLDEN_APPLE, 1));
        player.getInventory().setItem(8, new ItemStack(Material.ARROW, 48));

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 0, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100000, 1, true, false));
    }

    public static void addGoldenApple(Player player) {
        if(player.getInventory().getItem(7) != null && player.getInventory().getItem(7).getType().equals(Material.GOLDEN_APPLE)) {
            player.getInventory().setItem(7, new ItemStack(Material.GOLDEN_APPLE, player.getInventory().getItem(7).getAmount() + 1));
        } else {
            player.getInventory().setItem(7, new ItemStack(Material.GOLDEN_APPLE, 1));
        }
    }

    public static void addGoldenApple(Player player, int amount) {
        if(player.getInventory().getItem(7) != null && player.getInventory().getItem(7).getType().equals(Material.GOLDEN_APPLE)) {
            player.getInventory().setItem(7, new ItemStack(Material.GOLDEN_APPLE, player.getInventory().getItem(7).getAmount() + amount));
        } else {
            player.getInventory().setItem(7, new ItemStack(Material.GOLDEN_APPLE, amount));
        }
    }

    public static void handleDeath(Player victim) {
        victim.teleport(LocationUtil.getSpawnLocation());

        if(!Kit.getInstance().getDataManager().hasUserData(victim.getUniqueId())) {
            Kit.getInstance().getDataManager().createUserData(victim.getUniqueId());
        }

        UserData data = Kit.getInstance().getDataManager().getUserData(victim.getUniqueId());

        data.setDeaths(data.getDeaths() + 1);

        if(LocationUtil.isWithinSpawn(victim)) {
            victim.setHealth(20);
            applyKit(victim);
        }
    }

    public static void handleDeath(Player victim, Player killer) {
        victim.teleport(LocationUtil.getSpawnLocation());

        if(!Kit.getInstance().getDataManager().hasUserData(victim.getUniqueId())) {
            Kit.getInstance().getDataManager().createUserData(victim.getUniqueId());
        }

        UserData data = Kit.getInstance().getDataManager().getUserData(victim.getUniqueId());

        data.setDeaths(data.getDeaths() + 1);

        if(!Kit.getInstance().getDataManager().hasUserData(killer.getUniqueId())) {
            Kit.getInstance().getDataManager().createUserData(killer.getUniqueId());
        }

        UserData data2 = Kit.getInstance().getDataManager().getUserData(killer.getUniqueId());

        data2.setKills(data2.getKills() + 1);

        data2.setBalance(data2.getBalance() + 5.0);

        if(LocationUtil.isWithinSpawn(victim)) {
            victim.setHealth(20);
            applyKit(victim);
        }
    }
}
