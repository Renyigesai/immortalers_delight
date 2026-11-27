package com.renyigesai.immortalers_delight.block.soul_infuser;

import com.renyigesai.immortalers_delight.client.particle.CircleTwinkleParticleOption;
import com.renyigesai.immortalers_delight.client.particle.SpiralSoulParticleOption;
import com.renyigesai.immortalers_delight.client.particle.TwinkleParticleOption;
import com.renyigesai.immortalers_delight.entities.projectile.HatredfireBoltEntity;
import com.renyigesai.immortalers_delight.entities.projectile.SoulFireballEntity;
import com.renyigesai.immortalers_delight.message.ImmortalersEffectMessage;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class FireballAttackAction extends SoulInfuserAttackAction{
    public FireballAttackAction(SoulInfuserAttackHelper attackHelper) {
        super(attackHelper);
        this.cooldown = 100;
        this.duration = 30;
        this.minDuration = 10;
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
        if (actionTime == 20) {
            fireSingleFireball(level, pos);
            for (int i = 0;i <= this.attackHelper.lostShootCount;i++) {
                this.attackHelper.shootSoulFireBallRandom(level,pos.above(2));
            }
            this.attackHelper.lostShootCount=0;
        }

    }

    private void fireSingleFireball(Level level, BlockPos pos) {
        LivingEntity target = this.attackHelper.getDestroyTarget();
        if (target == null || level.isClientSide) return;

        Vec3 lookDirection = target.getEyePosition().add(0,2,0).normalize();
        this.attackHelper.checkToolEntity();
        if (this.attackHelper.toolEntity == null) return;
        SoulFireballEntity fireball = new SoulFireballEntity(level,this.attackHelper.toolEntity,target);
        fireball.setBaseDamage(this.attackHelper.attackDamage);
//                new HatredfireBoltEntity(pos.getX(), pos.getY() + 2, pos.getZ(),
//                lookDirection.x * 0.5D, lookDirection.y * 0.5D,lookDirection.z * 0.5D,
//                level
//        );
//        fireball.setPos(pos.getX(), pos.getY() + 2, pos.getZ());
        level.addFreshEntity(fireball);
    }

    @Override
    public void animationTick() {
        int animationTime = this.attackHelper.getAnimationTime();
        BlockPos pos = this.attackHelper.getBlockPos();
        if (this.attackHelper.level() == null) return;
        Level level = this.attackHelper.level();

        if (!level.isClientSide()) return;
        if (animationTime == 20) {
            Vec3 center = pos.getCenter();
            spawnBurstParticles(level, ParticleTypes.SOUL_FIRE_FLAME, center.add(0,2,0), 12, 0.15, 0.03);
        }
    }


    /**
     * 生成"向外扩散"的粒子团：以某点为中心，向随机方向发散。
     */
    private void spawnBurstParticles(Level level, ParticleOptions particle,
                                     Vec3 origin, int count, double spread, double speed) {
        System.out.println("火球攻击生成粒子");
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
