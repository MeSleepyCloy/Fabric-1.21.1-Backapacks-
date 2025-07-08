package ncm.backpackpp.item;

import ncm.backpackpp.Backpacks_pp;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import ncm.backpackpp.screen.BackpackScreenHandler;


public class BackpackItem extends Item { ;
    private static final Text TITLE = Text.translatable("item.backpacks_pp.backpack");
    public static final int BACKPACK_SLOTS = 9;

    public BackpackItem(Settings settings) {
        super(settings);
    }

    public void saveBackpackItems(ItemStack backpackStack, DefaultedList<ItemStack> items) {
        backpackStack.set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(items));
    }

    public DefaultedList<ItemStack> getBackpackItems(ItemStack backpackStack) {
        ContainerComponent container = backpackStack.get(DataComponentTypes.CONTAINER);
        if (container != null) {
            DefaultedList<ItemStack> items = DefaultedList.ofSize(BACKPACK_SLOTS, ItemStack.EMPTY);
            container.copyTo(items);
            return items;
        }
        return DefaultedList.ofSize(BACKPACK_SLOTS, ItemStack.EMPTY);
    }

    @Override
    public TypedActionResult<ItemStack> use (World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient && user instanceof ServerPlayerEntity serverPlayer) {
            if (user.currentScreenHandler instanceof BackpackScreenHandler) {
                return TypedActionResult.fail(stack);
            }

            DefaultedList<ItemStack> items = getBackpackItems(stack);
            serverPlayer.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, player) -> new BackpackScreenHandler(syncId, inv, items, stack),
                    TITLE
            ));
        }

        return TypedActionResult.success(stack, world.isClient);
    }

    @Override
    public boolean canBeNested() {
        return false;
    }
    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType == ClickType.RIGHT && Backpacks_pp.OPENED_BACKPACKS.size() > 0) {
            return false;
        }
        super.onStackClicked(stack, slot, clickType, player);
        return false;
    }
}