package com.cleanroommc.hadenoughids.core.visitors;

import org.apache.commons.lang3.ArrayUtils;
import org.objectweb.asm.*;

/**
 * @see net.minecraft.item.Item
 */
public class ItemVisitor extends ClassVisitor implements Opcodes {

    public static final String CLASS_NAME = "net.minecraft.item.Item";

    public ItemVisitor(ClassWriter classWriter) {
        super(ASM5, classWriter);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, ArrayUtils.add(interfaces, "com/cleanroommc/hadenoughids/api/IItemMetadataExtension"));
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if ("setDamage".equals(name)) { // Forge added method, wtf?
            return new SetDamageMethodVisitor(visitor);
        }
        return visitor;
    }

    private static class SetDamageMethodVisitor extends MethodVisitor {

        private boolean wipe = false;

        private SetDamageMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            if (wipe) {
                return;
            }
            super.visitVarInsn(opcode, var);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            if (wipe) {
                return;
            } else if (opcode == PUTFIELD) {
                super.visitVarInsn(ALOAD, 0);
                super.visitMethodInsn(
                        INVOKESTATIC,
                        "com/cleanroommc/hadenoughids/core/hooks/UniversalHooks",
                        "getCorrectItemMetadata",
                        "(ILcom/cleanroommc/hadenoughids/api/IItemMetadataExtension;)I",
                        false);
                wipe = true;
            }
            super.visitFieldInsn(opcode, owner, name, desc);
        }

        @Override
        public void visitLabel(Label label) {
            if (wipe) {
                return;
            }
            super.visitLabel(label);
        }

        @Override
        public void visitLineNumber(int line, Label start) {
            if (wipe) {
                return;
            }
            super.visitLineNumber(line, start);
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
            if (wipe) {
                return;
            }
            super.visitJumpInsn(opcode, label);
        }

        @Override
        public void visitInsn(int opcode) {
            if (wipe) {
                if (opcode != RETURN) {
                    return;
                }
                wipe = false;
            }
            super.visitInsn(opcode);
        }

        @Override
        public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
            if (wipe) {
                return;
            }
            super.visitFrame(type, nLocal, local, nStack, stack);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(3, maxLocals);
        }

    }

}
