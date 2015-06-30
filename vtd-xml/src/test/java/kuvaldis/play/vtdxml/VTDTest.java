package kuvaldis.play.vtdxml;

import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VTDTest {

    @Test
    public void testHelloWorld() throws Exception {
        assertEquals("Hello, World!", getHelloWorld());
    }

    private String getHelloWorld() throws NavException {
        final VTDGen gen = new VTDGen();
        if (gen.parseFile("xml/input.xml", true)) {
            final VTDNav nav = gen.getNav();
            if (nav.toElementNS(VTDNav.FIRST_CHILD, "someURL", "b")) {
                final int i = nav.getText();
                if (i != -1) {
                    return nav.toString(i);
                }
            }
        }
        return null;
    }
}
