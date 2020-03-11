package cc.funkemunky.test.commands;

import cc.funkemunky.api.commands.ancmd.Command;
import cc.funkemunky.api.commands.ancmd.CommandAdapter;
import cc.funkemunky.api.utils.Init;

@Init(commands = true)
public class UnloadChunk {

    @Command(name = "unloadchunk", description = "Unload your chunk.", permission = "test.unload", playerOnly = true)
    public void onCommand(CommandAdapter cmd) {
        if(cmd.getPlayer().getLocation().getChunk().unload()) {
            cmd.getSender().sendMessage("Unloaded chunk.");
        } else cmd.getSender().sendMessage("Problem unloading chunk.");
    }
}
