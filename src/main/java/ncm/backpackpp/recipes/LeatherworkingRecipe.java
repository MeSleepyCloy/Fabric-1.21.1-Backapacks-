package ncm.backpackpp.recipes;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.chars.CharArraySet;
import it.unimi.dsi.fastutil.chars.CharSet;
import ncm.backpackpp.mixin.DataAccessor;
import ncm.backpackpp.mixin.DefaultedListAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.*;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public record Pattern(DefaultedList<Ingredient> ingredients, Optional<RawShapedRecipe.Data> data) {

        public static final MapCodec<Pattern> CODEC = DataAccessor.getCODEC().flatXmap(Pattern::fromData, result -> result.data().map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Cannot encode unpacked recipe")));
        public static final PacketCodec<RegistryByteBuf, Pattern> PACKET_CODEC = Ingredient.PACKET_CODEC.collect(PacketCodecs.toList())
                .xmap(ingredients -> new Pattern(DefaultedListAccessor.createDefaultedList(ingredients, Ingredient.EMPTY), Optional.empty()),
                        Pattern::ingredients);

        public static Pattern create(Map<Character, Ingredient> key, List<String> pattern) {
            return fromData(new RawShapedRecipe.Data(key, pattern)).getOrThrow();
        }

        private static DataResult<Pattern> fromData(RawShapedRecipe.Data data) {
            List<String> pattern = data.pattern();
            DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(3 * 3, Ingredient.EMPTY);
            CharSet charSet = new CharArraySet(data.key().keySet());
            charSet.remove(' ');

            for(int k = 0; k < pattern.size(); ++k) {
                String string = pattern.get(k);

                for(int l = 0; l < string.length(); ++l) {
                    char c = string.charAt(l);
                    Ingredient ingredient = c == ' ' ? Ingredient.EMPTY : data.key().get(c);
                    if (ingredient == null) return DataResult.error(() -> "Pattern references symbol '" + c + "' but it's not defined in the key");

                    charSet.remove(c);
                    ingredients.set(l + 3 * k, ingredient);
                }
            }

            if (charSet.isEmpty()) return DataResult.success(new Pattern(ingredients, Optional.of(data)));
            return DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + charSet);
        }
    }
}
