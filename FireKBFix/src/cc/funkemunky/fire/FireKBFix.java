package cc.funkemunky.fire;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
    private static FireKBFix instance;
    public String serverVersion;

    public void onEnable() {
        pendingVelocity = new CopyOnWriteArrayList<>();
        instance = this;
        serverVersion = Bukkit.getServer().getClass().getPackage().getName().substring(23);

        horizontalMovement = new WeakHashMap<>();

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
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player
                && event.getEntity() instanceof Player
                && !event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
            Player player = (Player) event.getEntity();
            Player attacker = (Player) event.getDamager();
            Vector velocity = new Vector(-Math.sin(attacker.getEyeLocation().getYaw() * 3.1415927F / 180.0F) * (float) 1 * 0.5F, 0, Math.cos(attacker.getEyeLocation().getYaw() * 3.1415927F / 180.0F) * (float) 1 * 0.5F);

            double kbEnchantModifier = player.getItemInHand() != null ? player.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK) * 0.2 : 0;
            double modifyX = getConfig().getDouble("knockbackXZ.initial") + (attacker.isSprinting() ? 0.2 : 0) + (getPotionEffectLevel(player, PotionEffectType.SPEED) * 0.1) + horizontalMovement.getOrDefault(player, 0D) + kbEnchantModifier;
            double modifyZ = getConfig().getDouble("knockbackXZ.initial") + (attacker.isSprinting() ? 0.2 : 0) + (getPotionEffectLevel(player, PotionEffectType.SPEED) * 0.1 + horizontalMovement.getOrDefault(player, 0D)) + kbEnchantModifier;
            ReflectionsUtil.sendVelocityPacket(new Vector(velocity.getX() * modifyX, player.isOnGround() ? getConfig().getDouble("knockbackY.ground") : getConfig().getDouble("knockbackY.air"), velocity.getZ() * modifyZ), player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onVelocity(PlayerVelocityEvent event) {
        if(event.getPlayer().getLastDamageCause() != null && event.getPlayer().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            event.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("firekb.reload")) {
            if(args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <reload, setkb> [args]");
            } else {
                switch(args[0].toLowerCase()) {
                    case "reload":
                        reloadConfig();
                        sender.sendMessage(ChatColor.GREEN + "Reloaded the configuration!");
                        break;
                    default:
                        sender.sendMessage(ChatColor.RED + "Unknown argument \"" + args[0] + "\"!");
                        break;
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "No permission.");
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

    public static FireKBFix getInstance() {
        return instance;
    }
}
