package cc.funkemunky.test.listeners;

import cc.funkemunky.api.events.Listen;
import cc.funkemunky.test.TestCore;
import cc.funkemunky.test.user.User;
import dev.brighten.api.KauriAPI;
import dev.brighten.api.check.CancelType;
import dev.brighten.api.check.KauriCheck;
import dev.brighten.api.event.KauriEvent;
import dev.brighten.api.event.result.CancelResult;
import dev.brighten.api.event.result.FlagResult;
import dev.brighten.api.event.result.PunishResult;
import dev.brighten.api.listener.KauriCancelEvent;
import dev.brighten.api.listener.KauriFlagEvent;
import dev.brighten.api.listener.KauriPunishEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.github.paperspigot.Title;

import java.util.List;

public class CheatListeners implements KauriEvent {

    private static final TextComponent kickTitle;

    public CheatListeners() {
        KauriAPI.INSTANCE.registerEvent(TestCore.INSTANCE, this);
    }

    static {
        kickTitle = new TextComponent("Kauri kicked you for:");
        kickTitle.setColor(ChatColor.YELLOW);
    }

    @Override
    public PunishResult onPunish(Player player, KauriCheck check, String broadcastMessage, List<String> list,
                                 boolean cancelled) {
        if(!User.getUser(player.getUniqueId()).isAllowingKick()) {
            final Title title = new Title(new BaseComponent[]{kickTitle},
                    new ComponentBuilder(check.getName()).color(ChatColor.WHITE).create(),
                    3, 40, 3);
            player.sendTitle(title);
            return PunishResult.builder().cancelled(true).build();
        }
        return PunishResult.builder().cancelled(false).build();
    }

    @Override
    public FlagResult onFlag(Player player, KauriCheck check, String information, boolean cancelled) {
        User user = User.getUser(player.getUniqueId());

        if(!user.violations.containsKey(check.getName())) {
            user.violations.put(check.getName(), check.getVl());
        } else if(user.violations.get(check.getName()) < check.getVl()) {
            user.violations.put(check.getName(), check.getVl());
        }
        
        return FlagResult.builder().cancelled(cancelled).build();
    }

    @Override
    public CancelResult onCancel(Player player, CancelType cancelType, boolean cancelled) {
        return CancelResult.builder().cancelled(!User.getUser(player.getUniqueId()).isAllowingCancel()).build();
    }

    @Override
    public EventPriority priority() {
        return EventPriority.NORMAL;
    }
}
