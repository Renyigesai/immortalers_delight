package com.renyigesai.immortalers_delight.entities;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityNbtHelper {
    public static final String KNOCKBACK_SILVERFISH_TAG = "imm_del_knockback_bug_1";
    public static final String BOOMER_SILVERFISH_TAG = "imm_del_boomer_bug_1";
    @SubscribeEvent
    public static void onCreatureHurt(LivingHurtEvent evt) {
        LivingEntity hurtOne = evt.getEntity();
        LivingEntity attacker = null;
        if (evt.getSource().getEntity() instanceof LivingEntity livingEntity){
            attacker = livingEntity;
        }
        if (evt.isCanceled() || evt.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
            return;
        }

        if (!hurtOne.level().isClientSide) {
            if (attacker != null){
                //float damage = evt.getAmount();
                // 检查实体是否是玩家，如果是则不处理（可根据需求调整）
                if (attacker instanceof Player) {
                    return;
                }

                // 获取生物的NBT数据
                CompoundTag nbt = attacker.getPersistentData();

                // 检测是否存在特定的NBT标签
                if (nbt.contains("Tags", 9)) {
                    ListTag tagsList = nbt.getList("Tags", 8);
                    for (net.minecraft.nbt.Tag value : tagsList) {
                        if (value instanceof StringTag tag) {
                            if (KNOCKBACK_SILVERFISH_TAG.equals(tag.getAsString())) {
                                hurtOne.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 1, 100, false, false, false));
                                // 获取受伤生物的坐标
                                Vec3 hurtEntityPos = hurtOne.position();
                                // 获取伤害来源生物的坐标
                                Vec3 sourceEntityPos = attacker.position();

                                // 计算方向矢量
                                Vec3 directionVector = sourceEntityPos.subtract(hurtEntityPos);
                                hurtOne.setDeltaMovement(hurtOne.getDeltaMovement().add(directionVector));
                                /*System.out.println("生物具有特定的NBT标签 {Tags: [test]}");*/
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

//    @SubscribeEvent
//    public static void onPlayerDig(PlayerEvent.BreakSpeed event) {
//        //这里是把挖掘速度设置为目标方块的硬度，也就是众生平等
//        //这个代码仅用于展示事件中可用的方法
//        event.setNewSpeed(event.getState().getDestroySpeed(event.getEntity().level(),event.getPosition().get()));
//    }
}
