package ncm.backpackpp.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class TypedSlot<T extends Item> extends Slot {
    private final Class<T> cls;

    public TypedSlot(Inventory inventory, int index, int x, int y, Class<T> cls) {
        super(inventory, index, x, y);
        this.cls = cls;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        Class<?> itemCls = stack.getItem().getClass();
        return cls.isAssignableFrom(itemCls);
    }
}
