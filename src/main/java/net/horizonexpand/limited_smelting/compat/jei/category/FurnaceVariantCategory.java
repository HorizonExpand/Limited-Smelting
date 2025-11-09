package net.horizonexpand.limited_smelting.compat.jei.category;

import mezz.jei.api.constants.ModIds;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;

public abstract class FurnaceVariantCategory<T> implements IRecipeCategory<T> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModIds.JEI_ID, "textures/jei/gui/gui_vanilla.png");
    protected final IDrawableStatic staticFlame;
    protected final IDrawableAnimated animatedFlame;

    public FurnaceVariantCategory(IGuiHelper guiHelper) {
        staticFlame = guiHelper.createDrawable(TEXTURE, 82, 114, 14, 14);
        animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
    }
}