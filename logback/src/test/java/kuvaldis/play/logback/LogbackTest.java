package kuvaldis.play.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import kuvaldis.play.logback.disablelog.Enablelog;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class LogbackTest {

    @Test
    public void helloWorld() {
        final Logger logger = LoggerFactory.getLogger(LogbackTest.class);
        logger.debug("Hello world."); // prints something like "16:50:33.424 [main] DEBUG kuvaldis.play.logback.LogbackTest - Hello world."
    }

    @Test
    public void printInternalState() throws Exception {
        final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
    }

    @Test
    public void testLoggerLevelHierarchy() throws Exception {
        // by default root logger has DEBUG level
        assertEquals(Level.DEBUG, ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).getLevel());
        final Logger packageLogger = LoggerFactory.getLogger("kuvaldis.play.logback");
        ((ch.qos.logback.classic.Logger) packageLogger).setLevel(Level.INFO);
        final Logger logger = LoggerFactory.getLogger(LogbackTest.class);
        logger.debug("bla"); // level is inherited from parent, which has INFO logger level, so nothing is print
    }

    @Test
    public void testAppenderInheritance() throws Exception {
        new Enablelog().logMessage("bla"); // bla will be print, see config file for details why it's imprtant
    }
}
