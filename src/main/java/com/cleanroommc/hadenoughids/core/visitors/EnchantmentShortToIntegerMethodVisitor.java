package com.cleanroommc.hadenoughids.core.visitors;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class EnchantmentShortToIntegerMethodVisitor extends MethodVisitor implements Opcodes {

    private static final String GET_SHORT_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "getShort" : "func_74765_d";
    private static final String GET_INTEGER_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "getInteger" : "func_74762_e";
    private static final String SET_SHORT_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "setShort" : "func_74777_a";
    private static final String SET_INTEGER_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "setInteger" : "func_74768_a";

    private boolean prepareChange;

    public EnchantmentShortToIntegerMethodVisitor(MethodVisitor methodVisitor) {
        super(ASM5, methodVisitor);
    }

    @Override
    public void visitLdcInsn(Object cst) {
        if ("id".equals(cst)) {
            prepareChange = true;
        }
        super.visitLdcInsn(cst);
    }

    @Override
    public void visitInsn(int opcode) {
        if (prepareChange && opcode == I2S) {
            return;
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if (prepareChange) {
            if (name.equals(GET_SHORT_METHOD)) {
                super.visitMethodInsn(opcode, owner, GET_INTEGER_METHOD, desc.replace(")S", ")I"), itf);
                prepareChange = false;
            } else if (name.equals(SET_SHORT_METHOD)) {
                prepareChange = false;
                super.visitMethodInsn(opcode, owner, SET_INTEGER_METHOD, desc.replace("S)", "I)"), itf);
            } else {
                super.visitMethodInsn(opcode, owner, name, desc, itf);
            }
            return;
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

}
