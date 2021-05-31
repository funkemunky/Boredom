package cc.funkemunky.test.listeners;

import cc.funkemunky.api.events.AtlasListener;
import cc.funkemunky.api.events.Listen;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.msg.ChatBuilder;
import cc.funkemunky.test.user.Settings;
import cc.funkemunky.test.user.User;
import dev.brighten.api.listener.KauriCancelEvent;
import dev.brighten.api.listener.KauriFlagEvent;
import dev.brighten.api.listener.KauriPunishEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.github.paperspigot.Title;

import java.awt.*;

public class CheatListeners implements AtlasListener {

    @Listen
    public void onCheat(KauriFlagEvent event) {
        User user = User.getUser(event.player.getUniqueId());

        if(!user.violations.containsKey(event.check.getName())) {
            user.violations.put(event.check.getName(), event.check.getVl());
        } else if(user.violations.get(event.check.getName()) < event.check.getVl()) {
            user.violations.put(event.check.getName(), event.check.getVl());
        }
    }

    private static TextComponent kickTitle;

    @Listen
    public void onEvent(KauriPunishEvent event) {
        if(!User.getUser(event.getPlayer().getUniqueId()).isAllowingKick()) {
            TextComponent comp = new TextComponent(kickTitle);

            final Title title = new Title(new BaseComponent[]{kickTitle},
                    ChatBuilder.create().text(event.getCheck().getName()).color(Color.White).build(),
                    3, 40, 3);
            event.getPlayer().sendTitle(title);
            event.setCancelled(true);
        }
    }

    @Listen
    public void onEvent(KauriCancelEvent event) {
        event.setCancelled(!User.getUser(event.getPlayer().getUniqueId()).isAllowingCancel());
    }

    static {
        kickTitle = new TextComponent("Kauri kicked you for:");
        kickTitle.setColor(ChatColor.YELLOW);
    }
}
