package cc.funkemunky.Meme;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import anticheat.FionaAPI;
import anticheat.detections.Checks;
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
		table.put(0, 3, Color.Yellow + Color.Bold + "Player Information");
		table.put(0, 4, Color.Gray + "Ping: " + Color.White + FionaAPI.getAPI().getPing(player));
		table.put(0, 5, Color.Gray + "Flight: " + Color.White + player.getAllowFlight());
		table.put(0, 6, Color.Gray + "Op: " + Color.White + player.isOp());
		table.put(1, 2, Color.Gold + Color.Bold + "Fiona Test Server");
		table.put(1, 4, Color.Yellow + Color.Bold + "Your Violations");
		int i = 5;
		if (FionaAPI.getAPI().getViolations(player).size() > 0) {
			for (Checks check : FionaAPI.getAPI().getViolations(player).keySet()) {
				table.put(1, i, Color.White + "- " + Color.Gray + check.getName() + Color.Red + " (" + FionaAPI.getAPI().getViolations(player).get(check) + " VL)");
			    i++;
			} 
		} else {
			table.put(1, 5, Color.Green + Color.Italics + "No violations recorded.");
		}
		table.put(2, 3, Color.Yellow + Color.Bold + "Fiona Information");
		table.put(2, 4, Color.Gray + "Fiona Version: " + Color.White + FionaAPI.getAPI().getVersion());
		table.put(2, 5, Color.Gray + "Alerts: " + (FionaAPI.getAPI().hasAlerts(player) ? Color.Green + FionaAPI.getAPI().hasAlerts(player) : Color.Red + FionaAPI.getAPI().hasAlerts(player)));
		table.put(2, 6, Color.Gray + "TPS: " + Color.White + format.format(FionaAPI.getAPI().getTPS()));
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

