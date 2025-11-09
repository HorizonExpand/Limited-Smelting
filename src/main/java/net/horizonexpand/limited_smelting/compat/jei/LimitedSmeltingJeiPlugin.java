package net.horizonexpand.limited_smelting.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

import net.horizonexpand.limited_smelting.LimitedSmelting;
import net.horizonexpand.limited_smelting.recipe.FuelCookingRecipe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;

import java.util.List;

@JeiPlugin
public class LimitedSmeltingJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(LimitedSmelting.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FuelCookingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<FuelCookingRecipe> fuelCookingRecipes = NonNullList.create();

        List<SmeltingRecipe> smeltingRecipes = recipeManager.getAllRecipesFor(RecipeType.SMELTING);
        for (Recipe<?> recipe : smeltingRecipes) {
            if (recipe instanceof FuelCookingRecipe) {
                fuelCookingRecipes.add((FuelCookingRecipe) recipe);
            }
        }

        List<BlastingRecipe> blastingRecipes = recipeManager.getAllRecipesFor(RecipeType.BLASTING);
        for (Recipe<?> recipe : blastingRecipes) {
            if (recipe instanceof FuelCookingRecipe) {
                fuelCookingRecipes.add((FuelCookingRecipe) recipe);
            }
        }

        List<SmokingRecipe> smokingRecipes = recipeManager.getAllRecipesFor(RecipeType.SMOKING);
        for (Recipe<?> recipe : smokingRecipes) {
            if (recipe instanceof FuelCookingRecipe) {
                fuelCookingRecipes.add((FuelCookingRecipe) recipe);
            }
        }

        registration.addRecipes(FuelCookingCategory.FUEL_SMELTING_TYPE, fuelCookingRecipes);
    }
}