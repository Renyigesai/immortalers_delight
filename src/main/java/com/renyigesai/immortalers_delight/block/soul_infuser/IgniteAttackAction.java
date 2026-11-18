package com.renyigesai.immortalers_delight.block.soul_infuser;

import com.renyigesai.immortalers_delight.entities.projectile.HatredfireBoltEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class IgniteAttackAction extends SoulInfuserAttackAction{
    public IgniteAttackAction(SoulInfuserAttackHelper attackHelper) {
        super(attackHelper);
        this.cooldown = 140;
        this.duration = 40;
        this.minDuration = 20;
    }


    @Override
    public boolean isReady() {
        if (this.attackHelper.getDestroyTarget() == null || !this.attackHelper.getDestroyTarget().isAlive()) return false;
        else return super.isReady();
    }
    @Override
    public void actionTick() {
        super.actionTick();
        BlockPos pos = this.attackHelper.getBlockPos();
        if (this.attackHelper.level() == null) return;
        Level level = this.attackHelper.level();

        if (level.isClientSide()) return;
        if (actionTime == 26) {fireSingleFireball(level);}

    }

    private void fireSingleFireball(Level level) {
        LivingEntity target = this.attackHelper.getDestroyTarget();
        if (target == null || level.isClientSide) return;

        this.attackHelper.checkToolEntity();
        if (this.attackHelper.toolEntity == null) return;
        HatredfireBoltEntity fireball = new HatredfireBoltEntity(level,this.attackHelper.toolEntity,target,20);
        fireball.setBaseDamage(this.attackHelper.attackDamage * 2.5f);
        if (DifficultyModeUtil.isPowerBattleMode()) fireball.setDangerous(true);
        level.addFreshEntity(fireball);
    }

    @Override
    public void animationTick() {
        int animationTime = this.attackHelper.getAnimationTime();
        BlockPos pos = this.attackHelper.getBlockPos();
        if (this.attackHelper.level() == null) return;
        Level level = this.attackHelper.level();

        if (!level.isClientSide()) return;
        if (animationTime == 26) {
            Vec3 center = pos.getCenter();
            spawnBurstParticles(level, ImmortalersDelightParticleTypes.HATRED_FLAME.get(), center.add(0,2,0), 5, 0.3, 0.1);
        }
    }


    /**
     * 生成"向外扩散"的粒子团：以某点为中心，向随机方向发散。
     */
    private void spawnBurstParticles(Level level, ParticleOptions particle,
                                     Vec3 origin, int count, double spread, double speed) {
        System.out.println("塔露拉点燃攻击生成粒子");
        RandomSource random = level.random;
        for (int i = 0; i < count; i++) {
            double offsetX = random.nextGaussian() * spread;
            double offsetY = random.nextGaussian() * spread;
            double offsetZ = random.nextGaussian() * spread;

            double velX = random.nextGaussian() * speed;
            double velY = random.nextGaussian() * speed;
            double velZ = random.nextGaussian() * speed;

            level.addParticle(particle,
                    origin.x + offsetX, origin.y + offsetY, origin.z + offsetZ,
                    velX, velY, velZ);
        }
    }
}
