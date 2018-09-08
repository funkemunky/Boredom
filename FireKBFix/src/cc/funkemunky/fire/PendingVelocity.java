package cc.funkemunky.fire;

import org.bukkit.entity.Player;

public class PendingVelocity {
    private Player pending, attacker;
    private long timeStamp;
    public PendingVelocity(Player pending, Player attacker) {
        this.pending = pending;
        this.attacker = attacker;
        timeStamp = System.currentTimeMillis();
    }

    public Player getPending() {
        return pending;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Player getAttacker() {
        return attacker;
    }
}
