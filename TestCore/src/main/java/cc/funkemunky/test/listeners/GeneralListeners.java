package cc.funkemunky.test.listeners;

import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.RunUtils;
import cc.funkemunky.test.commands.LockdownMode;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

@Init
public class GeneralListeners implements Listener {

    public GeneralListeners() {
        RunUtils.taskTimer(() -> Bukkit.getWorlds().forEach(world -> world.setTime(8000L)), 10L, 4L);
    }

    @EventHandler
    public void onEvent(EntityTargetEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onEvent(AsyncPlayerPreLoginEvent event) {
        if(LockdownMode.lockdownMode && !LockdownMode.uuids.contains(event.getUniqueId())) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, "gtfo");
        }
    }
}
