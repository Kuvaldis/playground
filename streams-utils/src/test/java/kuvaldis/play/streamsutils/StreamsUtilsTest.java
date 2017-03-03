package kuvaldis.play.streamsutils;

import org.junit.Test;
import org.paumard.streams.StreamsUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class StreamsUtilsTest {

    @Test
    public void testCycling() throws Exception {
        // given
        final Stream<String> stringStream = Stream.of("a", "b", "c", "d");

        // when
        final Stream<String> cycle = StreamsUtils.cycle(stringStream);

        // then
        final List<String> resultList = cycle.limit(10)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("a", "b", "c", "d", "a", "b", "c", "d", "a", "b"), resultList);
    }

    @Test
    public void testRolling() throws Exception {
        // given
        final Stream<String> stringStream = Stream.of("a", "b", "c", "d");

        // when
        final Stream<Stream<String>> roll = StreamsUtils.roll(stringStream, 2);

        // then
        final List<List<String>> resultList = roll.map(stream -> stream.collect(Collectors.toList()))
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("b", "c"), Arrays.asList("c", "d")),
                resultList);
    }

    @Test
    public void testValidating() throws Exception {
        // given
        final Stream<String> stringStream = Stream.of("a", "bbbb", "b", "aa", "aaa");

        // when
        final Stream<Integer> validate = StreamsUtils.validate(stringStream,
                (str) -> str.startsWith("a"),
                String::length,
                (str) -> -1);

        // then
        final List<Integer> resultList = validate.collect(Collectors.toList());
        assertEquals(Arrays.asList(1, -1, -1, 2, 3), resultList);
    }

    @Test
    public void testTakeWhile() throws Exception {
        // given
        final Stream<String> stringStream = Stream.of("a", "b", "c", "d");

        // when
        final Stream<String> interrupt = StreamsUtils.interrupt(stringStream, (str) -> str.equals("c"));

        // then
        final List<String> resultList = interrupt.collect(Collectors.toList());
        assertEquals(Arrays.asList("a", "b"), resultList);
    }

    @Test
    public void testZip() throws Exception {
        // given
        final Stream<String> stream1 = Stream.of("a", "b", "c", "d");
        final Stream<String> stream2 = Stream.of("z", "y", "x", "w");

        // when
        final Stream<String> zip = StreamsUtils.zip(stream1, stream2, String::concat);

        // then
        final List<String> resultList = zip.collect(Collectors.toList());
        assertEquals(Arrays.asList("az", "by", "cx", "dw"), resultList);
    }

    @Test
    public void testWindow() throws Exception {
        // given
        final Stream<String> stringStream = Stream.of("a", "b", "c", "d", "e");

        // when
        final Stream<String> windowCollect = StreamsUtils.shiftingWindowCollect(stringStream, 3,
                Collector.of(StringBuilder::new,
                        StringBuilder::append,
                        (b1, b2) -> {
                            b1.append(b2.toString());
                            return b1;
                        }))
                .map(StringBuilder::toString);

        // then
        final List<String> resultList = windowCollect.collect(Collectors.toList());
        assertEquals(Arrays.asList("abc", "bcd", "cde"), resultList);
    }

    @Test
    public void testFilterMaxKeys() throws Exception {
        // given
        final Stream<Integer> intStream = Stream.of(1, 2, 5, 2, 3, 4, 4, 5, 1, 3, 2, 4, 1, 5, 5);

        // when
        final Stream<Integer> maxKeys = StreamsUtils.filteringMaxKeys(intStream, 2);

        // then
        final List<Integer> resultList = maxKeys.collect(Collectors.toList());
        assertEquals(Arrays.asList(5, 4), resultList);
    }
}
