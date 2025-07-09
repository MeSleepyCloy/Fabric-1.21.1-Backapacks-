package ncm.backpackpp.recipes;

import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ncm.backpackpp.Backpacks_pp;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final RecipeSerializer<LeatherworkingRecipe> LEATHERWORKING_SERIALIZER =
            new RecipeSerializer<>() {
                // Определяем Codec для сериализации/десериализации JSON
                private final MapCodec<LeatherworkingRecipe> codec = RecordCodecBuilder.mapCodec(
                        instance -> instance.group(
                                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("inputItem").forGetter(LeatherworkingRecipe::inputItem),
                                ItemStack.CODEC.fieldOf("output").forGetter(LeatherworkingRecipe::output)
                        ).apply(instance, LeatherworkingRecipe::new)
                );

                // Определяем PacketCodec для сетевой синхронизации
                private final PacketCodec<RegistryByteBuf, LeatherworkingRecipe> packetCodec = PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, LeatherworkingRecipe::inputItem,
                        ItemStack.PACKET_CODEC, LeatherworkingRecipe::output,
                        LeatherworkingRecipe::new
                );

                @Override
                public MapCodec<LeatherworkingRecipe> codec() {
                    return this.codec;
                }

                @Override
                public PacketCodec<RegistryByteBuf, LeatherworkingRecipe> packetCodec() {
                    return this.packetCodec;
                }
            };

    public static final RecipeType<LeatherworkingRecipe> LEATHERWORKING_RECIPE_TYPE =
            new RecipeType<>() {
                @Override
                public String toString() {
                    return "leatherworking";
                }
            };

    public static void registerRecipes() {
        Registry.register(Registries.RECIPE_SERIALIZER,
                Identifier.of(Backpacks_pp.MOD_ID, "leatherworking"),
                LEATHERWORKING_SERIALIZER);

        Registry.register(Registries.RECIPE_TYPE,
                Identifier.of(Backpacks_pp.MOD_ID, "leatherworking"),
                LEATHERWORKING_RECIPE_TYPE);
    }
}