package daniel.craftable_enchants.screen;

import daniel.craftable_enchants.CraftableEnchants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class EnchantmentCraftingScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;
    private final Inventory inventory;

    public EnchantmentCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public EnchantmentCraftingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(CraftableEnchants.ENCHANTMENT_CRAFTING_SCREEN_HANDLER, syncId);

        this.inventory = new SimpleInventory(3) {
            public void markDirty() {
                super.markDirty();
                EnchantmentCraftingScreenHandler.this.onContentChanged(this);
            }
        };

        this.addSlot(new Slot(this.inventory, 0, 15, 47) {
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.BOOK);
            }

            public int getMaxItemCount() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.inventory, 1, 35, 47) {
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.LAPIS_LAZULI);
            }
        });
        this.addSlot(new Slot(this.inventory, 1, 55, 47) {
            public boolean canInsert(ItemStack stack) {
                return true;
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
    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, CraftableEnchants.ENCHANTMENT_CRAFTING_TABLE);
    }
}
