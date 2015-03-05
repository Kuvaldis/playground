package kuvaldis.play.asm;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.ClassReader;

import static org.junit.Assert.assertEquals;

public class ClassVisitorTest {

    @Test
    public void testClassVisitor() throws Exception {
        ClassPrinter cp = new ClassPrinter();
        ClassReader cr = new ClassReader("java.lang.Runnable");
        cr.accept(cp, 0);
        final String visitingResult = cp.getSb().toString();
        System.out.println(visitingResult);
        assertEquals("java/lang/Runnable extends java/lang/Object {" + System.lineSeparator() +
                " run()V" + System.lineSeparator() +
                "}", visitingResult);
    }
}
