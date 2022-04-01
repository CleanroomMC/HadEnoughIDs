package com.cleanroommc.hadenoughids.core.visitors.hooks;

import com.cleanroommc.hadenoughids.api.IItemWithExtendedMetadata;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

@SuppressWarnings("unused")
public class UniversalHooks {

    public static void warn(int steps) {
        throw new UnsupportedOperationException("");
    }

    @SuppressWarnings("all")
    public static int getCorrectItemMetadata(int damage, Item item) {
        if (item instanceof IItemWithExtendedMetadata) {
            IItemWithExtendedMetadata casted = (IItemWithExtendedMetadata) item;
            return damage < casted.getMinMetadata() ? casted.getMinMetadata() : damage > casted.getMaxMetadata() ? casted.getMaxMetadata() : damage;
        }
        return damage < 0 ? 0 : damage;
    }

    @SuppressWarnings("all")
    public static int getCorrectItemMetadataFromNBT(Item item, NBTTagCompound nbt) {
        if (item instanceof IItemWithExtendedMetadata) {
            IItemWithExtendedMetadata casted = (IItemWithExtendedMetadata) item;
            int damage = nbt.getInteger("Metadata");
            return damage < casted.getMinMetadata() ? casted.getMinMetadata() : damage > casted.getMaxMetadata() ? casted.getMaxMetadata() : damage;
        }
        return Math.max(0, nbt.getShort("Metadata"));
    }

    public static boolean getMetadataSignifyEmpty(int damage, Item item) {
        if (item instanceof IItemWithExtendedMetadata) {
            IItemWithExtendedMetadata casted = (IItemWithExtendedMetadata) item;
            return damage < casted.getMinEmptyMetadata() || damage > casted.getMaxEmptyMetadata();
        }
        return damage < -32768 || damage > 65535;
    }

}
