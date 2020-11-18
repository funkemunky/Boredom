package cc.funkemunky.test.user;

import cc.funkemunky.api.utils.Setting;
import cc.funkemunky.api.utils.TickTimer;
import cc.funkemunky.api.utils.math.RollingAverageDouble;
import cc.funkemunky.api.utils.menu.preset.button.SettingButton;
import lombok.RequiredArgsConstructor;
import lombok.val;
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
    private boolean cancelling, allowingKick;
    private TickTimer lastDbCheck = new TickTimer(40);
    public ItemStack[] previousInventoryContents;

    public static User getUser(UUID uuid) {
        return users.computeIfAbsent(uuid, key -> {
           User user = new User(uuid);
           users.put(key, user);
           return user;
        });
    }

    public boolean isAllowingCancel() {
        if(lastDbCheck.hasPassed()) {
            Player player = Bukkit.getPlayer(uuid);

            if(player != null) {
                this.cancelling = Settings.allowKauriCancel.getValue(player);
                this.allowingKick = Settings.allowKauriKicking.getValue(player);
            }
            lastDbCheck.reset();
        }
        return this.cancelling;
    }

    public boolean isAllowingKick() {
        if(lastDbCheck.hasPassed()) {
            Player player = Bukkit.getPlayer(uuid);

            if(player != null) {
                this.cancelling = Settings.allowKauriCancel.getValue(player);
                this.allowingKick = Settings.allowKauriKicking.getValue(player);
            }
            lastDbCheck.reset();
        }
        return this.allowingKick;
    }
}
