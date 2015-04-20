package kuvaldis.play.picocontainer;

import org.slf4j.Logger;

public class Apple2 implements Peelable {

    private final Logger logger;

    public Apple2(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void peel() {
        logger.info("bla");
    }
}
