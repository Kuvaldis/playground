package kuvaldis.play.java;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class FeatureTest {

    @Test
    public void testHashMapMutableEqualsHashcode() throws Exception {
        //noinspection EqualsWhichDoesntCheckParameterClass
        final class IntegerPair {
            private int forEquals;
            private int forHashCode;

            public IntegerPair(int forEquals, int forHashCode) {
                this.forEquals = forEquals;
                this.forHashCode = forHashCode;
            }

            @Override
            public boolean equals(Object obj) {
                return ((IntegerPair) obj).forEquals == forEquals;
            }

            @Override
            public int hashCode() {
                return forHashCode;
            }
        }

        final IntegerPair pair = new IntegerPair(1, 2);
        final HashMap<IntegerPair, String> hashMap = new HashMap<>();
        hashMap.put(pair, "Me Willy");
        assertEquals("Me Willy", hashMap.get(pair));
        pair.forEquals = 3;
        // even if equals will return false it's not called since hash code is the same
        assertEquals("Me Willy", hashMap.get(pair));
        pair.forEquals = 1;
        pair.forHashCode = 3;
        // no key with hash code 3, so can't find the value
        assertEquals(null, hashMap.get(pair));
    }
}
