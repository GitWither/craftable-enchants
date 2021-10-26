package daniel.craftable_enchants.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookMixin {

    @Inject(at = @At("TAIL"), method = "appendTooltip")
    public void setFromFragmentTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo cb) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains("FromFragment")) {
            tooltip.add(new TranslatableText("tooltip.craftable_enchants.reconstructed").setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.AQUA))));
        }
    }
}
