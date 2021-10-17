package daniel.craftable_enchants;

import daniel.craftable_enchants.block.EnchantmentCraftingTableBlock;
import daniel.craftable_enchants.client.screen.EnchantmentCraftingScreen;
import daniel.craftable_enchants.screen.EnchantmentCraftingScreenHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.impl.screenhandler.ExtendedScreenHandlerType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CraftableEnchants implements ClientModInitializer, DedicatedServerModInitializer, ModInitializer {
    public static final String MOD_ID = "craftable_enchants";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static Block ENCHANTMENT_CRAFTING_TABLE;

    public static ScreenHandlerType<EnchantmentCraftingScreenHandler> ENCHANTMENT_CRAFTING_SCREEN_HANDLER;

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(ENCHANTMENT_CRAFTING_SCREEN_HANDLER, EnchantmentCraftingScreen::new);
    }

    @Override
    public void onInitializeServer() {

    }

    @Override
    public void onInitialize() {
        ENCHANTMENT_CRAFTING_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID, "enchantment_crafting"), EnchantmentCraftingScreenHandler::new);

        ENCHANTMENT_CRAFTING_TABLE = Registry.register(
                Registry.BLOCK,
                new Identifier(MOD_ID, "enchantment_crafting_table"),
                new EnchantmentCraftingTableBlock(AbstractBlock.Settings.of(Material.DECORATION))
        );

        Registry.register(
                Registry.ITEM,
                Registry.BLOCK.getId(ENCHANTMENT_CRAFTING_TABLE),
                new BlockItem(ENCHANTMENT_CRAFTING_TABLE, new Item.Settings().group(ItemGroup.DECORATIONS))
        );
    }
}
