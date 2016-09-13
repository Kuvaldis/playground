package kuvaldis.play.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParametrizedTest {

    @Parameters(name = "{index}: {0} * 5 == {1}")
    public static Collection<Object[]> data() {
        return IntStream.rangeClosed(1, 10)
                .boxed()
                .map(i -> new Object[] {i, i * 5})
                .collect(Collectors.toList());
    }

    @Parameter
    public int first;

    @Parameter(value = 1)
    public int second;

    @Test
    public void testParametrized() throws Exception {
        assertEquals(first * 5, second);
    }
}
