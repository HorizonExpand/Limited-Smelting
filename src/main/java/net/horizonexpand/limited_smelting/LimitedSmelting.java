package net.horizonexpand.limited_smelting;

import net.horizonexpand.limited_smelting.recipe.FuelSmeltingRecipeSerializer;

import net.minecraft.world.item.crafting.RecipeSerializer;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod("limited_smelting")
public class LimitedSmelting {
    public static final String MOD_ID = "limited_smelting";
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> FUEL_SMELTING =
            RECIPE_SERIALIZERS.register("fuel_smelting", FuelSmeltingRecipeSerializer::new);

    public LimitedSmelting() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        RECIPE_SERIALIZERS.register(bus);
    }
}