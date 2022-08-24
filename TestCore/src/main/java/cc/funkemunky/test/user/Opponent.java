package cc.funkemunky.test.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class Opponent {
    private final String name;
    private final UUID uuid;
}
