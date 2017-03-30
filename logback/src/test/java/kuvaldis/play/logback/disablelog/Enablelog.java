package kuvaldis.play.logback.disablelog;

import org.slf4j.LoggerFactory;

public class Enablelog {

    public void logMessage(final String message) {
        LoggerFactory.getLogger(Enablelog.class).info(message);
    }
}
