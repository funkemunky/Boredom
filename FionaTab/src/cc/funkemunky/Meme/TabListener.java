package cc.funkemunky.Meme;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import anticheat.Fiona;
import anticheat.detections.Checks;
import anticheat.user.User;
import anticheat.utils.Color;
import me.vertises.aztec.tablist.TablistEntrySupplier;

public class TabListener
implements TablistEntrySupplier {
	
    private final Tab plugin;

    public TabListener(Tab tab) {
        this.plugin = tab;
    }

	@Override
	public Table<Integer, Integer, String> getEntries(Player player) {
		HashBasedTable<Integer, Integer, String> table = HashBasedTable.create();

		DecimalFormat format = new DecimalFormat("#.##"); 
		table.put(0, 1, Color.Gold + "Player Information");
		table.put(0, 2, Color.Gray + "Ping: " + Color.White + Fiona.getAC().getPing().getPing(player));
		table.put(0, 3, Color.Gray + "Flight: " + Color.White + player.getAllowFlight());
		table.put(0, 4, Color.Gray + "Op: " + Color.White + player.isOp());
		table.put(1, 0, Color.Gold + Color.Bold + "Fiona Test Server");
		table.put(1, 3, Color.Gold + "Your Violations");
		int i = 4;
		User user = Fiona.getUserManager().getUser(player.getUniqueId());
		if (user.getVLs().size() > 0) {
			for (Checks check : user.getVLs().keySet()) {
				table.put(1, i, Color.White + "- " + Color.Gray + check.getName() + Color.Red + " (" + user.getVLs().get(check) + " VL)");
			    i++;
			} 
		} else {
			table.put(1, 4, Color.Green + Color.Italics + "No violations recorded.");
		}
		table.put(2, 1, Color.Gold + "Fiona Information");
		table.put(2, 2, Color.Gray + "Fiona Version: " + Color.White + plugin.getServer().getPluginManager().getPlugin("Fiona").getDescription().getVersion());
		table.put(2, 3, Color.Gray + "Alerts: " + (user.isHasAlerts() ? Color.Green + user.isHasAlerts() : Color.Red + user.isHasAlerts()));
		table.put(2, 4, Color.Gray + "TPS: " + Color.White + format.format(Fiona.getAC().getPing().getTPS()));
		return table;
	}

    @Override
    public String getHeader(Player player) {
        return Color.Gold + Color.Bold + "Fiona Test Server";
    }

    @Override
    public String getFooter(Player player) {
        return Color.Gold + "By funkemunky and XTasyCode";
    }


    public static ConsoleCommandSender getConsoleCommandSender() {
        return Bukkit.getServer().getConsoleSender();
    }

    public static FileConfigurationOptions getFileConfigurationOptions() {
        return Tab.getInstance().getConfig().options();
    }

}

