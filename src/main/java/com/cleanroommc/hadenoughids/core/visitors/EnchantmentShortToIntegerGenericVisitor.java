package com.cleanroommc.hadenoughids.core.visitors;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class EnchantmentShortToIntegerGenericVisitor extends MethodVisitor implements Opcodes {

    private static final String GET_SHORT_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "getShort" : "func_74765_d";
    private static final String GET_INTEGER_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "getInteger" : "func_74762_e";
    private static final String SET_SHORT_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "setShort" : "func_74777_a";
    private static final String SET_INTEGER_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "setInteger" : "func_74768_a";

    private boolean primed;

    public EnchantmentShortToIntegerGenericVisitor(MethodVisitor methodVisitor) {
        super(ASM5, methodVisitor);
    }

    @Override
    public void visitLdcInsn(Object cst) {
        if ("id".equals(cst)) {
            primed = true;
        }
        super.visitLdcInsn(cst);
    }

    @Override
    public void visitInsn(int opcode) {
        if (primed && opcode == I2S) {
            return;
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if (primed) {
            if (GET_SHORT_METHOD.equals(name)) {
                super.visitMethodInsn(opcode, owner, GET_INTEGER_METHOD, desc.replace(")S", ")I"), itf);
                primed = false;
            } else if (SET_SHORT_METHOD.equals(name)) {
                primed = false;
                super.visitMethodInsn(opcode, owner, SET_INTEGER_METHOD, desc.replace("S)", "I)"), itf);
            } else {
                super.visitMethodInsn(opcode, owner, name, desc, itf);
            }
            return;
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

}
