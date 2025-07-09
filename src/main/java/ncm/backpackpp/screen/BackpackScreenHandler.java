package ncm.backpackpp.screen;

import ncm.backpackpp.item.BackpackItem;
import ncm.backpackpp.init.BppItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

import static ncm.backpackpp.item.BackpackItem.BACKPACK_SLOTS;

public class BackpackScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final ItemStack backpackStack;

    // Клиентский конструктор
    public BackpackScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, DefaultedList.ofSize(BACKPACK_SLOTS, ItemStack.EMPTY), findBackpackInInventory(playerInventory));
    }

    // Серверный конструктор
    public BackpackScreenHandler(int syncId, PlayerInventory playerInventory, DefaultedList<ItemStack> items, ItemStack backpackStack) {
        super(BPScreenHandlers.BACKPACK_SCREEN_HANDLER, syncId);
        this.inventory = new SimpleInventory(BACKPACK_SLOTS);
        this.backpackStack = backpackStack;

        // Заполняем инвентарь
        for (int i = 0; i < Math.min(items.size(), BACKPACK_SLOTS); i++) {
            this.inventory.setStack(i, items.get(i));
        }

        inventory.onOpen(playerInventory.player);

        // Слоты рюкзака (1 ряд)
        for (int j = 0; j < BACKPACK_SLOTS; ++j) {
            this.addSlot(new Slot(inventory, j, 8 + j * 18, 20) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return !stack.isOf(BppItems.BACKPACK);
                }
            });
        }

        // Инвентарь игрока (3 ряда)
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, j * 18 + 51));
            }
        }

        // Горячая панель (1 ряд)
        for (int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 109) {
                @Override
                public boolean canTakeItems(PlayerEntity player) {
                    return !isHeldBackpack(player, getStack());
                }
            });
        }
    }

    private boolean isHeldBackpack(PlayerEntity player, ItemStack stack) {
        return stack.isOf(BppItems.BACKPACK) &&
                (ItemStack.areEqual(stack, player.getMainHandStack()) ||
                        ItemStack.areEqual(stack, player.getOffHandStack()));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot != null && slot.hasStack()) {
            ItemStack stackInSlot = slot.getStack();
            originalStack = stackInSlot.copy();

            if (isHeldBackpack(player, stackInSlot)) {
                return ItemStack.EMPTY;
            }

            if (slotIndex < BACKPACK_SLOTS) {
                if (!this.insertItem(stackInSlot, this.slots.size() - 9, this.slots.size(), false)) {
                    if (!this.insertItem(stackInSlot, BACKPACK_SLOTS, this.slots.size() - 9, false));
                }

            } else if (!this.insertItem(stackInSlot, 0, BACKPACK_SLOTS, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return originalStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (!player.getWorld().isClient && backpackStack.getItem() instanceof BackpackItem) {
            ((BackpackItem)backpackStack.getItem())
                    .saveBackpackItems(backpackStack, getBackpackItems());
        }
        inventory.onClose(player);
    }

    public static BackpackScreenHandler create(int syncId, PlayerInventory playerInventory) {
        return new BackpackScreenHandler(syncId, playerInventory,
                DefaultedList.ofSize(BACKPACK_SLOTS, ItemStack.EMPTY),
                findBackpackInInventory(playerInventory));
    }

    private static ItemStack findBackpackInInventory(PlayerInventory inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem() instanceof BackpackItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private DefaultedList<ItemStack> getBackpackItems() {
        DefaultedList<ItemStack> items = DefaultedList.ofSize(BACKPACK_SLOTS, ItemStack.EMPTY);
        for (int i = 0; i < BACKPACK_SLOTS; i++) {
            items.set(i, inventory.getStack(i));
        }
        return items;
    }
}