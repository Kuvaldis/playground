package kuvaldis.play.logback.definer;

import ch.qos.logback.core.PropertyDefinerBase;

import java.util.concurrent.atomic.AtomicReference;

public class LogLevelDefiner extends PropertyDefinerBase {

    public static final AtomicReference<String> LEVEL = new AtomicReference<>("INFO");

    @Override
    public String getPropertyValue() {
        return LEVEL.get();
    }
}
