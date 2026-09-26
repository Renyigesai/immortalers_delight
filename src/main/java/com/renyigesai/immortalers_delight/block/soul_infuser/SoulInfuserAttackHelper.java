package com.renyigesai.immortalers_delight.block.soul_infuser;

import com.google.common.collect.Maps;
import com.renyigesai.immortalers_delight.block.WrappedHandler;
import com.renyigesai.immortalers_delight.client.particle.CircleTwinkleParticleOption;
import com.renyigesai.immortalers_delight.client.particle.SpiralSoulParticleOption;
import com.renyigesai.immortalers_delight.client.particle.TwinkleParticleOption;
import com.renyigesai.immortalers_delight.entities.projectile.KiBlastEntity;
import com.renyigesai.immortalers_delight.entities.projectile.SoulFireballEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.message.ImmortalersEffectMessage;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import com.renyigesai.immortalers_delight.recipe.SoulInfuserRecipe;
import com.renyigesai.immortalers_delight.screen.SoulInfuserMenu;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
public class SoulInfuserAttackHelper {

    // ======================= Tick主逻辑 ======================== //
    //客户端tick
    public static void AnimationTick(Level level, BlockPos pos, int animationTime) {
        if (!level.isClientSide()) return;
        Vec3 center = pos.getCenter();
        //前摇期间特效
        if (animationTime < 20) {
            ParticleOptions spiralSoul = new SpiralSoulParticleOption(11);
            ParticleOptions circleTwinkle = new CircleTwinkleParticleOption(9);
            ParticleOptions twinkle = new TwinkleParticleOption(11);
            level.addParticle(spiralSoul,center.x(),center.y() + 2,center.z(),0,0,0);
            level.addParticle(circleTwinkle,center.x(),center.y() + 1,center.z(),0,0,0);
            level.addParticle(twinkle,center.x(),center.y() + 1,center.z(),0,0,0);
        } else if (animationTime < 40) {
            ParticleOptions circleTwinkle = new CircleTwinkleParticleOption(89);
            ParticleOptions twinkle = new TwinkleParticleOption(91);
            ParticleOptions soul_fire = ParticleTypes.SOUL_FIRE_FLAME;
            level.addParticle(twinkle,center.x(),center.y() - 1,center.z(),0,0,0);
            level.addParticle(circleTwinkle,center.x(),center.y() - 1,center.z(),0,0,0);
            float dx = level.getRandom().nextFloat() * 4 - 2;
            float dy = level.getRandom().nextFloat() * 3 - 0.5f;
            float dz = level.getRandom().nextFloat() * 4 - 2;
            level.addParticle(soul_fire,center.x() + dx,center.y() + dy,center.z() + dz,0,0,0);
            //注意振屏的逻辑在服务端发出
        }
        if (animationTime == 45) spawnSoulBurstWarningParticles(level,pos);
        //焦点动画时间为50~65tick
        //后摇期间特效
        if (animationTime == 70) AOEDamageParticles(level,pos);
        if (animationTime > 70 && animationTime <= 77) {
            int r = (animationTime - 70) * 2 + 1;
            spawnRing(level,pos,r);
        }

    }
    //服务端tick
    public static void AttackTick(Level level, BlockPos pos, int attackTime) {
        if (level.isClientSide()) return;
        //振屏幕
        if (attackTime == 20 || attackTime == 25 || attackTime == 30 || attackTime == 40) {
            List<ServerPlayer> targets = level.getEntitiesOfClass(
                    ServerPlayer.class, new AABB(pos).inflate(16),
                    e -> e.isAlive());
            for (ServerPlayer serverPlayer : targets) {
                ImmortalersNetwork.getChannel().send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new ImmortalersEffectMessage(4, 70 - attackTime / 2)
                );
            }
        }
        //释放大量灵魂弹
        if (attackTime >20 && attackTime <= 85) {
            shootSoulFireBallRandom(level,pos);
//            if (attackTime % 5 == 0) shootSoulFireBallToPos();
        }

        //AOE伤害
        if (attackTime == 55 || attackTime == 60 || attackTime == 65) doSoulBurstAOE(level,pos,30);

