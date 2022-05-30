package cc.funkemunky.test.user;

import cc.funkemunky.api.utils.TickTimer;
import cc.funkemunky.api.utils.math.RollingAverageDouble;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class User {
    private static Map<UUID, User> users = new HashMap<>();
    public final UUID uuid;
    public Map<String, Float> violations = new HashMap<>();
    public long lastClick, inventoryStart;
    public RollingAverageDouble cpsAvg = new RollingAverageDouble(10, 0);
    private TickTimer lastDbCheck = new TickTimer(40);
    public ItemStack[] previousInventoryContents;
    private Player player;

    public static User getUser(UUID uuid) {
        return users.computeIfAbsent(uuid, key -> new User(uuid));
    }

    public boolean isAllowingCancel() {
        return Settings.allowKauriCancel.getValue(getPlayer());
    }

    public boolean isNoDamage() {
        return Settings.noDamage.getValue(getPlayer());
    }

    public boolean isNoHunger() {
        return Settings.noHunger.getValue(getPlayer());
    }

    public boolean isAllowingKick() {
        return Settings.allowKauriKicking.getValue(getPlayer());
    }

    public Player getPlayer() {
        this.player = Bukkit.getPlayer(uuid);

        return player;
    }
}
