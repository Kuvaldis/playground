package kuvaldis.play.java;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Deadlock {
    public static void main(String[] args) throws InterruptedException {
        final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        final Thread thread = new Thread() {
            @Override
            public void run() {
                lock.writeLock().lock();
            }
        };
        thread.start();
        thread.join();
        System.out.println("Now it will never acquire");
        lock.readLock().lock();
    }
}
