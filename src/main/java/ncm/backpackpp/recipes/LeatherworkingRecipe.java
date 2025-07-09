package ncm.backpackpp.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public record LeatherworkingRecipe (Ingredient inputItem, ItemStack output) implements Recipe<LeatherworkingRecipeInput> {
    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem);
        return list;
    }

    // read Recipe JSON files --> new GrowthChamberRecipe

    @Override
    public boolean matches(LeatherworkingRecipeInput input, World world) {
        if(world.isClient()) {
            return false;
        }

        return inputItem.test(input.getStackInSlot(0));
    }

    @Override
    public ItemStack craft(LeatherworkingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.LEATHERWORKING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.LEATHERWORKING_RECIPE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<LeatherworkingRecipe> {
        public static final MapCodec<LeatherworkingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(LeatherworkingRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(LeatherworkingRecipe::output)
        ).apply(inst, LeatherworkingRecipe::new));

        public static final PacketCodec<RegistryByteBuf, LeatherworkingRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, LeatherworkingRecipe::inputItem,
                        ItemStack.PACKET_CODEC, LeatherworkingRecipe::output,
                        LeatherworkingRecipe::new);

        @Override
        public MapCodec<LeatherworkingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, LeatherworkingRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}
