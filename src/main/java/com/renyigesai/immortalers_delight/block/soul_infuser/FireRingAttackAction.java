package com.renyigesai.immortalers_delight.block.soul_infuser;

import com.google.common.collect.Maps;
import com.renyigesai.immortalers_delight.client.particle.CircleTwinkleParticleOption;
import com.renyigesai.immortalers_delight.client.particle.SpiralSoulParticleOption;
import com.renyigesai.immortalers_delight.client.particle.TwinkleParticleOption;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.message.ImmortalersEffectMessage;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//造成半径10的AOE法术伤害，冷却40s，持续5s，前摇3s
public class FireRingAttackAction extends SoulInfuserAttackAction {
//    protected boolean canUse = false;
//    public void setCanUse(boolean can) {this.canUse = can;}
    public FireRingAttackAction(SoulInfuserAttackHelper attackHelper) {
        super(attackHelper);
        this.duration = 30;
        this.nextReadyTime = 99999;
        this.minDuration = 25;
    }
//    @Override
//    public boolean isReady() {
//        return this.canUse;
//    }
    @Override
    public void actionTick() {
        super.actionTick();
        BlockPos pos = this.attackHelper.getBlockPos();
        if (this.attackHelper.level() == null) return;
        Level level = this.attackHelper.level();

        int attackTime = this.actionTime;
        if (level.isClientSide()) return;

        //振屏幕
        if (attackTime == 15) {
            List<ServerPlayer> targets = level.getEntitiesOfClass(
                    ServerPlayer.class, new AABB(pos).inflate(11),
                    e -> e.isAlive());
            for (ServerPlayer serverPlayer : targets) {
                ImmortalersNetwork.getChannel().send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new ImmortalersEffectMessage(4, 70 - attackTime / 2)
                );
            }
        }

