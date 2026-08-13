package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

/**
 * 1.21 falling anvils only damage living entities, so Item.onDestroyed never sees FALLING_ANVIL.
 */
@EventBusSubscriber(modid = ImmortalersDelightMod.MODID)
public final class ObsidianWalnutHandler {
    private ObsidianWalnutHandler() {}

    @SubscribeEvent
    public static void onFallingAnvil(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof FallingBlockEntity falling)) {
            return;
        }
        Level level = falling.level();
        if (level.isClientSide()) {
            return;
        }
        if (!falling.getBlockState().is(BlockTags.ANVIL)) {
            return;
        }
        if (falling.fallDistance < 0.5F && falling.getDeltaMovement().y >= 0.0D) {
            return;
        }

        AABB box = falling.getBoundingBox().inflate(0.25D);
        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, box,
                entity -> entity.isAlive() && entity.getItem().is(ImmortalersDelightItems.OBSIDIAN_WALNUT.get()))) {
            crackWalnut(item);
        }
    }

    public static void crackWalnut(ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getItem();
        ItemEntity output = new ItemEntity(
                itemEntity.level(),
                itemEntity.getX(),
                itemEntity.getY(),
                itemEntity.getZ(),
                new ItemStack(ImmortalersDelightItems.OBSIDIAN_WALNUT_KERNEL.get(), stack.getCount()));
        output.setDeltaMovement(itemEntity.getDeltaMovement());
        itemEntity.discard();
        itemEntity.level().addFreshEntity(output);
    }
}
