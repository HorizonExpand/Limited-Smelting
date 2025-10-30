package net.horizonexpand.limited_smelting;

import net.horizonexpand.limited_smelting.recipe.LimitedSmeltingRecipe;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("limited_smelting")
public class LimitedSmelting {
    public static final String MOD_ID = "limited_smelting";

    public LimitedSmelting() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        LimitedSmeltingRecipe.register(bus);
    }
}