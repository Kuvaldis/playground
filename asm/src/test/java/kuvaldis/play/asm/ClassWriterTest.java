package kuvaldis.play.asm;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.objectweb.asm.Opcodes.*;

public class ClassWriterTest {

    @Test
    public void testClassWriter() throws Exception {
        ClassWriter cw = new ClassWriter(0);

        cw.visit(V1_8, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                "kuvaldis/play/asm/generated/Comparable", null, "java/lang/Object",
                new String[]{"kuvaldis/play/asm/Mesurable"});
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "LESS", "I", null, -1).visitEnd();
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL", "I", null, 0).visitEnd();
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GREATER", "I", null, 1).visitEnd();
        cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I", null, null).visitEnd();
        cw.visitEnd();

        byte[] b = cw.toByteArray();

        final Map<String, byte[]> extraClasses = new HashMap<>();
        extraClasses.put("kuvaldis.play.asm.generated.Comparable", b);
        final ClassLoader classLoader = new ByteClassLoader(this.getClass().getClassLoader(), extraClasses);
        final Class cls = classLoader.loadClass("kuvaldis.play.asm.generated.Comparable");
        assertEquals(-1, cls.getField("LESS").getInt(null));
    }

    @Test
    public void testAddSerializableInterface() throws Exception {
        ClassWriter cw = new ClassWriter(0);

        cw.visit(V1_8, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                "kuvaldis/play/asm/generated/DescribesNothingInterface", null, "java/lang/Object", null);
        cw.visitEnd();

        byte[] b1 = cw.toByteArray();

        cw = new ClassWriter(0);
        final ClassVisitor cv = new AddSerializableInterfaceAdapter(cw);
        ClassReader cr = new ClassReader(b1);
        cr.accept(cv, 0);
        byte[] b2 = cw.toByteArray();

        final Map<String, byte[]> extraClasses = new HashMap<>();
        extraClasses.put("kuvaldis.play.asm.generated.DescribesNothingInterface", b2);
        final ClassLoader classLoader = new ByteClassLoader(this.getClass().getClassLoader(), extraClasses);
        final Class cls = classLoader.loadClass("kuvaldis.play.asm.generated.DescribesNothingInterface");
        assertEquals("java.io.Serializable", cls.getInterfaces()[0].getName());
    }

    @Test
    public void testAddField() throws Exception {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream stream = classLoader.getResourceAsStream("kuvaldis/play/asm/ClassWithField.class");
        byte[] bytes = new byte[stream.available()];
        //noinspection ResultOfMethodCallIgnored
        stream.read(bytes);

        ClassWriter cw = new ClassWriter(0);
        ClassVisitor cv = new AddFieldAdapter(cw, ACC_PRIVATE, "field", "I");
        ClassReader cr = new ClassReader(bytes);
        cr.accept(cv, 0);
        final byte[] withFieldModified = cw.toByteArray();

        stream = classLoader.getResourceAsStream("kuvaldis/play/asm/ClassWithoutField.class");
        bytes = new byte[stream.available()];
        //noinspection ResultOfMethodCallIgnored
        stream.read(bytes);

        cw = new ClassWriter(0);
        cv = new AddFieldAdapter(cw, ACC_PRIVATE, "field", "I");
        cr = new ClassReader(bytes);
        cr.accept(cv, 0);
        final byte[] withoutFieldModified = cw.toByteArray();

        final Map<String, byte[]> extraClasses = new HashMap<>();
        extraClasses.put("kuvaldis.play.asm.ClassWithField", withFieldModified);
        extraClasses.put("kuvaldis.play.asm.ClassWithoutField", withoutFieldModified);
        final ClassLoader byteClassLoader = new ByteClassLoader(null, extraClasses);
        final Class classWithField = byteClassLoader.loadClass("kuvaldis.play.asm.ClassWithField");
        classWithField.getDeclaredField("field");
        final Class classWithoutField = byteClassLoader.loadClass("kuvaldis.play.asm.ClassWithoutField");
        classWithoutField.getDeclaredField("field");
        Field original = null;
        try {
            original = ClassWithoutField.class.getDeclaredField("field");
        } catch (NoSuchFieldException ignore) { }
        assertNull(original);
    }
}
