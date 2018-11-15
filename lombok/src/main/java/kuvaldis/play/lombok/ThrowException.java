package kuvaldis.play.lombok;

import lombok.SneakyThrows;

public class ThrowException {

    @SneakyThrows
    public void throwException() {
        throw new Exception();
    }
}
