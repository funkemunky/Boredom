/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.Table
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package me.vertises.aztec.tablist.example;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.vertises.aztec.tablist.TablistEntrySupplier;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ExampleSupplier
implements TablistEntrySupplier {
    @Override
    public Table<Integer, Integer, String> getEntries(Player player) {
        HashBasedTable table = HashBasedTable.create();
        table.put((Object)1, (Object)10, (Object)((Object)ChatColor.BLUE + "What a good example!"));
        return table;
    }

    @Override
    public String getHeader(Player player) {
        return "Godly header";
    }

    @Override
    public String getFooter(Player player) {
        return "Godly footer";
    }
}

