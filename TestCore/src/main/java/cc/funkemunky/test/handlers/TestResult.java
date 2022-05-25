package cc.funkemunky.test.handlers;

import cc.funkemunky.api.utils.math.SimpleAverage;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.UUID;

@RequiredArgsConstructor
public class TestResult {
    public final UUID uuid;
    public final Location startLoc;
    public Location previousLoc;
    public boolean started;
    public long startTime, endTime, moveTime;
    public double distance;
    public SimpleAverage average = new SimpleAverage(50, 0);
}
