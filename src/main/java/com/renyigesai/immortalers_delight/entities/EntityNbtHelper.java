package com.renyigesai.immortalers_delight.entities;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = ImmortalersDelightMod.MODID)
public class EntityNbtHelper {
    public static final String KNOCKBACK_SILVERFISH_TAG = "imm_del_knockback_bug_1";
    public static final String BOOMER_SILVERFISH_TAG = "imm_del_boomer_bug_1";

    @SubscribeEvent
    public static void onCreatureHurt(LivingIncomingDamageEvent evt) {
        LivingEntity hurtOne = evt.getEntity();
        LivingEntity attacker = null;
        if (evt.getSource().getEntity() instanceof LivingEntity livingEntity) {
            attacker = livingEntity;
        }
        if (evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
            return;
        }

        if (!hurtOne.level().isClientSide) {
            if (attacker != null) {
                if (attacker instanceof Player) {
                    return;
                }

                CompoundTag nbt = attacker.getPersistentData();

                if (nbt.contains("Tags", 9)) {
                    ListTag tagsList = nbt.getList("Tags", 8);
                    for (net.minecraft.nbt.Tag value : tagsList) {
                        if (value instanceof StringTag tag) {
                            if (KNOCKBACK_SILVERFISH_TAG.equals(tag.getAsString())) {
                                hurtOne.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 1, 100, false, false, false));
                                Vec3 hurtEntityPos = hurtOne.position();
                                Vec3 sourceEntityPos = attacker.position();
                                Vec3 directionVector = sourceEntityPos.subtract(hurtEntityPos);
                                hurtOne.setDeltaMovement(hurtOne.getDeltaMovement().add(directionVector));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
