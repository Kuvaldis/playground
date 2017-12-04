package kuvaldis.play.java9;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class MySubscriber<T> implements Subscriber<T> {

    private final CountDownLatch latch;

    private volatile Subscription subscription;

    public MySubscriber(final CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onSubscribe(final Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1L);
    }

    @Override
    public void onNext(final T item) {
        System.out.println("Received: " + item);
        latch.countDown();
        subscription.request(1L);
    }

    @Override
    public void onError(final Throwable throwable) {
        throwable.printStackTrace();
        for (long i = 0; i < latch.getCount(); i++) {
            latch.countDown();
        }
    }

    @Override
    public void onComplete() {
        System.out.println("Done");
        for (long i = 0; i < latch.getCount(); i++) {
            latch.countDown();
        }
    }
}
