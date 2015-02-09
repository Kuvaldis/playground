package kuvaldis.play.javassist;

import javassist.*;
import javassist.bytecode.*;
import org.junit.Test;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class ClassCreationTest {

    @Test
    public void createNew() throws CannotCompileException {
        final ClassPool classPool = ClassPool.getDefault();
        final CtClass cc = classPool.makeClass("kuvaldis.play.javassist.Point");
        final Class pointClass = cc.toClass();
        assertEquals("kuvaldis.play.javassist.Point", pointClass.getName());
    }

    @Test
    public void addClassPath() throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        final ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath("compiled");
        final CtClass cc = pool.get("HelloCup");
        final Class helloClass = cc.toClass();
        final Object instance = helloClass.newInstance();
        final Field helloField = helloClass.getField("hello");
        assertEquals("Hi WOW cup!", helloField.get(instance));
    }

    @Test
    public void addMethod() throws CannotCompileException, NotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        CtClass cc = ClassPool.getDefault().get("kuvaldis.play.javassist.Hello");
        CtMethod m = CtNewMethod.make("public void logSay() { $proceed(\"Hello, \", \"World!\"); }", cc, "this", "sayWithParams");
        cc.addMethod(m);
        final Class helloClass = cc.toClass();
        final Object instance = helloClass.newInstance();
        final Method sayMethod = helloClass.getMethod("logSay");
        sayMethod.invoke(instance); // prints Hello, World!
    }

    @Test
    public void classFile() throws IOException, DuplicateMemberException {
        ClassFile cf = new ClassFile(false, "test.Foo", null);
        cf.setInterfaces(new String[] { "java.lang.Cloneable" });
        FieldInfo f = new FieldInfo(cf.getConstPool(), "width", "I");
        f.setAccessFlags(AccessFlag.PUBLIC);
        cf.addField(f);
        cf.write(new DataOutputStream(new FileOutputStream("written/kuvaldis/play/javassist/Foo.class")));
    }
}
