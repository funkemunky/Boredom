package cc.funkemunky.test.commands;

//@Init(commands = true)
public class ToggleScoreboard {

   /* @Command(name = "togglescoreboard", description = "Toggle the scoreboard on or off.", playerOnly = true, aliases = {"ts", "t.scoreboard", "tscoreboard", "t.s"})
    public void onCommand(CommandAdapter cmd) {
        if(TestCore.INSTANCE.scoreboardMap.containsKey(cmd.getPlayer())) {
            Scoreboard scoreboard = TestCore.INSTANCE.scoreboardMap.get(cmd.getPlayer());
            scoreboard.deactivate();
            TestCore.INSTANCE.scoreboardMap.remove(cmd.getPlayer());
            cmd.getSender().sendMessage(Color.translate("&7Toggled the scoreboard &coff&7."));
        } else {
            Scoreboard scoreboard = TestCore.INSTANCE.getScoreboard(cmd.getPlayer());
            scoreboard.activate();

            TestCore.INSTANCE.scoreboardMap.put(cmd.getPlayer(), scoreboard);
            cmd.getSender().sendMessage(Color.translate("&7Toggled the scoreboard &con&7."));
        }
    }*/
}
