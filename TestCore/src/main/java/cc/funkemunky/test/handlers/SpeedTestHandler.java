package cc.funkemunky.test.handlers;

import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.Instance;
import cc.funkemunky.api.utils.MathUtils;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Init(commands = true)
public class SpeedTestHandler implements Listener {

    @Instance
    public static SpeedTestHandler INSTANCE;

    private Map<UUID, TestResult> testResultsMap = new HashMap<>();
    private static double setRatio = 6.66;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && !testResultsMap.containsKey(event.getPlayer().getUniqueId())
                && event.getClickedBlock() != null
                && event.getClickedBlock().getType().equals(Material.WALL_SIGN)) {
            Sign sign = (Sign) event.getClickedBlock().getState();

            if(sign.getLines().length >= 2 && sign.getLine(0).equals(Color.Red + "[SpeedTest]")) {
                String[] locString = sign.getLine(1).split(",");
                if(locString.length >= 3) {
                    Location loc = new Location(sign.getWorld(),  Double.parseDouble(locString[0]),
                            Double.parseDouble(locString[1]),  Double.parseDouble(locString[2]),
                            locString.length > 3 ? Float.parseFloat(locString[3]) : 0, 0);

                    TestResult result = new TestResult(event.getPlayer().getUniqueId(), loc);
                    result.previousLoc = event.getPlayer().getLocation().clone();
                    testResultsMap.put(event.getPlayer().getUniqueId(), result);
                    setPlayerStart(event.getPlayer(), loc,false);
                    TaskHandler.INSTANCE.addTask(62, ticks -> {
                        if(ticks % 20 == 0) {
                            event.getPlayer().sendMessage(Color.translate("&7There are currently &f"
                                    + ticks / 20 + " seconds &7left until start."));
                        }
                    }, () -> {
                        setPlayerStart(event.getPlayer(), loc, true);
                        result.started = true;
                        event.getPlayer().sendMessage(Color.Green + "Go!");
                        event.getPlayer().sendMessage(Color.Gray + Color.Italics + "You can sneak to quit.");
                        result.startTime = System.currentTimeMillis();
                    });

                } else {
                    event.getPlayer().sendMessage(Color.Red
                            + "There was an error teleporting you to the set location.");
                }
            }
        } else if(event.getAction().equals(Action.PHYSICAL)
                && event.getClickedBlock().getType() == Material.STONE_PLATE
                && testResultsMap.containsKey(event.getPlayer().getUniqueId())) {
            TestResult result = testResultsMap.get(event.getPlayer().getUniqueId());

            result.endTime = System.currentTimeMillis();

            long delta = result.endTime - result.startTime;

            double distance = event.getPlayer().getLocation().toVector().setY(0)
                    .distance(result.startLoc.toVector().setY(0));
            event.getPlayer().teleport(result.previousLoc);
            double blocksPerSecond = distance / (delta / 1000D);
            double vanillaRatio = blocksPerSecond / setRatio;
            double pct = vanillaRatio * 100;

            event.getPlayer().sendMessage(Color.translate("&7Completed speed test in &f"
                    + MathUtils.round(delta / 1000D, 1) + " seconds &7moving &f"
                    + MathUtils.round(distance, 1) + " blocks &7&o(&f&o%v blocks per seconds&7&o)."
                    .replace("%v", String.valueOf(MathUtils
                            .round(distance / (delta / 1000D), 2)))));
            double pctDelta = pct - 100;
            event.getPlayer().sendMessage(Color.translate("&7Your speed is " + (Math.abs(pctDelta) < 0.8
                    ? "the same as vanilla" : (pctDelta < 0 ? "slower than vanilla" : "faster than vanilla" + " &7(&f"
                    + MathUtils.round(pctDelta, 1) + "%&7)"))));
            testResultsMap.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if(event.isSneaking() && testResultsMap.containsKey(event.getPlayer().getUniqueId())) {
            TestResult result = testResultsMap.get(event.getPlayer().getUniqueId());

            if(result.started) {
                event.getPlayer().sendMessage(Color.Red + "Sucessfully quit.");
                event.getPlayer().teleport(result.previousLoc);
                testResultsMap.remove(event.getPlayer().getUniqueId());
            }
        }
    }
    //0.0067607482769938
    //0.0067067067067067

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(testResultsMap.containsKey(event.getPlayer().getUniqueId())) {
            TestResult result = testResultsMap.get(event.getPlayer().getUniqueId());

            if(!result.started) event.setCancelled(true);
            else result.distance+= event.getTo().toVector().setY(0).distance(event.getFrom().toVector().setY(0));
        }
    }

    @EventHandler
    public void onSign(SignChangeEvent event) {
        val lines = event.getLines();

        if(lines.length >= 2 && lines[0].equalsIgnoreCase("[SpeedTest]") && lines[1].split(",").length >= 3) {
            event.setLine(0, Color.Red + "[SpeedTest]");
            event.getPlayer().sendMessage(Color.Red + "Successfully created speed test sign!");
        }
    }

    private void setPlayerStart(Player player, Location location, boolean reset) {
        player.teleport(location);
        player.setWalkSpeed(reset ? 0.2f : 0);
        player.setHealth(20);
        player.setFoodLevel(20);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }
}
