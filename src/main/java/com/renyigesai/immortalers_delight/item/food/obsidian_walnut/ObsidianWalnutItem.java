package com.renyigesai.immortalers_delight.item.food.obsidian_walnut;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.event.ObsidianWalnutHandler;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ObsidianWalnutItem extends ItemNameBlockItem {
    public ObsidianWalnutItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        InteractionHand otherHand = pHand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack otherStack = pPlayer.getItemInHand(otherHand);
        if (itemstack.is(ImmortalersDelightItems.OBSIDIAN_WALNUT.get()) && otherStack.is(Items.FLINT_AND_STEEL)) {
            if (otherStack.getMaxDamage() > 0) {
                int damage = 1;
                if (pPlayer.isShiftKeyDown()) {
                    damage = otherStack.getMaxDamage() - otherStack.getDamageValue();
                    damage = Math.min(damage, itemstack.getCount());
                }
                if (!pLevel.isClientSide() && pPlayer instanceof ServerPlayer serverPlayer) {
                    itemstack.shrink(damage);
                    if (!pPlayer.getAbilities().instabuild) {
                        EquipmentSlot slot = otherHand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                        otherStack.hurtAndBreak(damage, serverPlayer, slot);
                    }
                    ItemEntity output = new ItemEntity(
                            pPlayer.level(),
                            pPlayer.getX() + 0.5 * pPlayer.getRandom().nextInt(3) - 0.5,
                            pPlayer.getY() + 0.5,
                            pPlayer.getZ() + 0.5 * pPlayer.getRandom().nextInt(3) - 0.5,
                            new ItemStack(ImmortalersDelightItems.BLAZING_OBSIDIAN_WALNUT.get(), damage));
                    output.setDeltaMovement(0, 0, 0);
                    pPlayer.level().addFreshEntity(output);
                }
                return InteractionResultHolder.consume(itemstack);
            }
        }
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level level, @NotNull Entity entity, int i, boolean held) {
        if (entity.level().isClientSide()) return;
        if (entity instanceof Player pPlayer && !pPlayer.getAbilities().instabuild) {
            MobEffectInstance effectInstance = pPlayer.getEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES);
            if (effectInstance != null && effectInstance.getDuration() % 32 == 0) {
                ItemEntity output = new ItemEntity(
                        pPlayer.level(),
                        pPlayer.getX(),
                        pPlayer.getY(),
                        pPlayer.getZ(),
                        new ItemStack(ImmortalersDelightItems.BLAZING_OBSIDIAN_WALNUT.get(), 1));
                output.setDeltaMovement(pPlayer.getDeltaMovement());
                pPlayer.level().addFreshEntity(output);
                stack.shrink(1);
            }
        }
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {
        if (damageSource.is(DamageTypeTags.IS_EXPLOSION)
                || damageSource.is(DamageTypes.FALLING_ANVIL)
                || damageSource.is(DamageTypes.FALLING_BLOCK)) {
            if (!itemEntity.level().isClientSide() && itemEntity.getItem().is(ImmortalersDelightItems.OBSIDIAN_WALNUT.get())) {
                ObsidianWalnutHandler.crackWalnut(itemEntity);
                return;
            }
        }
        super.onDestroyed(itemEntity, damageSource);
    }

    private String tooltipPath() {
        return BuiltInRegistries.ITEM.getKey(this).getPath();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
        MutableComponent textEmpty = Component.translatable("tooltip." + ImmortalersDelightMod.MODID + "." + tooltipPath());
        MutableComponent textEmpty1 = Component.translatable("tooltip." + ImmortalersDelightMod.MODID + "." + tooltipPath() + "_1");
        MutableComponent textEmpty2 = Component.translatable("tooltip." + ImmortalersDelightMod.MODID + "." + tooltipPath() + "_2");
        ChatFormatting color = ChatFormatting.GRAY;
        tooltip.add(textEmpty.withStyle(color));
        tooltip.add(textEmpty1.withStyle(color));
        tooltip.add(textEmpty2.withStyle(ChatFormatting.RED));
        super.appendHoverText(stack, context, tooltip, isAdvanced);
    }
}
