package net.horizonexpand.limited_smelting.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public class FuelSmeltingRecipe extends SmeltingRecipe {
    private final Ingredient requiredFuel;

    public FuelSmeltingRecipe(ResourceLocation id, String group, CookingBookCategory category, Ingredient ingredient,
                              ItemStack result, float experience, int cookingTime, Ingredient requiredFuel) {
        super(id, group, category, ingredient, result, experience, cookingTime);
        this.requiredFuel = requiredFuel;
    }

    public Ingredient getRequiredFuel() {
        return requiredFuel;
    }
}