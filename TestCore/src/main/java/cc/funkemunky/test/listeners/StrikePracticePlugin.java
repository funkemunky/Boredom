package cc.funkemunky.test.listeners;

import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.MiscUtils;
import cc.funkemunky.test.TestCore;
import dev.brighten.spigot.knockback.KnockbackModule;
import dev.brighten.spigot.knockback.KnockbackProfile;
import ga.strikepractice.battlekit.BattleKit;
import ga.strikepractice.events.BotDuelEndEvent;
import ga.strikepractice.events.BotDuelStartEvent;
import ga.strikepractice.events.DuelEndEvent;
import ga.strikepractice.events.DuelStartEvent;
import net.citizensnpcs.npc.CitizensNPC;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

@Init(requirePlugins = "StrikePractice")
public class StrikePracticePlugin implements Listener {

    public StrikePracticePlugin() {
        MiscUtils.printToConsole(Color.Green + "Setup hook into StrikePractice.");
    }

    @EventHandler
    public void onStrike(BotDuelStartEvent event) {
        final BotDuelStartEvent e = event;
        new BukkitRunnable() {
            public void run() {
                ((CraftPlayer) e.getPlayer()).getHandle().setKnockback(getProfileByKit(e.getFight().getKit()));
                ((CraftHumanEntity)e.getBot().getEntity())
                        .getHandle().setKnockback(getProfileByKit(e.getFight().getKit()));
            }
        }.runTaskLater(TestCore.INSTANCE, 2L);
    }

    @EventHandler
    public void onStrike(BotDuelEndEvent event) {
        final BotDuelEndEvent e = event;

        new BukkitRunnable() {
            public void run() {
                ((CraftPlayer) e.getPlayer()).getHandle().setKnockback(KnockbackModule.getProfile());
                if(e.getBot() != null && e.getBot().getEntity() != null) {
                    ((CraftHumanEntity)e.getBot().getEntity())
                            .getHandle().setKnockback(KnockbackModule.getProfile());
                }
            }
        }.runTaskLater(TestCore.INSTANCE, 2L);
    }

    @EventHandler
    public void onStrike(DuelStartEvent event) {
        ((CraftPlayer) event.getPlayer1()).getHandle().setKnockback(getProfileByKit(event.getFight().getKit()));
        ((CraftPlayer) event.getPlayer2()).getHandle().setKnockback(getProfileByKit(event.getFight().getKit()));
    }

    @EventHandler
    public void onStrike(DuelEndEvent event) {
        ((CraftPlayer) event.getWinner()).getHandle().setKnockback(KnockbackModule.getProfile());
        ((CraftPlayer) event.getLoser()).getHandle().setKnockback(KnockbackModule.getProfile());
    }

    public KnockbackProfile getProfileByKit(BattleKit kit) {
        return KnockbackModule.INSTANCE.profiles
                .getOrDefault(kit.getName(), KnockbackModule.getProfile());
    }
}
