package daniel.craftable_enchants.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import daniel.craftable_enchants.CraftableEnchants;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class EnchantmentCraftingScreen extends HandledScreen<ScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(CraftableEnchants.MOD_ID, "textures/gui/container/enchantment_crafting_table.png");

    private float scrollProgress;

    public EnchantmentCraftingScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        List<EnchantmentLevelEntry> enchantments = EnchantmentHelper.getPossibleEntries(1, handler.slots.get(0).getStack(), true);
        if (enchantments.size() > 0) {
            for (int i = 0; i < 3; i++) {
                drawTexture(matrices, x + 60, y + i * 19 + 16, 0, 166, 95, 19);
                this.textRenderer.drawWithShadow(matrices, enchantments.get(i).enchantment.getName(1), x + 62, y + i * 19 + 20, 3419941);
            }
        }

        //this.textRenderer.draw(matrices, "fucking kys", 155, 90, 3419941);
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
    }
}
