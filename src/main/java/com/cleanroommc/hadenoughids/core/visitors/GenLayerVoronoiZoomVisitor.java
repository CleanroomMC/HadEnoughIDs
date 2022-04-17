package com.cleanroommc.hadenoughids.core.visitors;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class GenLayerVoronoiZoomVisitor extends ClassVisitor implements Opcodes {

    public static final String CLASS_NAME = "net.minecraft.world.gen.layer.GenLayerVoronoiZoom";

    private static final String GET_INTS_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "getInts" : "func_75904_a";

    public GenLayerVoronoiZoomVisitor(ClassWriter classWriter) {
        super(ASM5, classWriter);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (GET_INTS_METHOD.equals(name)) {
            return new GetIntsMethodVisitor(visitor);
        }
        return visitor;
    }

    private static class GetIntsMethodVisitor extends MethodVisitor {

        private boolean primed = false;

        private GetIntsMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitIntInsn(int opcode, int operand) {
            if (operand == 255) {
                primed = true;
                return;
            }
            super.visitIntInsn(opcode, operand);
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
