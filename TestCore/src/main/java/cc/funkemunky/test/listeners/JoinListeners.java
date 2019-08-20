package cc.funkemunky.test.listeners;

import cc.funkemunky.anticheat.api.event.PlayerCheatEvent;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.test.TestCore;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Scoreboard scoreboard = TestCore.INSTANCE.getScoreboard(event.getPlayer());

        TestCore.INSTANCE.scoreboardMap.put(event.getPlayer(), scoreboard);

        scoreboard.activate();

        event.setJoinMessage(null);
    }

    @EventHandler
    public void onCheat(PlayerCheatEvent event) {
        TestCore.INSTANCE.lastViolation.put(event.getPlayer().getUniqueId(), event.getCheck().getName());
    }
}
