package kuvaldis.play.ideaplugin;

import com.intellij.openapi.components.PersistentStateComponent;
import org.jetbrains.annotations.Nullable;

public class TestService implements PersistentStateComponent<TestService.State> {
    class State {
        public long value;
    }

    private State state;

    @Nullable
    @Override
    public State getState() {
        return state;
    }

    @Override
    public void loadState(final State state) {
        this.state = state;
    }
}
