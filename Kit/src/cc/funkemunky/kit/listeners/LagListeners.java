package cc.funkemunky.kit.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class LagListeners implements Listener {

    @EventHandler
    public void onEvent(ProjectileHitEvent event) {
        if(event.getEntity().getType().equals(EntityType.ARROW)) {
            event.getEntity().remove();
        }
    }
}
