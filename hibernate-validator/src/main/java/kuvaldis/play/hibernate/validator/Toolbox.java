package kuvaldis.play.hibernate.validator;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Toolbox {

    private final List<@NotNull String> tools;

    public Toolbox(final List<@NotNull String> tools) {
        this.tools = tools;
    }

    public List<String> getTools() {
        return tools;
    }
}
