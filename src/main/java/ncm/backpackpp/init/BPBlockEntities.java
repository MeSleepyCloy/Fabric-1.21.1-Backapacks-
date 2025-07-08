package ncm.backpackpp.init;

import ncm.backpackpp.Backpacks_pp;
import ncm.backpackpp.block.entity.LeatherworkTableBlockEntity;
import ncm.backpackpp.block.entity.SmallChestBlockEntity;
import ncm.backpackpp.util.BpIndentifier;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static ncm.backpackpp.init.BPBlocks.LEATHERWORK_TABLE;

public class BPBlockEntities {
    public static final BlockEntityType<SmallChestBlockEntity> SMALL_CH =
            Registry.register(Registries.BLOCK_ENTITY_TYPE,
                    Identifier.of(Backpacks_pp.MOD_ID, "small_ch"),
                    BlockEntityType.Builder.create(SmallChestBlockEntity::new, BPBlocks.SMALL_CHEST).build(null));

    public static final BlockEntityType<LeatherworkTableBlockEntity> LEATHERWORK_TABLE_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            BpIndentifier.of("leatherwork_table_block_entity"),
            BlockEntityType.Builder.create(LeatherworkTableBlockEntity::new, LEATHERWORK_TABLE).build()
    );

    public static void registerBlockEntities() {
    }
}
