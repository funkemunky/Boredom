package cc.funkemunky.test.commands;

import cc.funkemunky.api.commands.ancmd.Command;
import cc.funkemunky.api.commands.ancmd.CommandAdapter;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.menu.preset.SettingsMenu;
import cc.funkemunky.api.utils.menu.preset.button.SettingButton;
import cc.funkemunky.test.user.Settings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

@Init(commands = true)
public class SettingsCommand {

    @Command(name = "settings", aliases = {"tsettings", "testsettings"}, playerOnly = true)
    public void onCommand(CommandAdapter cmd) {
        SettingsMenu menu = new SettingsMenu("Your Settings",
                new SettingButton(cmd.getPlayer(), "Cancelling",
                        "&fThis will toggle Kauri flag setbacks.", Settings.allowKauriCancel),
                new SettingButton(cmd.getPlayer(), "Punishments",
                        "&fThis will toggle Kauri punishments.", Settings.allowKauriKicking),
                new SettingButton(cmd.getPlayer(), "No Hunger", "&fTurn off food and saturation loss.",
                        Settings.noHunger),
                new SettingButton(cmd.getPlayer(), "No Damage", "&fTurn off any kind of damage.",
                        Settings.noDamage),
                new SettingButton(cmd.getPlayer(), "Time of Day", "&fChange your personal time of day.",
                        Settings.timeOfDay));

        menu.showMenu(cmd.getPlayer());
        cmd.getPlayer().sendMessage(new ComponentBuilder("Opened the test settings menu.")
                .color(ChatColor.GREEN).create());
    }
}