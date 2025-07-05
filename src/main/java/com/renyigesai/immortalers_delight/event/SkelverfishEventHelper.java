package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.entities.living.SkelverfishBomber;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishThrasher;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

@Mod.EventBusSubscriber
public class SkelverfishEventHelper {

    @SubscribeEvent
    public static void onExplosionDamage(LivingHurtEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_EXPLOSION) && event.getSource().getEntity() instanceof SkelverfishBomber bomber) {
            float f = event.getEntity().getRandom().nextFloat();
            float extraDamage = (bomber.getMaxFireDamage() +
                    (float)(bomber.getAttribute(Attributes.ATTACK_DAMAGE) == null ? 0.0F : bomber.getAttribute(Attributes.ATTACK_DAMAGE).getValue())) *
                    (bomber.isPowered() ? 4.0F : 2.0F) +
                    (bomber.level().getDifficulty() == Difficulty.EASY ? 2.0F + f * 1.5F : 0.0F) +
                    (bomber.level().getDifficulty() == Difficulty.NORMAL ? 5.0F + f * 4.0F : 0.0F) +
                    (bomber.level().getDifficulty() == Difficulty.HARD ? 6.0F + f * 3.0F : 0.0F);
            event.setAmount(event.getAmount() + extraDamage); // 提升爆炸伤害，造成攻击力200%或400%爆炸伤害
        }
    }

    @SubscribeEvent
    public static void onAmbusherAttack(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(attacker.getType());
            if (entityId != null && !attacker.level().isClientSide) {
                String idString = entityId.toString();
                if (idString.equals(ImmortalersDelightEntities.SKELVERFISH_AMBUSHER.getId().toString())) {
                    int hurtArmor = event.getEntity().getArmorValue();
                    if (hurtArmor > 0) {
                        if (hurtArmor > 20) hurtArmor = 20;
                        float damageBuffer = (1/(1-(hurtArmor * 0.04f))) > 3.0f ? 3.0f : (1/(1-(hurtArmor * 0.04f)));
                        event.setAmount(event.getAmount() * damageBuffer);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onThrasherJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Sniffer sniffer) {
            sniffer.goalSelector.addGoal(3, new TemptGoal(sniffer, 3.0D, Ingredient.of(ImmortalersDelightItems.SACHETS.get()), false));
        }
//        if (event.getEntity() instanceof SkelverfishThrasher skelverfishThrasher) {
//            skelverfishThrasher.goalSelector.removeGoal(new MeleeAttackGoal(skelverfishThrasher, 1.0D, false));
//        }
    }
}
