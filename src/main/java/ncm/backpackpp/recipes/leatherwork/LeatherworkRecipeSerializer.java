package ncm.backpackpp.recipes.leatherwork;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ncm.backpackpp.recipes.NcmRecipeSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public class LeatherworkRecipeSerializer extends NcmRecipeSerializer<LeatherworkRecipe> {

        public static final LeatherworkRecipeSerializer INSTANCE = new LeatherworkRecipeSerializer();

        private static final MapCodec<LeatherworkRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                LeatherworkRecipe.Pattern.CODEC.forGetter(LeatherworkRecipe::getPattern),
                ItemStack.CODEC.fieldOf("result").forGetter(LeatherworkRecipe::getOutput)
        ).apply(instance, LeatherworkRecipe::new));

        private static final PacketCodec<RegistryByteBuf, LeatherworkRecipe> PACKET_CODEC = PacketCodec.tuple(
                LeatherworkRecipe.Pattern.PACKET_CODEC, LeatherworkRecipe::getPattern,
                ItemStack.PACKET_CODEC, LeatherworkRecipe::getOutput, LeatherworkRecipe::new);

    public LeatherworkRecipeSerializer(String id) {
        super("leatherwork");
    }

    @Override
        public MapCodec<LeatherworkRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, LeatherworkRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }