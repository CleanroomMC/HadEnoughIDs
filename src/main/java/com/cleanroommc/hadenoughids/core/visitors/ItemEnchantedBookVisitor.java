package com.cleanroommc.hadenoughids.core.visitors;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ItemEnchantedBookVisitor extends ClassVisitor implements Opcodes {

    public static final String CLASS_NAME = "net.minecraft.item.ItemEnchantedBook";

    private static final String ADD_INFORMATION_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "addInformation" : "func_77624_a";
    private static final String ADD_ENCHANTMENT_METHOD = FMLLaunchHandler.isDeobfuscatedEnvironment() ? "addEnchantment" : "func_92115_a";

    public ItemEnchantedBookVisitor(ClassWriter classWriter) {
        super(ASM5, classWriter);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(ADD_INFORMATION_METHOD) || name.equals(ADD_ENCHANTMENT_METHOD)) {
            return new EnchantmentShortToIntegerMethodVisitor(methodVisitor);
        }
        return methodVisitor;
    }

}
