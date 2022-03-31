package com.cleanroommc.hadenoughids.core.visitors.hooks;

import com.cleanroommc.hadenoughids.api.IItemWithExtendedDamage;
import com.cleanroommc.moshimoshi.MoshiMoshi;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

@SuppressWarnings("unused")
public class UniversalHooks {

    public static void warn(int steps) {
        throw new UnsupportedOperationException("");
    }

    @SuppressWarnings("all")
    public static int getCorrectItemDamage(int damage, Item item) {
        if (item instanceof IItemWithExtendedDamage) {
            IItemWithExtendedDamage casted = (IItemWithExtendedDamage) item;
            return damage < casted.getMinDamage() ? casted.getMinDamage() : damage > casted.getMaxDamage() ? casted.getMaxDamage() : damage;
        }
        return damage < 0 ? 0 : damage;
    }

    @SuppressWarnings("all")
    public static int getCorrectItemDamageFromNBT(Item item, NBTTagCompound nbt) {
        if (item instanceof IItemWithExtendedDamage) {
            IItemWithExtendedDamage casted = (IItemWithExtendedDamage) item;
            int damage = nbt.getInteger("Damage");
            return damage < casted.getMinDamage() ? casted.getMinDamage() : damage > casted.getMaxDamage() ? casted.getMaxDamage() : damage;
        }
        return Math.max(0, nbt.getShort("Damage"));
    }

    public static boolean getDamageSignifyEmpty(int damage, Item item) {
        if (item instanceof IItemWithExtendedDamage) {
            IItemWithExtendedDamage casted = (IItemWithExtendedDamage) item;
            return damage < casted.getMinEmptyDamage() || damage > casted.getMaxEmptyDamage();
        }
        return damage < -32768 || damage > 65535;
    }

}
