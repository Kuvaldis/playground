package kuvaldis.play.java;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeCheck {

    public static void main(String[] args) throws Exception {
        final Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        final Unsafe unsafe = (Unsafe) f.get(null);
        System.out.println(unsafe.addressSize());
    }
}
