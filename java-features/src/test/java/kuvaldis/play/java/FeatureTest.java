package kuvaldis.play.java;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class FeatureTest {

    @Test
    public void testHashMapMutableEqualsHashcode() throws Exception {
        //noinspection EqualsWhichDoesntCheckParameterClass
        final class IntegerPair {
            private int forEquals;
            private int forHashCode;

            public IntegerPair(int forEquals, int forHashCode) {
                this.forEquals = forEquals;
                this.forHashCode = forHashCode;
            }

            @Override
            public boolean equals(Object obj) {
                return ((IntegerPair) obj).forEquals == forEquals;
            }

            @Override
            public int hashCode() {
                return forHashCode;
            }
        }

        final IntegerPair pair = new IntegerPair(1, 2);
        final HashMap<IntegerPair, String> hashMap = new HashMap<>();
        hashMap.put(pair, "Me Willy");
        assertEquals("Me Willy", hashMap.get(pair));
        pair.forEquals = 3;
        // even if equals will return false it's not called since hash code is the same
        assertEquals("Me Willy", hashMap.get(pair));
        pair.forEquals = 1;
        pair.forHashCode = 3;
        // no key with hash code 3, so can't find the value
        assertEquals(null, hashMap.get(pair));
    }

    @Test
    public void testWaitNotify() throws Exception {
        final boolean[] canConsume = new boolean[2];
        final Object lock = new Object();

        final Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    while (!canConsume[0]) {
                        System.out.println("before wait first");
                        lock.wait();
                        System.out.println("after wait first");
                    }
                    System.out.println("can consume first");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        final Thread t2 = new Thread(() -> {
            synchronized (lock) {
                try {
                    while (!canConsume[1]) {
                        System.out.println("before wait second");
                        lock.wait();
                        System.out.println("after wait second");
                    }
                    System.out.println("can consume second");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();

        new Thread() {
            @Override
            public void run() {
                synchronized (lock) {
                    canConsume[0] = true;
                    lock.notifyAll();
                    System.out.println("first released");
                    canConsume[1] = true;
                    lock.notifyAll();
                    System.out.println("second released");
                }
            }
        }.start();

        t1.join();
        t2.join();
    }
}
