package cc.funkemunky.kit.commands.kit.args;

import cc.funkemunky.api.commands.FunkeArgument;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.kit.utils.LocationUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnArgs extends FunkeArgument {
    public SpawnArgs() {
        super("spawn", "spawn", "Teleports you to set spawn point.", "kit.command.spawn");
    }

    @Override
    public void onArgument(CommandSender sender, Command command, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            if(LocationUtil.getSpawnLocation() != null) {
                player.teleport(LocationUtil.getSpawnLocation());

                sender.sendMessage(Color.Green + "Teleported to spawn!");
            } else {
                sender.sendMessage(Color.Red + "The spawn point must be set before you can teleport to it.");
            }
        } else {
            sender.sendMessage(Color.Red + "You must be a player to use this feature.");
        }
    }
}
