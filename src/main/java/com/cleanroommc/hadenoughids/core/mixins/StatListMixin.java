package com.cleanroommc.hadenoughids.core.mixins;

import com.cleanroommc.hadenoughids.util.UniqueStatList;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatCrafting;
import net.minecraft.stats.StatList;
import net.minecraft.util.text.TextComponentTranslation;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(StatList.class)
public abstract class StatListMixin {

    @Shadow @Final @Mutable public static List<StatCrafting> USE_ITEM_STATS = new UniqueStatList();
    @Shadow @Final @Mutable public static List<StatCrafting> MINE_BLOCK_STATS = new UniqueStatList();

    @Shadow @Final @Mutable private static StatBase[] BLOCKS_STATS = null;
    @Shadow @Final @Mutable private static StatBase[] CRAFTS_STATS = null;
    @Shadow @Final @Mutable private static StatBase[] OBJECT_USE_STATS = null;
    @Shadow @Final @Mutable private static StatBase[] OBJECT_BREAK_STATS = null;
    @Shadow @Final @Mutable private static StatBase[] OBJECTS_PICKED_UP_STATS = null;
    @Shadow @Final @Mutable private static StatBase[] OBJECTS_DROPPED_STATS = null;

    @Shadow private static String getItemName(Item itemIn) { throw new AssertionError(); }

    @Unique private static final UniqueStatList TYPED_USE_ITEM_STATS = (UniqueStatList) USE_ITEM_STATS;
    @Unique private static final UniqueStatList TYPED_MINE_BLOCK_STATS = (UniqueStatList) MINE_BLOCK_STATS;
    @Unique private static final Reference2ObjectMap<Item, StatBase> CLEANROOM$CRAFTS_STATS = new Reference2ObjectOpenHashMap<>();
    @Unique private static final Reference2ObjectMap<Item, StatBase> CLEANROOM$OBJECT_BREAK_STATS = new Reference2ObjectOpenHashMap<>();
    @Unique private static final Reference2ObjectMap<Item, StatBase> CLEANROOM$OBJECTS_PICKED_UP_STATS = new Reference2ObjectOpenHashMap<>();
    @Unique private static final Reference2ObjectMap<Item, StatBase> CLEANROOM$OBJECTS_DROPPED_STATS = new Reference2ObjectOpenHashMap<>();

    /**
     * @author Rongmario
     * @reason Rewrite getBlockStats behaviour
     */
    @Nullable
    @Overwrite
    public static StatBase getBlockStats(Block block) {
        if (block == null || block == Blocks.AIR || !block.getEnableStats()) {
            return null;
        }
        Item item = Item.getItemFromBlock(block);
        if (item == Items.AIR) {
            return null;
        }
        final Item correctItem = getSimilarItem(item);
        return TYPED_MINE_BLOCK_STATS.getOrPut(correctItem, () -> new StatCrafting("stat.mineBlock.", getItemName(correctItem), new TextComponentTranslation("stat.mineBlock",
                new ItemStack(correctItem).getTextComponent()), correctItem));
    }

    /**
     * @author Rongmario
     * @reason Rewrite getObjectUseStats behaviour
     */
    @Nullable
    @Overwrite
    public static StatBase getObjectUseStats(Item item) {
        if (item == Items.AIR || !(item instanceof ItemBlock)) {
            return null;
        }
        final Item correctItem = getSimilarItem(item);
        return TYPED_USE_ITEM_STATS.getOrPut(correctItem, () -> new StatCrafting("stat.useItem.", getItemName(correctItem), new TextComponentTranslation("stat.useItem",
                new ItemStack(correctItem).getTextComponent()), correctItem));
    }

    /**
     * @author Rongmario
     * @reason Rewrite getCraftStats behaviour
     */
    @Nullable
    @Overwrite
    public static StatBase getCraftStats(Item item) {
        if (item == Items.AIR) {
            return null;
        }
        final Item correctItem = getSimilarItem(item);
        StatBase stat = CLEANROOM$CRAFTS_STATS.get(correctItem);
        if (stat == null) {
            stat = new StatCrafting("stat.craftItem.", getItemName(correctItem), new TextComponentTranslation("stat.craftItem", new ItemStack(item).getTextComponent()), item);
            stat.registerStat();
            CLEANROOM$CRAFTS_STATS.put(correctItem, stat);
        }
        return stat;
    }

    /**
     * @author Rongmario
     * @reason Rewrite getObjectBreakStats behaviour
     */
    @Nullable
    @Overwrite
    public static StatBase getObjectBreakStats(Item item) {
        if (item == Items.AIR || !item.isDamageable()) {
            return null;
        }
        final Item correctItem = getSimilarItem(item);
        StatBase stat = CLEANROOM$OBJECT_BREAK_STATS.get(correctItem);
        if (stat == null) {
            stat = new StatCrafting("stat.breakItem.", getItemName(correctItem), new TextComponentTranslation("stat.breakItem", new ItemStack(item).getTextComponent()), item);
            stat.registerStat();
            CLEANROOM$OBJECT_BREAK_STATS.put(correctItem, stat);
        }
        return stat;
    }

