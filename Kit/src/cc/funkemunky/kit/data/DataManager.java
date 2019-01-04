package cc.funkemunky.kit.data;

import cc.funkemunky.kit.Kit;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DataManager {
    private List<UserData> dataUsers = Lists.newArrayList();

    public DataManager() {
        loadUsersFromConfig();
    }

    private void loadUsersFromConfig() {
        dataUsers.clear();

        if(Kit.getInstance().getConfig().isConfigurationSection("data")) {
            Set<String> keys = Kit.getInstance().getConfig().getConfigurationSection("data").getKeys(false);

            keys.forEach(key -> {
                UserData userData = new UserData(UUID.fromString(key));

                String path = "data." + key + ".";

                userData.setDeaths(Kit.getInstance().getConfig().getInt(path + "deaths"));
                userData.setKills(Kit.getInstance().getConfig().getInt(path + "kills"));
                userData.setBalance(Kit.getInstance().getConfig().getDouble(path + "balance"));

                dataUsers.add(userData);
            });
        }
    }

    public void createUserData(UUID uuid) {
        UserData data = new UserData(uuid);

        dataUsers.add(data);

        saveUserData(data);
    }

    public UserData getUserData(UUID uuid) {
        return dataUsers.stream().filter(data -> data.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public boolean hasUserData(UUID uuid) {
        return dataUsers.stream().anyMatch(data -> data.getUuid().equals(uuid));
    }

    public void saveUserData(UserData data) {
        String path = "data." + data.getUuid().toString() + ".";
        Kit.getInstance().getConfig().set(path + "deaths", data.getDeaths());
        Kit.getInstance().getConfig().set(path + "kills", data.getKills());
        Kit.getInstance().getConfig().set(path + "balance", data.getBalance());
        Kit.getInstance().saveConfig();
    }

    public void saveAllUsers() {
        dataUsers.forEach(this::saveUserData);
    }
}
