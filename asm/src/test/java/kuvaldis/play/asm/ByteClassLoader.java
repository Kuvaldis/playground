package kuvaldis.play.asm;

import java.util.HashMap;
import java.util.Map;

public class ByteClassLoader extends ClassLoader {
    private final Map<String, byte[]> extraClassDefinitions;

    public ByteClassLoader(ClassLoader parent, Map<String, byte[]> extraClassDefinitions) {
        super(parent);
        this.extraClassDefinitions = new HashMap<>(extraClassDefinitions);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        final byte[] classBytes = extraClassDefinitions.remove(name);
        if (classBytes != null) {
            return defineClass(name, classBytes, 0, classBytes.length);
        }
        return super.findClass(name);
    }
}
