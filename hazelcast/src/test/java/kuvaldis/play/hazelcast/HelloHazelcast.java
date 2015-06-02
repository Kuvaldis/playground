package kuvaldis.play.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.*;
import com.hazelcast.query.EntryObject;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.SqlPredicate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HelloHazelcast {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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
        assertEquals(2, LongStringMapStore.storage.size());
        assertEquals(asList("loadAllKeys", "load", "load"), LongStringMapStore.events.subList(0, 3));
        final List<String> tail = LongStringMapStore.events.subList(3, LongStringMapStore.events.size());
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

    @Test
    public void testBoundedQueue() throws Exception {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Queue is full!");
        final HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        final IQueue<String> queue = instance.getQueue("bounded-queue");
        queue.add("1");
        queue.add("2");
        queue.add("3");
        queue.add("4");
    }

    @Test
    public void testTopic() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        final ITopic<String> topic = instance.getTopic("topic");
        final int[] checker = new int[1];
        topic.addMessageListener(event -> {
            if ("sample".equals(event.getMessageObject())) {
                checker[0] = 1;
                latch.countDown();
            }
        });
        topic.publish("sample");
        latch.await(1l, TimeUnit.SECONDS);
        assertEquals(1, checker[0]);
    }

    @Test
    public void testLock() throws Exception {
        final boolean[] canConsume = new boolean[1];
        final boolean[] consumed = new boolean[1];
        final boolean[] produced = new boolean[1];
        final CountDownLatch latch = new CountDownLatch(2);
        final HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        final ILock lock = instance.getLock("lock");
        final ICondition condition = lock.newCondition("condition");
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            lock.lock();
            try {
                while (!canConsume[0]) {
                    condition.await();
                }
                consumed[0] = true;
                latch.countDown();
                condition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        executorService.submit(() -> {
            lock.lock();
            try {
                while (canConsume[0]) {
                    condition.await();
                }
                produced[0] = true;
                canConsume[0] = true;
                latch.countDown();
                condition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        latch.await(1l, TimeUnit.SECONDS);
        assertTrue(consumed[0]);
        assertTrue(produced[0]);
    }

    @Test
    public void testMigrationEvent() throws Exception {
        final boolean[] checker = new boolean[1];
        final CountDownLatch latch = new CountDownLatch(1);
        final HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        instance.getPartitionService().addMigrationListener(new MigrationListener() {
            @Override
            public void migrationStarted(MigrationEvent migrationEvent) {
                checker[0] = true;
                latch.countDown();
            }

            @Override
            public void migrationCompleted(MigrationEvent migrationEvent) {

            }

            @Override
            public void migrationFailed(MigrationEvent migrationEvent) {

            }
        });
        final ISet<String> set = instance.getSet("set");
        set.add("1");
        set.add("2");
        Hazelcast.newHazelcastInstance();
        latch.await(1l, TimeUnit.SECONDS);
        assertTrue(checker[0]);
    }

    @Test
    public void testQuery() throws Exception {
        final HazelcastInstance instance = Hazelcast.newHazelcastInstance();
        final IMap<Long, Person> people = instance.getMap("people");
        people.addIndex("active", false);
        people.addIndex("age", true);
        people.put(1l, new Person(1l, "Paul", 30, false));
        people.put(2l, new Person(2l, "Maria", 15, true));
        people.put(3l, new Person(3l, "Rob", 43, true));

        final EntryObject e = new PredicateBuilder().getEntryObject();
        final Predicate builtPredicate = e.is("active").and(e.get("age").lessThan(20));
        final Collection<Person> predicateFoundPeople = people.values(builtPredicate);
        assertEquals(1, predicateFoundPeople.size());
        assertEquals("Maria", predicateFoundPeople.iterator().next().getName());

        final Predicate sqlPredicate = new SqlPredicate("active AND age > 40");
        final Collection<Person> sqlFoundPeople = people.values(sqlPredicate);
        assertEquals(1, sqlFoundPeople.size());
        assertEquals("Rob", sqlFoundPeople.iterator().next().getName());
    }
}
