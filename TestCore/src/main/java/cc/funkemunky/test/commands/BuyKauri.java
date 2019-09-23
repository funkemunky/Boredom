package cc.funkemunky.test.commands;

import cc.funkemunky.api.commands.ancmd.Command;
import cc.funkemunky.api.commands.ancmd.CommandAdapter;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;

@Init(commands = true)
public class BuyKauri {

    @Command(name = "buykauri", description = "You wanna buy Kauri? Do it!", aliases = {"buy", "makemepoor"})
    public void onCommand(CommandAdapter cmd) {
        cmd.getSender().sendMessage(Color.translate("&7You can buy &6Kauri Anticheat &7on &fhttps://funkemunky.cc/shop&7!"));
    }
}
