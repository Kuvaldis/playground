package kuvaldis.play.asm;

import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.ASM5;

public class ClassPrinter extends ClassVisitor {

    private StringBuilder sb = new StringBuilder();

    public ClassPrinter() {
        super(ASM5);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        sb.append(name).append(" extends ").append(superName).append(" {").append(System.lineSeparator());
    }

    @Override
    public void visitSource(String source, String debug) {
    }

    @Override
    public void visitOuterClass(String owner, String name, String desc) {
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return null;
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
        return null;
    }

    @Override
    public void visitAttribute(Attribute attr) {
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        sb.append(" ").append(desc).append(" ").append(name).append(System.lineSeparator());
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        sb.append(" ").append(name).append(desc).append(System.lineSeparator());
        return null;
    }

    @Override
    public void visitEnd() {
        sb.append("}");
    }

    public StringBuilder getSb() {
        return sb;
    }
}
