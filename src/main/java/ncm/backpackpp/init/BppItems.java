package ncm.backpackpp.init;

import ncm.backpackpp.Backpacks_pp;
import ncm.backpackpp.item.BackpackItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BppItems { ;
    public static final Item BACKPACK = registerItem("backpack", new BackpackItem(new Item.Settings()
            .maxCount(1)));
    public static final Item GOLD_NEEDLE = registerItem("gold_needle", new Item(new Item.Settings()
            .maxCount(1)
            .maxDamage(4)));
    public static final Item COPPER_NEEDLE = registerItem("copper_needle", new Item(new Item.Settings()
            .maxCount(1)
            .maxDamage(6)));
    public static final Item IRON_NEEDLE = registerItem("iron_needle", new Item(new Item.Settings()
            .maxCount(1)
            .maxDamage(8)));
    public static final Item DIAMOND_NEEDLE = registerItem("diamond_needle", new Item(new Item.Settings()
            .maxCount(1)
            .maxDamage(10)));
    public static final Item NETHERITE_NEEDLE = registerItem("netherite_needle", new Item(new Item.Settings()
            .maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Backpacks_pp.MOD_ID, name), item);
    }

    public static void registerModItems() {
    }
}