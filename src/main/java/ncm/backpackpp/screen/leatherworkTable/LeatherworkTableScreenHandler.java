package ncm.backpackpp.screen.leatherworkTable;

import ncm.backpackpp.block.entity.LeatherworkTableBlockEntity;
import ncm.backpackpp.init.BppItems;
import ncm.backpackpp.item.NeedleItem;
import ncm.backpackpp.screen.BPScreenHandlers;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class LeatherworkTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PlayerEntity player;

    private static final Item[] NEEDLES = {
            BppItems.IRON_NEEDLE,
            BppItems.COPPER_NEEDLE,
            BppItems.GOLD_NEEDLE,
            BppItems.DIAMOND_NEEDLE,
            BppItems.NETHERITE_NEEDLE
    };

    public LeatherworkTableScreenHandler(int syncId, PlayerInventory inventory, BlockPos pos) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(pos), new ArrayPropertyDelegate(9));
    }

    public LeatherworkTableScreenHandler(int syncId, PlayerInventory playerInventory,
                                         BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {
        super(BPScreenHandlers.LEATHERWORK_TABLE_SCREEN_HANDLER, syncId);
        this.inventory = (Inventory) blockEntity;
        this.player = playerInventory.player;
        checkSize(this.inventory, 11);
        this.inventory.onOpen(playerInventory.player);

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlot(new CraftingSlot(this.inventory, row * 3 + col, 14 + col * 18, 17 + row * 18));
            }
        }

        this.addSlot(new ResultSlot(this.inventory, 9, 106, 35));
        this.addSlot(new NeedleSlot(this.inventory, 10, 153, 7));

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        addProperties(arrayPropertyDelegate);
    }

    private class NeedleSlot extends Slot {
        public NeedleSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return isNeedle(stack);
        }

        private boolean isNeedle(ItemStack stack) {
            for (Item needle : NEEDLES) {
                if (stack.isOf(needle)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void setStack(ItemStack stack) {
            super.setStack(stack);
            updateResult();
        }
    }

    private class CraftingSlot extends Slot {
        public CraftingSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public void setStack(ItemStack stack) {
            super.setStack(stack);
            updateResult();
        }
    }

    private class ResultSlot extends Slot {
        public ResultSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }

        @Override
        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            consumeIngredients();
            damageNeedle(player);
            updateResult();
            super.onTakeItem(player, stack);
        }

        private void damageNeedle(PlayerEntity player) {
            ItemStack needleStack = inventory.getStack(10);
            if (NeedleItem.isNeedle(needleStack)) {
                NeedleItem.damageNeedle(needleStack, player);
                if (needleStack.isEmpty()) {
                    inventory.setStack(10, ItemStack.EMPTY);
                }
            }
        }

        @Override
        public void setStack(ItemStack stack) {
            super.setStack(stack);
            if (stack.isEmpty()) {
                updateResult();
            }
        }
    }

    private void updateResult() {
        if (matchesRecipe()) {
            if (inventory.getStack(9).isEmpty()) {
                inventory.setStack(9, new ItemStack(BppItems.BACKPACK));
            }
        } else {
            if (!inventory.getStack(9).isEmpty()) {
                inventory.setStack(9, ItemStack.EMPTY);
            }
        }
    }

    private boolean matchesRecipe() {
        if (!NeedleItem.isNeedle(inventory.getStack(10))) {
            return false;
        }

        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStack(i);
            if (i == 4) {
                if (!stack.isOf(Items.STRING) || stack.getCount() < 1) return false;
            } else {
                if (!stack.isOf(Items.LEATHER) || stack.getCount() < 1) return false;
            }
        }
        return true;
    }

    private void consumeIngredients() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                stack.decrement(1);
                if (stack.getCount() <= 0) {
                    inventory.setStack(i, ItemStack.EMPTY);
                }
            }
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            if (slotIndex < 11) {
                if (!this.insertItem(originalStack, 11, 47, true)) {
                    return ItemStack.EMPTY;
                }

                if (slotIndex == 9) {
                    ItemStack needleStack = inventory.getStack(10);
                    if (NeedleItem.isNeedle(needleStack)) {
                        NeedleItem.damageNeedle(needleStack, player);
                        if (needleStack.isEmpty()) {
                            inventory.setStack(10, ItemStack.EMPTY);
                        }
                    }
                }
            }
            else {
                boolean isNeedle = NeedleItem.isNeedle(originalStack);

                if (isNeedle) {
                    if (!this.insertItem(originalStack, 10, 11, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.insertItem(originalStack, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, originalStack);
            updateResult();
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return BPScreenHandlers.LEATHERWORK_TABLE_SCREEN_HANDLER;
    }
}