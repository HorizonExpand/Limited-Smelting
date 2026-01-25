package net.horizonexpand.limited_smelting.recipe;

import com.google.gson.JsonObject;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Blocks;

public class FuelCookingRecipe extends AbstractCookingRecipe {
    private final Ingredient requiredFuel;

    public FuelCookingRecipe(RecipeType<?> recipeType, ResourceLocation id, String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime, Ingredient requiredFuel) {
        super(recipeType, id, group, category, ingredient, result, experience, cookingTime);
        this.requiredFuel = requiredFuel != null ? requiredFuel : Ingredient.EMPTY;
    }

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

    public Ingredient getRequiredFuel() {
        return this.requiredFuel;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    @Override public RecipeSerializer<?> getSerializer() {
        return FuelCookingRegister.FUEL_SMELTING_SERIALIZER.get();
    }

    @Override public RecipeType<?> getType() {
        return Type.FUEL_SMELTING;
    }

    public static class Type implements RecipeType<FuelCookingRecipe> {
        public static final Type FUEL_SMELTING = new Type();
        public static final String ID = "fuel_smelting";

        public static final Type FUEL_BLASTING = new Type();
        public static final Type FUEL_SMOKING = new Type();
    }

    public static class Serializer implements RecipeSerializer<FuelCookingRecipe> {
//        public static final Serializer FUEL_COOKING = new Serializer();
//        public static final String ID = "fuel_cooking";
        private final RecipeType<?> recipeType;

        public Serializer(RecipeType<?> recipeType) {
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

                String recipeID = String.valueOf(id);
                System.out.println("Loaded recipe: " + recipeID + " for type " + Type.FUEL_SMELTING);

                if (recipeType.equals(RecipeType.SMELTING)) {
                    return new FuelCookingRecipe(Type.FUEL_SMELTING, id, group, cookingbookcategory, ingredient, result, experience, cookingTime, requiredFuel);
                }
                else if (recipeType.equals(RecipeType.BLASTING)) {
                    return new FuelCookingRecipe(Type.FUEL_BLASTING, id, group, cookingbookcategory, ingredient, result, experience, cookingTime, requiredFuel);
                }
                else if (recipeType.equals(RecipeType.SMOKING)) {
                    return new FuelCookingRecipe(Type.FUEL_SMOKING, id, group, cookingbookcategory, ingredient, result, experience, cookingTime, requiredFuel);
                }
                else {
                    return null;
                }

            } catch (Exception e) {
                String recipeID = String.valueOf(id);
                System.err.println("Error parsing recipe " + recipeID + ": " + e.getMessage());
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

            if (recipeType.equals(RecipeType.SMELTING)) {
                return new FuelCookingRecipe(Type.FUEL_SMELTING, id, group, cookingbookcategory, ingredient, result, experience, cookingTime, requiredFuel);
            }
            else if (recipeType.equals(RecipeType.BLASTING)) {
                return new FuelCookingRecipe(Type.FUEL_BLASTING, id, group, cookingbookcategory, ingredient, result, experience, cookingTime, requiredFuel);
            }
            else if (recipeType.equals(RecipeType.SMOKING)) {
                return new FuelCookingRecipe(Type.FUEL_SMOKING, id, group, cookingbookcategory, ingredient, result, experience, cookingTime, requiredFuel);
            }
            else {
                return null;
            }
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
}