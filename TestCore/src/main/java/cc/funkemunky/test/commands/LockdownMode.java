package cc.funkemunky.test.commands;

import cc.funkemunky.api.commands.ancmd.Command;
import cc.funkemunky.api.commands.ancmd.CommandAdapter;
import cc.funkemunky.api.utils.Init;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Init(commands = true)
public class LockdownMode {

    public static boolean lockdownMode = false;
    public static List<UUID> uuids = new ArrayList<>();

    @Command(name = "lockdownmode", permission = "test.lockdown")
    public void onCommand(CommandAdapter cmd) {
        if(cmd.getArgs().length == 0) {
            lockdownMode = !lockdownMode;

            cmd.getSender().sendMessage("Lockdown mode: " + lockdownMode);
        } else if(cmd.getArgs().length > 1) {
            if(cmd.getArgs()[0].equalsIgnoreCase("add")) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(cmd.getArgs()[1]);

                uuids.add(player.getUniqueId());
                cmd.getSender().sendMessage("Added " + player.getName() + " to lockdown whitelist.");
            }
        }
    }
}
