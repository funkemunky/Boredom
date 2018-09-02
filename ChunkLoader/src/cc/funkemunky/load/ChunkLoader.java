package cc.funkemunky.load;

import cc.funkemunky.load.cmd.ChunkLoaderCommand;
import cc.funkemunky.load.listener.BlockPlace;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.CitizensPlugin;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.trait.Gravity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ChunkLoader extends JavaPlugin {
    private List<Location> chunkLoaders;
    private List<Entity> entities;
    private static ChunkLoader instance;
    public ItemStack chunkLoaderItem;
    public void onEnable() {
        instance = this;
        chunkLoaders = new ArrayList<>();
        entities = new ArrayList<>();
        saveDefaultConfig();
        init();
        getCommand("chunkloader").setExecutor(new ChunkLoaderCommand());
        getServer().getPluginManager().registerEvents(new BlockPlace(), this);
    }

    public void onDisable() {
        CitizensAPI.getNPCRegistry().sorted().forEach(npc -> {
            if(chunkLoaders.contains(npc.getStoredLocation())) {
                npc.despawn();
                CitizensAPI.getNPCRegistry().deregister(npc);
            }
        });
    }

    private void init() {
        chunkLoaderItem = new ItemStack(Material.getMaterial(getConfig().getString("chunkLoader.id")), 1);
        ItemMeta meta = chunkLoaderItem.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString("chunkLoader.name")));

        chunkLoaderItem.setItemMeta(meta);
        if(getConfig().getList("blockLocations") != null) {
            for(Object object : getConfig().getList("blockLocations")) {
                Location location = (Location) object;

                chunkLoaders.add(location);

                NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, getConfig().getString("playerName"));

                npc.data().set("removefromplayerlist", false);
                npc.setFlyable(true);
                npc.setProtected(true);

                Gravity gravTrait = CitizensAPI.getTraitFactory().getTrait("gravity");
                gravTrait.gravitate(true);
                npc.addTrait(gravTrait);

                npc.data().setPersistent("removefromplayerlist", false);
                npc.setProtected(true);
                npc.spawn(location);

                if(!location.getChunk().isLoaded()) {
                    location.getChunk().load();
                }
            }
        }
    }

    public void addChunkLoader(Location location) {
        chunkLoaders.add(location);


        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, getConfig().getString("playerName"));

        npc.data().set("removefromplayerlist", false);
        npc.setFlyable(true);
        npc.setProtected(true);

        Gravity gravTrait = CitizensAPI.getTraitFactory().getTrait("gravity");
        gravTrait.gravitate(true);
        npc.addTrait(gravTrait);

        npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, "funkemunky");

        npc.data().setPersistent("removefromplayerlist", false);
        npc.setProtected(true);
        npc.spawn(location);

        if(!location.getChunk().isLoaded()) {
            location.getChunk().load();
        }

        getConfig().set("blockLocations", chunkLoaders);
        saveConfig();
    }
    public List<Location> getChunkLoaders() {
        return chunkLoaders;
    }
    public List<Entity> getEntities() {
        return entities;
    }

    public static ChunkLoader getInstance() {
        return instance;
    }
}
