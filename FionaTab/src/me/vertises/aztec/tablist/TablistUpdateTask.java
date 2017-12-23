/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package me.vertises.aztec.tablist;

import java.util.function.Consumer;
import java.util.stream.Stream;
import me.vertises.aztec.tablist.Tablist;
import me.vertises.aztec.tablist.TablistManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TablistUpdateTask
implements Runnable {
    @Override
    public void run() {
        TablistManager manager = TablistManager.INSTANCE;
        if (manager == null) {
            return;
        }
        Stream.of(Bukkit.getOnlinePlayers()).forEach(player -> {
            Tablist tablist = manager.getTablist(player);
            if (tablist != null) {
                tablist.hideRealPlayers().update();
            }
        }
        );
    }
}

