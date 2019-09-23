package cc.funkemunky.test;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.test.listeners.JoinListeners;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestCore extends JavaPlugin {

    //public Map<Player, Scoreboard> scoreboardMap = new WeakHashMap<>();
    public Map<UUID, String> lastViolation = new HashMap<>();
    public static TestCore INSTANCE;
    public boolean kauriEnabled;

    public void onEnable() {
        INSTANCE = this;

        /*if(kauriEnabled = Bukkit.getPluginManager().getPlugin("Kauri") != null) {
            ScoreboardLib.setPluginInstance(this);
            for(Player player : Bukkit.getOnlinePlayers()) {
                Scoreboard scoreboard = getScoreboard(player);
                scoreboard.activate();
                scoreboardMap.put(player, scoreboard);
            }
            Bukkit.getPluginManager().registerEvents(new JoinListeners(), this);
        }*/
        Bukkit.getPluginManager().registerEvents(new JoinListeners(), this);
        Atlas.getInstance().initializeScanner(getClass(), this, true, true);
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
        Atlas.getInstance().getEventManager().unregisterAll(this);
        Atlas.getInstance().getCommandManager().unregisterCommand("renameitem");
        Atlas.getInstance().getCommandManager().unregisterCommand("buykauri");
        Bukkit.getScheduler().cancelTasks(this);
    }

    /*public Scoreboard getScoreboard(Player player) {
        return ScoreboardLib.createScoreboard(player).setHandler(new ScoreboardHandler() {

            private HighlightedString testServer = new HighlightedString("Kauri Test Server", "&f&l", "&7&l");

            @Override
            public String getTitle(Player player) {
                return testServer.next();
            }

            @Override
            public List<Entry> getEntries(Player player) {
                return new EntryBuilder()
                        .blank()
                        .next("&6&lKauri Versions")
                        .next("&8» &eTest Version: &f" + Bukkit.getPluginManager().getPlugin("Kauri").getDescription().getVersion())
                        .next("&8» &eReleased Version: &f" + ConfigSettings.kauriVersion)
                        .blank()
                        .next("&6&lLast Violation")
                        .next(Color.White + TestCore.INSTANCE.lastViolation.getOrDefault(player.getUniqueId(), "None"))
                        .blank()
                        .next("&6&lLike what you see?")
                        .next(Color.White + "https://funkemunky.cc/shop")
                        .blank().build();
            }
        }).setUpdateInterval(5);
    }*/
}
