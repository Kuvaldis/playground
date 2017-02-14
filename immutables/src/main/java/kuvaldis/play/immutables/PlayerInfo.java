package kuvaldis.play.immutables;

import org.immutables.value.Value;

@Value.Immutable
public abstract class PlayerInfo {

    @Value.Parameter
    public abstract long id();

    @Value.Default
    public String getName() {
        return "Anonymous_" + id();
    }

    @Value.Default
    public int gamesPlayed() {
        return 0;
    }
}
