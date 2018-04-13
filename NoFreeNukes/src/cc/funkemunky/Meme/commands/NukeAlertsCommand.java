package cc.funkemunky.Meme.commands;

import cc.funkemunky.Meme.Core;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NukeAlertsCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender.hasPermission("nofreenukes.alerts") && sender instanceof Player) {
            Player player = (Player) sender;

            if(Core.getCore().hasAlertsOn.contains(player)) {
                Core.getCore().hasAlertsOn.remove(player);
                player.sendMessage(ChatColor.GREEN + "Toggled your alerts off!");
            } else {
                Core.getCore().hasAlertsOn.add(player);
                player.sendMessage(ChatColor.GREEN + "Toggled yor alerts on!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "No permission.");
        }
        return true;
    }
}
