package com.cleanroommc.hadenoughids.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * This interface should be implemented in your {@link Item} extensions.
 *
 * This will be checked when serializing/saving/deserializing/loading from NBT tags, and on {@link ItemStack#isEmpty()}
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

    /**
     * @return what the minimum damage the Item's stack can have before isEmpty is true. CAN be negative.
     */
    default int getMinEmptyDamage() {
        return -32768; // Default in vanilla
    }

    /**
     * @return what the maximum damage the Item's stack can have before isEmpty is true. CAN be negative.
     */
    default int getMaxEmptyDamage() {
        return 65535; // Default in vanilla
    }

}
