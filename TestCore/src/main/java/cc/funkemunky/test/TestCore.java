package cc.funkemunky.test;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.bungee.BungeeAPI;
import cc.funkemunky.api.reflections.impl.MinecraftReflection;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.MathUtils;
import cc.funkemunky.api.utils.MiscUtils;
import cc.funkemunky.api.utils.RunUtils;
import cc.funkemunky.api.utils.math.RollingAverageDouble;
import cc.funkemunky.test.listeners.CheatListeners;
import cc.funkemunky.test.listeners.ScaffoldListeners;
import cc.funkemunky.test.user.User;
import cc.funkemunky.test.utils.ConfigSettings;
import me.tigerhix.lib.scoreboard.ScoreboardLib;
import me.tigerhix.lib.scoreboard.common.EntryBuilder;
import me.tigerhix.lib.scoreboard.common.animate.HighlightedString;
import me.tigerhix.lib.scoreboard.type.Entry;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import me.tigerhix.lib.scoreboard.type.ScoreboardHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class TestCore extends JavaPlugin {

    public Map<UUID, Scoreboard> scoreboardMap = new HashMap<>();
    public static TestCore INSTANCE;
    public boolean kauriEnabled;
    public Plugin kauri;
    private long lastTick;
    private double tps;
    public RollingAverageDouble tpsAvg = new RollingAverageDouble(20, 0);

    public void onEnable() {
        INSTANCE = this;
        MiscUtils.printToConsole("Loading TestCore v" + getDescription().getVersion() + "...");
        if(kauriEnabled = (kauri = Bukkit.getPluginManager().getPlugin("Kauri")) != null
                && (kauriEnabled = Bukkit.getPluginManager().isPluginEnabled("Kauri"))) {
            MiscUtils.printToConsole("Kauri enabled! Loading Kauri Test server specific things...");
            Atlas.getInstance().getEventManager().registerListeners(new CheatListeners(), this);
            ScoreboardLib.setPluginInstance(this);
            for(Player player : Bukkit.getOnlinePlayers()) {
                Scoreboard scoreboard = getScoreboard(player);
                scoreboard.activate();
                scoreboardMap.put(player.getUniqueId(), scoreboard);
            }
        }
        MiscUtils.printToConsole("Running scanner...");
        Atlas.getInstance().initializeScanner(this, true, true);
        MiscUtils.printToConsole("Running TPS task...");
    }

    public void onDisable() {
        ScaffoldListeners.reset();
        runBungeeStuff();
        scoreboardMap.forEach((uuid, board) -> {
            board.deactivate();
            scoreboardMap.remove(uuid);
        });
        HandlerList.unregisterAll(this);
        Atlas.getInstance().getEventManager().unregisterAll(this);
        Atlas.getInstance().getCommandManager().unregisterCommand("renameitem");
        Atlas.getInstance().getCommandManager().unregisterCommand("buykauri");
        Bukkit.getScheduler().cancelTasks(this);
    }

    public Scoreboard getScoreboard(Player player) {
        Scoreboard board = ScoreboardLib.createScoreboard(player).setHandler(new ScoreboardHandler() {

            private HighlightedString testServer = new HighlightedString("Kauri Test Server", "&f&l", "&7&l");

            @Override
            public String getTitle(Player player) {
                return testServer.next();
            }

            @Override
            public List<Entry> getEntries(Player player) {
                try {
                    User user = User.getUser(player.getUniqueId());
                    EntryBuilder builder = new EntryBuilder()
                            .next(MiscUtils.line(ChatColor.RESET.toString() + Color.Dark_Gray).substring(0, 24))
                            .next("&6&lYour Information")
                            .next("&8» &ePing&7: &f" + MinecraftReflection.getPing(player))
                            .blank()
                            .next("&6&lViolations");
                    if(user.violations.size() == 0) {
                        builder.next(Color.White + "None");
                    } else {
                        user.violations.keySet().stream()
                                .sorted(Comparator.comparing(user.violations::get, Comparator.reverseOrder()))
                                .limit(4).
                                forEach(vl -> {
                                    builder.next("&8- &f"
                                            + vl
                                            + " &7(&c" + MathUtils.round(user.violations.get(vl), 2) + "&7)");
                                });
                        if(user.violations.size() > 5) {
                            builder.next("&7&o*More Violations*");
                        }
                    }
                    return builder
                            .next(MiscUtils.line(Color.Dark_Gray).substring(0, 22)).build();
                } catch(NullPointerException e) {
                    e.printStackTrace();
                }
                return new EntryBuilder().blank().next("&cError. Check console").build();
            }
        }).setUpdateInterval(2);

        scoreboardMap.put(player.getUniqueId(), board);
        return board;
    }

    private void runBungeeStuff() {
        if(ConfigSettings.bungeeEnabled) {
            Bukkit.getOnlinePlayers().forEach(player ->
                            BungeeAPI.movePlayerToServer(player.getUniqueId(), ConfigSettings.backupServer));
        }
    }
}
