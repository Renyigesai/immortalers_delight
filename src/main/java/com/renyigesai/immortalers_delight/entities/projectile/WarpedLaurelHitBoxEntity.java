package com.renyigesai.immortalers_delight.entities.projectile;

import com.renyigesai.immortalers_delight.client.particle.SpiralSoulParticleOption;
import com.renyigesai.immortalers_delight.fluid.ImmortalersDelightFluids;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class WarpedLaurelHitBoxEntity extends EffectCloudBaseEntity{
    protected int damageAmplification = 0;
    public int getDamageAmp() { return this.damageAmplification;}
    public void setDamageAmp(int damageAmp) { this.damageAmplification = damageAmp;}
    public WarpedLaurelHitBoxEntity(EntityType<? extends EffectCloudBaseEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public WarpedLaurelHitBoxEntity(Level pLevel, double pX, double pY, double pZ) {
        this(ImmortalersDelightEntities.WARPED_LAUREL_HITBOX.get(), pLevel);
        this.reapplicationDelay = 10;
        this.setPos(pX, pY, pZ); // 设置初始位置
    }

    @Override
    public void doOnStart(float range) {
        if (!this.level().isClientSide()) {
            spawnHotSpring(this.level(), this.blockPosition(), 2, 2, 2);
        }
    }

    @Override
    public boolean doAdditionalAction(float range) {
        if (this.level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 8; i++) {
                SpiralSoulParticleOption particleOption = new SpiralSoulParticleOption((int) (this.getRadius() + 1));
                Vec3 vec3 = new Vec3(this.getX() - 0.5f + serverLevel.random.nextFloat(),this.getY() + 0.7f + serverLevel.random.nextFloat(),this.getZ() - 0.5f + serverLevel.random.nextFloat());
                serverLevel.sendParticles(
                        particleOption, vec3.x, vec3.y, vec3.z, 1, 0, 0, 0, 0.025
                );
            }
        }
        if (isDangerous()) {
            // 获取碰撞箱半径内的所有生物实体
            List<LivingEntity> list1 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
            if (!list1.isEmpty()) {
                for(LivingEntity livingentity : list1) {
                    LivingEntity caster = this.getOwner(); // 获取效果所有者
                    if (livingentity.isAlive() && livingentity != caster) {
                        if (caster == null || (!caster.isAlliedTo(livingentity) && !livingentity.isAlliedTo(caster))) {

                            // 计算生物与效果云中心的水平距离平方（优化：避免开方）
                            double d8 = livingentity.getX() - this.getX();
                            double d1 = livingentity.getZ() - this.getZ();
                            double d3 = d8 * d8 + d1 * d1;

                            // 距离小于等于半径平方（在范围内）
                            if (d3 <= (double)(range * range)) {
                                //对范围内的生物造成伤害
                                //造成目标生命上限0.5%的伤害，伤害每级+100%
                                float damage = livingentity.getMaxHealth() * 0.005f;

                                int amp = this.getDamageAmp();
                                damage += damage * amp;
                                if (DifficultyModeUtil.isPowerBattleMode() && damage < 4) damage = 4;

                                //如果伤害将杀死目标，使用有源伤害；否则造成无来源岩浆伤害
                                DamageSource source = livingentity.damageSources().lava();
                                if (damage >= livingentity.getHealth()) {
                                    if (caster instanceof Player player) source = player.damageSources().playerAttack(player);
                                    else if (caster != null) source = caster.damageSources().mobAttack(caster);
                                }

                                livingentity.hurt(source, damage);
                                //造成伤害会令目标的燃烧时间叠加
                                if (!livingentity.fireImmune()) livingentity.setSecondsOnFire(Math.max(15 * (amp + 2), livingentity.getRemainingFireTicks() / 20 + 15));
                            }
                        }
                    }
                }
            }
        }
        return super.doAdditionalAction(range);
    }


    public void spawnHotSpring(Level pLevel, BlockPos pPos, int x, int y, int z) {
        if (this.isDangerous() || pLevel.isClientSide()) return;
        for (int i = -x; i <= x; i++) {
            for (int j = -y; j <= y; j++) {
                for (int k = -z; k <= z; k++) {
                    if (i != 0 || j > 0 || k != 0) {
                        BlockPos blockPos = pPos.offset(i,j,k);
                        BlockState blockState = pLevel.getBlockState(blockPos);
                        if (blockState.is(Blocks.WATER)) {
                            pLevel.setBlock(blockPos, ImmortalersDelightFluids.HOT_SPRING.get().defaultFluidState().createLegacyBlock(), 11);
                            //pLevel.setBlockAndUpdate(blockPos, Blocks.NETHERRACK.defaultBlockState());
                        }
                    }
                }
            }
        }
    }

    public float getAnimationProgress(float pPartialTicks) {
        int i = this.getWaitTime() - this.lifeTicks;
        if (i <= 0) {
            return (this.lifeTicks + pPartialTicks) / this.getWaitTime();
        }
        return 1.0F - ((float)i - pPartialTicks) / this.getWaitTime();
    }
}
