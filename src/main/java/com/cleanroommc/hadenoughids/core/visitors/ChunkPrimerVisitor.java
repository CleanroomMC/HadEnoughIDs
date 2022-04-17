package com.cleanroommc.hadenoughids.core.visitors;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.*;

/**
 * @see net.minecraft.world.chunk.ChunkPrimer
 */
public class ChunkPrimerVisitor extends ClassVisitor implements Opcodes {

    public static final String CLASS_NAME = "net.minecraft.world.chunk.ChunkPrimer";

    private static final String DATA_FIELD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "data" : "field_177860_a";

    private static final String GET_BLOCK_INDEX_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "getBlockIndex" : "func_186137_b";

    public ChunkPrimerVisitor(ClassWriter classWriter) {
        super(ASM5, classWriter);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (DATA_FIELD.equals(name)) {
            return super.visitField(access, "heid$data", "[I", signature, value);
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if ("<init>".equals(name)) {
            return new InitMethodVisitor(visitor);
        } else if (!GET_BLOCK_INDEX_METHOD.equals(name)) {
            return new CharArray2IntArrayGenericVisitor(visitor);
        }
        return visitor;
    }

    private static class InitMethodVisitor extends MethodVisitor {

        private InitMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitIntInsn(int opcode, int operand) {
            if (opcode == NEWARRAY) {
                super.visitIntInsn(opcode, T_INT);
                return;
            }
            super.visitIntInsn(opcode, operand);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            if (DATA_FIELD.equals(name)) {
                super.visitFieldInsn(opcode, owner, "heid$data", "[I");
                return;
            }
            super.visitFieldInsn(opcode, owner, name, desc);
        }

    }

    private static class CharArray2IntArrayGenericVisitor extends MethodVisitor {

        private CharArray2IntArrayGenericVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            if (DATA_FIELD.equals(name)) {
                super.visitFieldInsn(opcode, owner, "heid$data", "[I");
                return;
            }
            super.visitFieldInsn(opcode, owner, name, desc);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == I2C) {
                return;
            } else if (opcode == CASTORE) {
                super.visitInsn(IASTORE);
                return;
            } else if (opcode == CALOAD) {
                super.visitInsn(IALOAD);
                return;
            }
            super.visitInsn(opcode);
        }

    }

}
