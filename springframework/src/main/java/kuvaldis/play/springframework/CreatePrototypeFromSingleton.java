package kuvaldis.play.springframework;

import java.util.concurrent.atomic.AtomicInteger;

public class CreatePrototypeFromSingleton {

    public static abstract class CommandManager {

        public String process(final String input) {
            final Command command = createCommand();
            command.setInput(input);
            return command.execute();
        }

        protected abstract Command createCommand();
    }

    public static class Command {

        private static final AtomicInteger COUNTER = new AtomicInteger();

        private String input;

        public Command() {
            COUNTER.incrementAndGet();
        }

        public void setInput(final String input) {
            this.input = input;
        }

        public String execute() {
            return "Executed by (" + COUNTER.get() + "): " + input;
        }
    }
}
