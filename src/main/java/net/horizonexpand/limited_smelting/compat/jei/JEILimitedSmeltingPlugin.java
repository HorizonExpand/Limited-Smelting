package net.horizonexpand.limited_smelting.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.horizonexpand.limited_smelting.LimitedSmelting;
import net.horizonexpand.limited_smelting.compat.jei.category.FuelSmeltingCategory;
import net.horizonexpand.limited_smelting.recipe.FuelSmeltingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEILimitedSmeltingPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(LimitedSmelting.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FuelSmeltingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<FuelSmeltingRecipe> fuelSmeltingRecipes = recipeManager.getAllRecipesFor(FuelSmeltingRecipe.Type.INSTANCE);
        registration.addRecipes(FuelSmeltingCategory.FUEL_SMELTING_TYPE, fuelSmeltingRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AbstractFurnaceScreen.class, 60, 30, 20, 30, FuelSmeltingCategory.FUEL_SMELTING_TYPE);
    }
}
