package cc.funkemunky.kit.commands.kit.args;

import cc.funkemunky.api.commands.FunkeArgument;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.kit.Kit;
import cc.funkemunky.kit.utils.LocationUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnArgs extends FunkeArgument {

    public SetSpawnArgs() {
        super("setspawn", "setspawn [area, point]", "set the spawn area or location.", "kit.command.setspawn");

        addTabComplete(2, "area");
        addTabComplete(2, "point");
        addTabComplete(3, "one,area,2");
        addTabComplete(3, "two,area,2");
    }

    @Override
    public void onArgument(CommandSender sender, Command cmd, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length >= 2) {
                switch (args[1].toLowerCase()) {
                    case "area": {
                        if (args.length >= 3) {
                            if (args[2].equalsIgnoreCase("one")) {
                                LocationUtil.getCube().setOne(player.getLocation());
                                Kit.getInstance().getConfig().set("location.one", LocationUtil.locationToString(player.getLocation()));
                                Kit.getInstance().saveConfig();

                                sender.sendMessage(Color.Green + "Successfully set point one of the spawn area.");
                            } else if (args[2].equalsIgnoreCase("two")) {
                                LocationUtil.getCube().setTwo(player.getLocation());
                                Kit.getInstance().getConfig().set("location.two", LocationUtil.locationToString(player.getLocation()));
                                Kit.getInstance().saveConfig();
                                sender.sendMessage(Color.Green + "Successfully set point two of the spawn area.");
                            } else {
                                sender.sendMessage(Color.Red + "Invalid arguments. Please check the help page.");
                            }
                        }
                        break;
                    }
                    case "point": {
                        if(LocationUtil.getCube().getOne() != null || LocationUtil.getCube().getTwo() != null) {
                            if(LocationUtil.isWithinSpawn(player)) {
                                LocationUtil.setSpawnLocation(player.getLocation());
                                Kit.getInstance().getConfig().set("location.point", LocationUtil.locationToString(player.getLocation()));
                                Kit.getInstance().saveConfig();

                                sender.sendMessage(Color.Green + "Successfully set the spawn point to your location.");
                            } else {
                                sender.sendMessage(Color.Red + "The spawn point must be within the spawn borders.");
                            }
                        } else {
                            sender.sendMessage(Color.Red + "The spawn borders must be set before you can set a spawn-point.");
                        }
                        break;
                    }
                    default: {
                        sender.sendMessage(Color.Red + "Invalid arguments. Please check the help page.");
                        break;
                    }
                }
                return;
            }
            sender.sendMessage(Color.Red + "Invalid arguments. Please check the help page.");
            return;
        }
        sender.sendMessage(Color.Red + "You must be a player to use this feature.");
    }
}
