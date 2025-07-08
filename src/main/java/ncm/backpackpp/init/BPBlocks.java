package ncm.backpackpp.init;

import ncm.backpackpp.Backpacks_pp;
import ncm.backpackpp.block.custom.LeatherworkTableBlock;
import ncm.backpackpp.block.custom.SmallChestBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class BPBlocks {
    public static final Block SMALL_CHEST = registerBlock("small_chest",
            new SmallChestBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
            ));

    public static final LeatherworkTableBlock LEATHERWORK_TABLE = (LeatherworkTableBlock) new LeatherworkTableBlock().registerAll();

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Backpacks_pp.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Backpacks_pp.MOD_ID, name), block);
    }

    public static void registerAll() {
    }
}
