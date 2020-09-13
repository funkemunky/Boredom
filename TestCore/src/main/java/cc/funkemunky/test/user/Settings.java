package cc.funkemunky.test.user;

import cc.funkemunky.api.utils.Setting;
import cc.funkemunky.test.TestCore;
import cc.funkemunky.test.utils.MinecraftTime;
import dev.brighten.db.db.StructureSet;
import lombok.val;

import java.util.UUID;

public class Settings {
    public static Setting<Boolean> allowKauriCancel = new Setting<>("kauriCancel",
            (pl) -> getStructureSet(pl.getUniqueId()).getObject("cancelling"), (pl, enabled) -> {
        StructureSet set = getStructureSet(pl.getUniqueId());
        set.input("cancelling", enabled);
        set.save(TestCore.INSTANCE.database);
    }, true, false);
    public static Setting<Boolean> allowKauriKicking = new Setting<>("kauriKicking",
            (pl) -> getStructureSet(pl.getUniqueId()).getObject("kicking"), (pl, enabled) -> {
        StructureSet set = getStructureSet(pl.getUniqueId());
        set.input("kicking", enabled);
        set.save(TestCore.INSTANCE.database);
    }, true, false);
    public static Setting<Boolean> noDamage = new Setting<>("noDamage",
            (pl) -> getStructureSet(pl.getUniqueId()).getObject("noDamage"), (pl, enabled) -> {
        StructureSet set = getStructureSet(pl.getUniqueId());
        set.input("noDamage", enabled);
        set.save(TestCore.INSTANCE.database);
    }, true, false);
    public static Setting<Boolean> noHunger = new Setting<>("noHunger",
            (pl) -> getStructureSet(pl.getUniqueId()).getObject("noHunger"), (pl, enabled) -> {
        StructureSet set = getStructureSet(pl.getUniqueId());
        set.input("noHunger", enabled);
        set.save(TestCore.INSTANCE.database);
    }, true, false);
    public static Setting<MinecraftTime> timeOfDay = new Setting<>("timeOfDay",
            (pl) -> {
                Object object = getStructureSet(pl.getUniqueId()).getObject("timeOfDay");

                if (object instanceof MinecraftTime) {
                    return (MinecraftTime) object;
                } else if (object instanceof String) {
                    return MinecraftTime.valueOf((String) object);
                } else {
                    System.out.println("Time was stored in some unknown objects. Oops");
                    return MinecraftTime.DAY;
                }
            }, (pl, dayTime) -> {
        StructureSet set = getStructureSet(pl.getUniqueId());
        set.input("timeOfDay", dayTime);

        pl.setPlayerTime(dayTime.timeMillis, false);
        set.save(TestCore.INSTANCE.database);
    }, MinecraftTime.values());

    public static StructureSet getStructureSet(UUID uuid) {
        val results = TestCore.INSTANCE.database.get(uuid.toString());

        if (results.size() > 0) {
            StructureSet set = results.get(0);

            if (!set.contains("noDamage")) {
                set.input("noDamage", true);
            }
            if (!set.contains("noHunger")) {
                set.input("noHunger", true);
            }
            if (!set.contains("timeOfDay")) {
                set.input("timeOfDay", MinecraftTime.DAY);
            }

            return set;
        } else {
            StructureSet set = TestCore.INSTANCE.database.create(uuid.toString());

            set.input("cancelling", true);
            set.input("kicking", true);
            set.input("noDamage", true);
            set.input("noHunger", true);
            set.input("timeOfDay", MinecraftTime.DAY);

            set.save(TestCore.INSTANCE.database);
            return set;
        }
    }
}
