package kuvaldis.play.http.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class UriProcessorMap {
    private UriProcessorMap() {}

    public static final Map<String, Class<? extends UriProcessor>> PROCESSOR_MAP = createMap();

    private static  Map<String, Class<? extends UriProcessor>> createMap() {
        final Map<String, Class<? extends UriProcessor>> map = new HashMap<>();
        map.put("/", RootProcessor.class);
        return map;
    }

    public static Optional<UriProcessor> getProcessor(final String uri) {
        try {
            return Optional.of(PROCESSOR_MAP.getOrDefault(uri, NotFoundProcessor.class).newInstance());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
