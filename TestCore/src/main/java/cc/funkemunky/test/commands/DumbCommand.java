package cc.funkemunky.test.commands;

import cc.funkemunky.api.commands.ancmd.Command;
import cc.funkemunky.api.commands.ancmd.CommandAdapter;
import cc.funkemunky.api.utils.RunUtils;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

//@Init(commands = true)
public class DumbCommand {

    @Command(name = "seteverything", playerOnly = true, permission = "dont.run")
    public void onCommand(CommandAdapter cmd) {
        RunUtils.task(() -> {
            cmd.getPlayer().sendMessage("Setting blocks...");
            val loc = cmd.getPlayer().getLocation();
            int minX = Math.min(loc.getBlockX() + 25, loc.getBlockX());
            int minZ = Math.min(loc.getBlockZ(), loc.getBlockZ() + 25);
            int maxX = Math.max(loc.getBlockX() + 25, loc.getBlockX());
            int maxZ = Math.max(loc.getBlockZ() + 20, loc.getBlockZ());
            int y = loc.getBlockY();

            List<Integer> ids = new ArrayList<>();

            for(int id = 0 ; id < 500 ; id++) {
                if(Material.getMaterial(id) == null || !Material.getMaterial(id).isSolid()) continue;

                ids.add(id);
            }

            cmd.getPlayer().sendMessage("ids=");
            int currentId = 0;

            for(int x = minX ; x < maxX ; x++) {
                for(int z = minZ ; z < maxZ ; z++) {
                    if(currentId++ > ids.size()) break;

                    Block block = new Location(cmd.getPlayer().getWorld(), x, y, z).getBlock();

                    Material material = Material.getMaterial(currentId);

                    if(material.equals(Material.WATER_LILY)) {
                        new Location(cmd.getPlayer().getWorld(), x, y-1, z).getBlock().setType(Material.WATER);
                    }
                    block.setType(Material.getMaterial(currentId));
                }
            }
            cmd.getPlayer().sendMessage("Done!");
        });
    }
}