        //AOE伤害
        float attack = this.attackHelper.attackDamage * (DifficultyModeUtil.isPowerBattleMode() ? 5 : 2);
        if (attackTime >= 16 && attackTime < 20) doSoulBurstAOE(level,pos,attack,20-3*(attackTime - 16));
    }

    @Override
    public void endAction() {
        super.endAction();
//        canUse = false;
        victims.clear();
    }

    protected final Map<Entity, Integer> victims = Maps.newHashMap(); // 记录受影响实体及下次可再次受影响的刻数
    List<LivingEntity> buffer = new ArrayList<>();
    private void doSoulBurstAOE(Level level, BlockPos pos, float attack, float range) {
        if (level.isClientSide) return;

        double damage = attack * 2.0D;
        List<LivingEntity> targets = new ArrayList<>(buffer);
        if (targets.isEmpty()) targets = level.getEntitiesOfClass(
                LivingEntity.class, new AABB(pos).inflate(25,4,25),
                e -> e.isAlive());

        Vec3 center = pos.above().getCenter();

        for (LivingEntity e : targets) {
            //只对没有伤害过的实体造成伤害
            if (victims.containsKey(e)) return;

            // 计算生物与效果云中心的水平距离平方（优化：避免开方）
            double d8 = e.getX() - pos.getCenter().x();
            double d1 = e.getZ() - pos.getCenter().z();
            double d3 = d8 * d8 + d1 * d1;

            // 距离小于等于半径平方（在范围内）
            if (d3 <= 625 && d3 >= range * range) {
                e.hurt(level.damageSources().indirectMagic(e,e), (float) damage);
            }
        }
        level.playSound((Player)null, center.x(), center.y(),center.z(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1.5F, 1.0F);
    }


    @Override
    public void animationTick() {
        System.out.println("火环行为的客户端tick");
        int animationTime = this.attackHelper.getAnimationTime();
        BlockPos pos = this.attackHelper.getBlockPos();
        if (this.attackHelper.level() == null) return;
        Level level = this.attackHelper.level();

        if (!level.isClientSide()) return;
        Vec3 center = pos.getCenter();
        if (animationTime == 1) {
            spawnSwirlingRing(level, pos, ParticleTypes.CAMPFIRE_COSY_SMOKE, 25, 40, -0.143f, 0.05f);
            spawnSwirlingRing(level, pos.above(), ParticleTypes.SOUL_FIRE_FLAME, 25, 120, -1.013f, 0.2f);
        }
        if (animationTime == 5) spawnSwirlingRing(level, pos.above(), ParticleTypes.SOUL_FIRE_FLAME, 25, 120, -1.013f, 0.2f);
        //前摇期间特效
        if (animationTime < 15) {
            ParticleOptions soul_fire = ParticleTypes.SOUL_FIRE_FLAME;
            for (int i = 0;i < animationTime; i++) {
                float dx = level.getRandom().nextFloat() * 4 - 2;
                float dy = level.getRandom().nextFloat() * 3 - 0.5f;
                float dz = level.getRandom().nextFloat() * 4 - 2;
                level.addParticle(soul_fire,center.x() + dx,center.y() + dy,center.z() + dz,0,0,0);
            }
        }
        //后摇期间特效
        if (animationTime > 15 && animationTime <= 22) {
            int r = (27 - animationTime) * 2 + 1;
            spawnRing(level,pos,r);
        }
    }


    /**
     * 生成旋转的扩张/收缩粒子环
     *
     * @param level         世界实例（建议仅在客户端调用，避免网络发包风暴）
     * @param particleData  粒子类型（如 ParticleTypes.CRIT, ParticleTypes.SOUL_FIRE_FLAME 等）
     * @param radius        发射环的起始半径
     * @param particleCount 环上生成的粒子总数
     * @param radialSpeed   径向速度（> 0 为扩张环，< 0 为向中心收缩环，= 0 为纯切线飞溅）
     * @param tangentSpeed  切线速度（控制粒子沿边缘旋转的初速度大小，符号控制顺/逆时针）
     */
    public static void spawnSwirlingRing(
            Level level,
            BlockPos center,
            ParticleOptions particleData,
            double radius,
            int particleCount,
            double radialSpeed,
            double tangentSpeed
    ) {
        if (!level.isClientSide) return;

        Vec3 vec3 = center.getCenter();
        double centerX = vec3.x();
        double centerY = vec3.y();
        double centerZ = vec3.z();

        double angleStep = (Math.PI * 2.0) / particleCount;

        for (int i = 0; i < particleCount; i++) {
            double angle = i * angleStep;

            // 1. 计算粒子在圆周上的初始生成位置
            double cos = Mth.cos((float) angle);
            double sin = Mth.sin((float) angle);

            double spawnX = centerX + radius * cos;
            double spawnY = centerY;
            double spawnZ = centerZ + radius * sin;

            // 2. 矢量合成：径向速度(扩张/收缩) + 切向速度(旋转)
            double vx = radialSpeed * cos - tangentSpeed * sin;
            double vy = 0.0; // 维持水平，也可以加轻微随机浮动避免死板
            double vz = radialSpeed * sin + tangentSpeed * cos;

            // 3. 产生粒子
            level.addParticle(particleData, spawnX, spawnY, spawnZ, vx, vy, vz);
        }
    }

    /**
     * 在给定半径的圆周上均匀取点，对每个点做地面探测后生成粒子。
     */
    private void spawnRing(Level level,BlockPos pos,int radius) {

//        System.out.println("spawnRing尝试生成地面环");
        if (radius <= 0) return;

        // 根据半径动态计算角度步进，保证圆周上格子间距大致为 1 格，避免半径越大点越稀疏或半径小时点重叠过密
        // 圆周长 = 2 * PI * r，用 circumference 估算需要的采样点数
        double circumference = 2 * Math.PI * radius;
        int sampleCount = Math.max(8, (int) Math.ceil(circumference)); // 至少8个点，避免小半径时太稀疏

        // 用一个 Set 去重，因为角度采样在格子化后可能出现重复坐标（尤其小半径时）
        java.util.Set<Long> visited = new java.util.HashSet<>();

        for (int i = 0; i < sampleCount; i++) {
            double angle = (2 * Math.PI * i) / sampleCount;
            int dx = (int) Math.round(radius * Math.cos(angle));
            int dz = (int) Math.round(radius * Math.sin(angle));

            int worldX = pos.getX() + dx;
            int worldZ = pos.getZ() + dz;
            long key = (((long) worldX) << 32) ^ (worldZ & 0xffffffffL);

            if (!visited.add(key)) {
                continue; // 已处理过这个坐标，跳过（避免重复生成粒子造成密度不均）
            }

            trySpawnAt(level,pos,worldX, worldZ,true);
            if ((radius == 15 || radius == 21) && i % (radius / 3 - 3) == 0) trySpawnAt(level,pos,worldX, worldZ,false);
        }
    }

    /**
     * 在指定水平坐标上做地面探测，找到后在该格中心生成粒子。
     * 探测逻辑：以 center.getY() 为基准，向下搜索最近的"实心方块正上方的空气格"，
     * 若超过 maxHeightDiff 仍未找到，则放弃该坐标。
     */
    private void trySpawnAt(Level level, BlockPos pos, int worldX, int worldZ, boolean isRise) {
        int baseY = pos.getY();

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(worldX, baseY, worldZ);

        // 先检查基准高度本身是否已经是合适的地面
        BlockPos groundPos = findGround(level,mutablePos, baseY, 4);
        if (groundPos == null) {
//            System.out.println("trySpawnAt高度差超过限制");
            return; // 高度差超过限制，跳过该坐标
        }

        BlockState state = level.getBlockState(groundPos);
        if (state.isAir()) {
//            System.out.println("trySpawnAt发现地面是空气");
            return; // 理论上 findGround 已经保证非空气，这里是双重保险
        }

        spawnParticleAt(level,groundPos, isRise);
    }

    /**
     * 从 baseY 开始，先向下搜索，找不到再向上搜索，直到超过 maxHeightDiff。
     * 返回"实心方块正上方那一格"的坐标（即粒子应该生成的格子，而非方块本身所在格）。
     */
    private BlockPos findGround(Level level, BlockPos.MutableBlockPos mutablePos, int baseY, int maxHeightDiff) {

        // 向上搜索（应对地形是上坡的情况）
        for (int offset = 1; offset <= maxHeightDiff; offset++) {
            int y = baseY + offset;
            mutablePos.setY(y);
            BlockState state = level.getBlockState(mutablePos);
            if (!state.isAir() && state.isSolidRender(level, mutablePos)) {
                return mutablePos.immutable();
            }
        }
        // 向下搜索
        for (int offset = 0; offset <= maxHeightDiff; offset++) {
            int y = baseY - offset;
            mutablePos.setY(y);
            BlockState state = level.getBlockState(mutablePos);
            if (!state.isAir() && state.isSolidRender(level, mutablePos)) {
                return mutablePos.immutable();
            }
        }

//        System.out.println("findGround没有找到地面：" + mutablePos);
        return null; // 超出范围仍未找到地面
    }

    /**
     * 在指定格子中心生成 SHOCKED_BLOCK 粒子。
     */
    private void spawnParticleAt(Level level ,BlockPos pos, boolean isRise) {
        // 注意：这里的方块状态应该是"地面方块"本身的状态，而不是above()那一格的状态
        BlockState groundState = level.getBlockState(pos);
//        System.out.println("spawnParticleAt检查地面" + groundState);

        level.addParticle(
                new net.minecraft.core.particles.BlockParticleOption(isRise ? ImmortalersDelightParticleTypes.RISE_BLOCK.get() : ImmortalersDelightParticleTypes.SHOCKED_BLOCK.get(), groundState),
                false,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                0.0, 0.0, 0.0
        );
    }

}
