package cc.funkemunky.test.listeners;

import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.ConfigSetting;
import cc.funkemunky.api.utils.MiscUtils;
import cc.funkemunky.test.TestCore;
import cc.funkemunky.test.user.User;
import cc.funkemunky.test.user.WrappedFight;
import dev.brighten.spigot.knockback.KnockbackModule;
import dev.brighten.spigot.knockback.KnockbackProfile;
import ga.strikepractice.StrikePracticeAPI;
import ga.strikepractice.battlekit.BattleKit;
import ga.strikepractice.events.BotDuelEndEvent;
import ga.strikepractice.events.BotDuelStartEvent;
import ga.strikepractice.events.DuelEndEvent;
import ga.strikepractice.events.DuelStartEvent;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class StrikePracticePlugin implements Listener {

    public static StrikePracticePlugin INSTANCE;
    public StrikePracticePlugin() {
        INSTANCE = this;

        MiscUtils.printToConsole(Color.Green + "Setup hook into StrikePractice.");
    }

    public static boolean notInTestMap(Player player) {
        if(INSTANCE == null) return false;

        return INSTANCE.runAPICheck(player);
    }

    protected boolean runAPICheck(Player player) {
        return StrikePracticeAPI.isInEvent(player) || StrikePracticeAPI.isInFight(player);
    }
    @ConfigSetting(name = "bot-kb-preset")
    private static String botKnockback = "none";

    @EventHandler
    public void onStrike(BotDuelStartEvent event) {
        final BotDuelStartEvent e = event;
        new BukkitRunnable() {
            public void run() {
                ((CraftPlayer) e.getPlayer()).getHandle().setKnockback(getProfileByKit(e.getFight().getKit()));
                ((CraftHumanEntity)e.getBot().getEntity())
                        .getHandle().setKnockback(!botKnockback.equals("none")
                        ? KnockbackModule.INSTANCE.profiles.getOrDefault(botKnockback,
                        getProfileByKit(e.getFight().getKit())) :  getProfileByKit(e.getFight().getKit()));
            }
        }.runTaskLater(TestCore.INSTANCE, 2L);

        User user = User.getUser(e.getPlayer().getUniqueId());

        user.setFight(new WrappedFight(e.getFight(), event.getBot()));
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

        User user = User.getUser(e.getPlayer().getUniqueId());
        user.setFight(null);
    }

    @EventHandler
    public void onStrike(DuelStartEvent event) {
        ((CraftPlayer) event.getPlayer1()).getHandle().setKnockback(getProfileByKit(event.getFight().getKit()));
        ((CraftPlayer) event.getPlayer2()).getHandle().setKnockback(getProfileByKit(event.getFight().getKit()));

        User user1 = User.getUser(event.getPlayer1().getUniqueId()),
                user2 = User.getUser(event.getPlayer2().getUniqueId());

        user1.setFight(new WrappedFight(event.getFight(), event.getPlayer2()));
        user2.setFight(new WrappedFight(event.getFight(), event.getPlayer1()));
    }

    @EventHandler
    public void onStrike(DuelEndEvent event) {
        ((CraftPlayer) event.getWinner()).getHandle().setKnockback(KnockbackModule.getProfile());
        ((CraftPlayer) event.getLoser()).getHandle().setKnockback(KnockbackModule.getProfile());

        User user1 = User.getUser(event.getWinner().getUniqueId()),
                user2 = User.getUser(event.getLoser().getUniqueId());

        user1.setFight(null);
        user2.setFight(null);
    }

    public KnockbackProfile getProfileByKit(BattleKit kit) {
        return KnockbackModule.INSTANCE.profiles
                .getOrDefault(kit.getName().toLowerCase(), KnockbackModule.getProfile());
    }
}
