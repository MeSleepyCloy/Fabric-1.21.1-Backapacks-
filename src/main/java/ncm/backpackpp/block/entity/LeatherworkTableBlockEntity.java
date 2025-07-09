package ncm.backpackpp.block.entity;

import ncm.backpackpp.init.BPBlockEntities;
import ncm.backpackpp.recipes.LeatherworkingRecipe;
import ncm.backpackpp.recipes.LeatherworkingRecipeInput;
import ncm.backpackpp.recipes.ModRecipes;
import ncm.backpackpp.screen.leatherworkTable.LeatherworkTableScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public class LeatherworkTableBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, Inventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(11, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 9;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 100;


    public LeatherworkTableBlockEntity(BlockPos pos, BlockState state) {
        super(BPBlockEntities.LEATHERWORK_TABLE_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> LeatherworkTableBlockEntity.this.progress;
                    case 1 -> LeatherworkTableBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        LeatherworkTableBlockEntity.this.progress = value;
                    case 1:
                        LeatherworkTableBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if(hasRecipe()) {
            increaseCraftingProgress();
            markDirty(world, pos, state);

            if(hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 100;
        this.maxProgress = 100;
    }

    private void craftItem() {
        Optional<RecipeEntry<LeatherworkingRecipe>> recipe = getCurrentRecipe();

        ItemStack output = recipe.get().value().output();
        this.removeStack(INPUT_SLOT, 1);
        this.setStack(OUTPUT_SLOT, new ItemStack(output.getItem(),
                this.getStack(OUTPUT_SLOT).getCount() + output.getCount()));
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        this.progress++;
    }

    private boolean hasRecipe() {
        Optional<RecipeEntry<LeatherworkingRecipe>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().output();
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output);
    }

    private Optional<RecipeEntry<LeatherworkingRecipe>> getCurrentRecipe() {
        return this.getWorld().getRecipeManager()
                .getFirstMatch(ModRecipes.LEATHERWORKING_RECIPE_TYPE, new LeatherworkingRecipeInput(inventory.get(INPUT_SLOT)), this.getWorld());
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = this.getStack(OUTPUT_SLOT).isEmpty() ? 64 : this.getStack(OUTPUT_SLOT).getMaxCount();
        int currentCount = this.getStack(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.backpacks_pp.leatherwork_table");
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int count) {
        return Inventories.splitStack(inventory, slot, count);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return pos.isWithinDistance(player.getPos(), 4.5); // Примерная проверка
    }

    @Nullable
    @Override
    public  ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new LeatherworkTableScreenHandler(syncId, inv, this);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.writeNbt(nbt, inventory, registryLookup);
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        inventory.clear();
        Inventories.readNbt(nbt, inventory, registryLookup);
    }

    public Inventory getInventory() {
        return this;
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    public void dropContentsExcludingResult(World world, BlockPos pos) {
        for (int i = 0; i < this.size(); i++) {
            if (i != 9 && !this.getStack(i).isEmpty()) {
                ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), this.getStack(i));
                this.setStack(i, ItemStack.EMPTY);
            }
        }
        this.setStack(9, ItemStack.EMPTY);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
