package daniel.craftable_enchants.test;

import daniel.craftable_enchants.CraftableEnchants;
import daniel.craftable_enchants.block.EnchantmentCraftingTableBlock;
import daniel.craftable_enchants.item.EnchantmentFragmentItem;
import daniel.craftable_enchants.screen.EnchantmentCraftingScreenHandler;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.OptionalInt;

public class CraftableEnchantsTestSuite {

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testEnchantmentBookFracture(TestContext context) {
        CreeperEntity creeper = context.spawnMob(EntityType.CREEPER, 0, 1, 0);
        ItemEntity item = context.spawnItem(Items.ENCHANTED_BOOK, 0.5f, 1, 0.5f);
        EnchantedBookItem.addEnchantment(item.getStack(), new EnchantmentLevelEntry(Enchantments.FEATHER_FALLING, 1));

        creeper.ignite();

        context.addFinalTaskWithDuration(30, () -> {
            context.expectItemAt(CraftableEnchants.ENCHANTMENT_FRAGMENT, new BlockPos(0, 1, 0), 5);
        });
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testEnchantmentCraftingTable(TestContext context) {
        BlockPos pos = new BlockPos(0, 1, 0);
        BlockState state = CraftableEnchants.ENCHANTMENT_CRAFTING_TABLE.getDefaultState();

        context.setBlockState(pos, state);
        PlayerEntity player = context.createMockPlayer();


        EnchantmentCraftingScreenHandler currentScreenHandler = new EnchantmentCraftingScreenHandler(0, player.getInventory());
        currentScreenHandler.getBookSlot().setStack(Items.BOOK.getDefaultStack());
        currentScreenHandler.getLapisSlot().setStack(Items.LAPIS_LAZULI.getDefaultStack());

        ItemStack fragment = CraftableEnchants.ENCHANTMENT_FRAGMENT.getDefaultStack();
        EnchantedBookItem.addEnchantment(fragment, new EnchantmentLevelEntry(Enchantments.FEATHER_FALLING, 1));

        currentScreenHandler.getItemSlot().setStack(fragment);

        context.addFinalTask(() -> {
            ItemStack result = currentScreenHandler.slots.get(3).getStack();

            if (result.isOf(Items.ENCHANTED_BOOK) && result.getNbt().contains(EnchantmentFragmentItem.FROM_FRAGMENT_KEY)) {
                context.complete();
            }
            else {
                throw new GameTestException("Result is wrong!");
            }
        });
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void testBlockDrop(TestContext context) {
        //.setBlockState(0, 1, 0, Blocks.OBSIDIAN.getDefaultState());
        BlockPos pos = new BlockPos(0, 1, 0);
        context.setBlockState(pos, CraftableEnchants.ENCHANTMENT_CRAFTING_TABLE);
        context.getWorld().breakBlock(context.getAbsolutePos(pos), true, context.createMockPlayer());

        context.addFinalTask(() -> {
            context.expectItemAt(CraftableEnchants.ENCHANTMENT_CRAFTING_TABLE.asItem(), pos, 5);
        });
    }
}
