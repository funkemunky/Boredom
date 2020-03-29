package cc.funkemunky.test.handlers;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.UUID;

@RequiredArgsConstructor
public class TestResult {
    public final UUID uuid;
    public final Location startLoc;
    public Location previousLoc;
    public boolean started;
    public long startTime, endTime;
    public double totalLength, distance;
}
