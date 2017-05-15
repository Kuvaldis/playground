package kuvaldis.play.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import kuvaldis.play.logback.definer.LogLevelDefiner;
import kuvaldis.play.logback.disablelog.Enablelog;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    @Test
    public void testDefiner() throws Exception {
        // getPropertyValue is called once, not on each log event or something. So it does not give ability to dynamically change this value
        LogLevelDefiner.LEVEL.set("DEBUG");
        final Logger logger = LoggerFactory.getLogger("kuvaldis.play.logback.definer");
        logger.debug("bla");
    }

    @Test
    public void testRollingFileAppender() throws Exception {
        final Logger logger = LoggerFactory.getLogger("RollingFileAppenderExample");
        // rolling happens every minute, so inside a folder there will be a file per minute
        logger.info("bla");
    }

    @Test
    public void testSiftingAppender() throws Exception {
        final Logger logger = LoggerFactory.getLogger("SiftingAppenderExample");
        logger.debug("Unknown user message"); // goes to the file with unknown user
        MDC.put("userId", "Alice");
        logger.debug("Alice's message");
        MDC.put("userId", "Bob");
        logger.debug("Bob's message");
    }

    @Test
    public void testConverter() throws Exception {
        final Logger logger = LoggerFactory.getLogger("SampleConverterExample");
        logger.debug("bla");
    }

    @Test
    public void testFilter() throws Exception {
        final Logger logger = LoggerFactory.getLogger("ImportantFilterExample");
        logger.debug("Important: bla");
        logger.debug("Forget about it"); // will be skipped
    }

    @Test
    public void testMDCAnotherThread() throws Exception {
        final Logger logger = LoggerFactory.getLogger("MDCDifferentThreadAppenderExample");
        MDC.put("client", "Vasiliy");
        final Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        final ExecutorService executorService = Executors.newCachedThreadPool();
        final Future<?> future1 = executorService.submit(() -> {
            MDC.setContextMap(copyOfContextMap);
            logger.debug("bla"); // will be 'Vasiliy - bla'
        });
        final Future<?> future2 = executorService.submit(() -> {
            logger.debug("bla"); // will be ' - bla'
        });
        future1.get();
        future2.get();
    }
}
