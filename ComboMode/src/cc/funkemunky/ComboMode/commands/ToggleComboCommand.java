package cc.funkemunky.ComboMode.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cc.funkemunky.ComboMode.Core;

public class ToggleComboCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("combomode.admin") && !sender.isOp()) {
			sender.sendMessage(ChatColor.RED + "No permission.");
			return true;
		}
		
		boolean isCombo = Core.getInstance().isComboMode() ? false : true;
		
		Core.getInstance().setComboMode(isCombo);
		
		if(isCombo) {
			if(Bukkit.getOnlinePlayers().length > 0) {
				for(Player player : Bukkit.getOnlinePlayers()) {
					player.setNoDamageTicks(0);
					player.setMaximumNoDamageTicks(1);
				}
			}
			Bukkit.broadcastMessage(ChatColor.GREEN + "Combo mode has been turned on!");
		} else {
			if(Bukkit.getOnlinePlayers().length > 0) {
				for(Player player : Bukkit.getOnlinePlayers()) {
					player.setNoDamageTicks(20);
					player.setMaximumNoDamageTicks(20);
				}
			}
			Bukkit.broadcastMessage(ChatColor.GREEN + "Combo mode has been turned off!");
		}
		return true;
	}

}
