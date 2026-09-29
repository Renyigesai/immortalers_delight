package com.renyigesai.immortalers_delight.item.food;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.projectile.ScreenLayerParticleModel;
import com.renyigesai.immortalers_delight.client.particle.CircleTwinkleParticleOption;
import com.renyigesai.immortalers_delight.client.particle.TwinkleParticleOption;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.item.EnchantAbleFoodItem;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlfalfaPopsicleItem extends EnchantAbleFoodItem {

    protected final float attackDamage;
    protected final float extraDamage;
    protected final float attackSpeed;
    protected final float extraSpeed;
    public AlfalfaPopsicleItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip, hasCustomTooltip ? 9 : 12);
        this.attackDamage = 7;
        this.extraDamage = 6;
        this.attackSpeed = -3.0f;
        this.extraSpeed = 0.6f;
    }
    public AlfalfaPopsicleItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, float attackDamage, float attackSpeed, float extraDamage) {
        super(properties, hasFoodEffectTooltip, hasCustomTooltip);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.extraDamage = extraDamage;
        this.extraSpeed = 0;
    }

    //基础面板。既然是复刻血脉断绝剑那么在超凡模式下这个可以当剑使用（
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack)
    {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.<Attribute, AttributeModifier>create();
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            if (isPowerful) {
                double damage = this.extraDamage + this.attackDamage;
                double speed = this.extraSpeed + this.attackSpeed;
                multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", speed, AttributeModifier.Operation.ADDITION));
                multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", damage, AttributeModifier.Operation.ADDITION));
                return multimap;
            } else {
                multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
                multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
                return multimap;
            }
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, @NotNull LivingEntity consumer) {
        //服务端行为，实现冷却啊伤害啊之类的一些功能
        if (!level.isClientSide) {
            //记录消耗的饥饿值
            int hungerNeed = 0;
            //记录伤害
            double damage = consumer.getAttribute(Attributes.ATTACK_DAMAGE) != null ? consumer.getAttributeValue(Attributes.ATTACK_DAMAGE) : 0;
            //在主手时造成攻击力100%的伤害，否则50%
            boolean inhand = consumer.getItemInHand(InteractionHand.MAIN_HAND).is(stack.getItem());
            if (!inhand) damage *= 0.5;

            //伤害保底：普通模式，如果使用者攻击面板不如冰棒自身面板伤害，取冰棒的伤害
            //超凡模式则直接加一个冰棒的伤害上去
            if (DifficultyModeUtil.isPowerBattleMode()) damage += this.attackDamage + this.extraDamage;
            else if (damage < this.attackDamage) damage = this.attackDamage;
            //如果没有伤害，那没必要打AOE了，直接返回
            if (damage <= 0) return super.finishUsingItem(stack,level,consumer);

            //计算范围，不会有生物没有追踪距离吧
            double range = 16;
            if (consumer instanceof Player) range += 4;
            else if (consumer.getAttribute(Attributes.FOLLOW_RANGE) != null) range = consumer.getAttributeValue(Attributes.FOLLOW_RANGE);
            List<LivingEntity> entities = level.getEntitiesOfClass(
                    LivingEntity.class,
                    consumer.getBoundingBox().inflate(range,range / 4, range)
            );
            for (LivingEntity livingentity : entities) {
                if (livingentity.getUUID() != consumer.getUUID() && livingentity.isAlive() && !consumer.isAlliedTo(livingentity) && !livingentity.isAlliedTo(consumer)) {
                    DamageSource source = consumer.damageSources().indirectMagic(consumer,consumer);
                    livingentity.invulnerableTime = 0;
                    livingentity.hurt(source, (float) damage);
                    livingentity.invulnerableTime = 0;
                    hungerNeed += 1;
                }
            }

            //气死自己
            consumer.hurt(consumer.damageSources().explosion(consumer,consumer), (float) (damage / 2));

            //处理扣饥饿，以及冷却
            if (consumer instanceof Player player) {

                //扣饥饿
                FoodData foodData = player.getFoodData();
                int hunger = foodData.getFoodLevel() - hungerNeed;
                if (hunger < 0) hunger = 0;
                foodData.setFoodLevel(hunger);
                //添加冷却
                int time = DifficultyModeUtil.isPowerBattleMode() ? 300 : 700;
                if (player.hasEffect(ImmortalersDelightMobEffect.SATIATED.get())) time = DifficultyModeUtil.isPowerBattleMode() ? 20 : 70;

                if (inhand) player.getCooldowns().addCooldown(player.getItemInHand(InteractionHand.MAIN_HAND).getItem(), time);
                else player.getCooldowns().addCooldown(player.getItemInHand(InteractionHand.OFF_HAND).getItem(), time);

            }

        } else {
            //计算范围，不会有生物没有追踪距离吧
            double range = 16;
            if (consumer instanceof Player) range += 4;
            else if (consumer.getAttribute(Attributes.FOLLOW_RANGE) != null) range = consumer.getAttributeValue(Attributes.FOLLOW_RANGE);

            //客户端行为：生成粒子
            sendParticle2002(consumer.getRandom(),new Vec3(consumer.getX(),consumer.getY(),consumer.getZ()),consumer.level(),range);
        }
        return super.finishUsingItem(stack,level,consumer);
    }

    public void sendParticle2002(RandomSource randomsource, Vec3 pPos, Level level, double range){

        float radius = (float) range;
        //客户端行为：生成粒子效果
        Vec3 center = new Vec3(pPos.x, pPos.y + 0.5, pPos.z);
        for (int i = 0; i < 100; i++) {
            double angle = 2 * Math.PI * Math.random();
            double r = radius * Math.sqrt(Math.random());
            double x = center.x + r * Math.cos(angle);
            double z = center.z + r * Math.sin(angle);
            double y = center.y;
            if (i % 3 == 0) {
                level.addParticle(
                        new TwinkleParticleOption(63), false, x, y, z, 0, 0.025, 0
                );
            } else level.addParticle(
                    new TwinkleParticleOption(39), false, x, y, z, 0, 0.025, 0
            );
        }

        for(int i = 0; i < 8; ++i) {
            float dx = 0;
            float dy = 0;
            float dz = 0;
            if (i >= 1) {
                if (i <= 6) {
                    dx = (float) Math.sin(i);
                    dz = (float) Math.cos(i);
                } else dy = 0.6f;
            }
            level.addParticle(new CircleTwinkleParticleOption(34), pPos.x + dx, pPos.y + dy, pPos.z + dz, randomsource.nextGaussian() * 0.15D, randomsource.nextDouble() * 0.2D, randomsource.nextGaussian() * 0.15D);
        }

        ParticleOptions particleoption = ParticleTypes.ANGRY_VILLAGER;
        ParticleOptions particleoptions = new TwinkleParticleOption(-57);

        for(int k2 = 0; k2 < 32; ++k2) {
            double d13 = randomsource.nextDouble() * 4.0D;
            double d19 = randomsource.nextDouble() * Math.PI * 2.0D;
            double d25 = Math.cos(d19) * d13;
            double d30 = 0.01D + randomsource.nextDouble() * 0.5D;
            double d31 = Math.sin(d19) * d13;
            level.addParticle(particleoption,
                    pPos.x + d25, pPos.y + 0.3D, pPos.z + d31,
                    d25, d30, d31);

            level.addParticle(particleoptions,
                    pPos.x + d25 * 0.1D * range, pPos.y + 0.3D, pPos.z + d31 * 0.1D * range,
                    d25, d30, d31);
        }

        level.playLocalSound(BlockPos.containing(pPos), SoundEvents.IRON_GOLEM_ATTACK, SoundSource.NEUTRAL, 1.0F, randomsource.nextFloat() * 0.1F + 0.9F, false);
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        super.appendHoverText(pStack,pLevel,pTooltip,pFlag);

        List<Component> tooltips = new ArrayList<>();
        tooltips.add(Component.translatable("tooltip.immortalers_delight." + this + "_0").withStyle(ChatFormatting.DARK_PURPLE));
        tooltips.add(Component.translatable("tooltip.immortalers_delight." + this + "_1").withStyle(ChatFormatting.YELLOW));
//        tooltips.add(Component.translatable("tooltip.immortalers_delight." + this + "_2").withStyle(ChatFormatting.GRAY));
        /*多条时需按住Shift查看*/
        if (Screen.hasShiftDown()){
            pTooltip.addAll(tooltips);
        }else {
            pTooltip.add(Component.translatable("tooltip.immortalers_delight.tooltip_item_name_block_item").withStyle(ChatFormatting.GRAY));
        }
    }

}
