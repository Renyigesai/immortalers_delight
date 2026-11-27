package com.renyigesai.immortalers_delight.block.soul_infuser;

import com.renyigesai.immortalers_delight.entities.projectile.SoulFireHitboxEntity;
import com.renyigesai.immortalers_delight.entities.projectile.SoulFireballEntity;
import com.renyigesai.immortalers_delight.message.ImmortalersEffectMessage;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.renyigesai.immortalers_delight.entities.projectile.SoulFireballEntity;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class FireColumnAttackAction extends SoulInfuserAttackAction{

    private int countPerColumn = 0;   // 每列已生成的火柱的数量
    public FireColumnAttackAction(SoulInfuserAttackHelper attackHelper) {
        super(attackHelper);
        this.cooldown = 320;
        this.duration = 45;
        this.minDuration = 15;
    }

    @Override
    public void startAction() {
        this.countPerColumn = 0;
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
        //第一段攻击：球状散射火球
        if (actionTime == 2) {
            shootOmniSoulFireballBurst(level,pos.above(2).getCenter(), attackHelper.toolEntity, attackHelper.getDestroyTarget());
        }
        if (actionTime >= 32) {

            LivingEntity target = this.attackHelper.getDestroyTarget();
            if (target == null || level.isClientSide) return;

            this.attackHelper.checkToolEntity();
            if (this.attackHelper.toolEntity == null) return;

            float damage = this.attackHelper.attackDamage * (DifficultyModeUtil.isPowerBattleMode() ? 7.5f : 3f);
            castTridentSoulFireSpike(level, this.attackHelper.toolEntity, target, damage, countPerColumn);
            countPerColumn++;
            if (actionTime == 32) {
                LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
                if (lightningBolt != null) {
                    lightningBolt.moveTo((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, 0.0F, 0.0F);
                    lightningBolt.setVisualOnly(true);
                    level.addFreshEntity(lightningBolt);
                }
            }
        }

    }

    private void fireSingleFireball(Level level) {
        LivingEntity target = this.attackHelper.getDestroyTarget();
        if (target == null || level.isClientSide) return;

        this.attackHelper.checkToolEntity();
        if (this.attackHelper.toolEntity == null) return;
        SoulFireballEntity fireball = new SoulFireballEntity(level,this.attackHelper.toolEntity,target);
        level.addFreshEntity(fireball);
    }

    /**
     * 360度全方位 26 向弹幕爆发，并在原点发射第 27 发精准锁定核心弹
     *
     * @param level       服务端世界
     * @param originPos   弹幕爆发的核心中心点
     * @param caster      施法者实体（可为 null，用于伤害来源归属）
     * @param mainTarget  首要锁定目标（可为 null，若无则第 27 发朝正上方冲天发射）
     */
    public void shootOmniSoulFireballBurst(Level level, Vec3 originPos, @Nullable LivingEntity caster, @Nullable LivingEntity mainTarget) {
        if (level.isClientSide) return;

        // 遍历三维网格中的 27 个组合
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {

                    // ==========================================
                    // 分支一：处理 (0, 0, 0) -> 第 27 发核心锁定弹
                    // ==========================================
                    if (x == 0 && y == 0 && z == 0) {
                        fireSingleFireball(level);
                        continue;
                    }

                    // ==========================================
                    // 分支二：处理 26 个方位的扩散霰弹
                    // ==========================================
                    Vec3 dir = new Vec3(x, y, z).normalize();

                    // 初始微弱散开速度（0.45 格/tick），前 15 tick 会因阻力停在半径约 1.5 ~ 2 格的半空
                    double spreadSpeed = 0.45D;
                    Vec3 initialMotion = dir.scale(spreadSpeed);

                    SoulFireballEntity barrageBall = new SoulFireballEntity(
                            originPos.x, originPos.y, originPos.z,
                            initialMotion.x, initialMotion.y, initialMotion.z,
                            level
                    );

                    if (caster != null) {
                        barrageBall.setOwner(caster);
                    }

                    // 关键：将归一化方向绑定为降级发射朝向！
                    // 这确保了在没有实体目标锁定时，它在第 16 tick 会继续沿着这个方向做爆发式突进
                    barrageBall.setFallbackDirection(dir);
                    // 对于斜向下发射的，使用直线轨迹
                    barrageBall.setDangerous(y < 0);

                    barrageBall.setBaseDamage(this.attackHelper.attackDamage);
                    level.addFreshEntity(barrageBall);
                }
            }
        }

        // 爆发音效：强烈的地狱幽火喷发声
        level.playSound(null, originPos.x, originPos.y, originPos.z,
                SoundEvents.RESPAWN_ANCHOR_DEPLETE.get(), SoundSource.HOSTILE, 2.0F, 0.7F);
    }


    /**
     * 生成三列扇形延伸的灵魂地火刺
     *
     * @param level   施法世界
     * @param caster  施法者（不可为 null）
     * @param target  攻击目标（不可为 null）
     * @param damage  地火基础伤害
     */
    public void castTridentSoulFireSpike(Level level, LivingEntity caster, LivingEntity target, float damage, int warmupDelay) {
        if (level.isClientSide || caster == null || target == null) return;

        // 1. 垂直高度检测边界（限制在双方高度差区间，并给予合理缓冲，避免高低差过大导致搜索漏空）
        double minY = Math.min(target.getY(), caster.getY()) - 2.0D;
        double maxY = Math.max(target.getY(), caster.getY()) + 3.0D;

        // 2. 计算施法者正对目标的基准水平偏航角（弧度制）
        float baseAngle = (float) Mth.atan2(target.getZ() - caster.getZ(), target.getX() - caster.getX());

        // 3. 扇形参数配置
        // 30度角对应的弧度值 (30 * PI / 180)
        float angleStep = 30.0F * (float) Math.PI / 180.0F;
        // 三列分别对应：-30° (左)、0° (正对目标)、+30° (右)
        float[] columnAngles = new float[] {
                baseAngle - angleStep,
                baseAngle,
                baseAngle + angleStep
        };

        final double spacing = 3.5D;      // 间距 3.5 格
        final float hitboxRadius = 1.5F;  // 伤害半径 1.5 格

        // 4. 循环生成三列地火
        for (float angle : columnAngles) {
            float cos = Mth.cos(angle);
            float sin = Mth.sin(angle);

            // 距离从 1 个步长 (3.5格) 到 10 个步长 (35.0格)
            double dist = spacing * (warmupDelay + 1);
            double targetX = caster.getX() + (double) cos * dist;
            double targetZ = caster.getZ() + (double) sin * dist;

            // 波浪式延迟推进：每个地火递增 2 ticks (0.1秒)
            // 距离施法者越远，钻出地面的时机越晚，形成壮观的推进动画

            spawnSoulFireEntity(level, targetX, targetZ, minY, maxY, angle, warmupDelay, damage, hitboxRadius, caster);
        }
    }

    /**
     * 寻找坚固承载面并生成地火实体（含楼梯/台阶高度自适应）
     */
    private static void spawnSoulFireEntity(Level level, double pX, double pZ, double pMinY, double pMaxY,
                                            float pYRot, int pWarmupDelay, float damage, float radius, LivingEntity owner) {
        BlockPos checkPos = BlockPos.containing(pX, pMaxY, pZ);
        boolean foundGround = false;
        double surfaceYOffset = 0.0D;

        // 垂直自上而下探测合适的地表
        do {
            BlockPos belowPos = checkPos.below();
            BlockState belowState = level.getBlockState(belowPos);

            // 判断下方方块顶面是否坚固（可站立支撑）
            if (belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
                // 如果当前方块不是空气（如半砖、雪层、地毯），则获取其准确顶面高度
                if (!level.isEmptyBlock(checkPos)) {
                    BlockState currentState = level.getBlockState(checkPos);
                    VoxelShape collisionShape = currentState.getCollisionShape(level, checkPos);
                    if (!collisionShape.isEmpty()) {
                        surfaceYOffset = collisionShape.max(Direction.Axis.Y);
                    }
                }
                foundGround = true;
                break;
            }

            checkPos = checkPos.below();
        } while (checkPos.getY() >= Mth.floor(pMinY) - 1);

        // 如果成功找到坚固地面，生成实体
        if (foundGround) {
            double finalSpawnY = (double) checkPos.getY() + surfaceYOffset;

            SoulFireHitboxEntity entity = new SoulFireHitboxEntity(
                    level,
                    pX, finalSpawnY, pZ,
                    pYRot,
                    2 + pWarmupDelay,
                    damage,
                    owner
            );

            entity.setRadius(radius);
            level.addFreshEntity(entity);
        }
    }


    @Override
    public void animationTick() {
        int animationTime = this.attackHelper.getAnimationTime();
        BlockPos pos = this.attackHelper.getBlockPos();
        if (this.attackHelper.level() == null) return;
        Level level = this.attackHelper.level();

        if (!level.isClientSide()) return;
        if (animationTime == 10 || animationTime == 12 || animationTime == 15) {
            Vec3 center = pos.getCenter();
            spawnConvergingParticles(level, ParticleTypes.SOUL_FIRE_FLAME, center.add(0,2,0), 1, 30, 0.3);
        }
    }

    /**
     * 生成"向外扩散"的粒子效果：在球面上随机取点，计算指向中心的速度向量。
     */
    private void spawnConvergingParticles(Level level, ParticleOptions particle,
                                          Vec3 center, double radius, int count, double speed) {
        RandomSource random = level.random;
        for (int i = 0; i < count; i++) {
            double theta = random.nextDouble() * Math.PI * 2;
            double phi = Math.acos(2 * random.nextDouble() - 1);
            double px = center.x + radius * Math.sin(phi) * Math.cos(theta);
            double py = center.y + radius * Math.cos(phi);
            double pz = center.z + radius * Math.sin(phi) * Math.sin(theta);

            Vec3 toCenter = center.subtract(px, py, pz).normalize().scale(speed);
            level.addParticle(particle, center.x, center.y, center.z, toCenter.x, toCenter.y, toCenter.z);
        }
    }

}
