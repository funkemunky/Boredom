package cc.funkemunky.test.listeners;

import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.RunUtils;
import cc.funkemunky.test.TestCore;
import cc.funkemunky.test.user.Settings;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
        event.getPlayer().setPlayerTime(Settings.timeOfDay.getValue(event.getPlayer()).timeMillis, false);

        RunUtils.taskLater(() -> {
            TextComponent component = new TextComponent("Try the new ");
            component.setColor(ChatColor.GRAY);

            TextComponent command = new TextComponent("/testsettings");
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent
                    .fromLegacyText(new TextComponent(Color.Gray + Color.Italics + "Click me to run"
                            + Color.White + Color.Italics + "/testsettings")
                            .toLegacyText())));
            command.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/testsettings"));
            command.setColor(ChatColor.YELLOW);
            command.setItalic(true);

            TextComponent ending = new TextComponent(" command! You can change settings on how Kauri handles you ;)");
            ending.setColor(ChatColor.GRAY);

            event.getPlayer().sendMessage(component, command, ending);
        }, 40L);
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
