package kuvaldis.play.javassist;

import javassist.*;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Objects;

public class ClassLoaderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void differentClassLoaders() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        expectedException.expect(ClassCastException.class);
        final ClassLoader classLoader = new Loader();
        final Class clazz = classLoader.loadClass("kuvaldis.play.javassist.Hello");
        final Object object = clazz.newInstance();
        final Hello hello = (Hello) object;
    }

    @Test
    public void loaderTest() throws NotFoundException, CannotCompileException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassPool pool = ClassPool.getDefault();
        Loader cl = new Loader(pool);
        CtClass ct = pool.get("kuvaldis.play.javassist.Rectangle");
        final int[] checker = new int[1];
        cl.addTranslator(pool, new Translator() {
            @Override
            public void start(ClassPool pool) throws NotFoundException, CannotCompileException { }
            @Override
            public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
                CtClass cc = pool.get(classname);
                cc.setModifiers(Modifier.PUBLIC);
                checker[0] = 1;
            }
        });
        ct.setSuperclass(pool.makeClass("kuvaldis.play.javassist.Point"));
        Class c = cl.loadClass("kuvaldis.play.javassist.Rectangle");
        Assert.assertEquals("kuvaldis.play.javassist.Point", c.getSuperclass().getName());
        Assert.assertEquals(1, checker[0]);
    }
}
