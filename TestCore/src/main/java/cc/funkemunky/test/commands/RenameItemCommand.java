package cc.funkemunky.test.commands;

import cc.funkemunky.api.commands.ancmd.Command;
import cc.funkemunky.api.commands.ancmd.CommandAdapter;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.msg.ChatBuilder;
import lombok.val;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Init(commands = true)
public class RenameItemCommand {

    @Command(name = "renameitem", description = "Rename the item in your hand", aliases = {"rename", "ri"},
            permission = "testcore.renameitem", playerOnly = true)
    public void onCommand(CommandAdapter cmd) {
        if(cmd.getPlayer().getItemInHand() != null && !cmd.getPlayer().getItemInHand().getType().equals(Material.AIR)) {
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

    @Command(name = "setlore", description = "Set the lore of an item in hand",
            permission = "testcore.setlore", playerOnly = true)
    public void onSetLore(CommandAdapter cmd) {
        if(cmd.getPlayer().getItemInHand() != null && !cmd.getPlayer().getItemInHand().getType().equals(Material.AIR)) {
            if(cmd.getArgs().length > 1) {
                StringBuilder lineBuilder = new StringBuilder();
                for(int i = 1 ; i < cmd.getArgs().length ; i++) {
                    lineBuilder.append(cmd.getArgs()[i]).append(" ");
                }

                int lineNumber = 0;

                try {
                    lineNumber = Integer.parseInt(cmd.getArgs()[0]);
                } catch(NumberFormatException e) {
                    cmd.getPlayer().sendMessage(ChatBuilder
                            .create("The argument \"%s\" is not a number!", cmd.getArgs()[0])
                            .color(Color.Red).build());
                    return;
                }

                if(lineNumber < 0) {
                    cmd.getPlayer().sendMessage(Color.Red + "You can't go less than 0!");
                    return;
                }

                String line = Color.translate(lineBuilder.toString().trim());
                ItemMeta meta = cmd.getPlayer().getItemInHand().getItemMeta();

                List<String> lore = meta.getLore() != null ? new ArrayList<>(meta.getLore()) : new ArrayList<>();

                if(lore.size() < lineNumber) {
                    cmd.getPlayer().sendMessage(ChatBuilder
                            .create().color(Color.Red)
                            .text("You must complete line %s before you can go higher.", lore.size()).build());
                    return;
                }

                if(lore.size() > lineNumber)
                    lore.set(lineNumber, line);
                else lore.add(line);

                meta.setLore(lore);
                cmd.getPlayer().getItemInHand().setItemMeta(meta);

                cmd.getPlayer().sendMessage(TextComponent.fromLegacyText(Color.translate(String
                        .format("&7Edited line &f%s &7with &r%s", lineNumber, line))));
            } else if(cmd.getArgs().length > 0) {
                int lineNumber = 0;

                try {
                    lineNumber = Integer.parseInt(cmd.getArgs()[0]);
                } catch (NumberFormatException e) {
                    cmd.getPlayer().sendMessage(ChatBuilder
                            .create("The argument \"%s\" is not a number!", cmd.getArgs()[0])
                            .color(Color.Red).build());
                    return;
                }

                if (lineNumber < 0) {
                    cmd.getPlayer().sendMessage(Color.Red + "You can't go less than 0!");
                    return;
                }

                String line = "";
                ItemMeta meta = cmd.getPlayer().getItemInHand().getItemMeta();
                List<String> lore = meta.getLore() != null ? new ArrayList<>(meta.getLore()) : new ArrayList<>();

                if (lore.size() < lineNumber) {
                    cmd.getPlayer().sendMessage(ChatBuilder
                            .create().color(Color.Red)
                            .text("You must complete line %s before you can go higher.", lore.size()).build());
                    return;
                }

                if (lore.size() > lineNumber)
                    lore.set(lineNumber, line);
                else lore.add(line);

                meta.setLore(lore);
                cmd.getPlayer().getItemInHand().setItemMeta(meta);

                cmd.getPlayer().sendMessage(ChatBuilder.create().color(Color.Gray).text("Removed line ").reset()
                        .color(Color.White).text("" + lineNumber).build());
            }
        } else cmd.getPlayer().sendMessage(ChatBuilder
                .create("You must be holding an item in your hand to rename an item.")
                .color(Color.Red).build());
    }
}
