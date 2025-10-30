package net.horizonexpand.limited_smelting.recipe;

import net.horizonexpand.limited_smelting.LimitedSmelting;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LimitedSmeltingRecipe {
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, LimitedSmelting.MOD_ID);

    public static final RegistryObject<RecipeSerializer<FuelSmeltingRecipe>> FUEL_SMELTING_SERIALIZER =
            SERIALIZERS.register("fuel_smelting", () -> FuelSmeltingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
