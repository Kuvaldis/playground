package kuvaldis.play.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class AddSerializableInterfaceAdapter extends ClassVisitor {
    public AddSerializableInterfaceAdapter(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, new String[]{"java/io/Serializable"});
    }
}
