package com.cleanroommc.hadenoughids.core.visitors;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.objectweb.asm.*;

public class SPacketEntityEffectVisitor extends ClassVisitor implements Opcodes {

    public static final String CLASS_NAME = "net.minecraft.network.play.server.SPacketEntityEffect";
    public static final String EFFECT_ID_FIELD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "effectId" : "field_149432_b";
    public static final String AMPLIFIER_FIELD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "amplifier" : "field_149433_c";
    public static final String GET_EFFECT_ID_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "getEffectId" : "func_149427_e";
    public static final String GET_AMPLIFIER_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "getAmplifier" : "func_149428_f";
    public static final String READ_PACKET_DATA_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "readPacketData" : "func_148837_a";
    public static final String WRITE_PACKET_DATA_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "writePacketData" : "func_148840_b";

    private int tries;

    private boolean visitedCtor, visitedReadPacketData, visitedWritePacketData;

    public SPacketEntityEffectVisitor(ClassWriter classWriter) {
        super(ASM5, classWriter);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, ArrayUtils.add(interfaces, "com/cleanroommc/hadenoughids/api/ICorrectlyTypedSPacketEntityEffect"));
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (tries < 2 && desc.equals("B")) {
            tries++;
            return super.visitField(access, name, "I", signature, value);
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (!visitedCtor && desc.startsWith("(I")) {
            visitedCtor = true;
            return new InitMethodVisitor(visitor);
        } else if (!visitedReadPacketData && name.equals(READ_PACKET_DATA_METHOD)) {
            visitedReadPacketData = true;
            return new ReadPacketDataMethodVisitor(visitor);
        } else if (!visitedWritePacketData && name.equals(WRITE_PACKET_DATA_METHOD)) {
            visitedWritePacketData = true;
            return new WriterPacketDataMethodVisitor(visitor);
        }
        return visitor;
    }

    @Override
    public void visitEnd() {
        FieldVisitor fieldVisitor = super.visitField(ACC_PRIVATE, "cleanroom$effectId", "I", null, 0);
        fieldVisitor.visitEnd();
        fieldVisitor = super.visitField(ACC_PRIVATE, "cleanroom$amplifier", "I", null, 0);
        fieldVisitor.visitEnd();
        generateGetters("cleanroom$getEffectId", "cleanroom$effectId");
        generateGetters("cleanroom$getAmplifier", "cleanroom$amplifier");
        super.visitEnd();
    }

    private void generateGetters(String getterName, String fieldName) {
        String classType = CLASS_NAME.replace('.', '/');
        MethodVisitor methodVisitor = super.visitMethod(ACC_PUBLIC, getterName, "()I", null, null);
        AnnotationVisitor annotationVisitor = methodVisitor.visitAnnotation("@Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
        annotationVisitor.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
        annotationVisitor.visitEnd();
        Label start = new Label();
        methodVisitor.visitLabel(start);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitFieldInsn(ACC_PRIVATE, classType, fieldName, "I");
        methodVisitor.visitInsn(IRETURN);
        Label end = new Label();
        methodVisitor.visitLabel(end);
        methodVisitor.visitLocalVariable("this", 'L' + classType + ';', null, start, end, 0);
        methodVisitor.visitMaxs(1, 1);
    }

    private static class InitMethodVisitor extends MethodVisitor {

        private InitMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitIntInsn(int opcode, int operand) {
            if (operand == 255) {
                return;
            }
            super.visitIntInsn(opcode, operand);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == IAND || opcode == I2B) {
                return;
            }
            super.visitInsn(opcode);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            if (opcode == PUTFIELD) {
                if (name.equals(EFFECT_ID_FIELD)) {
                    super.visitFieldInsn(opcode, owner, "cleanroom$effectId", "I");
                    return;
                } else if (name.equals(AMPLIFIER_FIELD)) {
                    super.visitFieldInsn(opcode, owner, "cleanroom$amplifier", "I");
                    return;
                }
            }
            super.visitFieldInsn(opcode, owner, name, desc);
        }

    }

    private static class ReadPacketDataMethodVisitor extends MethodVisitor {

        private int tries;

        private ReadPacketDataMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (tries < 2 && name.equals("readByte")) {
                tries++;
                super.visitMethodInsn(opcode, owner, "readInt", desc, itf);
                return;
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }

    }

    private static class WriterPacketDataMethodVisitor extends MethodVisitor {

        private int tries;

        private WriterPacketDataMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (tries < 2 && name.equals("writeByte")) {
                tries++;
                super.visitMethodInsn(opcode, owner, "writeInt", desc, itf);
                return;
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }

    }

}
