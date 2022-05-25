package cc.funkemunky.test;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.bungee.BungeeAPI;
import cc.funkemunky.api.reflections.impl.MinecraftReflection;
import cc.funkemunky.api.utils.*;
import cc.funkemunky.api.utils.math.RollingAverageDouble;
import cc.funkemunky.test.commands.ToggleScoreboard;
import cc.funkemunky.test.handlers.SpeedTestHandler;
import cc.funkemunky.test.handlers.TestResult;
import cc.funkemunky.test.listeners.CheatListeners;
import cc.funkemunky.test.listeners.ScaffoldListeners;
import cc.funkemunky.test.listeners.StrikePracticePlugin;
import cc.funkemunky.test.user.Settings;
import cc.funkemunky.test.user.User;
import cc.funkemunky.test.utils.ConfigSettings;
import cc.funkemunky.test.utils.StringUtil;
import dev.brighten.api.KauriAPI;
import dev.brighten.db.db.Database;
import dev.brighten.db.db.FlatfileDatabase;
import me.tigerhix.lib.scoreboard.ScoreboardLib;
import me.tigerhix.lib.scoreboard.common.EntryBuilder;
import me.tigerhix.lib.scoreboard.common.animate.HighlightedString;
import me.tigerhix.lib.scoreboard.common.animate.ScrollableString;
import me.tigerhix.lib.scoreboard.type.Entry;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import me.tigerhix.lib.scoreboard.type.ScoreboardHandler;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TestCore extends JavaPlugin {

    public final Map<UUID, Scoreboard> scoreboardMap = Collections.synchronizedMap(new HashMap<>());
    public static TestCore INSTANCE;
    public boolean kauriEnabled;
    public Plugin kauri;
    public Database database;
    private CheatListeners listeners;
    //Lag Information
    public RollingAverageDouble tps = new RollingAverageDouble(4, 20);
    public long lastTick;
    public int currentTick;

    public void onEnable() {
        INSTANCE = this;
        MiscUtils.printToConsole("Loading TestCore v" + getDescription().getVersion() + "...");
        MiscUtils.printToConsole("Running scanner...");
        Atlas.getInstance().initializeScanner(this, true, true);
        if(kauriEnabled =
                ((kauri = Bukkit.getPluginManager().getPlugin("Kauri")) != null
                        && Bukkit.getPluginManager().isPluginEnabled("Kauri"))) {
            MiscUtils.printToConsole("Kauri enabled! Loading Kauri Test server specific things...");
            listeners = new CheatListeners();
            ScoreboardLib.setPluginInstance(this);
            for(Player player : Bukkit.getOnlinePlayers()) {
                Scoreboard scoreboard = getScoreboard(player);
                scoreboard.activate();
                scoreboardMap.put(player.getUniqueId(), scoreboard);
            }
        }
        if(Bukkit.getPluginManager().getPlugin("StrikePractice") != null) {
            try {
                Class.forName("dev.brighten.spigot.knockback.KnockbackProfile");
                Bukkit.getPluginManager().registerEvents(new StrikePracticePlugin(), this);
            } catch(ClassNotFoundException | NullPointerException e) {
                MiscUtils.printToConsole("fSpigot is not present. Unregistering...");
                HandlerList.unregisterAll(this);
                return;
            }
        }
        database = new FlatfileDatabase("testcoreInfo");
        database.loadMappings();
        MiscUtils.printToConsole("Setting times of players based on settings...");
        Bukkit.getOnlinePlayers().forEach(player ->
                player.setPlayerTime(Settings.timeOfDay.getValue(player).timeMillis, false));

        //Restart every 12 hours.
        if(ConfigSettings.serverRestart) {
            MiscUtils.printToConsole("Restarting server in 12 hours...");
            new BukkitRunnable() {
                public void run() {
                    MiscUtils.printToConsole("&cShutting down server...");
                    Bukkit.getServer().shutdown();
                }
            }.runTaskLater(this, 864000L);
        }
    }

    private void runTpsTask() {
        AtomicInteger ticks = new AtomicInteger();
        AtomicLong lastTimeStamp = new AtomicLong(0);
        RunUtils.taskTimer(() -> {
            ticks.getAndIncrement();
            currentTick++;
            long currentTime = System.currentTimeMillis();
            if(ticks.get() >= 10) {
                ticks.set(0);
                tps.add(500D / (currentTime - lastTimeStamp.get()) * 20);
                lastTimeStamp.set(currentTime);
            }
            lastTick = currentTime;
        }, this, 1L, 1L);
    }

    public void onDisable() {
        if(listeners != null) {
            KauriAPI.INSTANCE.unregisterEvents(this);
        }
        ScaffoldListeners.reset();
        runBungeeStuff();
        scoreboardMap.clear();
        HandlerList.unregisterAll(this);
        Atlas.getInstance().getCommandManager(this).unregisterCommands();
        Bukkit.getScheduler().cancelTasks(this);
    }

    public Scoreboard getScoreboard(Player player) {
        Scoreboard board = ScoreboardLib.createScoreboard(player).setHandler(new ScoreboardHandler() {

            private final HighlightedString testServer = new HighlightedString(ToggleScoreboard.devServer
                    ? "Kauri Dev Server" : "Kauri Test Server", "&f&l", "&7&l");

            @Override
            public String getTitle(Player player) {
                return testServer.next();
            }

            @Override
            public List<Entry> getEntries(Player player) {
                try {
                    User user = User.getUser(player.getUniqueId());
                    EntryBuilder builder = new EntryBuilder()
                            .next(MiscUtils.line(ChatColor.RESET.toString() + Color.Dark_Gray).substring(0, 30))
                            .next("&6&lLag Information")
                            .next("&8» &ePing&7: &f" + player.spigot().getPing())
                            .next("&8» &eTPS&7: &f" + MathUtils.round(tps.getAverage(), 2))
                            .blank()
                            .next("&6&lViolations");
                    if(user.violations.size() == 0) {
                        builder.next("&8» &fNone");
                    } else {
                        user.violations.keySet().stream()
                                .sorted(Comparator.comparing(user.violations::get, Comparator.reverseOrder()))
                                .limit(4).
                                forEach(vl -> {
                                    builder.next("&8» &f"
                                            + vl
                                            + " &7(&c" + MathUtils.round(user.violations.get(vl), 2) + "&7)");
                                });
                        if(user.violations.size() > 5) {
                            builder.next("&7&o*More Violations*");
                        }
                    }

                    if(SpeedTestHandler.testResultsMap.containsKey(player.getUniqueId())) {
                        builder.blank();
                        builder.next("&6&lSpeed Test");
                        TestResult tr = SpeedTestHandler.testResultsMap.get(player.getUniqueId());
                        if(tr.startTime == 0) {
                            builder.next(Color.Red + Color.Italics + "Starting...");
                        } else {
                            builder.next(String.format("&8» &7Average BPS: &f%.3f", tr.average.getAverage()));
                            builder.next(String.format("&8» &7Time Elapsed: &f" + StringUtil
                                    .formatDuration(System.currentTimeMillis() - tr.startTime)));
                        };
                    }

                    if(ToggleScoreboard.devServer) {
                        builder.blank();
                        builder.next("&6&lCurrent Server");
                        builder.next("&8» &f" + ConfigSettings.currentServerip);
                    }
                    return builder
                            .next(MiscUtils.line(Color.Dark_Gray).substring(0, 28)).build();
                } catch(NullPointerException e) {
                    e.printStackTrace();
                }
                return new EntryBuilder().blank().next("&cError. Check console").build();
            }
        }).setUpdateInterval(4);

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
