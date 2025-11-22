package net.horizonexpand.limited_smelting.mixins;

import net.horizonexpand.limited_smelting.recipe.FuelCookingRecipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import org.jetbrains.annotations.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {
    @Shadow @Final
    private RecipeType<? extends AbstractCookingRecipe> recipeType;

    @Shadow protected abstract boolean canBurn(RegistryAccess registryAccess, @Nullable Recipe<?> recipe, NonNullList<ItemStack> inventory, int itemsCount);

    @Inject(method = "canBurn", at = @At("HEAD"), cancellable = true)
    private void canBurnWithFuelRequirement(RegistryAccess registryAccess, Recipe<?> recipe, NonNullList<ItemStack> inventory, int itemsCount, CallbackInfoReturnable<Boolean> cir) {
        AbstractFurnaceBlockEntity furnace = (AbstractFurnaceBlockEntity)(Object)this;
        Level level = furnace.getLevel();
        if (level == null) return;

        Optional<FuelCookingRecipe> cookingRecipe = level.getRecipeManager()
                .getRecipeFor(FuelCookingRecipe.Type.FUEL_COOKING, furnace, level);

        if (cookingRecipe.isEmpty()) {
            return;
        }

        FuelCookingRecipe fuelCookingRecipe = cookingRecipe.get();
        ItemStack fuelStack = inventory.get(1);
        ItemStack result = fuelCookingRecipe.getResultItem(level.registryAccess());

        if (!fuelStack.isEmpty() && !fuelCookingRecipe.getRequiredFuel().test(fuelStack)) {
            cir.setReturnValue(false);
            return;
        }

        ItemStack outputSlot = inventory.get(2);
        if (outputSlot.isEmpty()
                || (ItemStack.isSameItemSameTags(outputSlot, result)
                && outputSlot.getCount() + result.getCount() <= outputSlot.getMaxStackSize())) {
            
            cir.setReturnValue(true);
        } else {
            cir.setReturnValue(false);
        }


    }

    @Inject(method = "burn", at = @At("HEAD"), cancellable = true)
    private void onBurn(RegistryAccess registryAccess, Recipe<?> recipe, NonNullList<ItemStack> inventory, int itemsCount, CallbackInfoReturnable<Boolean> cir) {
        AbstractFurnaceBlockEntity furnace = (AbstractFurnaceBlockEntity)(Object)this;
        Level level = furnace.getLevel();
        if (level == null) return;

        Optional<FuelCookingRecipe> cookingRecipe = level.getRecipeManager()
                .getRecipeFor(FuelCookingRecipe.Type.FUEL_COOKING, furnace, level);

        if (cookingRecipe.isEmpty()) {
            return;
        }

        FuelCookingRecipe fuelCookingRecipe = cookingRecipe.get();

        ItemStack input = inventory.get(0);
        ItemStack outputSlot = inventory.get(2);
        ItemStack result = fuelCookingRecipe.getResultItem(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));

        if (!canBurn(registryAccess, fuelCookingRecipe, inventory, itemsCount)) {
            cir.setReturnValue(false);
            return;
        }

        input.shrink(1);

        if (outputSlot.isEmpty()) {
            inventory.set(2, result.copy());
        } else {
            outputSlot.grow(result.getCount());
        }

        furnace.setRecipeUsed(fuelCookingRecipe);

        cir.setReturnValue(true);
    }
}