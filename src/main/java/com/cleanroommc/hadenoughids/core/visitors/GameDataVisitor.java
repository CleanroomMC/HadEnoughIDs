package com.cleanroommc.hadenoughids.core.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class GameDataVisitor extends ClassVisitor implements Opcodes {

    public static final String CLASS_NAME = "net.minecraftforge.registries.GameData";

    public GameDataVisitor(ClassWriter classWriter) {
        super(ASM5, classWriter);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if ((access & ACC_STATIC) != 0 && name.equals("init")) {
            return new InitMethodVisitor(visitor);
        }
        return visitor;
    }

    private static class InitMethodVisitor extends MethodVisitor {

        private InitMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitIntInsn(int opcode, int operand) {
            switch (operand) {
                case 4095: // Blocks
                    // (((Integer.MAX_VALUE - 1) * 2) / 16) - 1
                    // This is to account for the Block::getStateId issue
                    // Theoretically could be slightly larger still
                    // Because vanilla only occupies 254 blocks, 1674 unique meta states
                    // Which averages to around 6.59 meta values occupied per block
                    super.visitLdcInsn(268435454);
                    break;
                case 255: // Biomes, Potions
                case 1024: // Villager Professions
                case 31999: // Items
                case 32766: // Enchantments
                    super.visitLdcInsn(Integer.MAX_VALUE - 1);
                    break;
                default:
                    super.visitIntInsn(opcode, operand);
            }
        }

    }

}
