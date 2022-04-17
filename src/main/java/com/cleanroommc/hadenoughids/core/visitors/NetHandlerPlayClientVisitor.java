package com.cleanroommc.hadenoughids.core.visitors;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @see net.minecraft.client.network.NetHandlerPlayClient
 */
public class NetHandlerPlayClientVisitor extends ClassVisitor implements Opcodes {

    public static final String CLASS_NAME = "net.minecraft.client.network.NetHandlerPlayClient";

    private static final String HANDLE_ENTITY_EFFECT_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "handleEntityEffect" : "func_147260_a";
    private static final String HANDLE_SPAWN_OBJECT_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "handleSpawnObject" : "func_147235_a";

    public NetHandlerPlayClientVisitor(ClassWriter classWriter) {
        super(ASM5, classWriter);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (HANDLE_ENTITY_EFFECT_METHOD.equals(name)) {
            return new HandleEntityEffectMethodVisitor(visitor);
        } else if (HANDLE_SPAWN_OBJECT_METHOD.equals(name)) {
            return new HandleSpawnObjectMethodVisitor(visitor);
        }
        return visitor;
    }

    private static class HandleEntityEffectMethodVisitor extends MethodVisitor {

        private boolean cancellingAndBitOp = false;

        private HandleEntityEffectMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (SPacketEntityEffectVisitor.GET_EFFECT_ID_METHOD.equals(name)) {
                cancellingAndBitOp = true;
                super.visitTypeInsn(CHECKCAST, "com/cleanroommc/hadenoughids/api/ICorrectlyTypedSPacketEntityEffect");
                super.visitMethodInsn(
                        INVOKEINTERFACE,
                        "com/cleanroommc/hadenoughids/api/ICorrectlyTypedSPacketEntityEffect",
                        "cleanroom$getEffectId",
                        "()I",
                        true);
                return;
            } else if (SPacketEntityEffectVisitor.GET_AMPLIFIER_METHOD.equals(name)) {
                super.visitTypeInsn(CHECKCAST, "com/cleanroommc/hadenoughids/api/ICorrectlyTypedSPacketEntityEffect");
                super.visitMethodInsn(
                        INVOKEINTERFACE,
                        "com/cleanroommc/hadenoughids/api/ICorrectlyTypedSPacketEntityEffect",
                        "cleanroom$getAmplifier",
                        "()I",
                        true);
                return;
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }

        @Override
        public void visitIntInsn(int opcode, int operand) {
            if (cancellingAndBitOp && operand == 255) {
                return;
            }
            super.visitIntInsn(opcode, operand);
        }

        @Override
        public void visitInsn(int opcode) {
            if (cancellingAndBitOp && opcode == IAND) {
                cancellingAndBitOp = false;
                return;
            }
            super.visitInsn(opcode);
        }

    }

    private static class HandleSpawnObjectMethodVisitor extends MethodVisitor {

        private boolean primed;

        private HandleSpawnObjectMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitTypeInsn(int opcode, String type) {
            if ("net/minecraft/entity/item/EntityFallingBlock".equals(type)) {
                primed = true;
            }
            super.visitTypeInsn(opcode, type);
        }

        @Override
        public void visitLdcInsn(Object cst) {
            if (primed && cst instanceof Integer && (Integer) cst == 65535) {
                return;
            }
            super.visitLdcInsn(cst);
        }

        @Override
        public void visitInsn(int opcode) {
            if (primed && opcode == IAND) {
                primed = false;
                return;
            }
            super.visitInsn(opcode);
        }

    }

}
