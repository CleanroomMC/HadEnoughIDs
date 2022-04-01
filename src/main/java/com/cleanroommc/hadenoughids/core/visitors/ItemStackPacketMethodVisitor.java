package com.cleanroommc.hadenoughids.core.visitors;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ItemStackPacketMethodVisitor extends MethodVisitor implements Opcodes {

    private boolean prepareToPatch = false;

    public ItemStackPacketMethodVisitor(MethodVisitor methodVisitor) {
        super(ASM5, methodVisitor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if (!prepareToPatch && ("writeByte".equals(name) || "readByte".equals(name))) {
            prepareToPatch = true;
        } else if (prepareToPatch) {
            if ("writeShort".equals(name)) {
                prepareToPatch = false;
                super.visitMethodInsn(opcode, owner, "writeInteger", desc, itf);
                return;
            } else if ("readShort".equals(name)) {
                super.visitMethodInsn(opcode, owner, "readInteger", "()I", itf);
                return;
            }
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

}
