package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.loot.SnifferBiomeSeedHandler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.abraxator.moresnifferflowers.lootmodifers.AddItemsModifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * More Sniffer Flowers' {@link AddItemsModifier} clears the entire sniffer dig loot list and picks one of its own
 * seeds. That runs after Immortalers' biome GLM and wipes himekaido / other biome drops. Re-apply biome seeds after it.
 * <p>
 * Only loaded when {@code moresnifferflowers} is present ({@link ImmortalersDelightMixinPlugin}).
 */
@Mixin(value = AddItemsModifier.class, remap = false)
public abstract class MoreSnifferFlowersAddItemsModifierMixin {
    @Inject(method = "doApply", at = @At("RETURN"), cancellable = true, require = 0, remap = false)
    private void immortalers$reapplyBiomeSeedsAfterClear(
            ObjectArrayList<ItemStack> generatedLoot,
            LootContext context,
            CallbackInfoReturnable<ObjectArrayList<ItemStack>> cir
    ) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (!(entity instanceof Sniffer)) {
            return;
        }
        ObjectArrayList<ItemStack> afterMs = cir.getReturnValue();
        if (afterMs == null) {
            afterMs = generatedLoot;
        }
        cir.setReturnValue(SnifferBiomeSeedHandler.applyFromLootContext(afterMs, context));
    }
}
