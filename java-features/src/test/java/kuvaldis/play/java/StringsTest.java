package kuvaldis.play.java;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringsTest {

    @Test
    public void testStringsPool() throws Exception {
        assertSame("1", "1");
        assertNotSame(new String("1"), "1");
        assertSame(new String("1").intern(), "1");
        assertSame("1" + "1", "11");
        assertNotSame(new StringBuilder().append("1").append("1").toString(), "11");
        assertNotSame(new StringBuffer().append("1").append("1").toString(), "11");
        String s1 = "1";
        String s2 = "1";
        assertNotSame(s1 + s2, "11");
        assertSame((s1 + s2).intern(), "11");
    }
}
