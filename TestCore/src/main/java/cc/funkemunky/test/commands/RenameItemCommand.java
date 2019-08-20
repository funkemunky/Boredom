package cc.funkemunky.test.commands;

import cc.funkemunky.api.commands.ancmd.Command;
import cc.funkemunky.api.commands.ancmd.CommandAdapter;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;
import lombok.val;
import org.bukkit.Material;

@Init(commands = true)
public class RenameItemCommand {

    @Command(name = "renameitem", description = "Rename the item in your hand", aliases = {"rename", "ri"}, permission = "testcore.renameitem", playerOnly = true)
    public void onCommand(CommandAdapter cmd) {
        if(cmd.getPlayer().getItemInHand() != null && !cmd.getPlayer().getItemInHand().equals(Material.AIR)) {
            if(cmd.getArgs().length > 0) {
                StringBuilder nameBuilder = new StringBuilder();
                for(int i = 0 ; i < cmd.getArgs().length ; i++) {
                    nameBuilder.append(cmd.getArgs()[i]).append(" ");
                }

                String name = Color.translate(nameBuilder.toString().trim());

                val itemMeta = cmd.getPlayer().getItemInHand().getItemMeta();

                itemMeta.setDisplayName(name);
                cmd.getPlayer().getItemInHand().setItemMeta(itemMeta);
                cmd.getPlayer().sendMessage(Color.Gray + "Renamed the item in your hand to \"" + name + Color.Gray + "\".");
            } else cmd.getPlayer().sendMessage(Color.Red + "Please provide a name.");
        } else cmd.getPlayer().sendMessage(Color.Red + "You must be holding an item in your hand to rename an item.");
    }
}
