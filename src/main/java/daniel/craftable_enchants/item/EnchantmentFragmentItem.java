package daniel.craftable_enchants.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnchantmentFragmentItem extends Item {
    public static final String USES_KEY = "Uses";

    public EnchantmentFragmentItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        ItemStack.appendEnchantments(tooltip, EnchantedBookItem.getEnchantmentNbt(stack));

        tooltip.add(new TranslatableText("tooltip.craftable_enchants.uses_left", getUsesNbt(stack)));
    }

    public static byte getUsesNbt(ItemStack book) {
        NbtCompound nbtCompound = book.getNbt();
        return nbtCompound == null ? 0 : nbtCompound.getByte(USES_KEY);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
