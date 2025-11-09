package net.horizonexpand.limited_smelting.compat.jei.category;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;

import net.horizonexpand.limited_smelting.LimitedSmelting;
import net.horizonexpand.limited_smelting.recipe.FuelCookingRecipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

public class FuelBlastingCategory extends AbstractFuelCookingCategory<FuelCookingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(LimitedSmelting.MOD_ID, "fuel_blasting");
    public static final RecipeType<FuelCookingRecipe> FUEL_BLASTING_TYPE =
            new RecipeType<>(UID, FuelCookingRecipe.class);

    public FuelBlastingCategory(IGuiHelper guiHelper) {
        super(guiHelper, Blocks.BLAST_FURNACE, "gui.jei.category.fuel_blasting", 200);
    }

    @Override
    public RecipeType<FuelCookingRecipe> getRecipeType() {
        return FUEL_BLASTING_TYPE;
    }
}