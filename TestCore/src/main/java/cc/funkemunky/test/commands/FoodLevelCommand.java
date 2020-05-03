package cc.funkemunky.test.commands;

import cc.funkemunky.api.commands.ancmd.Command;
import cc.funkemunky.api.commands.ancmd.CommandAdapter;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;
import org.bukkit.entity.Player;

@Init(commands = true)
public class FoodLevelCommand {

    @Command(name = "setfoodlevel", usage = "/<command> <level>", playerOnly = true,
            description = "Set the food level of yourself", permission = "test.foodlevel")
    public void onCommand(CommandAdapter cmd) {
        Player target = cmd.getPlayer();

        if(cmd.getArgs().length > 0) {
            try {
                target.setFoodLevel(Integer.parseInt(cmd.getArgs()[0]));
                target.sendMessage(Color.Green + "Set food level to " + cmd.getArgs()[0]);
            } catch(NumberFormatException e) {
                target.sendMessage(Color.Red + "The argument provided is not an integer.");
            }
        } else cmd.getSender().sendMessage(Color.Red + "Invalid arguments.");
    }
}
