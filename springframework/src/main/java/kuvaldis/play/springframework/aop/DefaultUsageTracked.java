package kuvaldis.play.springframework.aop;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultUsageTracked implements UsageTracked {

    private AtomicInteger count = new AtomicInteger();

    @Override
    public void incrementUseCount() {
        System.out.println("increment and get");
        count.incrementAndGet();
    }

    @Override
    public int getCount() {
        return count.get();
    }
}
