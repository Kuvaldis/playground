package kuvaldis.play.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public class AddFieldAdapter extends ClassVisitor {

    private final int fieldAccess;
    private final String fieldName;
    private final String fieldDescription;

    private boolean isFieldPresent;

    public AddFieldAdapter(final ClassVisitor cv,
                           final int fieldAccess,
                           final String fieldName,
                           final String fieldDescription) {
        super(Opcodes.ASM5, cv);
        this.fieldAccess = fieldAccess;
        this.fieldName = fieldName;
        this.fieldDescription = fieldDescription;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (name.equals(fieldName)) {
            isFieldPresent = true;
        }
        return cv.visitField(access, name, desc, signature, value);
    }

    @Override
    public void visitEnd() {
        if (!isFieldPresent) {
            final FieldVisitor fv = cv.visitField(fieldAccess, fieldName, fieldDescription, null, null);
            if (fv != null) {
                fv.visitEnd();
            }
        }
        cv.visitEnd();
    }
}
