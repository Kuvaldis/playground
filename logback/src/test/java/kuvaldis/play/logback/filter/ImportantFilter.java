package kuvaldis.play.logback.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class ImportantFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(final ILoggingEvent event) {
        if (event.getMessage().contains("Important")) {
            return FilterReply.ACCEPT;
        } else {
            return FilterReply.DENY;
        }
    }
}
