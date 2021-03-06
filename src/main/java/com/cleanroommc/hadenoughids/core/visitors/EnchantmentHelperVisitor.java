package com.cleanroommc.hadenoughids.core.visitors;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @see net.minecraft.enchantment.EnchantmentHelper
 */
public class EnchantmentHelperVisitor extends ClassVisitor implements Opcodes {

    public static final String CLASS_NAME = "net.minecraft.enchantment.EnchantmentHelper";

    private static final String GET_ENCHANTMENT_LEVEL_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "getEnchantmentLevel" : "func_77506_a";
    private static final String GET_ENCHANTMENTS_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "getEnchantments" : "func_82781_a";
    private static final String APPLY_ENCHANTMENT_MODIFIER_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "applyEnchantmentModifier" : "func_77518_a";
    private static final String SET_ENCHANTMENTS_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "setEnchantments" : "func_82782_a";

    public EnchantmentHelperVisitor(ClassWriter classWriter) {
        super(ASM5, classWriter);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (GET_ENCHANTMENT_LEVEL_METHOD.equals(name) || GET_ENCHANTMENTS_METHOD.equals(name) || APPLY_ENCHANTMENT_MODIFIER_METHOD.equals(name) ||
                SET_ENCHANTMENTS_METHOD.equals(name)) {
            return new EnchantmentShortToIntegerGenericVisitor(methodVisitor);
        }
        return methodVisitor;
    }

}
