package cc.funkemunky.Meme.commands;

import cc.funkemunky.Meme.Core;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.awt.*;

public class NoFreeNukesCmd implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender.hasPermission("nofreenukes.admin")) {
            if(args.length == 0) {
                sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------");
                sender.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "NoFreeNukes v" + Core.getCore().getDescription().getVersion() + " Help");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.YELLOW + "Commands:");
                sender.sendMessage(ChatColor.GRAY + "/nofreenukes " + Color.WHITE + "The NoFreeNukes main command.");
                sender.sendMessage(ChatColor.GRAY  + "/nofreenukes reload " + Color.WHITE + "Reload the config.");
                sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------");
            } else {
                if(args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(ChatColor.GRAY + "Reloading NoFreeNukes...");
                    Core.getCore().reloadConfig();
                    sender.sendMessage(ChatColor.GREEN + "Reloaded NoFreeNukes!");
                    return true;
                }
                sender.sendMessage(ChatColor.RED + "Unknown argument! Do /nofreenukes to view the help page.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This server is using NoFreeNukes v" + Core.getCore().getDescription().getVersion() + " by funkemunky.");
        }
        return true;
    }
}
