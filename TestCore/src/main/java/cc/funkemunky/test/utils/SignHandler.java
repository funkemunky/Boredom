/*
  Class from com.faithful.framework.util.SignHandler from FaithfulMC
 */
package cc.funkemunky.test.utils;

import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.com.google.common.collect.HashMultimap;
import cc.funkemunky.api.utils.com.google.common.collect.Multimap;
import cc.funkemunky.test.TestCore;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@Init
public class SignHandler implements Listener
{
    private final Multimap<UUID, SignChange> signUpdateMap = HashMultimap.create();
    public static SignHandler INSTANCE;

    public SignHandler() {
        INSTANCE = this;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerKick(final PlayerQuitEvent event) {
        this.cancelTasks(event.getPlayer(), null, false);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.cancelTasks(event.getPlayer(), null, false);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onWorldChange(final PlayerChangedWorldEvent event) {
        this.cancelTasks(event.getPlayer(), null, false);
    }

    public boolean showLines(final Player player, final Sign sign, final String[] newLines, final long ticks, final boolean forceChange) {
        final String[] lines = sign.getLines();
        if (Arrays.equals(lines, newLines)) {
            return false;
        }
        final Collection<SignChange> signChanges = this.getSignChanges(player);
        final Iterator<SignChange> iterator = signChanges.iterator();
        while (iterator.hasNext()) {
            final SignChange location = iterator.next();
            if (location.sign.equals(sign)) {
                if (!forceChange && Arrays.equals(location.newLines, newLines)) {
                    return false;
                }
                location.runnable.cancel();
                iterator.remove();
                break;
            }
        }
        final Location location2 = sign.getLocation();
        player.sendSignChange(location2, newLines);
        final SignChange signChange;
        if (signChanges.add(signChange = new SignChange(sign, newLines))) {
            final Block block = sign.getBlock();
            final BlockState previous = block.getState();
            final BukkitRunnable runnable = new BukkitRunnable() {
                public void run() {
                    if (SignHandler.this.signUpdateMap.remove(player.getUniqueId(), signChange)
                            && previous.equals(block.getState())) {
                        player.sendSignChange(location2, lines);
                    }
                }
            };
            runnable.runTaskLater(TestCore.INSTANCE, ticks);
            signChange.runnable = runnable;
        }
        return true;
    }

    public Collection<SignChange> getSignChanges(Player player) {
        return this.signUpdateMap.get(player.getUniqueId());
    }

    public void cancelTasks(Sign sign) {
        Iterator<SignChange> iterator = this.signUpdateMap.values().iterator();
        while (iterator.hasNext()) {
            SignChange signChange = iterator.next();
            if (sign != null && !signChange.sign.equals(sign)) continue;
            signChange.runnable.cancel();
            signChange.sign.update();
            iterator.remove();
        }
    }

    public void cancelTasks(Player player, Sign sign, boolean revertLines) {
        UUID uuid = player.getUniqueId();
        Iterator<Map.Entry<UUID, SignChange>> iterator = this.signUpdateMap.entries().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, SignChange> entry = iterator.next();
            if (!entry.getKey().equals(uuid)) continue;
            SignChange signChange = entry.getValue();
            if (sign != null && !signChange.sign.equals(sign)) continue;
            if (revertLines) {
                player.sendSignChange(signChange.sign.getLocation(), signChange.sign.getLines());
            }
            signChange.runnable.cancel();
            iterator.remove();
        }
    }

    private static final class SignChange
    {
        public final Sign sign;
        public final String[] newLines;
        public BukkitRunnable runnable;

        public SignChange(final Sign sign, final String[] newLines) {
            this.sign = sign;
            this.newLines = newLines;
        }
    }
}