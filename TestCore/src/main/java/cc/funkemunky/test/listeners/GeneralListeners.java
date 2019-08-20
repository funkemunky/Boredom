package cc.funkemunky.test.listeners;

import cc.funkemunky.api.utils.Init;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

@Init
public class GeneralListeners implements Listener {
    @EventHandler
    public void entityTarget(EntityTargetEvent e) {
        e.setCancelled(true);
    }
}
