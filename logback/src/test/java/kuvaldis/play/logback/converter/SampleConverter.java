package kuvaldis.play.logback.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class SampleConverter extends ClassicConverter {

    @Override
    public String convert(final ILoggingEvent event) {
        return Long.toString(System.currentTimeMillis() - event.getTimeStamp());
    }
}
