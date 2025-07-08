package ncm.backpackpp.util;

import ncm.backpackpp.block.entity.ImplementedInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface UpdatableInventory extends ImplementedInventory {
    void onTrackedSlotTake(PlayerEntity player, ItemStack stack, int index);
    void onTrackedUpdate(int index);
    void onSpecialEvent(int eventId, ItemStack stack);
}
