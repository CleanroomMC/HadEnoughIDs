package com.cleanroommc.hadenoughids.core;

import com.cleanroommc.hadenoughids.core.visitors.GameDataVisitor;
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
        }
        return classBytes;
    }

}
