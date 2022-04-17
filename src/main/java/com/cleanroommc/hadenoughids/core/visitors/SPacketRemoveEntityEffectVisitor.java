package com.cleanroommc.hadenoughids.core.visitors;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @see net.minecraft.network.play.server.SPacketRemoveEntityEffect
 */
public class SPacketRemoveEntityEffectVisitor extends ClassVisitor implements Opcodes {

    public static final String CLASS_NAME = "net.minecraft.network.play.server.SPacketRemoveEntityEffect";

    private static final String READ_PACKET_DATA_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "readPacketData" : "func_148837_a";
    private static final String WRITE_PACKET_DATA_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "writePacketData" : "func_148840_b";

    public SPacketRemoveEntityEffectVisitor(ClassWriter classWriter) {
        super(ASM5, classWriter);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (READ_PACKET_DATA_METHOD.equals(name)) {
            return new ReadPacketDataMethodVisitor(visitor);
        } else if (WRITE_PACKET_DATA_METHOD.equals(name)) {
            return new WritePacketDataMethodVisitor(visitor);
        }
        return visitor;
    }

    private static class ReadPacketDataMethodVisitor extends MethodVisitor {

        private ReadPacketDataMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if ("readUnsignedByte".equals(name)) {
                super.visitMethodInsn(opcode, owner, "readVarInt", "()I", itf);
                return;
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }

    }

    private static class WritePacketDataMethodVisitor extends MethodVisitor {

        private WritePacketDataMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if ("writeByte".equals(name)) {
                super.visitMethodInsn(opcode, owner, "writeVarInt", desc, itf);
                return;
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }

    }

}
