package ncm.backpackpp.init;

import ncm.backpackpp.Backpacks_pp;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModCreativeTabBP {
    public static final ItemGroup BACKPACKS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Backpacks_pp.MOD_ID, "backpacks"),
            FabricItemGroup.builder().icon(() -> new ItemStack(BppItems.BACKPACK))
                    .displayName(Text.translatable("Backpacks++"))
                    .entries((displayContext, entries) -> {
                        entries.add(BPBlocks.SMALL_CHEST);
                        entries.add(BppItems.BACKPACK);
                        entries.add(BppItems.GOLD_NEEDLE);
                        entries.add(BppItems.COPPER_NEEDLE);
                        entries.add(BppItems.IRON_NEEDLE);
                        entries.add(BppItems.DIAMOND_NEEDLE);
                        entries.add(BppItems.NETHERITE_NEEDLE);
                        entries.add(BPBlocks.LEATHERWORK_TABLE);
                    }).build());

    public static void registerBPItemGroups() {
    }
}