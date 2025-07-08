package ncm.backpackpp.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public class RecipeInventory<T extends Inventory> implements RecipeInput {
    private final T inventory;

    public RecipeInventory(T inventory) {
        this.inventory = inventory;
    }

    public static <T extends Inventory> RecipeInventory<T> of(T inventory) {
        return new RecipeInventory<>(inventory);
    }

    public T getInventory() {
        return inventory;
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStack(slot);
    }

    @Override
    public int getSize() {
        return inventory.size();
    }
}