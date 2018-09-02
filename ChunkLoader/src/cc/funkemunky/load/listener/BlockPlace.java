package cc.funkemunky.load.listener;

import cc.funkemunky.load.ChunkLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class BlockPlace implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        if(e.getItemInHand().isSimilar(ChunkLoader.getInstance().chunkLoaderItem)) {
            ChunkLoader.getInstance().addChunkLoader(e.getBlockPlaced().getLocation());
            e.getBlockPlaced().setType(Material.AIR);
        }
    }

    @EventHandler
    public void chunkLoadEvent(ChunkUnloadEvent event) {
        if(ChunkLoader.getInstance().getChunkLoaders().stream().anyMatch(loc -> loc.getChunk() == event.getChunk())) {
            event.setCancelled(true);
            event.getChunk().load();
            Bukkit.broadcastMessage("chunk unload cancelled");
        }
    }
}
