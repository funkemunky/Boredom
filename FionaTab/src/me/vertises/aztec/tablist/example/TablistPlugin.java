/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.vertises.aztec.tablist.example;

import java.util.concurrent.TimeUnit;
import me.vertises.aztec.tablist.TablistEntrySupplier;
import me.vertises.aztec.tablist.TablistManager;
import me.vertises.aztec.tablist.example.ExampleSupplier;
import org.bukkit.plugin.java.JavaPlugin;

public class TablistPlugin
extends JavaPlugin {
    public void onEnable() {
        new me.vertises.aztec.tablist.TablistManager(this, new ExampleSupplier(), TimeUnit.SECONDS.toMillis(5));
    }
}

