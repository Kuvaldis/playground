package kuvaldis.play.javaslang;

import javaslang.*;
import javaslang.collection.List;
import javaslang.collection.Stream;
import javaslang.collection.Vector;
import javaslang.control.Option;
import javaslang.control.Try;
import org.junit.Test;

import java.io.IOException;
import java.util.Comparator;
import java.util.function.DoubleToIntFunction;
import java.util.function.Function;

import static javaslang.API.$;
import static javaslang.API.Case;
import static javaslang.API.Match;
import static javaslang.Predicates.instanceOf;
import static org.junit.Assert.*;

public class JavaslangTests {

    @Test
    public void testTuple() throws Exception {
        // when
        final Tuple2<String, Integer> tuple1 = Tuple.of("Java", 8);
        final Tuple2<String, Integer> tuple2 = tuple1.map(
                s -> s + "slang",
                i -> i / 4);

        // then
        assertEquals("Java", tuple1._1);
        assertEquals(8, tuple1._2.intValue());
        assertEquals("Javaslang", tuple2._1);
        assertEquals(2, tuple2._2.intValue());
    }

    @Test
    public void testFunction() throws Exception {
        // when
        final Function1<Integer, Integer> plusOne = a -> a + 1;
        final Function1<Integer, Integer> multiplyByTwo = a -> a * 2;
        final Function1<Integer, Integer> result = plusOne.andThen(multiplyByTwo);

        // then
        assertEquals(6, result.apply(2).intValue());
    }

    @Test
    public void testLifting() throws Exception {
        // when
        final Function2<Integer, Integer, Integer> divide = (a, b) -> a / b;
        final Function2<Integer, Integer, Option<Integer>> safeDivide = Function2.lift(divide);

        // since divide fails when 1 / 0, it returns None, so lifting returns None when operation has failed
        final Option<Integer> i1 = safeDivide.apply(1, 0);
        final Option<Integer> i2 = safeDivide.apply(4, 2);

        // then
        assertTrue(i1.isEmpty());
        assertTrue(i2.isDefined());
        assertEquals(2, i2.get().intValue());
    }

    @Test
    public void partialApplication() throws Exception {
        // when
        final Function3<Integer, Integer, Integer, Integer> sum = (a, b, c) -> a + b + c;
        final Function2<Integer, Integer, Integer> add2 = sum.apply(2);

        // then
        assertEquals(6, add2.apply(3).apply(1).intValue());
    }

    @Test
    public void currying() throws Exception {
        // when
        final Function3<Integer, Integer, Integer, Integer> sum = (a, b, c) -> a + b + c;
        // see the difference with partial application
        final Function1<Integer, Function1<Integer, Function1<Integer, Integer>>> curriedSum = sum.curried();

        // then
        assertEquals(6, curriedSum.apply(2).apply(3).apply(1).intValue());
    }

    @Test
    public void tryMonad() throws Exception {
        // when
        final String result = Try.<String>of(() -> {
            throw new RuntimeException();
        }).recover(x -> Match(x).of(
                Case(instanceOf(RuntimeException.class), () -> "Runtime Exception"),
                Case(instanceOf(IOException.class), () -> "IO Exception")
        )).getOrElse("bla");

        // then
        assertEquals("Runtime Exception", result);
    }

    @Test
    public void patternMatching() throws Exception {
        // given
        final int i = 3;

        // when
        final String result = Match(i).of(
                Case($(1), "one"),
                Case($(2), "two"),
                Case($(), "?")
        );

        // then
        assertEquals("?", result);
    }

    @Test
    public void list() throws Exception {
        // when
        final List<Integer> list = List.of(1, 2, 3);
        final Number sum = list.sum();
        // then
        assertEquals(6L, sum);
    }
}
