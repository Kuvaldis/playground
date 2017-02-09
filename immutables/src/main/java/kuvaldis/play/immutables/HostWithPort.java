package kuvaldis.play.immutables;

import org.immutables.value.Value;

@Value.Immutable
public interface HostWithPort {
    @Value.Parameter
    String hostname();
    @Value.Parameter
    int port();
}
