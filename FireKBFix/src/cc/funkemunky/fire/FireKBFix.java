package cc.funkemunky.fire;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class FireKBFix extends JavaPlugin implements Listener, CommandExecutor {

    private List<PendingVelocity> pendingVelocity;
    private Map<Player, Double> horizontalMovement;
    private Map<Player, Long> lastMovePacket;

    public void onEnable() {
        pendingVelocity = new CopyOnWriteArrayList<>();

        horizontalMovement = new WeakHashMap<>();
        lastMovePacket = new WeakHashMap<>();

        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("firekbfix").setExecutor(this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom().clone();
        Location to = event.getTo().clone();

        to.setY(0);
        from.setY(0);
        double horizontal = to.distance(from);

        if(horizontal > 0) {
            horizontalMovement.put(event.getPlayer(), horizontal);
        } else {
            horizontalMovement.remove(event.getPlayer());
        }
        lastMovePacket.put(event.getPlayer(), System.currentTimeMillis());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player
                && event.getEntity() instanceof Player) {
            pendingVelocity.add(new PendingVelocity((Player) event.getEntity(), (Player) event.getDamager()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onVelocity(PlayerVelocityEvent event) {
        Optional<PendingVelocity> opVel = pendingVelocity.stream().filter(velocity -> velocity.getPending().getUniqueId().equals(velocity.getPending().getUniqueId())).findFirst();
        if(opVel.isPresent()) {
            PendingVelocity vel = opVel.get();

            Vector velocity = new Vector(-Math.sin(vel.getAttacker().getEyeLocation().getYaw() * 3.1415927F / 180.0F) * (float) 1 * 0.5F, 0.1, Math.cos(vel.getAttacker().getEyeLocation().getYaw() * 3.1415927F / 180.0F) * (float) 1 * 0.5F);

            velocity.setY(0);

            double modifyX = getConfig().getDouble("knockbackXZ.initial") + (vel.getAttacker().isSprinting() ? 0.2 : 0) + (getPotionEffectLevel(event.getPlayer(), PotionEffectType.SPEED) * 0.1) + horizontalMovement.getOrDefault(event.getPlayer(), 0D);
            double modifyZ = getConfig().getDouble("knockbackXZ.initial") + (vel.getAttacker().isSprinting() ? 0.2 : 0) + (getPotionEffectLevel(event.getPlayer(), PotionEffectType.SPEED) * 0.1 + horizontalMovement.getOrDefault(event.getPlayer(), 0D));
            event.setVelocity(new Vector(velocity.getX() * modifyX, vel.getPending().isOnGround() ? getConfig().getDouble("knockbackY.ground") : getConfig().getDouble("knockbackY.air"), velocity.getZ() * modifyZ));
            pendingVelocity.remove(vel);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("firekb.reload")) {
            reloadConfig();
            sender.sendMessage("Reloaded the configuration!");
        } else {
            sender.sendMessage("No permission.");
        }
        return true;
    }

    private int getPotionEffectLevel(Player player, PotionEffectType pet) {
        for (PotionEffect pe : player.getActivePotionEffects()) {
            if (!pe.getType().getName().equals(pet.getName())) continue;
            return pe.getAmplifier() + 1;
        }
        return 0;
    }
}
