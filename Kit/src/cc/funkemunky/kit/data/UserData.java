package cc.funkemunky.kit.data;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserData {
    private UUID uuid;
    private int kills, deaths;
    private double balance;

    public UserData(UUID uuid) {
        this.uuid = uuid;
    }
}
