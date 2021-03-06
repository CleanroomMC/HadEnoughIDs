package com.cleanroommc.hadenoughids.core;

import com.cleanroommc.hadenoughids.core.visitors.*;
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
            case SPacketRemoveEntityEffectVisitor.CLASS_NAME:
            {
                ClassWriter classWriter = new ClassWriter(0);
                new ClassReader(classBytes).accept(new SPacketRemoveEntityEffectVisitor(classWriter), 0);
                return classWriter.toByteArray();
            }
            case NetHandlerPlayClientVisitor.CLASS_NAME:
            {
                ClassWriter classWriter = new ClassWriter(0);
                new ClassReader(classBytes).accept(new NetHandlerPlayClientVisitor(classWriter), 0);
                return classWriter.toByteArray();
            }
            case ItemStackVisitor.CLASS_NAME:
            {
                ClassWriter classWriter = new ClassWriter(0);
                new ClassReader(classBytes).accept(new ItemStackVisitor(classWriter), 0);
                return classWriter.toByteArray();
            }
            case ItemStackPacketVisitor.PACKET_UTIL_CLASS_NAME:
            {
                ClassWriter classWriter = new ClassWriter(0);
                new ClassReader(classBytes).accept(new ItemStackPacketVisitor(classWriter, s -> s.equals(ItemStackPacketVisitor.WRITE_ITEMSTACK_FROM_CLIENT_TO_SERVER_METHOD)), 0);
                return classWriter.toByteArray();
            }
            case ItemStackPacketVisitor.PACKET_BUFFER_CLASS_NAME:
            {
                ClassWriter classWriter = new ClassWriter(0);
                new ClassReader(classBytes).accept(new ItemStackPacketVisitor(classWriter,
                        s -> s.equals(ItemStackPacketVisitor.WRITE_ITEMSTACK_METHOD) || s.equals(ItemStackPacketVisitor.READ_ITEMSTACK_METHOD)), 0);
                return classWriter.toByteArray();
            }
            case ItemVisitor.CLASS_NAME:
            {
                ClassWriter classWriter = new ClassWriter(0);
                new ClassReader(classBytes).accept(new ItemVisitor(classWriter), 0);
                return classWriter.toByteArray();
            }
            case NetHandlerPlayServerVisitor.CLASS_NAME:
            {
                ClassWriter classWriter = new ClassWriter(0);
                new ClassReader(classBytes).accept(new NetHandlerPlayServerVisitor(classWriter), 0);
                return classWriter.toByteArray();
            }
            case EnchantmentHelperVisitor.CLASS_NAME:
            {
                ClassWriter classWriter = new ClassWriter(0);
                new ClassReader(classBytes).accept(new EnchantmentHelperVisitor(classWriter), 0);
                return classWriter.toByteArray();
            }
            case ItemEnchantedBookVisitor.CLASS_NAME:
            {
                ClassWriter classWriter = new ClassWriter(0);
                new ClassReader(classBytes).accept(new ItemEnchantedBookVisitor(classWriter), 0);
                return classWriter.toByteArray();
            }
            case GenLayerVoronoiZoomVisitor.CLASS_NAME:
            {
                ClassWriter classWriter = new ClassWriter(0);
                new ClassReader(classBytes).accept(new GenLayerVoronoiZoomVisitor(classWriter), 0);
                return classWriter.toByteArray();
            }
            case ChunkPrimerVisitor.CLASS_NAME:
            {
                ClassWriter classWriter = new ClassWriter(0);
                new ClassReader(classBytes).accept(new ChunkPrimerVisitor(classWriter), 0);
                return classWriter.toByteArray();
            }
        }
        return classBytes;
    }

}
