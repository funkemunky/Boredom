package cc.funkemunky.test.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        /*Scoreboard scoreboard = TestCore.INSTANCE.getScoreboard(event.getPlayer());

        TestCore.INSTANCE.scoreboardMap.put(event.getPlayer(), scoreboard);

        scoreboard.activate();*/

        event.setJoinMessage(null);
    }

   /* @EventHandler
    public void onCheat(PlayerCheatEvent event) {
        TestCore.INSTANCE.lastViolation.put(event.getPlayer().getUniqueId(), event.getCheck().getName());
    }*/
}
