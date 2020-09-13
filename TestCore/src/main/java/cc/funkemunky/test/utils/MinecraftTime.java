package cc.funkemunky.test.utils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MinecraftTime {
    DAY(7000),
    MORNING(600),
    NIGHT(14000);

    public final long timeMillis;
}
