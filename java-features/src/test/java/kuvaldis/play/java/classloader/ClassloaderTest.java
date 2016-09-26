package kuvaldis.play.java.classloader;

import kuvaldis.play.java.classloader.AbstractCounter;
import kuvaldis.play.java.classloader.ByteClassLoader;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ClassloaderTest {

    private static final byte[] CONCRETE_COUNTER_CLASS = new byte[] {
            -54, -2, -70, -66, 0, 0, 0, 52, 0, 21, 10, 0, 4, 0, 15, 9, 0, 3, 0, 16, 7, 0, 17, 7, 0, 18, 1, 0, 6, 60,
            105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 1, 0, 15, 76, 105, 110, 101, 78,
            117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 18, 76, 111, 99, 97, 108, 86, 97, 114, 105, 97, 98, 108,
            101, 84, 97, 98, 108, 101, 1, 0, 4, 116, 104, 105, 115, 1, 0, 48, 76, 107, 117, 118, 97, 108, 100, 105, 115,
            47, 112, 108, 97, 121, 47, 106, 97, 118, 97, 47, 99, 108, 97, 115, 115, 108, 111, 97, 100, 101, 114, 47, 67,
            111, 110, 99, 114, 101, 116, 101, 67, 111, 117, 110, 116, 101, 114, 59, 1, 0, 8, 60, 99, 108, 105, 110, 105,
            116, 62, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 20, 67, 111, 110, 99, 114, 101, 116,
            101, 67, 111, 117, 110, 116, 101, 114, 46, 106, 97, 118, 97, 12, 0, 5, 0, 6, 12, 0, 19, 0, 20, 1, 0, 46, 107,
            117, 118, 97, 108, 100, 105, 115, 47, 112, 108, 97, 121, 47, 106, 97, 118, 97, 47, 99, 108, 97, 115, 115, 108,
            111, 97, 100, 101, 114, 47, 67, 111, 110, 99, 114, 101, 116, 101, 67, 111, 117, 110, 116, 101, 114, 1, 0, 46,
            107, 117, 118, 97, 108, 100, 105, 115, 47, 112, 108, 97, 121, 47, 106, 97, 118, 97, 47, 99, 108, 97, 115, 115,
            108, 111, 97, 100, 101, 114, 47, 65, 98, 115, 116, 114, 97, 99, 116, 67, 111, 117, 110, 116, 101, 114, 1, 0,
            5, 99, 111, 117, 110, 116, 1, 0, 1, 73, 0, 33, 0, 3, 0, 4, 0, 0, 0, 0, 0, 2, 0, 1, 0, 5, 0, 6, 0, 1, 0, 7, 0,
            0, 0, 47, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 1, -79, 0, 0, 0, 2, 0, 8, 0, 0, 0, 6, 0, 1, 0, 0, 0, 3, 0, 9, 0,
            0, 0, 12, 0, 1, 0, 0, 0, 5, 0, 10, 0, 11, 0, 0, 0, 8, 0, 12, 0, 6, 0, 1, 0, 7, 0, 0, 0, 37, 0, 2, 0, 0, 0, 0,
            0, 9, -78, 0, 2, 4, 96, -77, 0, 2, -79, 0, 0, 0, 1, 0, 8, 0, 0, 0, 10, 0, 2, 0, 0, 0, 5, 0, 8, 0, 6, 0, 1, 0,
            13, 0, 0, 0, 2, 0, 14
    };

    @Test
    public void testClassLoaders() throws Exception {
        final ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println("System ClassLoader: " + systemClassLoader.getClass().getName());

        final ClassLoader currentClassClassLoader = this.getClass().getClassLoader();
        System.out.println("Current class ClassLoader: " + currentClassClassLoader);

        final ClassLoader currentClassClassLoaderClassLoader = currentClassClassLoader.getClass().getClassLoader();

        System.out.println("Current class ClassLoader ClassLoader: " + currentClassClassLoaderClassLoader);
    }

    @Test
    public void testStaticFieldWithSeveralClassLoaders() throws Exception {
        assertEquals(0, AbstractCounter.count);

        final ClassLoader currentClassloader = getClass().getClassLoader();
        final Map<String, byte[]> externalClasses = new HashMap<>();
        externalClasses.put("kuvaldis.play.java.classloader.ConcreteCounter", CONCRETE_COUNTER_CLASS);

        final ByteClassLoader byteClassLoader1 = new ByteClassLoader(currentClassloader, externalClasses);
        final Class<?> concreteCounterClass1 = byteClassLoader1.loadClass("kuvaldis.play.java.classloader.ConcreteCounter");
        concreteCounterClass1.newInstance();
        assertEquals(1, AbstractCounter.count);

        final ByteClassLoader byteClassLoader2 = new ByteClassLoader(currentClassloader, externalClasses);
        final Class<?> concreteCounterClass2 = byteClassLoader2.loadClass("kuvaldis.play.java.classloader.ConcreteCounter");
        concreteCounterClass2.newInstance();
        assertEquals(2, AbstractCounter.count);
    }

}
