package cc.funkemunky.test.listeners;

import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.ConfigSetting;
import cc.funkemunky.api.utils.MiscUtils;
import cc.funkemunky.test.user.User;
import cc.funkemunky.test.user.WrappedFight;
import ga.strikepractice.StrikePracticeAPI;
import ga.strikepractice.events.BotDuelEndEvent;
import ga.strikepractice.events.BotDuelStartEvent;
import ga.strikepractice.events.DuelEndEvent;
import ga.strikepractice.events.DuelStartEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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

        User user = User.getUser(e.getPlayer().getUniqueId());

        user.setFight(new WrappedFight(e.getFight(), event.getBot()));
    }

    @EventHandler
    public void onStrike(BotDuelEndEvent event) {
        final BotDuelEndEvent e = event;

        User user = User.getUser(e.getPlayer().getUniqueId());
        user.setFight(null);
    }

    @EventHandler
    public void onStrike(DuelStartEvent event) {

        User user1 = User.getUser(event.getPlayer1().getUniqueId()),
                user2 = User.getUser(event.getPlayer2().getUniqueId());

        user1.setFight(new WrappedFight(event.getFight(), event.getPlayer2()));
        user2.setFight(new WrappedFight(event.getFight(), event.getPlayer1()));
    }

    @EventHandler
    public void onStrike(DuelEndEvent event) {

        User user1 = User.getUser(event.getWinner().getUniqueId()),
                user2 = User.getUser(event.getLoser().getUniqueId());

        user1.setFight(null);
        user2.setFight(null);
    }
}
