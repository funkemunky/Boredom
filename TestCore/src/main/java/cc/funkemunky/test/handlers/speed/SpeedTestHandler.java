package cc.funkemunky.test.handlers.speed;

import cc.funkemunky.api.utils.Color;
import cc.funkemunky.api.utils.Init;
import cc.funkemunky.api.utils.Instance;
import cc.funkemunky.test.utils.SignHandler;
import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;

import java.util.*;

@Init(commands = true)
public class SpeedTestHandler implements Listener {

    @Instance
    public static SpeedTestHandler INSTANCE;

    private static Map<UUID, SpeedTest> speedTestMap = new LinkedHashMap<>();

    private static double setRatio = 6.2;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.getBlockPlaced() instanceof Sign) {
            Sign sign = (Sign) event.getBlockPlaced();

            String[] lines = sign.getLines();

            Location startLoc, finishLocation;

            for (int i = 0; i < lines.length; i++) {
                String line = lines[0];

                if(line == null) continue;

                switch(i) {
                    case 0: {
                        if(!line.equals("[SpeedTest]")) return;
                        break;
                    }
                    case 1: {
                        try {
                            String[] splitLocs = line.split(",");

                            if(splitLocs.length == 4) {
                                startLoc = new Location(event.getPlayer().getWorld(),
                                        Double.parseDouble(splitLocs[0]), Double.parseDouble(splitLocs[1]),
                                        Double.parseDouble(splitLocs[2]), Float.parseFloat(splitLocs[3]), 0);

                            } else {
                                event.setCancelled(true);
                                event.getPlayer().sendMessage("Incorrect amount of splits: " + splitLocs.length);
                                return;
                            }
                        } catch(Exception e) {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(String.format("Error with \"%s\" on line 1", line));
                            return;
                        }
                    }
                    case 2: {
                        try {
                            String[] splitLocs = line.split(",");

                            if(splitLocs.length == 2) {
                               finishLocation = new Location(event.getPlayer().getWorld(),
                                        Double.parseDouble(splitLocs[0]), 0, Double.parseDouble(splitLocs[1]));
                            } else {
                                event.setCancelled(true);
                                event.getPlayer().sendMessage("Incorrect amount of splits: " + splitLocs.length);
                                return;
                            }
                        } catch(Exception e) {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(String.format("Error with \"%s\" on line 2", line));
                            return;
                        }
                    }
                }
            }

            sign.setLine(0, Color.Red + "[SpeedTest]");

            String[] fakeLines = Arrays.asList(sign.getLines()).toArray(new String[0]);

            fakeLines[1] = Color.Green + "Successfully created";
            fakeLines[2] = Color.Green + "Speed test!";
            fakeLines[3] = "";

            SignHandler.INSTANCE.showLines(event.getPlayer(), sign, fakeLines, 40L, true);
        }
    }
}