        //清空缓存的不受伤害的实体记录
        if (attackTime % 5 == 0) victims.entrySet().removeIf((p_287380_) -> {
            return p_287380_.getKey().tickCount >= p_287380_.getValue();
        });
    }


    public static void shootSoulFireBallRandom(Level level, BlockPos pos) {

        //发射灵魂弹实体
        // 1. 随机生成射弹的方向向量
        int i = level.getRandom().nextBoolean() ? 1 : -1;
        int j = level.getRandom().nextBoolean() ? 1 : -1;
        int k = level.getRandom().nextBoolean() ? 1 : -1;
        Vec3 lookDirection = new Vec3(
//                    1,1,1
                level.getRandom().nextFloat() * i,
                level.getRandom().nextFloat() * j,
                level.getRandom().nextFloat() * k
        );
        // 2. 后续逻辑：沿该方向生成投射物（示例）
        double spawnX = pos.getX() + 0.5f + lookDirection.x;
        double spawnY = pos.getY() + 0.5f + lookDirection.y;
        double spawnZ = pos.getZ() + 0.5f + lookDirection.z;
        SoulFireballEntity fireball = new SoulFireballEntity(spawnX,spawnY,spawnZ,
                lookDirection.x * 0.5D, lookDirection.y * 0.5D,lookDirection.z * 0.5D,
                level
        );

        if (DifficultyModeUtil.isPowerBattleMode()) fireball.setDangerous(true);
//            fireball.shootFromRotation(livingEntity, livingEntity.getXRot(), livingEntity.getYRot(), 0.0F, 1.5F, 1.0F);
//            fireball.setPos(spawnX, spawnY, spawn4 Z);
        level.addFreshEntity(fireball);
    }
    public static void shootSoulFireBallToPos(Level level, BlockPos pos, BlockPos targetPos) {
        double spawnX = pos.getX() + 0.5D;
        double spawnY = pos.getY() + 0.5D;
        double spawnZ = pos.getZ() + 0.5D;

        Vec3 targetCenter = new Vec3(
                targetPos.getX() + 0.5D,
                targetPos.getY() + 0.5D,
                targetPos.getZ() + 0.5D
        );
        Vec3 spawnPos = new Vec3(spawnX, spawnY, spawnZ);
        Vec3 delta = targetCenter.subtract(spawnPos);

        // 防止发射点与目标点重合导致normalize()产生NaN，进而使弹射物出现异常行为
        if (delta.lengthSqr() < 1.0E-4) return;

        Vec3 lookDirection = delta.normalize();

        SoulFireballEntity fireball = new SoulFireballEntity(spawnX, spawnY, spawnZ,
                lookDirection.x * 0.5D, lookDirection.y * 0.5D, lookDirection.z * 0.5D,
                level
        );

        if (DifficultyModeUtil.isPowerBattleMode()) fireball.setDangerous(true);
        level.addFreshEntity(fireball);
    }

    /**
     * 自身周围生成闪光粒子
     */
    private static void spawnSoulBurstWarningParticles(Level level, BlockPos pos) {
        Vec3 center = pos.above().getCenter();
        for (int i = 0;i < 20; i++) {
            float dx = level.getRandom().nextFloat() * 3 - 1.5f;
            float dy = level.getRandom().nextFloat() * 2;
            float dz = level.getRandom().nextFloat() * 3 - 1.5f;
            float xSpeed = dx * 0.02f;
            float ySpeed = dy * 0.02f;
            float zSpeed = dz * 0.02f;
            level.addParticle(ParticleTypes.SOUL, center.x + dx, center.y + dy, center.z + dz, xSpeed,ySpeed,zSpeed);
            if (i <= 3) {
                level.addParticle(ParticleTypes.FLASH, center.x + dx, center.y + dy, center.z + dz,0,0,0);
            }
        }

    }

    protected static final Map<Entity, Integer> victims = Maps.newHashMap(); // 记录受影响实体及下次可再次受影响的刻数
    private static void doSoulBurstAOE(Level level, BlockPos pos, float attack) {
            if (level.isClientSide) return;

        double damage = attack * 2.0D;
        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class, new AABB(pos).inflate(16),
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
            if (d3 <= 225) {
                e.hurt(level.damageSources().indirectMagic(e,e), (float) damage);
                if (level instanceof ServerLevel serverLevel) {
                    Vec3 targetPos = e.getEyePosition();
                    spawnLightningLineParticles(serverLevel, ParticleTypes.SOUL, center, targetPos, 6, 0.6);
                    //缓存伤害过的实体避免重复伤害
                    victims.put(e, e.tickCount + 20);
                }
            }
        }
        level.playSound((Player)null, center.x(), center.y(),center.z(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1.5F, 1.0F);
    }

    private static void AOEDamageParticles(Level level, BlockPos pos) {

        int count = 0;
        Vec3 center = pos.above().getCenter();
        // 爆炸粒子：自身周围
        level.addParticle(ParticleTypes.EXPLOSION_EMITTER, center.x, center.y, center.z, 0, 0, 0);
        do {
            level.addParticle(ParticleTypes.EXPLOSION, center.x, center.y, center.z, 0.5, 0.5, 0.5);
            //生成打向随机位置的闪电状特效
            float lastAngle = level.getRandom().nextFloat();
            float f2 = (lastAngle * ((float)Math.PI * 2F)); // 旋转角度
            float f3 = 6 + level.getRandom().nextFloat() * 4; // 距离
            double d0 = center.x() + (double)(Mth.cos(f2) * f3); // X坐标
            double d2 = center.y() + 10.1 - f3; // Y坐标
            double d4 = center.z() + (double)(Mth.sin(f2) * f3); // Z坐标
            Vec3 targetPos = new Vec3(d0,
                    d2,
                    d4
            );
            count++;
            spawnLightningLineParticles(level, ParticleTypes.SOUL, center, targetPos, 6, 0.6);
        } while (count <= 8);
    }

