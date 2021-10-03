package cc.funkemunky.test.handlers.speed;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Sign;

@Builder
@Getter
public class SpeedTest {
    private final Sign sign;
    private final Location startLoc, finishLocation, spawn;
    @Setter
    private Location endLoc;
    @Setter
    private long start, end;

    public double getBlocksPerSecond() {
        assert endLoc != null : "endloc field is null. Do not run method until it is set.";
        assert start != 0 : "The Speedtest appears to have never started.";
        assert end != 0 : "The Speedtest appears to have never ended.";

        return startLoc.distance(endLoc) / ((end - start) / 1000.);
    }

    public double getPercentOverVanilla() {
        return getBlocksPerSecond() / 6.2  * 100;
    }
}
