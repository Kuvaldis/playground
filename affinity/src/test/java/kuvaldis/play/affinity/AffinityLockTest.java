package kuvaldis.play.affinity;

import net.openhft.affinity.AffinityLock;
import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinitySupport;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * The purpose of using these locks is to stick some thread which can become sleeping or whatever to some cpu or core.
 * It provides you faster thread state restoring (after sleep for example) because some data is cached in cpu or core.
 * You can also create child locks to provide different cpu binding behavior (for child threads for instance).
 */
public class AffinityLockTest {

    @Test
    public void testCpuNoAffinity() throws Exception {
        System.out.println(AffinitySupport.getCpu()); // prints some number from 0 to 7 (whatever)
        Thread.sleep(100);
        System.out.println(AffinitySupport.getCpu()); // can be different number
    }

    @Test
    public void testCpuAffinity() throws Exception {
        try (final AffinityLock affinityLock = AffinityLock.acquireLock()) {
            int beforeSleepCpu = AffinitySupport.getCpu();
            Thread.sleep(100);
            assertEquals(beforeSleepCpu, AffinitySupport.getCpu());
        }
    }

    @Test
    public void testFixedAffinity() throws Exception {
        // it's 8 possible logical cores on my current machine
        // mark 7 and 6 as general use cpu. others will be available for affinity locks
        AffinitySupport.setAffinity(1l << 7 | 1l << 6);
        // doesn't work. can't figure why
//        AffinitySupport.setAffinity(1l << (AffinityLock.cpuLayout().cpus() - 2) |
//                1l << (AffinityLock.cpuLayout().cpus() - 1));
        try (final AffinityLock affinityLock = AffinityLock.acquireLock()) {
            assertEquals(5, AffinitySupport.getCpu());
//            assertEquals(AffinityLock.cpuLayout().cpus() - 3, AffinitySupport.getCpu());
        }
    }

    @Test
    public void testSame() throws Exception {
        try (final AffinityLock al = AffinityLock.acquireLock()) {
            final int core = AffinityLock.cpuLayout().coreId(al.cpuId());
            final int[] core2 = new int[1];
            Thread t = new Thread(() -> {
                try (AffinityLock al2 = al.acquireLock(AffinityStrategies.SAME_CORE)) {
                    core2[0] = AffinityLock.cpuLayout().coreId(al2.cpuId());
                    System.out.println(core2[0]);
                }
            });
            t.start();
            t.join();
            assertEquals(core, core2[0]);
            t = new Thread(() -> {
                try (AffinityLock al2 = al.acquireLock(AffinityStrategies.DIFFERENT_CORE)) {
                    core2[0] = AffinityLock.cpuLayout().coreId(al2.cpuId());
                    System.out.println(core2[0]);
                }
            });
            t.start();
            t.join();
            assertNotEquals(core, core2[0]);
        }
    }
}
