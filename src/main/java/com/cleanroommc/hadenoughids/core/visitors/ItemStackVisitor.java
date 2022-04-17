package com.cleanroommc.hadenoughids.core.visitors;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.*;

/**
 * @see net.minecraft.item.ItemStack
 */
public class ItemStackVisitor extends ClassVisitor implements Opcodes {

    public static final String CLASS_NAME = "net.minecraft.item.ItemStack";

    private static final String DELEGATED_INIT_METHOD_DESC = "(Lnet/minecraft/item/Item;IILnet/minecraft/nbt/NBTTagCompound;)V";
    private static final String NBT_INIT_METHOD_DESC = "(Lnet/minecraft/nbt/NBTTagCompound;)V";
    private static final String IS_EMPTY_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "isEmpty" : "func_190926_b";
    private static final String WRITE_TO_NBT_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "writeToNBT" : "func_77955_b";
    private static final String ADD_ENCHANTMENT_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "addEnchantment" : "func_77966_a";

    public ItemStackVisitor(ClassWriter classWriter) {
        super(ASM5, classWriter);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if ("<init>".equals(name)) {
            if (DELEGATED_INIT_METHOD_DESC.equals(desc)) {
                return new DelegatedInitMethodVisitor(visitor);
            } else if (NBT_INIT_METHOD_DESC.equals(desc)) {
                return new NBTInitMethodVisitor(visitor);
            }
        } else if (IS_EMPTY_METHOD.equals(name)) {
            return new IsEmptyMethodVisitor(visitor);
        } else if (WRITE_TO_NBT_METHOD.equals(name)) {
            return new WriteToNBTMethodVisitor(visitor);
        } else if (ADD_ENCHANTMENT_METHOD.equals(name)) {
            return new EnchantmentShortToIntegerGenericVisitor(visitor);
        }
        return visitor;
    }

    private static class DelegatedInitMethodVisitor extends MethodVisitor {

        private static final String ITEM_DAMAGE_FIELD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "itemDamage" : "field_77991_e";
        private static final String STACK_SIZE_FIELD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "stackSize" : "field_77994_a";

        private boolean removeItemDamageChecks = false;
        private int itemDamagePutFieldInsnCount = 0;

        private DelegatedInitMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
            super.visitFrame(type, nLocal, local, nStack, stack);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            if (removeItemDamageChecks) {
                if (ITEM_DAMAGE_FIELD.equals(name)) {
                    if (++itemDamagePutFieldInsnCount == 2) {
                        removeItemDamageChecks = false;
                    }
                }
                return;
            }
            if (ITEM_DAMAGE_FIELD.equals(name)) {
                super.visitVarInsn(ALOAD, 1);
                super.visitMethodInsn(
                        INVOKESTATIC,
                        "com/cleanroommc/hadenoughids/core/hooks/UniversalHooks",
                        "getCautiousCorrectItemMetadata",
                        "(ILcom/cleanroommc/hadenoughids/api/IItemMetadataExtension;)I",
                        false);
            } else if (STACK_SIZE_FIELD.equals(name)) {
                removeItemDamageChecks = true;
            }
            super.visitFieldInsn(opcode, owner, name, desc);
        }

        @Override
        public void visitLineNumber(int line, Label start) {
            if (removeItemDamageChecks) {
                return;
            }
            super.visitLineNumber(line, start);
        }

        @Override
        public void visitLabel(Label label) {
            if (removeItemDamageChecks) {
                return;
            }
            super.visitLabel(label);
        }

        @Override
        public void visitInsn(int opcode) {
            if (removeItemDamageChecks) {
                return;
            }
            super.visitInsn(opcode);
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            if (removeItemDamageChecks) {
                return;
            }
            super.visitVarInsn(opcode, var);
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
            if (removeItemDamageChecks) {
                return;
            }
            super.visitJumpInsn(opcode, label);
        }

    }

    private static class NBTInitMethodVisitor extends MethodVisitor {

        private boolean removeItemDamageChecks = false;

        private NBTInitMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            if (removeItemDamageChecks) {
                removeItemDamageChecks = false;
                super.visitVarInsn(ALOAD, 0);
                super.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "net/minecraft/item/ItemStack",
                        "getItemRaw",
                        "()Lnet/minecraft/item/Item;",
                        false);
                super.visitVarInsn(ALOAD, 1);
                super.visitMethodInsn(
                        INVOKESTATIC,
                        "com/cleanroommc/hadenoughids/core/hooks/UniversalHooks",
                        "getCorrectItemMetadataFromNBT",
                        "(Lcom/cleanroommc/hadenoughids/api/IItemMetadataExtension;Lnet/minecraft/nbt/NBTTagCompound;)I",
                        false);
            }
            super.visitFieldInsn(opcode, owner, name, desc);
        }

        @Override
        public void visitInsn(int opcode) {
            if (!removeItemDamageChecks && opcode == ICONST_0) {
                removeItemDamageChecks = true;
                return;
            } else if (removeItemDamageChecks) {
                return;
            }
            super.visitInsn(opcode);
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            if (removeItemDamageChecks) {
                return;
            }
            super.visitVarInsn(opcode, var);
        }

        @Override
        public void visitLdcInsn(Object cst) {
            if (removeItemDamageChecks) {
                return;
            }
            super.visitLdcInsn(cst);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (removeItemDamageChecks) {
                return;
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }

    }

    private static class IsEmptyMethodVisitor extends MethodVisitor {

        private IsEmptyMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitIntInsn(int opcode, int operand) {
            if (operand == -32768) {
                super.visitVarInsn(ALOAD, 0);
                super.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "net/minecraft/item/ItemStack",
                        "getItemRaw",
                        "()Lnet/minecraft/item/Item;",
                        false);
                super.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "net/minecraft/item/Item",
                        "getMinEmptyMetadata",
                        "()I",
                        false);
                return;
            }
            super.visitIntInsn(opcode, operand);
        }

        @Override
        public void visitLdcInsn(Object cst) {
            if (cst instanceof Integer && (Integer) cst == 65535) {
                super.visitVarInsn(ALOAD, 0);
                super.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "net/minecraft/item/ItemStack",
                        "getItemRaw",
                        "()Lnet/minecraft/item/Item;",
                        false);
                super.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "net/minecraft/item/Item",
                        "getMaxEmptyMetadata",
                        "()I",
                        false);
                return;
            }
            super.visitLdcInsn(cst);
        }

    }

    private static class WriteToNBTMethodVisitor extends MethodVisitor {

        private static final String SET_SHORT_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "setShort" : "func_74777_a";
        private static final String SET_INTEGER_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "setInteger" : "func_74768_a";

        private WriteToNBTMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM5, methodVisitor);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (SET_SHORT_METHOD.equals(name)) {
                super.visitMethodInsn(opcode, owner, SET_INTEGER_METHOD, desc.replace("S)", "I)"), itf);
                return;
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }

    }

}
