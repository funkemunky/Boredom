/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.server.PluginDisableEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitTask
 */
package me.vertises.aztec.tablist;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;
import me.vertises.aztec.tablist.Tablist;
import me.vertises.aztec.tablist.TablistEntrySupplier;
import me.vertises.aztec.tablist.TablistUpdateTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class TablistManager
implements Listener {
    public static TablistManager INSTANCE;
    public final JavaPlugin plugin;
    private final Map<UUID, Tablist> tablists;
    public TablistEntrySupplier supplier;
    private int updateTaskId = -1;

    public TablistManager(JavaPlugin plugin, TablistEntrySupplier supplier, long updateTime) {
        long remainder;
        boolean startUpdater = true;
        if (INSTANCE != null) {
            int i = 0;
            while (i < 7) {
                Bukkit.getLogger().warning("");
                ++i;
            }
            Bukkit.getLogger().warning("WARNING! AN INSTANCE OF TABLISTMANAGER ALREADY EXISTS!");
            Bukkit.getLogger().warning("IT IS RECOMMENDED TO ONLY USE ONE OTHERWISE IT CAN CAUSE FLICKERING AND OTHER ISSUES!");
            i = 0;
            while (i < 7) {
                Bukkit.getLogger().warning("");
                ++i;
            }
        }
        if ((remainder = updateTime % 50) != 0) {
            updateTime -= remainder;
            Bukkit.getLogger().info("FIXING UPDATE TIME TO VALID TICK-COUNT...");
        }
        if (updateTime < 50) {
            startUpdater = false;
            int i = 0;
            while (i < 7) {
                Bukkit.getLogger().warning("");
                ++i;
            }
            Bukkit.getLogger().warning("WARNING! TABLIST UPDATE TASK NOT STARTED!");
            Bukkit.getLogger().warning("REASON: UPDATE TIME IS TOO SHORT.");
            i = 0;
            while (i < 7) {
                Bukkit.getLogger().warning("");
                ++i;
            }
        }
        INSTANCE = this;
        this.tablists = new ConcurrentHashMap<UUID, Tablist>();
        this.supplier = supplier;
        this.plugin = plugin;
        if (startUpdater) {
            this.updateTaskId = Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)plugin, (Runnable)new TablistUpdateTask(), updateTime / 50, updateTime / 50).getTaskId();
        }
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
        Stream.of(Bukkit.getOnlinePlayers()).forEach(player -> {
            this.getTablist(player, true);
        }
        );
    }

    @Deprecated
    public Tablist getTablist(Player player) {
        return this.getTablist(player, false);
    }

    @Deprecated
    public Tablist getTablist(Player player, boolean create) {
        UUID uniqueId = player.getUniqueId();
        Tablist tablist = this.tablists.get(uniqueId);
        if (tablist == null && create) {
            tablist = new Tablist(player);
            this.tablists.put(uniqueId, tablist);
        }
        return tablist;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Tablist tablist = this.getTablist(player, true);
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if (event.getPlugin() == this.plugin) {
            this.tablists.forEach((id, tablist) -> {
                tablist.hideFakePlayers().clear();
            }
            );
            this.tablists.clear();
            HandlerList.unregisterAll((Listener)this);
            if (this.updateTaskId != -1) {
                Bukkit.getScheduler().cancelTask(this.updateTaskId);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        Tablist tablist = this.tablists.remove(uniqueId);
        if (tablist != null) {
            tablist.hideFakePlayers().clear();
        }
    }
}

