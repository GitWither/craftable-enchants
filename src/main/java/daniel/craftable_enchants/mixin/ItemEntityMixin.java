package daniel.craftable_enchants.mixin;

import daniel.craftable_enchants.CraftableEnchants;
import daniel.craftable_enchants.item.EnchantmentFragmentItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract ItemStack getStack();

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;scheduleVelocityUpdate()V"), method = "damage", cancellable = true)
    public void convertEnchantingBookToFragment(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cb) {
        if (!this.getStack().isEmpty() && this.getStack().isOf(Items.ENCHANTED_BOOK) && source.getSource() instanceof CreeperEntity) {
            NbtCompound bookNbt = this.getStack().getNbt();
            if (bookNbt != null) {
                if (!this.getStack().getNbt().contains(EnchantmentFragmentItem.FROM_FRAGMENT_KEY)) {
                    ItemStack stack = CraftableEnchants.ENCHANTMENT_FRAGMENT.getDefaultStack();
                    NbtCompound enchant = this.getStack().getNbt();
                    if (enchant != null) {
                        enchant.putByte("Uses", (byte)RandomUtils.nextInt(4, 9));
                        stack.setNbt(enchant.copy());
                    }
                    ItemEntity entity = this.dropStack(stack);
                }
            }
        }
    }
}
