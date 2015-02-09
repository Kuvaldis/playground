package kuvaldis.play.javassist;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ModifyClassTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void modify() throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {
        final ClassPool classPool = ClassPool.getDefault();
        final CtClass cc = classPool.get("kuvaldis.play.javassist.Hello");
        final CtMethod m = cc.getDeclaredMethod("say");
        m.insertBefore("world = \"World\";");
        Class c = cc.toClass(); // should be before usage of Hello class
        Hello h = (Hello) c.newInstance();
        h.say(); // prints Hello, World!
        assertEquals("World", h.getWorld());
    }

    @Test
    public void input() throws NotFoundException, CannotCompileException {
        final ClassPool classPool = ClassPool.getDefault();
        final CtClass cc = classPool.get("kuvaldis.play.javassist.Hello");
        final CtMethod m = cc.getDeclaredMethod("sayWithParams");
        m.insertBefore("System.out.println(\"Second param is \" + $2);");
        m.insertBefore("System.out.println(\"First param is \" + $1);");
        cc.toClass();
        new Hello().sayWithParams("1", "2");
        /* Will print
        First param is 1
        Second param is 2
        12
         */
    }

    @Test
    public void addCatchException() throws NotFoundException, CannotCompileException, IOException {
        expectedException.expect(IOException.class);
        final ClassPool classPool = ClassPool.getDefault();
        final CtClass cc = classPool.get("kuvaldis.play.javassist.Hello");
        final CtMethod m = cc.getDeclaredMethod("sayThrowsIOException");
        CtClass etype = classPool.get("java.io.IOException");
        m.addCatch("System.out.println($e); throw $e;", etype);
        cc.toClass();
        new Hello().sayThrowsIOException(); // also prints java.io.IOException
    }

    @Test
    public void replaceConstructorArgument() throws CannotCompileException, NotFoundException {
        final ClassPool classPool = ClassPool.getDefault();
        final CtClass cc = classPool.get("kuvaldis.play.javassist.Hello");
        final CtMethod m = cc.getDeclaredMethod("callPrintln");
        m.instrument(new ExprEditor() {
            @Override
            public void edit(MethodCall c) throws CannotCompileException {
                if (c.getClassName().equals("java.io.PrintStream")) {
                    c.replace("$1 = \"11\"; $proceed($$);");
                }
            }
        });
        cc.toClass();
        new Hello().callPrintln("1"); // will print 11
    }
}
