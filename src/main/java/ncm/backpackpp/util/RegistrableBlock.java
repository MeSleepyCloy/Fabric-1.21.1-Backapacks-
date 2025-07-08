package ncm.backpackpp.util;

import ncm.backpackpp.init.AutoBlockLootTable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.jetbrains.annotations.Nullable;

@Deprecated // use BPBlocks
public interface RegistrableBlock {
    String getBlockId();

    default Item getItem() {
        return Registries.ITEM.get(BpIndentifier.of(getBlockId()));
    }

    default Block registerAll() {
        return registerAll(true, null);
    }

    default Block registerAll(boolean generateDrop) {
        return registerAll(generateDrop, null);
    }

    default Block registerAll(boolean generateDrop, @Nullable ItemConvertible drop) {
        Block block = registerBlock();
        registerItem();
        if (generateDrop) AutoBlockLootTable.markAsAuto(block, drop);
        return block;
    }

    default Block registerBlock() {
        String blockId = getBlockId();
        return Registry.register(Registries.BLOCK, BpIndentifier.of(blockId), (Block) this);
    }

    default void registerItem() {
        Items.register((Block) this);
    }
}