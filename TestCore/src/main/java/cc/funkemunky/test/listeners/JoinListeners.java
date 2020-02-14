package cc.funkemunky.test.listeners;

import cc.funkemunky.api.utils.Init;
import cc.funkemunky.test.TestCore;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Init
public class JoinListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(TestCore.INSTANCE.kauriEnabled) {
            Scoreboard scoreboard = TestCore.INSTANCE.getScoreboard(event.getPlayer());

            TestCore.INSTANCE.scoreboardMap.put(event.getPlayer().getUniqueId(), scoreboard);

            scoreboard.activate();
        }

        event.setJoinMessage(null);
    }

   @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
      if(TestCore.INSTANCE.kauriEnabled) {
          Scoreboard board = TestCore.INSTANCE.scoreboardMap.get(event.getPlayer().getUniqueId());

          if(board != null) {
              board.deactivate();
              TestCore.INSTANCE.scoreboardMap.remove(event.getPlayer().getUniqueId());
          }
      }
      event.setQuitMessage(null);
   }
}
