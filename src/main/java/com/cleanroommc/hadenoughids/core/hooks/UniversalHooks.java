package com.cleanroommc.hadenoughids.core.hooks;

import com.cleanroommc.hadenoughids.api.IItemMetadataExtension;
import net.minecraft.nbt.NBTTagCompound;

@SuppressWarnings("unused")
public class UniversalHooks {

    public static void warn(int steps) {
        throw new UnsupportedOperationException("");
    }

    @SuppressWarnings("all")
    public static int getCorrectItemMetadata(int damage, IItemMetadataExtension item) {
        return damage < item.getMinMetadata() ? item.getMinMetadata() : damage > item.getMaxMetadata() ? item.getMaxMetadata() : damage;
    }

    @SuppressWarnings("all")
    public static int getCautiousCorrectItemMetadata(int damage, IItemMetadataExtension item) {
        if (item == null) {
            return damage < 0 ? 0 : damage;
        }
        return damage < item.getMinMetadata() ? item.getMinMetadata() : damage > item.getMaxMetadata() ? item.getMaxMetadata() : damage;
    }

    @SuppressWarnings("all")
    public static int getCorrectItemMetadataFromNBT(IItemMetadataExtension item, NBTTagCompound nbt) {
        int damage = nbt.getInteger("Damage");
        return damage < item.getMinMetadata() ? item.getMinMetadata() : damage > item.getMaxMetadata() ? item.getMaxMetadata() : damage;
    }

}
