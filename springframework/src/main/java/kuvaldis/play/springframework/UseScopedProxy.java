package kuvaldis.play.springframework;

import java.util.concurrent.atomic.AtomicInteger;

public class UseScopedProxy {

    public static class First {

        private Second second;

        public Second getSecond() {
            return second;
        }

        public void setSecond(final Second second) {
            this.second = second;
        }
    }

    public static class Second {

        private static final AtomicInteger COUNTER = new AtomicInteger();

        public Second() {
            COUNTER.incrementAndGet();
        }

        public int getValue() {
            return COUNTER.get();
        }
    }
}
