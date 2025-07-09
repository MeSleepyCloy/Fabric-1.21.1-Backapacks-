package ncm.backpackpp.recipes;

import ncm.backpackpp.util.RecipeInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public interface NcmRecipe<T extends Inventory> extends NcmInputRecipe<RecipeInventory<T>> {
    @Override
    default boolean matches(RecipeInventory<T> input, World world) {
        return matches(input.getInventory(), world);
    }

    @Override
    default ItemStack craft(RecipeInventory<T> input, RegistryWrapper.WrapperLookup lookup) {
        return craft(input.getInventory(), lookup);
    }

    boolean matches(T inventory, World world);
    ItemStack craft(T inventory, RegistryWrapper.WrapperLookup lookup);
}
