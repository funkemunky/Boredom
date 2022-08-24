package cc.funkemunky.test.user;

import ga.strikepractice.fights.Fight;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.citizensnpcs.api.npc.NPC;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.entity.Player;

import java.util.*;

@RequiredArgsConstructor
public class WrappedFight {
    private final Fight fight;
    @Getter
    private final List<Opponent> opponents = new ArrayList<>();

    public WrappedFight(Fight fight, Player... opponents) {
        this.fight = fight;

        for (Player opponent : opponents) {
            this.opponents.add(new Opponent(opponent.getName(), opponent.getUniqueId()));
        }
    }

    public WrappedFight(Fight fight, NPC... opponents) {
        this.fight = fight;

        for (NPC opponent : opponents) {
            this.opponents.add(new Opponent(opponent.getName(), opponent.getUniqueId()));
        }
    }

    public long getDuration() {
        return fight.getDuration();
    }

    public String getFormattedDuration() {
        return DurationFormatUtils.formatDuration(getDuration(), "&fmm&7:&fss");
    }

    public String getKit() {
        return fight.getKit().getName();
    }

    public String getKitName() {
        return fight.getKit().getFancyName();
    }

    public long getStarted() {
        return fight.getStarted();
    }

    public String getArena() {
        return fight.getArena().getName();
    }

    public String getArenaName() {
        return fight.getArena().getDisplayName();
    }
}
