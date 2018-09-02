package cc.funkemunky.load.cmd;

import cc.funkemunky.load.ChunkLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkLoaderCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.hasPermission("cl.admin")
                && strings.length == 1) {
            Player player = Bukkit.getPlayer(strings[0]);

            if(player != null) {
                player.getInventory().addItem(ChunkLoader.getInstance().chunkLoaderItem);
                commandSender.sendMessage("Given 1 chunk loader.");
            }
            return true;
        } else {
            commandSender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }
    }


}
