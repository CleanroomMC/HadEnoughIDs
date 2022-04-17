package com.cleanroommc.hadenoughids.core.visitors;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.*;

/**
 * @see net.minecraft.network.NetHandlerPlayServer
 */
public class NetHandlerPlayServerVisitor extends ClassVisitor implements Opcodes {

    public static final String CLASS_NAME = "net.minecraft.network.NetHandlerPlayServer";

    private static final String PROCESS_CREATIVE_INVENTORY_ACTION_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "processCreativeInventoryAction" : "func_147344_a";

    public NetHandlerPlayServerVisitor(ClassWriter classWriter) {
        super(ASM5, classWriter);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (PROCESS_CREATIVE_INVENTORY_ACTION_METHOD.equals(name)) {
            return new ProcessCreativeInventoryActionMethodVisitor(visitor);
        }
        return visitor;
    }

    private static class ProcessCreativeInventoryActionMethodVisitor extends MethodVisitor {

        private static final String GET_ITEM_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "getItem" : "func_77973_b";

        private boolean foundIFLT = false;

        private ProcessCreativeInventoryActionMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
            if (!foundIFLT && opcode == IFLT) {
                foundIFLT = true;
                super.visitVarInsn(ALOAD, 3);
                super.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "net/minecraft/item/ItemStack",
                        GET_ITEM_METHOD,
                        "()Lnet/minecraft/item/Item;",
                        false);
                super.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "net/minecraft/item/Item",
                        "getMinMetadata",
                        "()I",
                        false);
                super.visitJumpInsn(IF_ICMPLT, label);
                return;
            }
            super.visitJumpInsn(opcode, label);
        }

    }

}
