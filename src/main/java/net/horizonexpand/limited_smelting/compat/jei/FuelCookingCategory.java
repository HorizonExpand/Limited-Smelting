package net.horizonexpand.limited_smelting.compat.jei;

import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.horizonexpand.limited_smelting.LimitedSmelting;
import net.horizonexpand.limited_smelting.recipe.FuelCookingRecipe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.Nullable;

public class FuelCookingCategory implements IRecipeCategory<FuelCookingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(LimitedSmelting.MOD_ID, "fuel_smelting");
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModIds.JEI_ID, "textures/jei/gui/gui_vanilla.png");

    public static final RecipeType<FuelCookingRecipe> FUEL_SMELTING_TYPE =
            new RecipeType<>(UID, FuelCookingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public FuelCookingCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 114, 82, 54);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Blocks.FURNACE));

    }

    @Override
    public RecipeType<FuelCookingRecipe> getRecipeType() {
        return FUEL_SMELTING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.furnace");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FuelCookingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 37).addIngredients(recipe.getRequiredFuel());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 19).addItemStack(recipe.getResultItem(null));
    }

    public void draw(FuelCookingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        drawType(guiGraphics, recipe, -64, 0);
        drawExperience(guiGraphics, recipe, 0);
        drawCookTime(guiGraphics, recipe, 45);
    }

    public void renderItemWithScale(GuiGraphics guiGraphics, ItemStack stack, int x, int y, float scale) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.renderItem(stack, 0, 0);
        guiGraphics.pose().popPose();
    }

    protected void drawType(GuiGraphics guiGraphics, FuelCookingRecipe block, int x, int y) {
        ItemStack blockToastSymbol = block.getToastSymbol();
        renderItemWithScale(guiGraphics, blockToastSymbol, x, y, 2F);
    }

    protected void drawExperience(GuiGraphics guiGraphics, FuelCookingRecipe recipe, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            Component experienceString = Component.translatable("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(experienceString);
            guiGraphics.drawString(fontRenderer, experienceString, getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }

    protected void drawCookTime(GuiGraphics guiGraphics, FuelCookingRecipe recipe, int y) {
        float cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            float cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            guiGraphics.drawString(fontRenderer, timeString, getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }
}