package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.event.SnifferEvent;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;


public class SnifferBrushItem extends BrushItem {
    public SnifferBrushItem(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (pPlayer != null && this.calculateHitResult(pPlayer).getType() == HitResult.Type.ENTITY) {
            pPlayer.startUsingItem(pUsedHand);
        }

        return InteractionResult.CONSUME;
    }

    private HitResult calculateHitResult(LivingEntity pEntity) {
        return ProjectileUtil.getHitResultOnViewVector(pEntity, (p_281111_) -> {
            return !p_281111_.isSpectator() && p_281111_.isPickable();
        }, (Math.sqrt(ServerGamePacketListenerImpl.MAX_INTERACTION_DISTANCE) - 1.0D));
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pRemainingUseDuration >= 0 && pLivingEntity instanceof Player player) {
            HitResult hitResult = this.calculateHitResult(player);
            if (hitResult instanceof EntityHitResult entityHitResult && hitResult.getType() == HitResult.Type.ENTITY) {
                //被刷毛的嗅探兽不移动
                if (pRemainingUseDuration % 4 == 0 && pLevel instanceof ServerLevel && entityHitResult.getEntity() instanceof Sniffer sniffer) sniffer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 5, false,  false));

                if ((this.getUseDuration(pStack) - pRemainingUseDuration + 1) == this.getUseDuration(pStack)/2) {
                    Entity entity = entityHitResult.getEntity();
                    BlockPos blockPos = entity.blockPosition();
                    pLevel.playSound(player, blockPos, SoundEvents.BRUSH_GENERIC, SoundSource.BLOCKS);

                    //客户端渲染爱心粒子
                    if (entity instanceof Sniffer sniffer) {
                        double d0 = sniffer.getRandom().nextGaussian() * 0.05D;
                        double d1 = sniffer.getRandom().nextGaussian() * 0.05D;
                        double d2 = sniffer.getRandom().nextGaussian() * 0.05D;
                        for(int i = 0; i < sniffer.getRandom().nextInt(2, 4); ++i) {
                            sniffer.level().addParticle(ParticleTypes.HEART, sniffer.getRandomX(1.0D), sniffer.getRandomY() + 0.5D, sniffer.getRandomZ(1.0D), d0, d1, d2);
                        }
                    }

                    //处理服务端逻辑
                    if (pLevel instanceof ServerLevel serverLevel && entity instanceof Sniffer sniffer) {

                        int itemDamage = 0;
                        CompoundTag tag = sniffer.getPersistentData();

                        //被刷毛的嗅探兽回血
                        float needHPS = sniffer.getMaxHealth() * 0.05f;
                        int lv = 0;
                        for (int i = 0; i < 10; i++) {
                            if(0.4 * (2 << i) >= needHPS) {
                                lv = i;
                                break;
                            }
                        }
                        sniffer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 599, lv + 1));
                        //回血消耗刷子耐久
                        if (!player.getAbilities().instabuild) {
                            itemDamage +=10;
                        }

                        //初始化冷却nbt
                        if (tag.get(SnifferEvent.SNIFFER_BRUSHING_COOLDOWN) == null) tag.putInt(SnifferEvent.SNIFFER_BRUSHING_COOLDOWN, 0);

                        //判断刷毛冷却是否完毕
                        boolean flag = !tag.contains(SnifferEvent.SNIFFER_BRUSHING_COOLDOWN, Tag.TAG_INT) || tag.getInt(SnifferEvent.SNIFFER_BRUSHING_COOLDOWN) <= 0;
                        //刷毛具体逻辑，创造玩家无视冷却
                        if (player.getAbilities().instabuild || flag) {
                            //生成掉落物
                            BlockPos pos = player.getOnPos().above();
                            vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(pLevel,new ItemStack(ImmortalersDelightItems.SNIFFER_FUR.get()),
                                    pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
                            //生成掉毛粒子
                            spawnSnifferFurParticle(serverLevel,sniffer.getOnPos().above( ));
                            //刷毛消耗耐久，并设置冷却
                            if (!player.getAbilities().instabuild) {
                                itemDamage += 20;
                                tag.putInt(SnifferEvent.SNIFFER_BRUSHING_COOLDOWN, 1728 * (2 + player.getRandom().nextInt(4)));
                            }
                        }
                        //实际消耗耐久
                        if (!player.getAbilities().instabuild) {
                            EquipmentSlot equipmentslot = pStack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                            pStack.hurtAndBreak(itemDamage, player, (action) -> {
                                action.broadcastBreakEvent(equipmentslot);
                            });
                        }

                        if (!flag) pLivingEntity.releaseUsingItem();
                    }
                }
            } else {
                super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
            }
        } else {
            pLivingEntity.releaseUsingItem();
        }
    }

    private void spawnSnifferFurParticle(Level level, BlockPos pPos) {
        if (level instanceof ServerLevel serverLevel) {
            Vec3 center = new Vec3(pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5);
            double radius = 3.0;
            for (int i = 0; i < 16; i++) {
                double angle = 2 * Math.PI * Math.random();
                double r = radius * Math.sqrt(Math.random());
                double x = center.x + r * Math.cos(angle);
                double z = center.z + r * Math.sin(angle);
                double y = center.y + angle * 0.5F;
                serverLevel.sendParticles(
                        ImmortalersDelightParticleTypes.SNIFFER_FUR.get(), x, y, z, 1, 0, 0, 0, 0.025
                );
            }
        }
    }
}
