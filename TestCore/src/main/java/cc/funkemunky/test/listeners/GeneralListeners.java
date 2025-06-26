package cc.funkemunky.test.listeners;

import cc.funkemunky.api.Atlas;
import cc.funkemunky.api.tinyprotocol.api.Packet;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.RunUtils;
import cc.funkemunky.api.utils.msg.ChatBuilder;
import cc.funkemunky.test.TestCore;
import cc.funkemunky.test.commands.LockdownMode;
import cc.funkemunky.test.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

@Init
public class GeneralListeners implements Listener {

    public GeneralListeners() {
        RunUtils.taskTimer(() -> Bukkit.getWorlds().forEach(world -> world.setTime(8000L)), 10L, 4L);

        Atlas.getInstance().getPacketProcessor().process(TestCore.INSTANCE, event -> {

        }, Packet.Client.ARM_ANIMATION, Packet.Client.FLYING);
    }

    @EventHandler
    public void onEvent(EntityTargetEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEvent(BlockBreakEvent event) {
        if(StrikePracticePlugin.INSTANCE != null && StrikePracticePlugin.notInTestMap(event.getPlayer())) return;
        if(!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEvent(AsyncPlayerPreLoginEvent event) {
        if(LockdownMode.lockdownMode && !LockdownMode.uuids.contains(event.getUniqueId())) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, "gtfo");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST) //So anything else can modify this if necessary.
    public void onEvent(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player attacker && event.getEntity() instanceof Player) {

            if(StrikePracticePlugin.INSTANCE != null && StrikePracticePlugin.notInTestMap(attacker)) return;

            User user = User.getUser(attacker.getUniqueId());

            if(user == null) return;

            if(user.isNoDamage()) {
                event.setCancelled(true);
                attacker.sendMessage(ChatBuilder
                        .create("You cannot attack other players while your noDamage is set to true.")
                        .color(Color.Red).build());
            } else {
                event.setDamage(0.000001);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)  //So anything else can modify this if necessary.
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player attacker) {

            if(StrikePracticePlugin.INSTANCE != null && StrikePracticePlugin.notInTestMap(attacker)) return;

            User user = User.getUser(attacker.getUniqueId());

            if(user == null) return;

            if(user.isNoDamage()) event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)  //So anything else can modify this if necessary.
    public void onHunger(FoodLevelChangeEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(StrikePracticePlugin.INSTANCE != null && StrikePracticePlugin.notInTestMap(player)) return;

            User user = User.getUser(player.getUniqueId());

            if(user == null) return;

            if(user.isNoHunger()) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.LEFT_CLICK_AIR)) {
            User user = User.getUser(event.getPlayer().getUniqueId());

            long timeStamp = System.currentTimeMillis();
            long delta = (timeStamp - user.lastClick);

            if(user.lastClick > 0) {
                user.cpsAvg.add(1000D / delta);
            }

            user.lastClick = timeStamp;
        }
    }
}
