package kuvaldis.play.asm;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;

public class TraceClassVisitorTest {

    @Test
    public void testTraceClassVisitor() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        final TraceClassVisitor tcv = new TraceClassVisitor(printWriter);
        ClassReader cr = new ClassReader("java.lang.Runnable");
        cr.accept(tcv, 0);
        printWriter.close();
        System.out.println(stringWriter);
    }
}
