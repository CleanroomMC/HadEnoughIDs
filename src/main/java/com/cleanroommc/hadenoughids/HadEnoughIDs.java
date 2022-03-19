package com.cleanroommc.hadenoughids;

import com.cleanroommc.hadenoughids.core.mixins.ForgeRegistryAccessor;
import com.cleanroommc.hadenoughids.core.mixins.RegistryManagerAccessor;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

@Mod(modid = "hadenoughids", name = "HadEnoughIDs", version = "1.0")
public class HadEnoughIDs {

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        IForgeRegistry<Block> blockRegistry = GameRegistry.findRegistry(Block.class);
        IForgeRegistry<Item> itemRegistry = GameRegistry.findRegistry(Item.class);
        for (int i = 0; i < 5000; i++) {
            Block block = new BlockFalling(Material.GROUND)
                    .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
                    .setRegistryName(new ResourceLocation("hadenoughids:block_" + i));
            blockRegistry.register(block);
            itemRegistry.register(new ItemBlock(block).setRegistryName(new ResourceLocation("hadenoughids:block_" + i)));
        }
    }

    @EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        if (FMLLaunchHandler.isDeobfuscatedEnvironment()) {
            HadEnoughIDsLogger.INSTANCE.warn("There are {} Blocks in Vanilla.", ForgeRegistries.BLOCKS.getValuesCollection().size());
            int statesCount = 0;
            for (Block block : ForgeRegistries.BLOCKS.getValuesCollection()) {
                IntSet set = new IntOpenHashSet(16);
                for (IBlockState state : block.getBlockState().getValidStates()) {
                    set.add(block.getMetaFromState(state));
                }
                statesCount += set.size();
            }
            HadEnoughIDsLogger.INSTANCE.warn("There are {} BlockStates in Vanilla.", statesCount);
            ((RegistryManagerAccessor) RegistryManager.ACTIVE).getRegistries().forEach((loc, registry) ->
                    HadEnoughIDsLogger.INSTANCE.warn("{}'s max ID: {}", loc, ((ForgeRegistryAccessor) registry).getMax()));
        }
    }


}
