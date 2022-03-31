package com.cleanroommc.hadenoughids.core;

import com.cleanroommc.hadenoughids.core.visitors.GameDataVisitor;
import com.cleanroommc.hadenoughids.core.visitors.NetHandlerPlayClientVisitor;
import com.cleanroommc.hadenoughids.core.visitors.SPacketEntityEffectVisitor;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class HadEnoughIDsTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] classBytes) {
        switch (transformedName) {
            case GameDataVisitor.CLASS_NAME:
            {
                ClassWriter classWriter = new ClassWriter(0);
                new ClassReader(classBytes).accept(new GameDataVisitor(classWriter), 0);
                return classWriter.toByteArray();
            }
            case SPacketEntityEffectVisitor.CLASS_NAME:
            {
                ClassWriter classWriter = new ClassWriter(0);
                new ClassReader(classBytes).accept(new SPacketEntityEffectVisitor(classWriter), 0);
                return classWriter.toByteArray();
            }
            case NetHandlerPlayClientVisitor.CLASS_NAME:
            {
                ClassWriter classWriter = new ClassWriter(0);
                new ClassReader(classBytes).accept(new NetHandlerPlayClientVisitor(classWriter), 0);
                return classWriter.toByteArray();
            }
        }
        return classBytes;
    }

}
