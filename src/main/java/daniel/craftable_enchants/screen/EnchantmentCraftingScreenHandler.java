package daniel.craftable_enchants.screen;

import daniel.craftable_enchants.CraftableEnchants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

        bookSlot = this.addSlot(new Slot(this.inventory, 0, 15, 47) {
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.BOOK);
            }

            public int getMaxItemCount() {
                return 1;
            }
        });
        lapisSlot = this.addSlot(new Slot(this.inventory, 1, 35, 47) {
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.LAPIS_LAZULI);
            }
        });
        fragmentSlot = this.addSlot(new Slot(this.inventory, 2, 55, 47) {
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(CraftableEnchants.ENCHANTMENT_FRAGMENT);
            }

            public int getMaxItemCount() {
                return 1;
            }
        });

        this.addSlot(new Slot(this.result, 3, 89, 47) {
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

            result.setStack(3, stack);
        }
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, CraftableEnchants.ENCHANTMENT_CRAFTING_TABLE);
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