    /**
     * @author Rongmario
     * @reason Rewrite getObjectsPickedUpStats behaviour
     */
    @Nullable
    @Overwrite
    public static StatBase getObjectsPickedUpStats(Item item) {
        if (item == Items.AIR) {
            return null;
        }
        final Item correctItem = getSimilarItem(item);
        StatBase stat = CLEANROOM$OBJECTS_PICKED_UP_STATS.get(correctItem);
        if (stat == null) {
            stat = new StatCrafting("stat.pickup.", getItemName(correctItem), new TextComponentTranslation("stat.pickup", new ItemStack(item).getTextComponent()), item);
            stat.registerStat();
            CLEANROOM$OBJECTS_PICKED_UP_STATS.put(correctItem, stat);
        }
        return stat;
    }

    /**
     * @author Rongmario
     * @reason Rewrite getDroppedObjectStats behaviour
     */
    @Nullable
    @Overwrite
    public static StatBase getDroppedObjectStats(Item item) {
        if (item == Items.AIR) {
            return null;
        }
        final Item correctItem = getSimilarItem(item);
        StatBase stat = CLEANROOM$OBJECTS_DROPPED_STATS.get(correctItem);
        if (stat == null) {
            stat = new StatCrafting("stat.drop.", getItemName(correctItem), new TextComponentTranslation("stat.drop", new ItemStack(item).getTextComponent()), item);
            stat.registerStat();
            CLEANROOM$OBJECTS_DROPPED_STATS.put(correctItem, stat);
        }
        return stat;
    }

    /**
     * @author Rongmario
     * @reason Stop initializing stats at the very start.
     */
    @Overwrite
    public static void init() {

    }

    /**
     * @author Rongmario
     * @reason Stop (re-)initializing stats at the very start.
     */
    @Overwrite(remap = false)
    public static void reinit() {

    }

    private static Item getSimilarItem(Item item) {
        if (!(item instanceof ItemBlock)) {
            return item;
        }
        Block block = ((ItemBlock) item).getBlock();
        if (block == Blocks.FLOWING_WATER || block == Blocks.WATER) {
            return Item.getItemFromBlock(Blocks.WATER);
        }
        if (block == Blocks.FLOWING_LAVA || block == Blocks.LAVA) {
            return Item.getItemFromBlock(Blocks.LAVA);
        }
        if (block == Blocks.LIT_PUMPKIN || block == Blocks.PUMPKIN) {
            return Item.getItemFromBlock(Blocks.PUMPKIN);
        }
        if (block == Blocks.LIT_FURNACE || block == Blocks.FURNACE) {
            return Item.getItemFromBlock(Blocks.FURNACE);
        }
        if (block == Blocks.LIT_REDSTONE_ORE || block == Blocks.REDSTONE_ORE) {
            return Item.getItemFromBlock(Blocks.REDSTONE_ORE);
        }
        if (block == Blocks.POWERED_REPEATER || block == Blocks.UNPOWERED_REPEATER) {
            return Item.getItemFromBlock(Blocks.UNPOWERED_REPEATER);
        }
        if (block == Blocks.POWERED_COMPARATOR || block == Blocks.UNPOWERED_COMPARATOR) {
            return Item.getItemFromBlock(Blocks.UNPOWERED_COMPARATOR);
        }
        if (block == Blocks.REDSTONE_TORCH || block == Blocks.UNLIT_REDSTONE_TORCH) {
            return Item.getItemFromBlock(Blocks.UNLIT_REDSTONE_TORCH);
        }
        if (block == Blocks.LIT_REDSTONE_LAMP || block == Blocks.REDSTONE_LAMP) {
            return Item.getItemFromBlock(Blocks.REDSTONE_LAMP);
        }
        if (block == Blocks.DOUBLE_STONE_SLAB || block == Blocks.STONE_SLAB) {
            return Item.getItemFromBlock(Blocks.STONE_SLAB);
        }
        if (block == Blocks.DOUBLE_WOODEN_SLAB || block == Blocks.WOODEN_SLAB) {
            return Item.getItemFromBlock(Blocks.WOODEN_SLAB);
        }
        if (block == Blocks.DOUBLE_STONE_SLAB2 || block == Blocks.STONE_SLAB2) {
            return Item.getItemFromBlock(Blocks.STONE_SLAB2);
        }
        if (block == Blocks.GRASS || block == Blocks.FARMLAND || block == Blocks.DIRT) {
            return Item.getItemFromBlock(Blocks.DIRT);
        }
        return item;
    }

}
