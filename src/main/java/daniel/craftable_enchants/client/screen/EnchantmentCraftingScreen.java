package daniel.craftable_enchants.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import daniel.craftable_enchants.CraftableEnchants;
import daniel.craftable_enchants.screen.EnchantmentCraftingScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class EnchantmentCraftingScreen extends HandledScreen<ScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(CraftableEnchants.MOD_ID, "textures/gui/container/enchantment_crafting_table.png");
    private static final int WHITE = (255 << 16) + (255 << 8) + 255;

    List<EnchantmentLevelEntry> enchantments;
    private int maxEnchants;
    private float scrollProgress;
    private int firstEnchantment;

    public EnchantmentCraftingScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        ((EnchantmentCraftingScreenHandler)handler).setInventoryChangeListener(this::onInventoryChanged);
    }

    private void onInventoryChanged() {
        this.enchantments = EnchantmentHelper.getPossibleEntries(1, ((EnchantmentCraftingScreenHandler)this.handler).getBookSlot().getStack(), true);
        System.out.println(this.enchantments.size());
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        if (handler.slots.get(0).hasStack()) {
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

                    this.textRenderer.drawWithShadow(matrices, new TranslatableText(currentEnchantment.getTranslationKey()),buttonX + 2, buttonY + 4, (255 << 16) + (255 << 8) + 255);
                }
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {

        //this.firstEnchantment++
        if (enchantments != null) {
            this.firstEnchantment = MathHelper.clamp((int)(firstEnchantment - amount), 0, Registry.ENCHANTMENT.getEntries().size() - 3);

            System.out.println(firstEnchantment + " : " + amount + " ");
        }

        return true;
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
