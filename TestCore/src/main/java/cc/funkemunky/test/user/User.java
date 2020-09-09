package cc.funkemunky.test.user;

import cc.funkemunky.api.utils.math.RollingAverageDouble;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class User {
    private static Map<UUID, User> users = new HashMap<>();
    public final UUID uuid;
    public Map<String, Float> violations = new HashMap<>();
    public long lastClick;
    public RollingAverageDouble cpsAvg = new RollingAverageDouble(10, 0);

    public static User getUser(UUID uuid) {
        return users.computeIfAbsent(uuid, key -> {
           User user = new User(uuid);
           users.put(key, user);
           return user;
        });
    }
}
