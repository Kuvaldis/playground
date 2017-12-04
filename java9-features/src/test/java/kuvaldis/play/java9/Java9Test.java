package kuvaldis.play.java9;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class Java9Test {

    @Test
    public void testHelloWorld() throws Exception {
        System.out.println("Hello, world!");
    }

    @Test
    public void testImmutableList() throws Exception {
        final List<Integer> integers = List.of(1, 2, 3);
        System.out.println(integers);
        System.out.println(integers.getClass());
    }

    interface A {
        private String get() {
            return "bla";
        }

        default String getString() {
            return get();
        }
    }

    @Test
    public void testPrivateInterfaceMethod() throws Exception {
        A a = new A() {
        };
        assertEquals("bla", a.getString());
    }

    @Test
    public void testStreamApi() throws Exception {
        final List<String> list = List.of("1", "2", "3");
        final List<Integer> result = list.stream()
                .map(Integer::parseInt)
                .takeWhile(i -> i < 3)
                .collect(Collectors.toList());
        assertEquals(2, result.size());
        assertEquals(List.of(1, 2), result);
    }

    @Test
    public void testOptional() throws Exception {
        final Optional<Integer> optional = Optional.of(1);
        final List<String> result = optional.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        assertEquals(List.of("1"), result);
    }

    @Test
    public void testTryWithResources() throws Exception {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("123.txt")));
        try (reader) {
            System.out.println(reader.readLine());
        }
    }

    static class Box<T> {
        T content;

        public Box(final T content) {
            this.content = content;
        }
    }

    @Test
    public void testDiamond() throws Exception {
        final Box<String> hi = createBox("hi");
        final String content = hi.content;
        assertEquals("hi", content);
    }

    private <T> Box<T> createBox(final T content) {
        return new Box<>(content) {
        };
    }

    @Test
    public void testStreamOptional() throws Exception {
        Stream<Optional<String>> maybeStrings = Stream.of(Optional.of("1"), Optional.empty(), Optional.of("2"));
        final List<Integer> ints = maybeStrings.flatMap(Optional::stream)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        assertEquals(List.of(1, 2), ints);
    }

    @Test
    public void testStackWalker() throws Exception {
        a();
    }

    private void a() {
        b();
    }

    private void b() {
        c();
    }

    private void c() {
        StackWalker.getInstance()
                .forEach(System.out::println);
        System.out.println();
        StackWalker.getInstance(Set.of(StackWalker.Option.SHOW_HIDDEN_FRAMES), 1)
                .forEach(System.out::println);
        System.out.println();
        StackWalker.getInstance()
                .walk(sfs -> sfs.filter(sf -> sf.getMethodName().equals("a")).findFirst())
                .ifPresent(System.out::println);
    }

    @Test
    public void testMapEntry() throws Exception {
        final Map.Entry<Integer, String> entry = Map.entry(1, "2");
        assertEquals(1, entry.getKey().intValue());
        assertEquals("2", entry.getValue());

        final Map<Integer, String> map = Map.ofEntries(Map.entry(1, "3"),
                Map.entry(2, "red"));
        System.out.println(map);
        System.out.println(map.getClass());
        assertEquals(Map.of(1, "3", 2, "red"), map);
    }

    @Test
    public void testSafeVararg() throws Exception {
        goodVarargsMethod(List.of("1"), List.of("red", "rabbit"));
    }

    @SafeVarargs // no warnings in this case. use it, when you are sure everything is safe in your method.
    private void goodVarargsMethod(final List<String>... ls) {
        for (List<String> l : ls) {
            System.out.println(l);
        }
    }

}
