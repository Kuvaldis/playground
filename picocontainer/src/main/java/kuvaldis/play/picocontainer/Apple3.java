package kuvaldis.play.picocontainer;

import org.slf4j.Logger;

public class Apple3 implements Peelable {

    private final Logger logger;

    public Apple3(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void peel() {
        logger.info("bla");
    }
}
