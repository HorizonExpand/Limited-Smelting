package net.horizonexpand.limited_smelting.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

import net.horizonexpand.limited_smelting.LimitedSmelting;
import net.horizonexpand.limited_smelting.compat.jei.category.FuelBlastingCategory;
import net.horizonexpand.limited_smelting.compat.jei.category.FuelSmeltingCategory;
import net.horizonexpand.limited_smelting.compat.jei.category.FuelSmokingCategory;
import net.horizonexpand.limited_smelting.recipe.FuelCookingRecipe;

import net.minecraft.client.Minecraft;
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
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(new FuelSmeltingCategory(guiHelper));
        registration.addRecipeCategories(new FuelBlastingCategory(guiHelper));
        registration.addRecipeCategories(new FuelSmokingCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<FuelCookingRecipe> fuelSmeltingRecipes = NonNullList.create();
        List<FuelCookingRecipe> fuelBlastingRecipes = NonNullList.create();
        List<FuelCookingRecipe> fuelSmokingRecipes = NonNullList.create();

        List<SmeltingRecipe> smeltingRecipes = recipeManager.getAllRecipesFor(RecipeType.SMELTING);
        for (Recipe<?> recipe : smeltingRecipes) {
            if (recipe instanceof FuelCookingRecipe) {
                fuelSmeltingRecipes.add((FuelCookingRecipe) recipe);
            }
        }

        List<BlastingRecipe> blastingRecipes = recipeManager.getAllRecipesFor(RecipeType.BLASTING);
        for (Recipe<?> recipe : blastingRecipes) {
            if (recipe instanceof FuelCookingRecipe) {
                fuelBlastingRecipes.add((FuelCookingRecipe) recipe);
            }
        }

        List<SmokingRecipe> smokingRecipes = recipeManager.getAllRecipesFor(RecipeType.SMOKING);
        for (Recipe<?> recipe : smokingRecipes) {
            if (recipe instanceof FuelCookingRecipe) {
                fuelSmokingRecipes.add((FuelCookingRecipe) recipe);
            }
        }

        registration.addRecipes(FuelSmeltingCategory.FUEL_SMELTING_TYPE, fuelSmeltingRecipes);
        registration.addRecipes(FuelBlastingCategory.FUEL_BLASTING_TYPE, fuelBlastingRecipes);
        registration.addRecipes(FuelSmokingCategory.FUEL_SMOKING_TYPE, fuelSmokingRecipes);
    }
}