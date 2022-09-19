package cc.funkemunky.test.user;

import cc.funkemunky.api.utils.RunUtils;
import cc.funkemunky.api.utils.Setting;
import cc.funkemunky.test.TestCore;
import cc.funkemunky.test.utils.MinecraftTime;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Settings {
    private static LoadingCache<UUID, Document> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(20, TimeUnit.SECONDS).build(new CacheLoader<UUID, Document>() {
                @Override
                public Document load(@NotNull UUID uuid) {
                    Document set = TestCore.INSTANCE.database.getSettings()
                            .find(Filters.eq("uuid", uuid.toString())).first();

                    if (set != null) {
                        return set;
                    } else {
                        set = new Document("uuid", uuid.toString());

                        set.put("cancelling", true);
                        set.put("kicking", true);
                        set.put("noDamage", true);
                        set.put("noHunger", true);
                        set.put("timeOfDay", MinecraftTime.DAY.toString());

                        Document finalSet = set;
                        RunUtils.taskAsync(() -> TestCore.INSTANCE.database.getSettings().insertOne(finalSet));
                        return set;
                    }
                }
            });

    public static Setting<Boolean> allowKauriCancel = new Setting<>("kauriCancel",
            (pl) -> getStructureSet(pl.getUniqueId()).getBoolean("cancelling"), (pl, enabled) -> {
        Document set = getStructureSet(pl.getUniqueId());

        TestCore.INSTANCE.database.getSettings()
                .updateOne(Filters.eq("uuid", pl.getUniqueId().toString()),
                        Updates.set("cancelling", enabled));
        cache.refresh(pl.getUniqueId());
    }, true, false);
    public static Setting<Boolean> allowKauriKicking = new Setting<>("kauriKicking",
            (pl) -> getStructureSet(pl.getUniqueId()).getBoolean("kicking"), (pl, enabled) -> {
        Document set = getStructureSet(pl.getUniqueId());

        TestCore.INSTANCE.database.getSettings()
                .updateOne(Filters.eq("uuid", pl.getUniqueId().toString()),
                        Updates.set("kicking", enabled));
        cache.refresh(pl.getUniqueId());
    }, true, false);
    public static Setting<Boolean> noDamage = new Setting<>("noDamage",
            (pl) -> getStructureSet(pl.getUniqueId()).getBoolean("noDamage"), (pl, enabled) -> {
        Document set = getStructureSet(pl.getUniqueId());

        TestCore.INSTANCE.database.getSettings()
                .updateOne(Filters.eq("uuid", pl.getUniqueId().toString()),
                        Updates.set("noDamage", enabled));
        cache.refresh(pl.getUniqueId());
    }, false, true);
    public static Setting<Boolean> noHunger = new Setting<>("noHunger",
            (pl) -> getStructureSet(pl.getUniqueId()).getBoolean("noHunger"), (pl, enabled) -> {
        Document set = getStructureSet(pl.getUniqueId());

        TestCore.INSTANCE.database.getSettings()
                .updateOne(Filters.eq("noHunger", pl.getUniqueId().toString()),
                        Updates.set("cancelling", enabled));
        cache.refresh(pl.getUniqueId());
    }, true, false);
    public static Setting<MinecraftTime> timeOfDay = new Setting<>("timeOfDay",
            (pl) -> {
                Object object = getStructureSet(pl.getUniqueId()).get("timeOfDay");

                if (object instanceof MinecraftTime) {
                    return (MinecraftTime) object;
                } else if (object instanceof String) {
                    return MinecraftTime.valueOf((String) object);
                } else {
                    System.out.println("Time was stored in some unknown objects. Oops");
                    return MinecraftTime.DAY;
                }
            }, (pl, dayTime) -> {
        Document set = getStructureSet(pl.getUniqueId());

        TestCore.INSTANCE.database.getSettings()
                .updateOne(Filters.eq("uuid", pl.getUniqueId().toString()),
                        Updates.set("timeOfDay", dayTime.toString()));
        cache.refresh(pl.getUniqueId());
    }, MinecraftTime.values());

    public static Document getStructureSet(UUID uuid) {
        return cache.getUnchecked(uuid);
    }
}
