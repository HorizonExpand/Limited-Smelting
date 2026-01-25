package net.horizonexpand.limited_smelting.recipe;

import net.horizonexpand.limited_smelting.LimitedSmelting;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FuelCookingRegister {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, LimitedSmelting.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> FUEL_SMELTING_SERIALIZER =
            RECIPE_SERIALIZERS.register("fuel_smelting", () -> new FuelSmeltingRecipeSerializer(RecipeType.SMELTING));
    public static final RegistryObject<RecipeSerializer<?>> FUEL_BLASTING_SERIALIZER =
            RECIPE_SERIALIZERS.register("fuel_blasting", () -> new FuelSmeltingRecipeSerializer(RecipeType.BLASTING));
    public static final RegistryObject<RecipeSerializer<?>> FUEL_SMOKING_SERIALIZER =
            RECIPE_SERIALIZERS.register("fuel_smoking", () -> new FuelSmeltingRecipeSerializer(RecipeType.SMOKING));

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }
}