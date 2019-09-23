package cc.funkemunky.test.commands;

import cc.funkemunky.api.commands.ancmd.Command;
import cc.funkemunky.api.commands.ancmd.CommandAdapter;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@Init(commands = true)
public class VelocityCommand {

    @Command(name = "velocity", description = "Apply velocity to yourself.", usage = "/<command> <x> <z>", permission = "test.velocity", playerOnly = true)
    public void onCommand(CommandAdapter cmd) {
        if(cmd.getArgs().length > 1) {
            try {
                double xz = Double.parseDouble(cmd.getArgs()[0]), y = Double.parseDouble(cmd.getArgs()[1]);

                Vector direction = cmd.getPlayer().getEyeLocation().getDirection();

                direction.setY(0);
                direction.multiply(xz);
                direction.setY(y);

                cmd.getPlayer().setVelocity(direction);

                cmd.getPlayer().sendMessage(Color.Green + "Applied velocity: (" + direction.lengthSquared() + ", " + y + ") input=(" + xz + ", " + y + ")");
            } catch(NumberFormatException e) {
                cmd.getPlayer().sendMessage(Color.Red + "Ensure your input is in the form of a number.");
            }
        } else cmd.getPlayer().sendMessage(Color.Red + "Invalid arguments! Usage: /" + cmd.getLabel() + " <x> <z>");
    }
}
