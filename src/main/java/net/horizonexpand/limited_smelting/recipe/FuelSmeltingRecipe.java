package net.horizonexpand.limited_smelting.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class FuelSmeltingRecipe extends AbstractCookingRecipe {
    private final Ingredient requiredFuel;

    public FuelSmeltingRecipe(RecipeType<?> recipeType, ResourceLocation id, String group, CookingBookCategory category,
                              Ingredient ingredient, ItemStack result, float experience, int cookingTime, Ingredient requiredFuel) {
        super(recipeType, id, group, category, ingredient, result, experience, cookingTime);
        this.requiredFuel = requiredFuel != null ? requiredFuel : Ingredient.EMPTY;
    }

    public Ingredient getRequiredFuel() {
        return requiredFuel;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override public RecipeSerializer<?> getSerializer() { return FuelCookingReg.FUEL_SMELTING_SERIALIZER.get(); }
    @Override public RecipeType<?> getType() { return Type.INSTANCE; }

    public static class Type implements RecipeType<FuelSmeltingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "fuel_smelting";
    }
}