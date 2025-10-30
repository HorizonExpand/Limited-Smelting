package net.horizonexpand.limited_smelting.recipe;

import com.google.gson.JsonObject;
import net.horizonexpand.limited_smelting.LimitedSmelting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class FuelSmeltingRecipe extends AbstractCookingRecipe {
    private final Ingredient requiredFuel;

    public FuelSmeltingRecipe(RecipeType<?> recipeType, ResourceLocation id, String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime, Ingredient requiredFuel) {
        super(recipeType, id, group, category, ingredient, result, experience, cookingTime);
        this.requiredFuel = requiredFuel != null ? requiredFuel : Ingredient.EMPTY;
    }

    public Ingredient getRequiredFuel() {
        return requiredFuel;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public ItemStack getToastSymbol() {
        if (this.type == RecipeType.SMELTING) {
            return new ItemStack(Blocks.FURNACE);
        } else if (this.type == RecipeType.BLASTING) {
            return new ItemStack(Blocks.BLAST_FURNACE);
        } else if (this.type == RecipeType.SMOKING) {
            return new ItemStack(Blocks.SMOKER);
        }
        return new ItemStack(Blocks.FURNACE);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<FuelSmeltingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "fuel_smelting";
    }

    public static class Serializer implements RecipeSerializer<FuelSmeltingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(LimitedSmelting.MOD_ID, "fuel_smelting");

        @Override
        public FuelSmeltingRecipe fromJson(ResourceLocation id, JsonObject json) {
            try {
                String group = GsonHelper.getAsString(json, "group", "");
                CookingBookCategory cookingbookcategory = CookingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CookingBookCategory.MISC);
                Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
                ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
                float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
                int cookingTime = GsonHelper.getAsInt(json, "cookingtime", 200);
                Ingredient requiredFuel = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "required_fuel"));

                System.out.println("Loaded recipe: " + id + " for type " + Type.INSTANCE);

                return new FuelSmeltingRecipe(Type.INSTANCE, id, group, cookingbookcategory, ingredient, result, experience, cookingTime, requiredFuel);

            } catch (Exception e) {
                System.err.println("Error parsing recipe " + id + ": " + e.getMessage());
                throw e;
            }
        }

        @Override
        public FuelSmeltingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            CookingBookCategory cookingbookcategory = (CookingBookCategory)buffer.readEnum(CookingBookCategory.class);
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            float experience = buffer.readFloat();
            int cookingTime = buffer.readVarInt();
            Ingredient requiredFuel = Ingredient.fromNetwork(buffer);
            return new FuelSmeltingRecipe(Type.INSTANCE, id, group, cookingbookcategory, ingredient, result, experience, cookingTime, requiredFuel);
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
}