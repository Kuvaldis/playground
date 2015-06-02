package kuvaldis.play.hazelcast;

import com.hazelcast.core.MapStore;

import java.util.*;
import java.util.stream.Collectors;

public class LongStringMapStore implements MapStore<Long, String> {

    public static final Map<Long, String> storage = new HashMap<>();
    public static final List<String> events = new ArrayList<>();

    @Override
    public void store(Long key, String value) {
        storage.put(key, value);
        events.add("store");
    }

    @Override
    public void storeAll(Map<Long, String> map) {
        storage.putAll(map);
        events.add("storeAll");
    }

    @Override
    public void delete(Long key) {
        storage.remove(key);
        events.add("delete");
    }

    @Override
    public void deleteAll(Collection<Long> keys) {
        keys.forEach(storage::remove);
        events.add("deleteAll");
    }

    @Override
    public String load(Long key) {
        events.add("load");
        return storage.get(key);
    }

    @Override
    public Map<Long, String> loadAll(Collection<Long> keys) {
        events.add("loadAll");
        return storage.entrySet().stream()
                .filter(keys::contains)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Set<Long> loadAllKeys() {
        events.add("loadAllKeys");
        return storage.keySet();
    }
}
