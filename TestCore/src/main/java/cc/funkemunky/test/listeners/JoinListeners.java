package cc.funkemunky.test.listeners;

import cc.funkemunky.test.TestCore;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Scoreboard scoreboard = TestCore.INSTANCE.getScoreboard(event.getPlayer());

        TestCore.INSTANCE.scoreboardMap.put(event.getPlayer().getUniqueId(), scoreboard);

        scoreboard.activate();

        event.setJoinMessage(null);
    }

   @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
       TestCore.INSTANCE.scoreboardMap.remove(event.getPlayer().getUniqueId());
   }
}
