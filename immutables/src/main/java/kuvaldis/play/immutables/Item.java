package kuvaldis.play.immutables;

import org.immutables.value.Value;

@Value.Immutable
public interface Item {
    @Value.Parameter
    String name();
    @Value.Parameter
    int count();
}
