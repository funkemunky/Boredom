package cc.funkemunky.test;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.bungee.BungeeAPI;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.MathUtils;
import cc.funkemunky.api.utils.MiscUtils;
import cc.funkemunky.api.utils.RunUtils;
import cc.funkemunky.api.utils.math.RollingAverageDouble;
import cc.funkemunky.test.commands.ToggleScoreboard;
import cc.funkemunky.test.db.Mongo;
import cc.funkemunky.test.handlers.SpeedTestHandler;
import cc.funkemunky.test.handlers.TestResult;
import cc.funkemunky.test.listeners.CheatListeners;
import cc.funkemunky.test.listeners.CheatListenersAnticheat;
import cc.funkemunky.test.listeners.ScaffoldListeners;
import cc.funkemunky.test.listeners.StrikePracticePlugin;
import cc.funkemunky.test.user.Opponent;
import cc.funkemunky.test.user.Settings;
import cc.funkemunky.test.user.User;
import cc.funkemunky.test.utils.ConfigSettings;
import cc.funkemunky.test.utils.StringUtil;
import dev.brighten.ac.api.AnticheatAPI;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TestCore extends JavaPlugin {

    public final Map<UUID, Scoreboard> scoreboardMap = Collections.synchronizedMap(new HashMap<>());
    public static TestCore INSTANCE;
    public boolean kauriEnabled;
    public Plugin kauri;
    public Mongo database;
    private CheatListeners listeners;
    private CheatListenersAnticheat listeners2;
    //Lag Information
    public RollingAverageDouble tps = new RollingAverageDouble(4, 20);
    public long lastTick;
    public int currentTick;

    public void onEnable() {
        INSTANCE = this;
        MiscUtils.printToConsole("Loading TestCore v" + getDescription().getVersion() + "...");
        MiscUtils.printToConsole("Running scanner...");
        Atlas.getInstance().initializeScanner(this, true, true);

        runTpsTask();

        MiscUtils.printToConsole("Loading Mongo...");
        database = new Mongo();

        if(kauriEnabled =
                ((kauri = Bukkit.getPluginManager().getPlugin("Kauri")) != null
                        && Bukkit.getPluginManager().isPluginEnabled("Kauri")) && Double.parseDouble(Bukkit.getPluginManager().getPlugin("Kauri").getDescription().getVersion()) < 3.0) {
            MiscUtils.printToConsole("Kauri enabled! Loading Kauri Test server specific things...");
            listeners = new CheatListeners();
        } else if(kauriEnabled = ((kauri = Bukkit.getPluginManager().getPlugin("Kauri")) != null
                && Bukkit.getPluginManager().isPluginEnabled("Kauri"))) {
            MiscUtils.printToConsole("Enterprise Anticheat enabled! Loading Anticheat Test server specific things...");
            listeners2 = new CheatListenersAnticheat();
        }

        ScoreboardLib.setPluginInstance(this);
        for(Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = getScoreboard(player);
            scoreboard.activate();
            scoreboardMap.put(player.getUniqueId(), scoreboard);
        }

        if(Bukkit.getPluginManager().getPlugin("StrikePractice") != null || Bukkit.getPluginManager().getPlugin("tPractice") != null) {
            try {
                Class.forName("dev.brighten.spigot.knockback.KnockbackProfile");
                Bukkit.getPluginManager().registerEvents(new StrikePracticePlugin(), this);
            } catch(ClassNotFoundException | NullPointerException e) {
                MiscUtils.printToConsole("fSpigot is not present. Unregistering...");
                HandlerList.unregisterAll(this);
                return;
            }
        }
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

            for (User user : User.users.values()) {
                if(currentTime - user.lastClick > 3000L && user.cpsAvg.getAverage() > 0) {
                    user.cpsAvg.add(0.00001);
                }
            }
        }, this, 1L, 1L);
    }

    public void onDisable() {
        if(listeners != null || listeners2 != null) {
            AnticheatAPI.INSTANCE.unregisterEvents(this);
            listeners = null;
            listeners2 = null;
        }
        ScaffoldListeners.reset();
        runBungeeStuff();
        scoreboardMap.clear();
        kauri = null;
        database = null;

        HandlerList.unregisterAll(this);
        Atlas.getInstance().getCommandManager(this).unregisterCommands();
        Bukkit.getScheduler().cancelTasks(this);
        INSTANCE = null;
    }

    public Scoreboard getScoreboard(Player player) {
        Scoreboard board = ScoreboardLib.createScoreboard(player).setHandler(new ScoreboardHandler() {

            private final HighlightedString testServer = new HighlightedString(ToggleScoreboard.devServer
                    ? "Dev Server" : "Test Server", "&f&l", "&7&l");

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
                            .next("&6&lInformation")
                            .next("&8» &ePing&7: &f" + player.spigot().getPing())
                            .next("&8» &eTPS&7: &f" + MathUtils.round(tps.getAverage(), 2))
                            .next(String.format("&8» &eCPS&7: &f%.1f", user.cpsAvg.getAverage())).blank();

                    if(user.getFight() != null) {
                        builder.next("&6&lFight")
                                .next("&8» &eKit&7: &f" + user.getFight().getKitName())
                                .next("&8» &eArena&7: &f" + user.getFight().getArenaName())
                                .next("&8» &eDuration&7: &f" + user.getFight().getFormattedDuration());

                        if(user.getFight().getOpponents().size() == 1) {
                            Opponent opponent = user.getFight().getOpponents().get(0);

                            builder.next("&8» &eOpponent&7: &f" + opponent.getName())
                                    .blank();
                        } else {
                            builder.next("&8» &eOpponents&7:");
                            for (Opponent opponent : user.getFight().getOpponents()) {
                                builder.next("  &7»» &f" + opponent.getName());
                            }
                            builder.blank();
                        }
                    }

                    builder.next("&6&lViolations");
                    if(user.violations.size() == 0) {
                        builder.next("&8» &fNone");
                    } else {
                        user.violations.keySet().stream()
                                .sorted(Comparator.comparing(user.violations::get, Comparator.reverseOrder()))
                                .limit(4).
                                forEach(vl -> {
                                    builder.next("&8» &f"
                                            + vl
                                            + " &7(&c" + String.valueOf(MathUtils.round(user.violations.get(vl), 2)).replace(".", "&c.") + "&7)");
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
                            builder.next(String.format("&8» &7Average BPS: &f%s", String.valueOf(MathUtils.round(tr.average.getAverage(), 3)).replace(".", "&f")));
                            builder.next("&8» &7Time Elapsed: &f" + StringUtil
                                    .formatDuration(System.currentTimeMillis() - tr.startTime));
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
