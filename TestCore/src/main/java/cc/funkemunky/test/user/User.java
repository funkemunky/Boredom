package cc.funkemunky.test.user;

import javafx.collections.transformation.SortedList;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class User {
    private static Map<UUID, User> users = new HashMap<>();
    public final UUID uuid;
    public Map<String, Float> violations = new HashMap<>();

    public static User getUser(UUID uuid) {
        return users.computeIfAbsent(uuid, key -> {
           User user = new User(uuid);
           users.put(key, user);
           return user;
        });
    }
}
