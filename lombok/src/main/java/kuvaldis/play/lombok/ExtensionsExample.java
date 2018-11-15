package kuvaldis.play.lombok;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Extensions.class)
public class ExtensionsExample {

    public String test() {
        return "hELlO, WORlD!".toTitleCase();
    }
}