//    /**
//     * 在给定半径的圆周上均匀取点，对每个点做地面探测后生成粒子。
//     */
//    private void spawnRing(int radius) {
//        if (radius <= 0) return;
//
//        // 根据半径动态计算角度步进，保证圆周上格子间距大致为 1 格，避免半径越大点越稀疏或半径小时点重叠过密
//        // 圆周长 = 2 * PI * r，用 circumference 估算需要的采样点数
//        double circumference = 2 * Math.PI * radius;
//        int sampleCount = Math.max(8, (int) Math.ceil(circumference)); // 至少8个点，避免小半径时太稀疏
//
//        // 用一个 Set 去重，因为角度采样在格子化后可能出现重复坐标（尤其小半径时）
//        java.util.Set<Long> visited = new java.util.HashSet<>();
//
//        for (int i = 0; i < sampleCount; i++) {
//            double angle = (2 * Math.PI * i) / sampleCount;
//            int dx = (int) Math.round(radius * Math.cos(angle));
//            int dz = (int) Math.round(radius * Math.sin(angle));
//
//            int worldX = this.getOnPos().above().getX() + dx;
//            int worldZ = this.getOnPos().above().getZ() + dz;
//            long key = (((long) worldX) << 32) ^ (worldZ & 0xffffffffL);
//
//            if (!visited.add(key)) {
//                continue; // 已处理过这个坐标，跳过（避免重复生成粒子造成密度不均）
//            }
//
//            trySpawnAt(worldX, worldZ);
//        }
//    }
//
//    /**
//     * 在指定水平坐标上做地面探测，找到后在该格中心生成粒子。
//     * 探测逻辑：以 center.getY() 为基准，向下搜索最近的"实心方块正上方的空气格"，
//     * 若超过 maxHeightDiff 仍未找到，则放弃该坐标。
//     */
//    private void trySpawnAt(int worldX, int worldZ) {
//        int baseY = this.getOnPos().above().getY();
//
//        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(worldX, baseY, worldZ);
//
//        // 先检查基准高度本身是否已经是合适的地面
//        BlockPos groundPos = findGround(mutablePos, baseY, 4);
//        if (groundPos == null) {
//            return; // 高度差超过限制，跳过该坐标
//        }
//
//        BlockState state = level.getBlockState(groundPos);
//        if (state.isAir()) {
//            return; // 理论上 findGround 已经保证非空气，这里是双重保险
//        }
//
//        spawnParticleAt(groundPos, state);
//    }
//
//    /**
//     * 从 baseY 开始，先向下搜索，找不到再向上搜索，直到超过 maxHeightDiff。
//     * 返回"实心方块正上方那一格"的坐标（即粒子应该生成的格子，而非方块本身所在格）。
//     */
//    private BlockPos findGround(BlockPos.MutableBlockPos mutablePos, int baseY, int maxHeightDiff) {
//        // 向下搜索
//        for (int offset = 0; offset <= maxHeightDiff; offset++) {
//            int y = baseY - offset;
//            mutablePos.setY(y);
//            BlockState state = level.getBlockState(mutablePos);
//            if (!state.isAir() && state.isSolidRender(level, mutablePos)) {
//                // 找到实心地面，返回其正上方一格
//                return mutablePos.above().immutable();
//            }
//        }
//
//        // 向上搜索（应对地形是上坡的情况）
//        for (int offset = 1; offset <= maxHeightDiff; offset++) {
//            int y = baseY + offset;
//            mutablePos.setY(y);
//            BlockState state = level.getBlockState(mutablePos);
//            if (!state.isAir() && state.isSolidRender(level, mutablePos)) {
//                return mutablePos.above().immutable();
//            }
//        }
//
//        return null; // 超出范围仍未找到地面
//    }
//
//    /**
//     * 在指定格子中心生成震起的方块 粒子。
//     */
//    private void spawnParticleAt(Level level, BlockPos pos, BlockState state) {
//        // 注意：这里的方块状态应该是"地面方块"本身的状态，而不是above()那一格的状态
//        BlockState groundState = level.getBlockState(pos.below());
//
//        level.addParticle(
//                new net.minecraft.core.particles.BlockParticleOption(ImmortalersDelightParticleTypes.RISE_BLOCK.get(), groundState),
//                false,
//                pos.getX() + 0.5,
//                pos.getY() + 0.5,
//                pos.getZ() + 0.5,
//                0.0, 0.0, 0.0
//        );
//    }


    /**
     * 在给定半径的圆周上均匀取点，对每个点做地面探测后生成粒子。
     */
    private static void spawnRing(Level level,BlockPos pos,int radius) {

        System.out.println("spawnRing尝试生成地面环");
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

            trySpawnAt(level,pos,worldX, worldZ);
        }
    }

    /**
     * 在指定水平坐标上做地面探测，找到后在该格中心生成粒子。
     * 探测逻辑：以 center.getY() 为基准，向下搜索最近的"实心方块正上方的空气格"，
     * 若超过 maxHeightDiff 仍未找到，则放弃该坐标。
     */
    private static void trySpawnAt(Level level, BlockPos pos, int worldX, int worldZ) {
        int baseY = pos.getY();

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(worldX, baseY, worldZ);

        // 先检查基准高度本身是否已经是合适的地面
        BlockPos groundPos = findGround(level,mutablePos, baseY, 4);
        if (groundPos == null) {
            System.out.println("trySpawnAt高度差超过限制");
            return; // 高度差超过限制，跳过该坐标
        }

        BlockState state = level.getBlockState(groundPos);
        if (state.isAir()) {
            System.out.println("trySpawnAt发现地面是空气");
            return; // 理论上 findGround 已经保证非空气，这里是双重保险
        }

        spawnParticleAt(level,groundPos, state);
    }

    /**
     * 从 baseY 开始，先向下搜索，找不到再向上搜索，直到超过 maxHeightDiff。
     * 返回"实心方块正上方那一格"的坐标（即粒子应该生成的格子，而非方块本身所在格）。
     */
    private static BlockPos findGround(Level level, BlockPos.MutableBlockPos mutablePos, int baseY, int maxHeightDiff) {

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

        System.out.println("findGround没有找到地面：" + mutablePos);
        return null; // 超出范围仍未找到地面
    }

    /**
     * 在指定格子中心生成 SHOCKED_BLOCK 粒子。
     */
    private static void spawnParticleAt(Level level ,BlockPos pos, BlockState state) {
        // 注意：这里的方块状态应该是"地面方块"本身的状态，而不是above()那一格的状态
        BlockState groundState = level.getBlockState(pos);
        System.out.println("spawnParticleAt检查地面" + groundState);

        level.addParticle(
                new net.minecraft.core.particles.BlockParticleOption(ImmortalersDelightParticleTypes.RISE_BLOCK.get(), groundState),
                false,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                0.0, 0.0, 0.0
        );
    }


    // =========================== 批量生成粒子的工具方法 =========================== //
    /**
     * 生成"向中心聚拢"的粒子效果：在球面上随机取点，计算指向中心的速度向量。
     */
    private static void spawnConvergingParticles(Level level, ParticleOptions particle,
                                          Vec3 center, double radius, int count, double speed) {
        RandomSource random = level.random;
        for (int i = 0; i < count; i++) {
            double theta = random.nextDouble() * Math.PI * 2;
            double phi = Math.acos(2 * random.nextDouble() - 1);
            double px = center.x + radius * Math.sin(phi) * Math.cos(theta);
            double py = center.y + radius * Math.cos(phi);
            double pz = center.z + radius * Math.sin(phi) * Math.sin(theta);

            Vec3 toCenter = center.subtract(px, py, pz).normalize().scale(speed);
            level.addParticle(particle, px, py, pz, toCenter.x, toCenter.y, toCenter.z);
        }
    }

    /**
     * 生成"向外扩散"的粒子团：以某点为中心，向随机方向发散。
     */
    private static void spawnBurstParticles(Level level, ParticleOptions particle,
                                     Vec3 origin, int count, double spread, double speed) {
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

    /**
     * 生成放射状（类似烟花）粒子：从中心沿多个方向直线状喷出。
     */
    private static void spawnRadialParticles(Level level, ParticleOptions particle,
                                      Vec3 center, int rays, int particlesPerRay, double maxDist) {
        RandomSource random = level.random;
        for (int i = 0; i < rays; i++) {
            double angle = (Math.PI * 2 / rays) * i;
            double dx = Math.cos(angle);
            double dz = Math.sin(angle);
            for (int j = 1; j <= particlesPerRay; j++) {
                double dist = maxDist * j / particlesPerRay;
                double px = center.x + dx * dist;
                double py = center.y + random.nextDouble() * dist;
                double pz = center.z + dz * dist;
                level.addParticle(particle, px, py, pz, dx * 0.02, 0.01, dz * 0.02);
            }
        }
    }

    /**
     * 生成"闪电状折线"粒子路径：从起点到终点之间生成若干段随机偏移的折线。
     */
    private static void spawnLightningLineParticles(Level level, ParticleOptions particle,
                                             Vec3 start, Vec3 end, int segments, double jitter) {
        if (level instanceof ServerLevel serverLevel) {
            Vec3 prev = start;
            for (int i = 1; i <= segments; i++) {
                double t = (double) i / segments;
                Vec3 base = start.lerp(end, t);
                // 每个中间点加入随机偏移，末端不偏移，保证落点精确
                Vec3 offset = (i == segments) ? Vec3.ZERO : new Vec3(
                        (level.random.nextDouble() - 0.5) * jitter,
                        (level.random.nextDouble() - 0.5) * jitter,
                        (level.random.nextDouble() - 0.5) * jitter);
                Vec3 point = base.add(offset);

                // 在 prev -> point 之间插值撒点，形成连续折线
                int subSteps = 4;
                for (int s = 0; s <= subSteps; s++) {
                    Vec3 p = prev.lerp(point, (double) s / subSteps);
                    serverLevel.sendParticles(particle, p.x, p.y, p.z, 1, 0, 0, 0, 0.0);
                }
                prev = point;
            }
        }
        else if (level.isClientSide()){
            RandomSource random = level.random;
            Vec3 prev = start;
            for (int i = 1; i <= segments; i++) {
                double t = (double) i / segments;
                Vec3 base = start.lerp(end, t);
                Vec3 offset = (i == segments) ? Vec3.ZERO : new Vec3(
                        (random.nextDouble() - 0.5) * jitter,
                        (random.nextDouble() - 0.5) * jitter,
                        (random.nextDouble() - 0.5) * jitter);
                Vec3 point = base.add(offset);

                int subSteps = 4;
                for (int s = 0; s <= subSteps; s++) {
                    Vec3 p = prev.lerp(point, (double) s / subSteps);
                    level.addParticle(particle, p.x, p.y, p.z, 0.0, 0.0, 0.0);
                }
                prev = point;
            }
        }
    }
}
