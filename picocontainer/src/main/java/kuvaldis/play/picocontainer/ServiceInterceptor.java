package kuvaldis.play.picocontainer;

import org.picocontainer.behaviors.Intercepted;

public class ServiceInterceptor implements Service {

    private final Intercepted.Controller controller;

    public ServiceInterceptor(Intercepted.Controller controller) {
        this.controller = controller;
    }

    @Override
    public String call() {
        controller.override();
        return String.format("Intercepted: %s", controller.getOriginalRetVal());
    }
}
