package com.cleanroommc.hadenoughids.core.visitors;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ItemStackPacketGenericVisitor extends MethodVisitor implements Opcodes {

    public ItemStackPacketGenericVisitor(MethodVisitor methodVisitor) {
        super(ASM5, methodVisitor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if ("writeShort".equals(name)) {
            super.visitMethodInsn(opcode, owner, "writeVarInt", desc, itf);
            return;
        } else if ("readShort".equals(name)) {
            super.visitMethodInsn(opcode, owner, "readVarInt", "()I", itf);
            return;
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

}
