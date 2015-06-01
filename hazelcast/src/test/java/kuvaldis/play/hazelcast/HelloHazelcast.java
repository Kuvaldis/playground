package kuvaldis.play.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.*;
import com.hazelcast.spring.cache.HazelcastCache;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HelloHazelcast {

    @Test
    public void testSeveralInstances() throws Exception {

        final HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        final HazelcastInstance instance2 = Hazelcast.newHazelcastInstance();

        final Map<Long, String> map = instance.getMap("a");
        final IdGenerator generator = instance.getIdGenerator("gen");
        for (int i = 0; i < 3; i++) {
            map.put(generator.newId(), "data" + i);
        }

        final Map<Long, String> map2 = instance2.getMap("a");

        assertEquals(3, map2.size());
        assertEquals(asList("data0", "data1", "data2"), map2.values().stream().sorted().collect(Collectors.toList()));
    }

    @Test
    public void testItemListener() throws Exception {
        final CountDownLatch addedLatch = new CountDownLatch(3);
        final CountDownLatch removedLatch = new CountDownLatch(1);
        final Set<String> checkingSet = new HashSet<>();
        final ItemListener<String> listener = new ItemListener<String>() {
            @Override
            public void itemAdded(ItemEvent item) {
                checkingSet.add((String) item.getItem());
                addedLatch.countDown();
            }

            @Override
            public void itemRemoved(ItemEvent item) {
                //noinspection SuspiciousMethodCalls
                checkingSet.remove(item.getItem());
                removedLatch.countDown();
            }
        };
        final HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        final ISet<String> set = instance.getSet("set");
        set.addItemListener(listener, true);
        set.add("1");
        set.add("a");
        set.add("~");
        set.remove("a");

        addedLatch.await(1l, TimeUnit.SECONDS);
        removedLatch.await(1l, TimeUnit.SECONDS);

        assertEquals(2, checkingSet.size());
        assertTrue(checkingSet.contains("1"));
        assertTrue(checkingSet.contains("~"));
    }

    @Test
    public void testClient() throws Exception {
        final HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        final Set<String> set = instance.getSet("set");
        set.add("1");

        final HazelcastInstance client = HazelcastClient.newHazelcastClient(new ClientConfig());
        Set<String> clientSet = client.getSet("set");
        assertEquals(1, clientSet.size());
        assertTrue(clientSet.contains("1"));
    }

    @Test
    public void testMapStorage() throws Exception {
        final HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        final Map<Long, String> map = instance.getMap("storage-map");
        map.put(1l, "1");
        map.put(2l, "2");
        Thread.sleep(2000);
        assertEquals(2, PersonMapStore.storage.size());
        assertEquals(asList("loadAllKeys", "load", "load"), PersonMapStore.events.subList(0, 3));
        final List<String> tail = PersonMapStore.events.subList(3, PersonMapStore.events.size());
        assertTrue(tail.size() == 1 && "storeAll".equals(tail.get(0)) ||
                tail.size() == 2 && "store".equals(tail.get(0)) && "store".equals(tail.get(0)));
    }

    @Test
    public void testMapLocking() throws Exception {
        final HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        final IMap<String, Long> map = instance.getMap("map");
        final String key = "count";
        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        IntStream.range(0, 10).forEach((i) -> executorService.submit(() -> {
            map.lock(key);
            try {
                map.put(key, map.getOrDefault(key, 0l) + 1);
            } finally {
                map.unlock(key);
            }
        }));
        executorService.awaitTermination(2l, TimeUnit.SECONDS);
        assertEquals(10l, map.get(key).longValue());
    }
}
