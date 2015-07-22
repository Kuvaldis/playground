package kuvaldis.play.http.mapper;

import kuvaldis.play.http.mapper.processor.NotFoundProcessor;
import kuvaldis.play.http.mapper.processor.RootProcessor;
import kuvaldis.play.http.mapper.processor.UriProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class UriProcessorMap {
    private UriProcessorMap() {}

    public static final Map<String, UriProcessorCreator> PROCESSOR_MAP = createMap();

    private interface UriProcessorCreator {
        UriProcessor create();
    }

    private static  Map<String, UriProcessorCreator> createMap() {
        final Map<String, UriProcessorCreator> map = new HashMap<>();
        map.put("/", RootProcessor::new);
        map.put("/home", RootProcessor::new);
        return map;
    }

    public static Optional<UriProcessor> getProcessor(final String uri) {
        try {
            return Optional.of(PROCESSOR_MAP.getOrDefault(uri, NotFoundProcessor::new).create());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
