package daniel.craftable_enchants.screen;

import daniel.craftable_enchants.CraftableEnchants;
import daniel.craftable_enchants.item.EnchantmentFragmentItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class EnchantmentCraftingScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;
    private final Inventory inventory;
    private Runnable inventoryChangeListener;

    private final CraftingResultInventory result = new CraftingResultInventory();

    private final Slot bookSlot;
    private final Slot lapisSlot;
    private final Slot fragmentSlot;


    public EnchantmentCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public EnchantmentCraftingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(CraftableEnchants.ENCHANTMENT_CRAFTING_SCREEN_HANDLER, syncId);

        this.inventoryChangeListener = () -> {
        };

        this.inventory = new SimpleInventory(3) {
            public void markDirty() {
                super.markDirty();
                EnchantmentCraftingScreenHandler.this.onContentChanged(this);
                EnchantmentCraftingScreenHandler.this.inventoryChangeListener.run();
            }
        };

        bookSlot = this.addSlot(new Slot(this.inventory, 0, 58, 15) {
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.BOOK);
            }
        });
        lapisSlot = this.addSlot(new Slot(this.inventory, 1, 80, 15) {
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.LAPIS_LAZULI);
            }
        });
        fragmentSlot = this.addSlot(new Slot(this.inventory, 2, 102, 15) {
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(CraftableEnchants.ENCHANTMENT_FRAGMENT);
            }

            public int getMaxItemCount() {
                return 1;
            }
        });

        this.addSlot(new Slot(this.result, 3, 80, 59) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                onTakeOutput(player, stack);
            }
        });

        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        this.context = context;
    }

    private void onTakeOutput(PlayerEntity player, ItemStack stack) {
        bookSlot.takeStack(1);
        lapisSlot.takeStack(1);

        ItemStack fragmentItem = fragmentSlot.getStack();
        NbtCompound fragmentNbt = fragmentItem.getNbt();
        if (fragmentNbt != null) {
            int usesLeft = fragmentNbt.getInt(EnchantmentFragmentItem.USES_KEY);

            if (usesLeft > 1)
                fragmentNbt.putInt(EnchantmentFragmentItem.USES_KEY, usesLeft - 1);
            else {
                fragmentSlot.setStack(ItemStack.EMPTY);
            }
        }
    }

    public void setInventoryChangeListener(Runnable inventoryChangeListener) {
        this.inventoryChangeListener = inventoryChangeListener;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);

        if (inventory != this.inventory) return;

        if (bookSlot.hasStack() && lapisSlot.hasStack() && fragmentSlot.hasStack()) {
            ItemStack stack = Items.ENCHANTED_BOOK.getDefaultStack();

            NbtList enchants = fragmentSlot.getStack().getNbt().getList(EnchantedBookItem.STORED_ENCHANTMENTS_KEY, 10);

            if (enchants != null) {
                stack.setSubNbt(EnchantedBookItem.STORED_ENCHANTMENTS_KEY, enchants.copy());
            }
            stack.setSubNbt(EnchantmentFragmentItem.FROM_FRAGMENT_KEY, NbtByte.of((byte)1));

            result.setStack(3, stack);
        }
        else {
            result.setStack(0, ItemStack.EMPTY);
        }
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;

        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index < 4) {
                if (!this.insertItem(itemStack2, 4, 38, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (itemStack2.isOf(Items.BOOK)) {
                if (!this.insertItem(itemStack2, 0, 1, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemStack2.isOf(Items.LAPIS_LAZULI)) {
                if (!this.insertItem(itemStack2, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemStack2.isOf(CraftableEnchants.ENCHANTMENT_FRAGMENT)) {
                if (!this.insertItem(itemStack2, 2, 3, true)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, CraftableEnchants.ENCHANTMENT_CRAFTING_TABLE);
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);

        this.context.run(((world, blockPos) -> {
            this.dropInventory(player, inventory);
        }));
    }

    public Slot getLapisSlot() {
        return lapisSlot;
    }

    public Slot getItemSlot() {
        return fragmentSlot;
    }

    public Slot getBookSlot() {
        return bookSlot;
    }
}
