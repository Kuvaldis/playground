package kuvaldis.play.java9;

import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PubSubTest {

    @Test
    public void testPubSub() throws Exception {
        final SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        final CountDownLatch latch = new CountDownLatch(100);
        final MySubscriber<String> subscriber = new MySubscriber<>(latch);
        publisher.subscribe(subscriber);
        IntStream.rangeClosed(1, 100).boxed()
                .map(Object::toString)
                .forEach(publisher::submit);
        publisher.close();
        latch.await();
    }
}
