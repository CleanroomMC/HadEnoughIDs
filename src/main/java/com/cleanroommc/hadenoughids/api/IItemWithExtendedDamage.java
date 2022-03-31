package com.cleanroommc.hadenoughids.api;

/**
 * This interface should be implemented in your {@link net.minecraft.item.Item} extensions.
 *
 * This will be checked when serializing/saving/deserializing/loading from NBT tags
 *
 */
public interface IItemWithExtendedDamage {

    /**
     * @return what the minimum damage of the Item's stack could be. CAN be negative. HAS to be same or above {@link Integer#MIN_VALUE}
     */
    default int getMinDamage() {
        return 0;
    }

    /**
     * @return what the maximum damage of the Item's stack could be. CAN be negative. HAS to be below {@link Integer#MAX_VALUE}
     */
    default int getMaxDamage() {
        return Short.MAX_VALUE; // Default in vanilla, ItemStack serializes damage values to shorts in NBT
    }

}
