package com.cleanroommc.hadenoughids.core.mixins;

import net.minecraftforge.registries.ForgeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ForgeRegistry.class, remap = false)
public interface ForgeRegistryAccessor {

    @Accessor
    int getMax();

}
