package com.cleanroommc.hadenoughids.test;

import com.cleanroommc.hadenoughids.api.IItemMetadataExtension;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import static com.cleanroommc.hadenoughids.HadEnoughIDs.MOD_ID;

public class TestItem extends Item implements IItemMetadataExtension {

    public static final TestItem INSTANCE = new TestItem();

    public static void addRecipes(IForgeRegistry<IRecipe> registry) {
        ShapelessRecipes shapeless = new TestRecipe();
        shapeless.setRegistryName(MOD_ID, "sixninesixninesix");
        registry.register(shapeless);
    }

    private TestItem() {
        setHasSubtypes(true);
        setCreativeTab(CreativeTabs.MISC);
        setRegistryName(MOD_ID, "test_meta_item");
        setTranslationKey("meta_test_item");
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (int i = getMinMetadata(); i <= getMaxMetadata(); i++) {
                if (i == OreDictionary.WILDCARD_VALUE) {
                    continue;
                }
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return "Hi I am MetaItem " + stack.getMetadata();
    }

    @Override
    public int getMinMetadata() {
        return -69;
    }

    @Override
    public int getMaxMetadata() {
        return 69696;
    }

    @Override
    public int getMinEmptyMetadata() {
        return getMinMetadata();
    }

    @Override
    public int getMaxEmptyMetadata() {
        return getMaxMetadata();
    }

    private static class TestRecipe extends ShapelessRecipes {

        private TestRecipe() {
            super("test",
                    new ItemStack(INSTANCE, 64, 69696),
                    NonNullList.from(
                            Ingredient.fromStacks(new ItemStack(INSTANCE, 1, -69)),
                            Ingredient.fromStacks(new ItemStack(INSTANCE, 1, 19696)),
                            Ingredient.fromStacks(new ItemStack(INSTANCE, 1, 50069))
                    ));
        }

    }

}
