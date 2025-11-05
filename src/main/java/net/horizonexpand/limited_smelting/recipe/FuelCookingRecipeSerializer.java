package net.horizonexpand.limited_smelting.recipe;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class FuelCookingRecipeSerializer implements RecipeSerializer<FuelSmeltingRecipe> {
    private final RecipeType<?> recipeType;

    public FuelCookingRecipeSerializer(RecipeType<?> recipeType) {
        this.recipeType = recipeType;
    }

    @Override
    public FuelSmeltingRecipe fromJson(ResourceLocation id, JsonObject json) {
        String group = GsonHelper.getAsString(json, "group", "");
        CookingBookCategory category = CookingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CookingBookCategory.MISC);
        Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
        int cookingTime = GsonHelper.getAsInt(json, "cookingtime", 200);
        Ingredient requiredFuel = json.has("required_fuel")
                ? Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "required_fuel"))
                : Ingredient.EMPTY;

        return new FuelSmeltingRecipe(this.recipeType, id, group, category, ingredient, result, experience, cookingTime, requiredFuel);
    }

    @Override
    public FuelSmeltingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        String group = buffer.readUtf();
        CookingBookCategory category = buffer.readEnum(CookingBookCategory.class);
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack result = buffer.readItem();
        float experience = buffer.readFloat();
        int cookingTime = buffer.readVarInt();
        Ingredient requiredFuel = Ingredient.fromNetwork(buffer);
        return new FuelSmeltingRecipe(this.recipeType, id, group, category, ingredient, result, experience, cookingTime, requiredFuel);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, FuelSmeltingRecipe recipe) {
        buffer.writeUtf(recipe.getGroup());
        buffer.writeEnum(recipe.category());
        recipe.getIngredients().get(0).toNetwork(buffer);
        buffer.writeItem(recipe.getResultItem(null));
        buffer.writeFloat(recipe.getExperience());
        buffer.writeVarInt(recipe.getCookingTime());
        recipe.getRequiredFuel().toNetwork(buffer);
    }
}