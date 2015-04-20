package kuvaldis.play.picocontainer;

import org.picocontainer.PicoContainer;
import org.picocontainer.injectors.FactoryInjector;
import org.slf4j.Logger;

import java.lang.reflect.Type;

public class FixedInstanceLogFactoryInjector extends FactoryInjector<Logger> {

    private final Logger logger;

    public FixedInstanceLogFactoryInjector(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Logger getComponentInstance(PicoContainer container, Type into) {
        return logger;
    }
}
