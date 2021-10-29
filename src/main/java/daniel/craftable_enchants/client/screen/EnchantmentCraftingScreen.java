package daniel.craftable_enchants.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import daniel.craftable_enchants.CraftableEnchants;
import daniel.craftable_enchants.screen.EnchantmentCraftingScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookGhostSlots;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class EnchantmentCraftingScreen extends HandledScreen<ScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(CraftableEnchants.MOD_ID, "textures/gui/container/enchantment_crafting_table.png");
    private static final int WHITE = (255 << 16) + (255 << 8) + 255;

    private final RecipeBookGhostSlots ghostSlots = new RecipeBookGhostSlots();

    private int firstEnchantment;
    boolean hasBook = false;
    boolean scrollbarClicked = false;

    public EnchantmentCraftingScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        ((EnchantmentCraftingScreenHandler)handler).setInventoryChangeListener(this::onInventoryChanged);
    }

    private void onInventoryChanged() {
        hasBook = handler.slots.get(0).hasStack();
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }



    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);

        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();


        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        titleY -= 1;
    }
}
