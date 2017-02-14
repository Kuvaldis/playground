package kuvaldis.play.immutables;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(stagedBuilder = true)
public interface Staged {
    String name();
    int age();
    boolean isEmployed();
}
