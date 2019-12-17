package cc.funkemunky.test.commands;

import cc.funkemunky.api.commands.ancmd.Command;
import cc.funkemunky.api.commands.ancmd.CommandAdapter;
import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.test.TestCore;
import me.tigerhix.lib.scoreboard.type.Scoreboard;

@Init(commands = true)
public class ToggleScoreboard {

    @Command(name = "togglescoreboard", description = "Toggle the scoreboard on or off.",
            playerOnly = true, aliases = {"ts", "t.scoreboard", "tscoreboard", "t.s"})
    public void onCommand(CommandAdapter cmd) {
        if(TestCore.INSTANCE.scoreboardMap.containsKey(cmd.getPlayer().getUniqueId())) {
            Scoreboard scoreboard = TestCore.INSTANCE.scoreboardMap.get(cmd.getPlayer().getUniqueId());
            scoreboard.deactivate();
            TestCore.INSTANCE.scoreboardMap.remove(cmd.getPlayer().getUniqueId());
            cmd.getSender().sendMessage(Color.translate("&7Toggled the scoreboard &coff&7."));
        } else {
            Scoreboard scoreboard = TestCore.INSTANCE.getScoreboard(cmd.getPlayer());
            scoreboard.activate();

            TestCore.INSTANCE.scoreboardMap.put(cmd.getPlayer().getUniqueId(), scoreboard);
            cmd.getSender().sendMessage(Color.translate("&7Toggled the scoreboard &con&7."));
        }
    }
}
