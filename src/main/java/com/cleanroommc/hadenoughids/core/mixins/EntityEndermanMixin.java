package com.cleanroommc.hadenoughids.core.mixins;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(EntityEnderman.class)
public abstract class EntityEndermanMixin {
    @Shadow @Nullable public abstract IBlockState getHeldBlockState();

    /**
     * @reason Make Endermen always save their carried block as a string to avoid issues with IDs above 32767.
     */
    @Redirect(method = "writeEntityToNBT", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;setShort(Ljava/lang/String;S)V", ordinal = 0))
    private void redirectCarried(NBTTagCompound instance, String key, short value) {
        if(key.equals("carried")) {
            instance.setString("carried", this.getHeldBlockState().getBlock().getRegistryName().toString());
        } else
            instance.setShort(key, value);
    }
}
