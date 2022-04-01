package com.cleanroommc.hadenoughids.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * This interface should be implemented in your {@link Item} extensions.
 * Make sure {@link Item#setHasSubtypes(boolean)} is true for your Item when implementing this class.
 *
 * This will be checked when serializing/saving/deserializing/loading from NBT tags, and on {@link ItemStack#isEmpty()}
 *
 * NOTE: In wildcard situations (vanilla examples):
 * 1. {@link net.minecraftforge.oredict.OreDictionary#WILDCARD_VALUE}
 * 2. {@link net.minecraft.item.crafting.Ingredient#test(Object)}
 * 3. {@link net.minecraft.item.crafting.FurnaceRecipes#compareItemStacks(ItemStack, ItemStack)}
 * It will EXPLICITLY check for metadata == 32767, this means that any ItemStacks under the Item will pass the checks.
 * I recommend NOT utilizing 32767 meta for your Meta Items or damageable items. Try skipping over it, special-casing it or whatever.
 * I could patch this all, but it will definitely cause issues in contexts where there are other mods.
 */
public interface IItemWithExtendedMetadata {

    /**
     * @return what the minimum meta of the Item's stack could be. CAN be negative. HAS to be same or above {@link Integer#MIN_VALUE}
     */
    default int getMinMetadata() {
        return 0;
    }

    /**
     * @return what the maximum meta of the Item's stack could be. CAN be negative. HAS to be below {@link Integer#MAX_VALUE}
     */
    default int getMaxMetadata() {
        return Short.MAX_VALUE; // Default in vanilla, ItemStack serializes damage values to shorts in NBT
    }

    /**
     * @return what the minimum meta the Item's stack can have before isEmpty is true. CAN be negative.
     */
    default int getMinEmptyMetadata() {
        return -32768; // Default in vanilla
    }

    /**
     * @return what the maximum meta the Item's stack can have before isEmpty is true. CAN be negative.
     */
    default int getMaxEmptyMetadata() {
        return 65535; // Default in vanilla
    }

}
