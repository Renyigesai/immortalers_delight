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

import java.util.List;
import java.util.Map;

//灵魂熔炉的出场爆炸，这个技能没有正常的冷却，在出场时与半血时强制触发
public class AwakenAttackAction extends SoulInfuserAttackAction {
    public AwakenAttackAction(SoulInfuserAttackHelper attackHelper) {super(attackHelper);}

    @Override
    public void actionTick() {
        super.actionTick();
        BlockPos pos = this.attackHelper.getBlockPos();
        if (this.attackHelper.level() == null) return;
        Level level = this.attackHelper.level();

        int attackTime = this.actionTime;
        if (level.isClientSide()) return;

        //额外刷新动画，因为从世界加载的时候会有bug，一开始客户端的世界会是null
        if (attackTime == 18) {
            //利用方块状态同步到客户端，动画状态切换为无动画
            BlockState state = level.getBlockState(pos);
            if (state.hasProperty(SoulInfuserBlock.AGE)) level.setBlockAndUpdate(pos, state.setValue(SoulInfuserBlock.AGE,SoulInfuserAttackHelper.ID_NONE));
        }
        if (attackTime == 20) {
            //然后马上再切回来
            BlockState state = level.getBlockState(pos);
            if (state.hasProperty(SoulInfuserBlock.AGE)) level.setBlockAndUpdate(pos, state.setValue(SoulInfuserBlock.AGE,SoulInfuserAttackHelper.ID_AWAKEN));
        }

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
        if (attackTime >24 && attackTime <= 56) {
            this.attackHelper.shootSoulFireBallRandom(level,pos.above(2));
//            if (attackTime % 5 == 0) shootSoulFireBallToPos();
        }

        //AOE伤害
        if (attackTime == 60 || attackTime == 65 || attackTime == 70) doSoulBurstAOE(level,pos,this.attackHelper.attackDamage);

        //清空缓存的不受伤害的实体记录
        if (attackTime % 5 == 0) victims.entrySet().removeIf((p_287380_) -> {
            return p_287380_.getKey().tickCount >= p_287380_.getValue();
        });
    }

    @Override
    public void endAction() {
        super.endAction();
        victims.clear();
    }

    protected final Map<Entity, Integer> victims = Maps.newHashMap(); // 记录受影响实体及下次可再次受影响的刻数
    private void doSoulBurstAOE(Level level, BlockPos pos, float attack) {
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
                e.hurt(level.damageSources().indirectMagic(e,e), (float) damage * (DifficultyModeUtil.isPowerBattleMode() ? 15 : 10));
                if (level instanceof ServerLevel serverLevel) {
                    Vec3 targetPos = e.getEyePosition();
                    //缓存伤害过的实体避免重复伤害
                    victims.put(e, e.tickCount + 20);
                }
            }
        }
        level.playSound((Player)null, center.x(), center.y(),center.z(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1.5F, 1.0F);
    }


    @Override
    public void animationTick() {

        System.out.println("出场爆炸的客户端tick");
        int animationTime = this.attackHelper.getAnimationTime();
        BlockPos pos = this.attackHelper.getBlockPos();
        if (this.attackHelper.level() == null) return;
        Level level = this.attackHelper.level();

        if (!level.isClientSide()) return;
        Vec3 center = pos.getCenter();
        //前摇期间特效
        if (animationTime < 15) {
            ParticleOptions spiralSoul = new SpiralSoulParticleOption(11);
            ParticleOptions circleTwinkle = new CircleTwinkleParticleOption(89);
            ParticleOptions twinkle = new TwinkleParticleOption(91);
            level.addParticle(spiralSoul,center.x(),center.y() + 2,center.z(),0,0,0);
            level.addParticle(circleTwinkle,center.x(),center.y() - 1,center.z(),0,0,0);
            level.addParticle(twinkle,center.x(),center.y() - 1,center.z(),0,0,0);
        } else if (animationTime < 25) {
            ParticleOptions circleTwinkle = new CircleTwinkleParticleOption(169);
            ParticleOptions twinkle = new TwinkleParticleOption(171);
            ParticleOptions circleTwinkle2 = new CircleTwinkleParticleOption(9);
            ParticleOptions twinkle2 = new TwinkleParticleOption(11);
            ParticleOptions soul_fire = ParticleTypes.SONIC_BOOM;
            level.addParticle(twinkle,center.x(),center.y() - 3,center.z(),0,0,0);
            level.addParticle(circleTwinkle,center.x(),center.y() - 3,center.z(),0,0,0);
            level.addParticle(twinkle2,center.x(),center.y() + 1,center.z(),0,0,0);
            level.addParticle(circleTwinkle2,center.x(),center.y() + 1,center.z(),0,0,0);
            float dx = level.getRandom().nextFloat() * 8 - 4;
            float dy = level.getRandom().nextFloat() * 6 - 1;
            float dz = level.getRandom().nextFloat() * 8 - 4;
            level.addParticle(soul_fire,center.x() + dx,center.y() + dy,center.z() + dz,0,0,0);
            //注意振屏的逻辑在服务端发出
        }
        if (animationTime == 30) spawnSoulBurstWarningParticles(level,pos);
        //焦点动画时间为30~45tick
        //后摇期间特效
        if (animationTime == 55) AOEDamageParticles(level,pos);
        if (animationTime > 57 && animationTime <= 62) {
            int r = (animationTime - 55) * 2 + 1;
            spawnRing(level,pos,r);
        }
    }

    /**
     * 自身周围生成闪光粒子
     */
    private void spawnSoulBurstWarningParticles(Level level, BlockPos pos) {
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

    /**
    * 爆炸瞬间生成爆炸粒子
    */
    private void AOEDamageParticles(Level level, BlockPos pos) {

        int count = 0;
        Vec3 center = pos.above().getCenter();
        // 爆炸粒子：自身周围
        level.addParticle(ParticleTypes.EXPLOSION_EMITTER, center.x, center.y, center.z, 0, 0, 0);
        do {
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
            level.addParticle(ParticleTypes.EXPLOSION_EMITTER, targetPos.x, targetPos.y, targetPos.z, 0.5, 0.5, 0.5);
            count++;
        } while (count <= 8);
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

            trySpawnAt(level,pos,worldX, worldZ);
        }
    }

    /**
     * 在指定水平坐标上做地面探测，找到后在该格中心生成粒子。
     * 探测逻辑：以 center.getY() 为基准，向下搜索最近的"实心方块正上方的空气格"，
     * 若超过 maxHeightDiff 仍未找到，则放弃该坐标。
     */
    private void trySpawnAt(Level level, BlockPos pos, int worldX, int worldZ) {
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

        spawnParticleAt(level,groundPos, state);
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
    private void spawnParticleAt(Level level ,BlockPos pos, BlockState state) {
        // 注意：这里的方块状态应该是"地面方块"本身的状态，而不是above()那一格的状态
        BlockState groundState = level.getBlockState(pos);
//        System.out.println("spawnParticleAt检查地面" + groundState);

        level.addParticle(
                new net.minecraft.core.particles.BlockParticleOption(ImmortalersDelightParticleTypes.RISE_BLOCK.get(), groundState),
                false,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                0.0, 0.0, 0.0
        );
    }

}
