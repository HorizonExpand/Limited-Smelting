package net.horizonexpand.limited_smelting.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;

public class FuelCookingRecipe extends AbstractCookingRecipe {
    private final Ingredient requiredFuel;

    public FuelCookingRecipe(RecipeType<?> recipeType, ResourceLocation id, String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime, Ingredient requiredFuel) {
        super(recipeType, id, group, category, ingredient, result, experience, cookingTime);
        this.requiredFuel = requiredFuel != null ? requiredFuel : Ingredient.EMPTY;
    }

    public ItemStack getToastSymbol() {
        if (this.type == RecipeType.SMELTING) {
            return new ItemStack(Blocks.FURNACE);
        } else if (this.type == RecipeType.BLASTING) {
            return new ItemStack(Blocks.BLAST_FURNACE);
        } else if (this.type == RecipeType.SMOKING) {
            return new ItemStack(Blocks.SMOKER);
        }
        return new ItemStack(Blocks.FURNACE);
    }

    public Ingredient getRequiredFuel() {
        return this.requiredFuel;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public RecipeSerializer<?> getSerializer() {
        return FuelCookingRegister.FUEL_SMELTING_SERIALIZER.get();
    }
}