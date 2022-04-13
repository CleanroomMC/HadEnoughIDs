package com.cleanroommc.hadenoughids.core.mixins;

import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(WorldInfo.class)
public class WorldInfoMixin {

    @ModifyConstant(method = "updateTagCompound", constant = @Constant(stringValue = "1.12.2", ordinal = 0))
    private String versionName(String currentValue) {
        return "1.12.2 w/ HEID";
    }

}
