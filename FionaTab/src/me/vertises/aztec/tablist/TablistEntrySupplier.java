/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Table
 *  org.bukkit.entity.Player
 */
package me.vertises.aztec.tablist;

import com.google.common.collect.Table;
import org.bukkit.entity.Player;

public interface TablistEntrySupplier {
    public Table<Integer, Integer, String> getEntries(Player var1);

    public String getHeader(Player var1);

    public String getFooter(Player var1);
}

