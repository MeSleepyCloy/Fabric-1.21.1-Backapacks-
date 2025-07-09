package ncm.backpackpp.screen.leatherworkTable;

import ncm.backpackpp.init.BppItems;
import ncm.backpackpp.screen.BPScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class LeatherworkTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PlayerEntity player;

    public LeatherworkTableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(BPScreenHandlers.LEATHERWORK_TABLE_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        this.player = playerInventory.player;
        checkSize(inventory, 10);
        inventory.onOpen(playerInventory.player);

        // Слоты для ингредиентов (3x3 сетка)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlot(new Slot(this.inventory, row * 3 + col, 14 + col * 18, 17 + row * 18));
            }
        }

        this.addSlot(new Slot(this.inventory, 9, 106, 35) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                consumeIngredients();
                super.onTakeItem(player, stack);
            }
        });

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    private boolean matchesRecipe() {
        return inventory.getStack(0).isOf(Items.LEATHER)
                && inventory.getStack(1).isOf(Items.LEATHER)
                && inventory.getStack(2).isOf(Items.LEATHER)
                && inventory.getStack(3).isOf(Items.LEATHER)
                && inventory.getStack(4).isOf(Items.STRING)
                && inventory.getStack(5).isOf(Items.LEATHER)
                && inventory.getStack(6).isOf(Items.LEATHER)
                && inventory.getStack(7).isOf(Items.LEATHER)
                && inventory.getStack(8).isOf(Items.LEATHER);
    }

    private void consumeIngredients() {
        for (int i = 0; i < 9; i++) {
            inventory.getStack(i).decrement(1);
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        if (matchesRecipe()) {
            inventory.setStack(9, new ItemStack(BppItems.BACKPACK));
        } else {
            inventory.setStack(9, ItemStack.EMPTY);
        }
        super.onContentChanged(inventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            int inventorySize = this.inventory.size();

            if (index < inventorySize - 1) {
                if (!this.insertItem(originalStack, inventorySize, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index != inventorySize - 1) {
                if (!this.insertItem(originalStack, 0, inventorySize - 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}