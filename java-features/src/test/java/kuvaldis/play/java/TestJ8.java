package kuvaldis.play.java;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TestJ8 {

    public final Runnable hwr = () -> System.out.println("Hello World!");

    public void printHelloWorld() {
        hwr.run();
    }

    public void runRunnable(Runnable r) {
        r.run();
    }

    public void checkSupplier(Supplier<String> s) {
        System.out.println(s.get());
    }

    public <T> void checkFunction(T o, Function<T, String> function) {
        System.out.println(function.apply(o));
    }

    public static void main(String[] args) {
        TestJ8 testJ8 = new TestJ8();
        testJ8.hwr.run();                                  // Hello World!
        testJ8.runRunnable(testJ8::printHelloWorld);       // Hello World!
        testJ8.checkSupplier("123"::toString);             // 123
        testJ8.checkFunction("ddd", String::toUpperCase);  // DDD
        String[] strings = {"a", "c", "b"};
        // sort with method reference
        Arrays.sort(strings, String::compareToIgnoreCase);
        testJ8.checkFunction(strings, Arrays::toString);   // [a, b, c]
        strings = new String[]{"a", "c", "b"};
        // the same
        Arrays.sort(strings, (o1, o2) -> o1.compareToIgnoreCase(o2));
        testJ8.checkFunction(strings, Arrays::toString);   // [a, b, c]
        Integer[] arrays = {2, 1, 2};
        Arrays.sort(arrays, (o1, o2) -> o1 - o2);
        testJ8.checkFunction(arrays, Arrays::toString);    // 1, 2, 2
        // default interface method
        Foo foo = new Foo();
        foo.foo();                                         // I'm a default function in an interface
        // bulk data operations
        List<Foo> fooList = Arrays.asList(new Foo("aaa"), new Foo("vvv"), new Foo("Fooler"));
        System.out.println(fooList.stream().filter(p ->
                (p.s.contains("Foo"))).collect(Collectors.toList())); // [Fooler]
        // date and time api
        LocalDate date = LocalDate.now();
        System.out.println(date);                                   // something like 2014-03-19
    }

    private interface IfcWithDefault {
        default void foo() {
            System.out.println("I'm a default function in an interface");
        }
    }

    private static class Foo implements IfcWithDefault {
        private String s;

        private Foo() {
        }

        private Foo(String s) {
            this.s = s;
        }

        private String getS() {
            return s;
        }

        @Override
        public String toString() {
            return s;
        }
    }
}