package daniel.craftable_enchants.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import daniel.craftable_enchants.CraftableEnchants;
import daniel.craftable_enchants.screen.EnchantmentCraftingScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

public class EnchantmentCraftingScreen extends HandledScreen<ScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(CraftableEnchants.MOD_ID, "textures/gui/container/enchantment_crafting_table.png");
    private static final int WHITE = (255 << 16) + (255 << 8) + 255;

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

        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, x + 156, y + (int)(46 * firstEnchantment / 38.0f) + 16, hasBook ? 176 : 188, 0, 12, 15);

        if (hasBook) {
            for (int i = firstEnchantment, yDelta = 0; i <  firstEnchantment + 3; i++, yDelta++) {
                Enchantment currentEnchantment = Registry.ENCHANTMENT.get(i);

                if (currentEnchantment != null) {
                    final int buttonX = x + 44;
                    final int buttonY = y + yDelta * 19 + 16;

                    //offset the starting position of the mouse
                    final int mouseZoneX = mouseX - buttonX;
                    final int mouseZoneY = mouseY - buttonY;

                    boolean isCursorOnButton = (mouseZoneX >= 0 && mouseZoneY >= 0) && (mouseZoneX <= 110 && mouseZoneY <= 19);

                    RenderSystem.setShaderTexture(0, TEXTURE);

                    drawTexture(matrices, buttonX, buttonY, 0, isCursorOnButton ? 204 : 166, 110, 19);

                    this.textRenderer.drawWithShadow(matrices, new TranslatableText(currentEnchantment.getTranslationKey()),buttonX + 2, buttonY + 4, WHITE);
                }
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {

        //this.firstEnchantment++
        this.firstEnchantment = MathHelper.clamp((int)(firstEnchantment - amount), 0, Registry.ENCHANTMENT.getEntries().size() - 3);

        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrollbarClicked = false;

        int scrollbarZoneX = this.x + 156;
        int scrollbarZoneY = this.y + 16;

        if (mouseX >= scrollbarZoneX && mouseX <= scrollbarZoneX + 12 && mouseY >= scrollbarZoneY && mouseY <= scrollbarZoneY + 56) {
            this.scrollbarClicked = true;
            System.out.println("hello");
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (scrollbarClicked) {
            this.firstEnchantment = MathHelper.clamp((int)((mouseY - 56) / 56f * 38), 0, Registry.ENCHANTMENT.getEntries().size() - 3);
            System.out.println((int)((mouseY - 56) / 38 * Registry.ENCHANTMENT.getEntries().size()));
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
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
