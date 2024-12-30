package cc.funkemunky.test.listeners;

import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.MathUtils;
import cc.funkemunky.api.utils.MiscUtils;
import cc.funkemunky.test.TestCore;
import cc.funkemunky.test.user.User;
import dev.brighten.ac.api.AnticheatAPI;
import dev.brighten.ac.api.check.ECheck;
import dev.brighten.ac.api.event.AnticheatEvent;
import dev.brighten.ac.api.event.result.CancelResult;
import dev.brighten.ac.api.event.result.FlagResult;
import dev.brighten.ac.api.event.result.PunishResult;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.github.paperspigot.Title;

import java.util.ArrayList;
import java.util.List;

public class CheatListenersAnticheat implements AnticheatEvent {

    private static TextComponent kickTitle;

    public CheatListenersAnticheat() {
        AnticheatAPI.INSTANCE.registerEvent(TestCore.INSTANCE, this);
    }

    static {
        kickTitle = new TextComponent("Anticheat kicked you for:");
        kickTitle.setColor(ChatColor.YELLOW);
    }

    static List<TextComponent> devComponents = new ArrayList<>(), components = new ArrayList<>();
    static {

        for (BaseComponent dev : new ComponentBuilder("[").color(ChatColor.DARK_GRAY).append("Dev")
                .color(ChatColor.RED).append("]").color(ChatColor.DARK_GRAY).create()) {
            devComponents.add((TextComponent)dev);
        }

        BaseComponent[] textComp = new ComponentBuilder("[").color(ChatColor.DARK_GRAY).append("!")
                .color(ChatColor.DARK_RED).append("]").color(ChatColor.DARK_GRAY).append(" %player%")
                .color(ChatColor.WHITE).append(" flagged").color(ChatColor.GRAY).append(" %check%")
                .color(ChatColor.WHITE).append(" (").color(ChatColor.DARK_GRAY).append("x%vl%")
                .color(ChatColor.YELLOW).append(")").color(ChatColor.DARK_GRAY).create();

        for (BaseComponent bc : textComp) {
            devComponents.add(new TextComponent((TextComponent)bc));
            components.add(new TextComponent((TextComponent)bc));
        }
    }

    @Override
    public PunishResult onPunish(Player player, ECheck check, List<String> commands, boolean cancelled) {
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
    public FlagResult onFlag(Player player, ECheck check, String info, boolean cancelled) {
        User user = User.getUser(player.getUniqueId());

        if(!user.violations.containsKey(check.getName())) {
            user.violations.put(check.getName(), check.getVl());
        } else if(user.violations.get(check.getName()) < check.getVl()) {
            user.violations.put(check.getName(), check.getVl());
        }

        List<BaseComponent> toSend = new ArrayList<>();

        for (TextComponent tc : components) {
            TextComponent ntc = new TextComponent(tc);
            ntc.setText(formatAlert(tc.getText(), player, check, info));

            ntc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Description:")
                    .color(ChatColor.YELLOW)
                    .append(formatAlert(" %desc%\n", player, check, info)).color(ChatColor.WHITE).append("Info:")
                    .color(ChatColor.YELLOW)
                    .append(formatAlert(" %info%\n", player, check, info)).color(ChatColor.WHITE)
                    .append("\n").append("Click to teleport to player")
                    .create()));;

            toSend.add(ntc);
        }

        player.spigot().sendMessage(toSend.toArray(new BaseComponent[0]));
        return FlagResult.builder().cancelled(cancelled).build();
    }

    private String formatAlert(String toFormat, Player player, ECheck checkData, String info) {
        return addPlaceHolders(player, checkData, Color.translate(toFormat.replace("%desc%", String.join("\n",
                        MiscUtils.splitIntoLine("", 20))))
                .replace("%info%", info));
    }

    private String addPlaceHolders(Player player, ECheck checkData, String string) {
        return string.replace("%player%", player.getName())
                .replace("%check%", checkData.getName()
                .replace("%name%",  player.getName()))
                .replace("%vl%", String.valueOf(MathUtils.round(checkData.getVl(), 1)));
    }

    @Override
    public CancelResult onCancel(Player player, ECheck eCheck, boolean cancelled) {
        return CancelResult.builder().cancelled(!User.getUser(player.getUniqueId()).isAllowingCancel()).build();
    }

    @Override
    public EventPriority priority() {
        return EventPriority.NORMAL;
    }
}
