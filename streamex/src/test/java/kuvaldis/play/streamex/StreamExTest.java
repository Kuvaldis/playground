package kuvaldis.play.streamex;

import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntPredicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StreamExTest {

    @Test
    public void testGetEmptyStream() throws Exception {
        // when
        final StreamEx<String> empty = StreamEx.empty();

        // then
        assertTrue(empty.toList().isEmpty());
    }

    @Test
    public void testOfNullable() throws Exception {
        // when
        final StreamEx<String> empty = StreamEx.ofNullable(null);

        // then
        assertTrue(empty.toList().isEmpty());
    }

    @Test
    public void testCartesianProduct() throws Exception {
        // given
        final List<List<String>> list = new ArrayList<>();
        final List<String> sublist1 = new ArrayList<>();
        sublist1.add("a");
        sublist1.add("b");
        list.add(sublist1);
        final List<String> sublist2 = new ArrayList<>();
        sublist2.add("1");
        sublist2.add("2");
        list.add(sublist2);

        // when
        final StreamEx<List<String>> result = StreamEx.cartesianProduct(list);
        final List<List<String>> product = result.toList();

        // then
        assertEquals(Arrays.asList("a", "1"), product.get(0));
        assertEquals(Arrays.asList("a", "2"), product.get(1));
        assertEquals(Arrays.asList("b", "1"), product.get(2));
        assertEquals(Arrays.asList("b", "2"), product.get(3));
    }

    @Test
    public void testFilterNull() throws Exception {
        // given
        final List<String> list = Arrays.asList("1", "2", null, "3", null);

        // when
        final List<String> listWithoutNulls = StreamEx.of(list).nonNull().toList();

        // then
        assertEquals(Arrays.asList("1", "2", "3"), listWithoutNulls);
    }

    @Test
    public void testTakeWhile() throws Exception {
        // given
        final IntStreamEx streamEx = IntStreamEx.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

        // when
        final IntStreamEx result = streamEx.takeWhile(value -> value <= 5);

        // then
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), result.boxed().toList());
    }
}
