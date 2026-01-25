package net.horizonexpand.limited_smelting.recipe;

import com.google.gson.JsonObject;
import java.io.PrintStream;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;

public class FuelSmeltingRecipeSerializer implements RecipeSerializer<FuelCookingRecipe> {
    private final RecipeType<?> recipeType;

    public FuelSmeltingRecipeSerializer(RecipeType<?> recipeType) {
        this.recipeType = recipeType;
    }

    public FuelCookingRecipe fromJson(ResourceLocation id, JsonObject json) {
        try {
            String group = GsonHelper.getAsString(json, "group", "");
            CookingBookCategory cookingbookcategory = CookingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CookingBookCategory.MISC);
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));

            ItemStack result;
            String result_id = GsonHelper.getAsString(json, "result");
            ResourceLocation resourcelocation = new ResourceLocation(result_id);
            result = new ItemStack(BuiltInRegistries.ITEM.getOptional(resourcelocation).orElseThrow(() -> new IllegalStateException("Item: " + result_id + " does not exist")));

            float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
            int cookingTime = GsonHelper.getAsInt(json, "cookingtime", 200);
            Ingredient requiredFuel = json.has("required_fuel") ? Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "required_fuel")) : Ingredient.EMPTY;
            PrintStream var11 = System.out;
            String var12 = String.valueOf(id);
            var11.println("Loaded recipe: " + var12 + " for type " + this.recipeType);
            return new FuelCookingRecipe(this.recipeType, id, group, cookingbookcategory, ingredient, result, experience, cookingTime, requiredFuel);
        } catch (Exception e) {
            PrintStream var10000 = System.err;
            String var10001 = String.valueOf(id);
            var10000.println("Error parsing recipe " + var10001 + ": " + e.getMessage());
            throw e;
        }
    }

    public FuelCookingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        String group = buffer.readUtf();
        CookingBookCategory cookingbookcategory = buffer.readEnum(CookingBookCategory.class);
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack result = buffer.readItem();
        float experience = buffer.readFloat();
        int cookingTime = buffer.readInt();
        Ingredient requiredFuel = Ingredient.fromNetwork(buffer);
        return new FuelCookingRecipe(this.recipeType, id, group, cookingbookcategory, ingredient, result, experience, cookingTime, requiredFuel);
    }

    public void toNetwork(FriendlyByteBuf buffer, FuelCookingRecipe recipe) {
        buffer.writeUtf(recipe.getGroup());
        buffer.writeEnum(recipe.category());
        recipe.getIngredients().get(0).toNetwork(buffer);
        buffer.writeItem(recipe.getResultItem(null));
        buffer.writeFloat(recipe.getExperience());
        buffer.writeVarInt(recipe.getCookingTime());
        recipe.getRequiredFuel().toNetwork(buffer);
    }
}
