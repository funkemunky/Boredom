package cc.funkemunky.kit.commands;

import cc.funkemunky.api.commands.FunkeCommand;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.MiscUtils;
import cc.funkemunky.kit.Kit;
import cc.funkemunky.kit.data.UserData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand extends FunkeCommand {
    public StatsCommand() {
        super(Kit.getInstance(), "stats", "stats", "View your statistics.", "kit.command.stats", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(Kit.getInstance().getDataManager().hasUserData(player.getUniqueId())) {
                UserData data = Kit.getInstance().getDataManager().getUserData(player.getUniqueId());

                sender.sendMessage(MiscUtils.line(Color.Dark_Gray));
                sender.sendMessage(Color.translate("&eKills: &f" + data.getKills()));
                sender.sendMessage(Color.translate("&eDeaths: &f" + data.getDeaths()));
                sender.sendMessage(Color.translate("&eBalance: &f" + data.getBalance()));
                sender.sendMessage(MiscUtils.line(Color.Dark_Gray));
            }
            return true;
        }
        sender.sendMessage(Color.Red + "You must be a player to use this feature.");
        return true;
    }

    @Override
    protected void addArguments() {

    }
}
