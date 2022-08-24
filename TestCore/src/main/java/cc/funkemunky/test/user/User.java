package cc.funkemunky.test.user;

import cc.funkemunky.api.utils.TickTimer;
import cc.funkemunky.api.utils.math.RollingAverageDouble;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@RequiredArgsConstructor
public class User {
    public static final Map<UUID, User> users = new HashMap<>();
    public final UUID uuid;
    public Map<String, Float> violations = new HashMap<>();
    public long lastClick, inventoryStart;
    public RollingAverageDouble cpsAvg = new RollingAverageDouble(4, 0);
    private boolean cancelling, allowingKick, noDamage, noHunger;
    private TickTimer lastDbCheck = new TickTimer(40);
    public ItemStack[] previousInventoryContents;
    @Getter
    @Setter
    private WrappedFight fight;

    public static User getUser(UUID uuid) {
        return users.computeIfAbsent(uuid, key -> new User(uuid));
    }

    public boolean isAllowingCancel() {
        if(lastDbCheck.hasPassed()) {
            Player player = Bukkit.getPlayer(uuid);

            if(player != null) {
                this.cancelling = Settings.allowKauriCancel.getValue(player);
                this.allowingKick = Settings.allowKauriKicking.getValue(player);
                this.noDamage = Settings.noDamage.getValue(player);
                this.noHunger = Settings.noHunger.getValue(player);
            }
            lastDbCheck.reset();
        }
        return this.cancelling;
    }

    public boolean isNoDamage() {
        if(lastDbCheck.hasPassed()) {
            Player player = Bukkit.getPlayer(uuid);

            if(player != null) {
                this.cancelling = Settings.allowKauriCancel.getValue(player);
                this.allowingKick = Settings.allowKauriKicking.getValue(player);
                this.noDamage = Settings.noDamage.getValue(player);
                this.noHunger = Settings.noHunger.getValue(player);
            }
            lastDbCheck.reset();
        }
        return this.noDamage;
    }

    public boolean isNoHunger() {
        if(lastDbCheck.hasPassed()) {
            Player player = Bukkit.getPlayer(uuid);

            if(player != null) {
                this.cancelling = Settings.allowKauriCancel.getValue(player);
                this.allowingKick = Settings.allowKauriKicking.getValue(player);
                this.noDamage = Settings.noDamage.getValue(player);
                this.noHunger = Settings.noHunger.getValue(player);
            }
            lastDbCheck.reset();
        }
        return this.noHunger;
    }

    public boolean isAllowingKick() {
        if(lastDbCheck.hasPassed()) {
            Player player = Bukkit.getPlayer(uuid);

            if(player != null) {
                this.cancelling = Settings.allowKauriCancel.getValue(player);
                this.allowingKick = Settings.allowKauriKicking.getValue(player);
                this.noDamage = Settings.noDamage.getValue(player);
                this.noHunger = Settings.noHunger.getValue(player);
            }
            lastDbCheck.reset();
        }
        return this.allowingKick;
    }
}
