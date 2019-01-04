package cc.funkemunky.kit;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.utils.MiscUtils;
import cc.funkemunky.kit.commands.StatsCommand;
import cc.funkemunky.kit.commands.kit.KitCommand;
import cc.funkemunky.kit.data.DataManager;
import cc.funkemunky.kit.listeners.GameListeners;
import cc.funkemunky.kit.listeners.LagListeners;
import cc.funkemunky.kit.listeners.ProtectionListeners;
import cc.funkemunky.kit.utils.LocationUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class Kit extends JavaPlugin {
    @Getter
    private static Kit instance;
    private DataManager dataManager;

    @Override
    public void onLoad() {
        MiscUtils.registerCommand("kit", this);
        MiscUtils.registerCommand("stats", this);
    }

    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        dataManager = new DataManager();
        registerCommands();
        runTasks();
        registerUtils();
        registerListeners();
    }

    public void onDisable() {
        dataManager.saveAllUsers();
    }

    private void registerCommands() {
        Atlas.getInstance().getFunkeCommandManager().addCommand(new KitCommand());
        Atlas.getInstance().getFunkeCommandManager().addCommand(new StatsCommand());
    }

    private void registerUtils() {
        new LocationUtil();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new GameListeners(), this);
        Bukkit.getPluginManager().registerEvents(new ProtectionListeners(), this);
        Bukkit.getPluginManager().registerEvents(new LagListeners(), this);
    }

    private void runTasks() {
        new BukkitRunnable() {
            public void run() {
                long count = Bukkit.getWorld("world").getEntities().stream()
                        .filter(entity -> entity.getType().equals(EntityType.DROPPED_ITEM) && ((Item) entity).getItemStack().getType().equals(Material.GOLD_INGOT)).count();
                
                if(count < 15) {
                    double minX = LocationUtil.getSpawnLocation().getX() - 60, maxX = LocationUtil.getSpawnLocation().getX() + 60;
                    double minZ = LocationUtil.getSpawnLocation().getZ() - 60, maxZ = LocationUtil.getSpawnLocation().getZ() + 60;

                    double locX = ThreadLocalRandom.current().nextDouble(minX, maxX), locZ = ThreadLocalRandom.current().nextDouble(minZ, maxZ);

                    Location loc = new Location(Bukkit.getWorld("world"), locX, 80, locZ);

                    Bukkit.getWorld("world").dropItemNaturally(loc, new ItemStack(Material.GOLD_INGOT, 1));
                }
            }
        }.runTaskTimer(this, 0L, 40L);
    }
}
