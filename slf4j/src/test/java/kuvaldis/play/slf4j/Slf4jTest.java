package kuvaldis.play.slf4j;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class Slf4jTest {

    @Test
    public void helloWorld() throws Exception {
        final Logger logger = LoggerFactory.getLogger(Slf4jTest.class);
        logger.info("Hello World");
    }

    @Test
    public void testDebug() throws Exception {
        final Logger logger = LoggerFactory.getLogger(Slf4jTest.class);
        logger.debug("Hello World"); // will not print anything since debug is not enabled
    }

    @Test
    public void testMdc() throws Exception {
        MDC.put("name", "Futhlebucher");
        final Logger logger = LoggerFactory.getLogger(Slf4jTest.class);
        logger.info("Hello World"); // name would be print as a param of logging template, for instance when slf4j is bind to logback
    }
}
