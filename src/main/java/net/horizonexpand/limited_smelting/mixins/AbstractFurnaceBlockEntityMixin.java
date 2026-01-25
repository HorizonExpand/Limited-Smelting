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

        Optional<FuelCookingRecipe> smeltingRecipe = level.getRecipeManager()
                .getRecipeFor(FuelCookingRecipe.Type.FUEL_SMELTING, furnace, level);
        Optional<FuelCookingRecipe> blastingRecipe = level.getRecipeManager()
                .getRecipeFor(FuelCookingRecipe.Type.FUEL_BLASTING, furnace, level);
        Optional<FuelCookingRecipe> smokingRecipe = level.getRecipeManager()
                .getRecipeFor(FuelCookingRecipe.Type.FUEL_SMOKING, furnace, level);

        if (smeltingRecipe.isEmpty() && blastingRecipe.isEmpty() && smokingRecipe.isEmpty()) {
            return;
        }

        FuelCookingRecipe fuelSmeltingRecipe = smeltingRecipe.get();
        FuelCookingRecipe fuelBlastingRecipe = blastingRecipe.get();
        FuelCookingRecipe fuelSmokingRecipe = smokingRecipe.get();

        ItemStack fuelStack = inventory.get(1);
        ItemStack resultSmelting = fuelSmeltingRecipe.getResultItem(level.registryAccess());
        ItemStack resultBlasting = fuelBlastingRecipe.getResultItem(level.registryAccess());
        ItemStack resultSmoking = fuelSmokingRecipe.getResultItem(level.registryAccess());

        if (!fuelStack.isEmpty() && !fuelSmeltingRecipe.getRequiredFuel().test(fuelStack) && !fuelBlastingRecipe.getRequiredFuel().test(fuelStack) && !fuelSmokingRecipe.getRequiredFuel().test(fuelStack)) {
            cir.setReturnValue(false);
            return;
        }

        if (recipe.equals(fuelSmeltingRecipe)) {
            ItemStack outputSlot = inventory.get(2);
            if (outputSlot.isEmpty()
                    || (ItemStack.isSameItemSameTags(outputSlot, resultSmelting)
                    && outputSlot.getCount() + resultSmelting.getCount() <= outputSlot.getMaxStackSize())) {

                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
        }
        else if (recipe.equals(fuelBlastingRecipe)) {
            ItemStack outputSlot = inventory.get(2);
            if (outputSlot.isEmpty()
                    || (ItemStack.isSameItemSameTags(outputSlot, resultBlasting)
                    && outputSlot.getCount() + resultBlasting.getCount() <= outputSlot.getMaxStackSize())) {

                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
        }
        else if (recipe.equals(fuelSmokingRecipe)) {
            ItemStack outputSlot = inventory.get(2);
            if (outputSlot.isEmpty()
                    || (ItemStack.isSameItemSameTags(outputSlot, resultSmoking)
                    && outputSlot.getCount() + resultSmoking.getCount() <= outputSlot.getMaxStackSize())) {

                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "burn", at = @At("HEAD"), cancellable = true)
    private void onBurn(RegistryAccess registryAccess, Recipe<?> recipe, NonNullList<ItemStack> inventory, int itemsCount, CallbackInfoReturnable<Boolean> cir) {
        AbstractFurnaceBlockEntity furnace = (AbstractFurnaceBlockEntity)(Object)this;
        Level level = furnace.getLevel();
        if (level == null) return;

        Optional<FuelCookingRecipe> smeltingRecipe = level.getRecipeManager()
                .getRecipeFor(FuelCookingRecipe.Type.FUEL_SMELTING, furnace, level);
        Optional<FuelCookingRecipe> blastingRecipe =level.getRecipeManager()
                .getRecipeFor(FuelCookingRecipe.Type.FUEL_BLASTING, furnace, level);
        Optional<FuelCookingRecipe> smokingRecipe = level.getRecipeManager()
                .getRecipeFor(FuelCookingRecipe.Type.FUEL_SMOKING, furnace, level);

        if (smeltingRecipe.isEmpty() && blastingRecipe.isEmpty() && smokingRecipe.isEmpty()) {
            return;
        }

        FuelCookingRecipe fuelSmeltingRecipe = smeltingRecipe.get();
        FuelCookingRecipe fuelBlastingRecipe = blastingRecipe.get();
        FuelCookingRecipe fuelSmokingRecipe = smokingRecipe.get();

        ItemStack input = inventory.get(0);
        ItemStack outputSlot = inventory.get(2);
        ItemStack resultSmelting = fuelSmeltingRecipe.getResultItem(level.registryAccess());
        ItemStack resultBlasting = fuelBlastingRecipe.getResultItem(level.registryAccess());
        ItemStack resultSmoking = fuelSmokingRecipe.getResultItem(level.registryAccess());

        if (!canBurn(registryAccess, fuelSmeltingRecipe, inventory, itemsCount) && !canBurn(registryAccess, fuelBlastingRecipe, inventory, itemsCount) && !canBurn(registryAccess, fuelSmokingRecipe, inventory, itemsCount)) {
            cir.setReturnValue(false);
            return;
        }

        input.shrink(1);

        if (recipe.equals(fuelSmeltingRecipe)) {
            if (outputSlot.isEmpty()) {
                inventory.set(2, resultSmelting.copy());
            } else {
                outputSlot.grow(resultSmelting.getCount());
            }

            furnace.setRecipeUsed(fuelSmeltingRecipe);

            cir.setReturnValue(true);
        }
        else if (recipe.equals(fuelBlastingRecipe)) {
            if (outputSlot.isEmpty()) {
                inventory.set(2, resultBlasting.copy());
            } else {
                outputSlot.grow(resultBlasting.getCount());
            }

            furnace.setRecipeUsed(fuelBlastingRecipe);

            cir.setReturnValue(true);
        }
        else if (recipe.equals(fuelSmokingRecipe)) {
            if (outputSlot.isEmpty()) {
                inventory.set(2, resultSmoking.copy());
            } else {
                outputSlot.grow(resultSmoking.getCount());
            }

            furnace.setRecipeUsed(fuelSmokingRecipe);

            cir.setReturnValue(true);
        }
    }
}