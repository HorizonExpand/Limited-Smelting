package net.horizonexpand.limited_smelting.compat.jei.category;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.horizonexpand.limited_smelting.recipe.FuelCookingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public abstract class AbstractFuelCookingCategory<T extends FuelCookingRecipe> extends FurnaceVariantCategory<T> {
    private final IDrawable background;
    private final int regularCookTime;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public AbstractFuelCookingCategory(IGuiHelper guiHelper, Block icon, String translationKey, int regularCookTime) {
        super(guiHelper);
        this.background = guiHelper.createDrawable(TEXTURE, 0, 114, 82, 54);
        this.regularCookTime = regularCookTime;
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(icon));
        this.localizedName = Component.translatable(translationKey);
        this.cachedArrows = CacheBuilder.newBuilder()
            .maximumSize(25)
            .build(new CacheLoader<>() {
                @Override
                public IDrawableAnimated load(Integer cookTime) {
                    return guiHelper.drawableBuilder(TEXTURE, 82, 128, 24, 17)
                        .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
            }
        });
    }

    protected IDrawableAnimated getArrow(T recipe) {
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        return this.cachedArrows.getUnchecked(cookTime);
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        animatedFlame.draw(guiGraphics, 1, 20);

        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(guiGraphics, 24, 18);

        drawExperience(recipe, guiGraphics, 0);
        drawCookTime(recipe, guiGraphics, 45);
    }

    protected void drawExperience(T recipe, GuiGraphics guiGraphics, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            Component experienceString = Component.translatable("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(experienceString);
            guiGraphics.drawString(fontRenderer, experienceString, getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }

    protected void drawCookTime(T recipe, GuiGraphics guiGraphics, int y) {
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

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FuelCookingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 37).addIngredients(recipe.getRequiredFuel());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 19).addItemStack(recipe.getResultItem(null));
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }
}