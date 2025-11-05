package net.horizonexpand.limited_smelting.mixins;

import net.horizonexpand.limited_smelting.recipe.FuelSmeltingRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {
    @Unique
    private ItemStack lastFuel = ItemStack.EMPTY;

    @Final
    @Shadow private RecipeType<? extends Recipe<Container>> recipeType;

    @Shadow
    int cookingProgress;

    @Inject(method = "serverTick", at = @At("HEAD"), cancellable = true)
    private static void onServerTick(Level level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity block, CallbackInfo ci) {
        AbstractFurnaceBlockEntityMixin mixin = (AbstractFurnaceBlockEntityMixin) (Object) block;
        assert mixin != null;
        Recipe<?> recipe = level.getRecipeManager().getRecipeFor(mixin.recipeType, block, level).orElse(null);
        if (recipe instanceof FuelSmeltingRecipe fuelCooking) {
            ItemStack fuel = block.getItem(1);
            Ingredient requiredFuel = fuelCooking.getRequiredFuel();

            if (!fuel.isEmpty()) {
                mixin.lastFuel = fuel.copy();
            }

            if (!requiredFuel.isEmpty()) {
                boolean isValidFuel = false;
                for (ItemStack validFuel : requiredFuel.getItems()) {
                    if (mixin.lastFuel.is(validFuel.getItem())) {
                        isValidFuel = true;
                        break;
                    }
                }
                if (!isValidFuel) {
                    mixin.cookingProgress = 0;
                    ci.cancel();
                }
            }
        }
    }
}