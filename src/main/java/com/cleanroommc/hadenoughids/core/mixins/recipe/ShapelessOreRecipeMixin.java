package com.cleanroommc.hadenoughids.core.mixins.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(ShapelessOreRecipe.class)
public class ShapelessOreRecipeMixin {

    @Shadow(remap = false) protected boolean isSimple;
    @Shadow(remap = false) protected NonNullList<Ingredient> input;

    /**
     * @author Rongmario
     * @reason Stop the use of overly-convoluted RecipeItemHelper, as it glitches out our Item ID/ItemStack meta expansion strategies.
     * @see Ingredient#getValidItemStacksPacked() <= nuisance
     */
    @Overwrite
    public boolean matches(InventoryCrafting inv, World world) {
        if (this.isSimple) {
            List<Ingredient> ingredients = null;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (!stack.isEmpty() && !stack.isItemDamaged() && !stack.isItemEnchanted() && !stack.hasDisplayName()) {
                    if (ingredients == null) {
                        ingredients = new ArrayList<>(input);
                    }
                    for (int j = 0; j < input.size(); j++) {
                        Ingredient ingredient = input.get(j);
                        if (ingredient.test(stack)) {
                            if (ingredients.get(j) == null) {
                                return false;
                            }
                            ingredients.set(j, null);
                            break;
                        }
                    }
                }
            }
            if (ingredients != null) {
                for (Ingredient ingredient : ingredients) {
                    if (ingredient != null) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        } else {
            List<ItemStack> inputs = new ArrayList<>();
            for (int i = 0; i < inv.getHeight(); i++) {
                for (int j = 0; j < inv.getWidth(); j++) {
                    ItemStack stack = inv.getStackInRowAndColumn(j, i);
                    if (!stack.isEmpty()) {
                        inputs.add(stack);
                    }
                }
            }
            return inputs.size() == this.input.size() && RecipeMatcher.findMatches(inputs, this.input) != null;
        }
    }

}
