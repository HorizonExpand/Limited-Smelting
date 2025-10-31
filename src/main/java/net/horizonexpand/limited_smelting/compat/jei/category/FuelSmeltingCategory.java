package net.horizonexpand.limited_smelting.compat.jei.category;

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
import net.horizonexpand.limited_smelting.recipe.FuelSmeltingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class FuelSmeltingCategory implements IRecipeCategory<FuelSmeltingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(LimitedSmelting.MOD_ID, "fuel_smelting");
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModIds.JEI_ID, "textures/jei/gui/gui_vanilla.png");

    public static final RecipeType<FuelSmeltingRecipe> FUEL_SMELTING_TYPE =
            new RecipeType<>(UID, FuelSmeltingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public FuelSmeltingCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 114, 82, 54);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Blocks.FURNACE));

    }

    @Override
    public RecipeType<FuelSmeltingRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, FuelSmeltingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 37).addIngredients(recipe.getRequiredFuel());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 19).addItemStack(recipe.getResultItem(null));
    }

    public void draw(FuelSmeltingRecipe block, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        drawType(guiGraphics, 4, 4, block);
    }

    protected void drawType(GuiGraphics guiGraphics, int x, int y, FuelSmeltingRecipe block) {
        ItemStack blockToastSymbol = block.getToastSymbol();
        Component typeString = Component.translatable("gui.jei.category.fuel_smelting.type" + blockToastSymbol);
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        int stringWidth = fontRenderer.width(typeString);

        guiGraphics.renderItem(blockToastSymbol, x, y);
        guiGraphics.drawString(fontRenderer, typeString, getWidth() - stringWidth, 0, 0xFF808080, false);
    }
}
