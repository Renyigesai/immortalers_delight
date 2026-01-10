package com.renyigesai.immortalers_delight.item.food;


import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.MathUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class ImmortalersDogFoodItem extends ConsumableItem {
    @Nullable
    private final FoodProperties dogFoodProperties;

    public ImmortalersDogFoodItem(Item.Properties properties, FoodProperties foodProperties) {
        super(properties);
        this.dogFoodProperties = foodProperties;
    }
    public ImmortalersDogFoodItem(Item.Properties properties, FoodProperties foodProperties, boolean hasEffectToolTip) {
        super(properties,hasEffectToolTip);
        this.dogFoodProperties = foodProperties;
    }
    public ImmortalersDogFoodItem(Item.Properties properties, FoodProperties foodProperties, boolean hasEffectToolTip, boolean hasCustomToolTip) {
        super(properties,hasEffectToolTip,hasCustomToolTip);
        this.dogFoodProperties = foodProperties;
    }

    public FoodProperties getDogFoodProperties() {
        return dogFoodProperties;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltip, isAdvanced);
        if ((Boolean)Configuration.FOOD_EFFECT_TOOLTIP.get()) {
            tooltip.add(Component.empty());
            MutableComponent textWhenFeeding = TextUtils.getTranslation("tooltip.dog_food.when_feeding", new Object[0]);
            tooltip.add(textWhenFeeding.withStyle(ChatFormatting.GRAY));

            MutableComponent effectDescription;
            MobEffect effect;
            Item item = stack.getItem();
            List<MobEffectInstance> effects = Lists.newArrayList();
            if (item instanceof ImmortalersDogFoodItem immortalersDogFoodItem && immortalersDogFoodItem.getDogFoodProperties() != null) {
                for(Pair<MobEffectInstance, Float> pair : immortalersDogFoodItem.getDogFoodProperties().getEffects()) {
                    effects.add(pair.getFirst());
                }
            }
            for(Iterator var6 = effects.iterator(); var6.hasNext(); tooltip.add(effectDescription.withStyle(effect.getCategory().getTooltipFormatting()))) {
                MobEffectInstance effectInstance = (MobEffectInstance)var6.next();
                effectDescription = Component.literal(" ");
                MutableComponent effectName = Component.translatable(effectInstance.getDescriptionId());
                effectDescription.append(effectName);
                effect = effectInstance.getEffect();
                if (effectInstance.getAmplifier() > 0) {
                    effectDescription.append(" ").append(Component.translatable("potion.potency." + effectInstance.getAmplifier()));
                }

                if (effectInstance.getDuration() > 20) {
                    effectDescription.append(" (").append(MobEffectUtil.formatDuration(effectInstance, 1.0F)).append(")");
                }
            }

        }
    }

    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        if (target instanceof Wolf wolf) {
            if (wolf.isAlive() && wolf.isTame()) {
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Bus.FORGE
    )
    public static class DogFoodEvent {
        public DogFoodEvent() {
        }

        @SubscribeEvent
        public static void onDogFoodApplied(PlayerInteractEvent.EntityInteract event) {
            Player player = event.getEntity();
            Entity target = event.getTarget();
            ItemStack itemStack = event.getItemStack();
            if (target instanceof LivingEntity entity) {
                if (target.getType().is(ModTags.DOG_FOOD_USERS)) {
                    boolean isTameable = entity instanceof TamableAnimal;
                    if (entity.isAlive() && (!isTameable || ((TamableAnimal)entity).isTame()) && itemStack.getItem() instanceof ImmortalersDogFoodItem  immortalersDogFoodItem) {
                        List<MobEffectInstance> effects = Lists.newArrayList();
                        if (immortalersDogFoodItem.getDogFoodProperties() != null) {
                            for(Pair<MobEffectInstance, Float> pair : immortalersDogFoodItem.getDogFoodProperties().getEffects()) {
                                target.level();
                                if (!target.level().isClientSide && pair.getFirst() != null && entity.getRandom().nextFloat() < pair.getSecond()) {
                                    effects.add(pair.getFirst());
                                }
                            }
                        }
                        for (MobEffectInstance effect : effects) {
                            entity.addEffect(new MobEffectInstance(effect));
                        }

                        entity.setHealth(entity.getMaxHealth());
                        entity.level().playSound((Player)null, target.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);

                        for(int i = 0; i < 5; ++i) {
                            double xSpeed = MathUtils.RAND.nextGaussian() * 0.02;
                            double ySpeed = MathUtils.RAND.nextGaussian() * 0.02;
                            double zSpeed = MathUtils.RAND.nextGaussian() * 0.02;
                            entity.level().addParticle((ParticleOptions)ModParticleTypes.STAR.get(), entity.getRandomX(1.0), entity.getRandomY() + 0.5, entity.getRandomZ(1.0), xSpeed, ySpeed, zSpeed);
                        }

                        if (!player.isCreative()) {
                            itemStack.shrink(1);
                            if (itemStack.getCraftingRemainingItem() != ItemStack.EMPTY) {
                                player.addItem(itemStack.getCraftingRemainingItem());
                            }
                        }

                        event.setCancellationResult(InteractionResult.SUCCESS);
                        event.setCanceled(true);
                    }
                }
            }

        }
    }
}

