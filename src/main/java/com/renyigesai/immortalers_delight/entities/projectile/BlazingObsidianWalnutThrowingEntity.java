package com.renyigesai.immortalers_delight.entities.projectile;

import com.renyigesai.immortalers_delight.client.particle.ShockWaveParticleOption;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.item.food.obsidian_walnut.BlazingObsidianWalnutItem;
import com.renyigesai.immortalers_delight.potion.BaseMobEffect;
import com.renyigesai.immortalers_delight.potion.InfernalForgingMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

public class BlazingObsidianWalnutThrowingEntity extends ThrowableItemProjectile {
    private int explosionPower = 1;
    private boolean hasBoomed = false;
    private int explosionTime = 0;
    private static final EntityDataAccessor<Boolean> DATA_DANGEROUS = SynchedEntityData.defineId(BlazingObsidianWalnutThrowingEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_BOOM = SynchedEntityData.defineId(BlazingObsidianWalnutThrowingEntity.class, EntityDataSerializers.BOOLEAN);

    public BlazingObsidianWalnutThrowingEntity(EntityType<? extends BlazingObsidianWalnutThrowingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public BlazingObsidianWalnutThrowingEntity(Level pLevel, LivingEntity pShooter) {
        super(ImmortalersDelightEntities.BLAZING_OBSIDIAN_WALNUT.get(), pShooter, pLevel);
    }

    public BlazingObsidianWalnutThrowingEntity(Level pLevel, double pX, double pY, double pZ) {
        super(ImmortalersDelightEntities.BLAZING_OBSIDIAN_WALNUT.get(), pX, pY, pZ, pLevel);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_BOOM, false);
        builder.define(DATA_DANGEROUS, false);
    }

    public boolean isDangerous() {
        return this.entityData.get(DATA_DANGEROUS);
    }

    public boolean isBoom() {
        return this.entityData.get(DATA_BOOM);
    }

    public void setDangerous(boolean pInvulnerable) {
        this.entityData.set(DATA_DANGEROUS, pInvulnerable);
    }

    public void setBoom(boolean pInvulnerable) {
        this.entityData.set(DATA_BOOM, pInvulnerable);
    }

    protected Item getDefaultItem() {
        return ImmortalersDelightItems.BLAZING_OBSIDIAN_WALNUT.get();
    }

    private ParticleOptions getParticle() {
        ItemStack itemstack = this.getItem();
        return itemstack.isEmpty() ? ParticleTypes.FLAME : new ItemParticleOption(ParticleTypes.ITEM, itemstack);
    }

    public void handleEntityEvent(byte pId) {
        if (pId == 3) {
            ParticleOptions particleoptions = this.getParticle();
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public void tick() {
        super.tick();
        boolean flag = this.isBoom();
        if (!this.level().isClientSide()) {
            if (this.tickCount > 40 && !flag) {
                this.boom();
                this.explosionTime = this.tickCount;
            }
            if (flag) {
                this.setDeltaMovement(0, 0, 0);
            }
            if (this.tickCount > 20 + this.explosionTime) {
                this.discard();
                ItemEntity output = new ItemEntity(
                        this.level(),
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        new ItemStack(ImmortalersDelightItems.OBSIDIAN_WALNUT_KERNEL.get(), this.getItem().getCount()));
                output.setDeltaMovement(this.getDeltaMovement());
                this.level().addFreshEntity(output);
            }
        } else {
            if (flag && !this.hasBoomed) {
                this.sendParticle2002(this.random, this.getEyePosition());
                this.hasBoomed = true;
            }
        }
    }

    public int bitLength(int n) {
        return 32 - Integer.numberOfLeadingZeros(n);
    }

    public float boomDamage(LivingEntity livingEntity, boolean isPowerful) {
        int power = 4;
        MobEffectInstance instance = livingEntity.getEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES);
        if (instance != null && instance.getEffect().value() instanceof BaseMobEffect effect) {
            int lv = effect.getTruthUsingAmplifier(instance.getAmplifier()) + 1;
            int ex = 1 << lv;
            power += ex;
        }
        float f = isPowerful ? 0.65f * (this.bitLength(power)) : 1;
        return 7 * (f * f + f) * power + 1;
    }

    public void boom() {
        if (this.getOwner() instanceof LivingEntity livingEntity) {
            AttributeInstance attributeInstance = livingEntity.getAttribute(Attributes.ATTACK_DAMAGE);
            float damage = attributeInstance != null ? (float) attributeInstance.getValue() : 0f;
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            MobEffectInstance instance = livingEntity.getEffect(ImmortalersDelightMobEffect.INFERNAL_FORGING);
            if (damage > 0 && instance != null && instance.getEffect().value() instanceof BaseMobEffect effect) {
                damage *= 1 + InfernalForgingMobEffect.getEntitiesWithStacks().getOrDefault(livingEntity.getUUID(), (byte) 0)
                        * (effect.getTruthUsingAmplifier(instance.getAmplifier()) + 2) * 0.03;
            }
            float baseDamage = this.boomDamage(livingEntity, isPowerful);
            damage += baseDamage;
            float range = this.bitLength((int) baseDamage);

            BlazingObsidianWalnutItem.performBlastAttack(livingEntity, this.getEyePosition(), range,
                    this.damageSources().explosion(this, livingEntity), damage,
                    true, false, true);
        } else {
            boolean flag = !EventHooks.canEntityGrief(this.level(), this.getOwner());
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float) this.explosionPower, flag, Level.ExplosionInteraction.MOB);
        }

        this.setBoom(true);
        this.explosionTime = this.tickCount;
    }

    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide) {
            if (!this.isBoom()) {
                this.boom();
            }
        }
    }

    public void sendParticle2002(RandomSource randomsource, Vec3 pPos) {
        Vec3 center = new Vec3(this.getX(), this.getY() + 0.5, this.getZ());
        float radius = 3.3f;
        for (int i = 0; i < 32; i++) {
            double angle = 2 * Math.PI * Math.random();
            double r = radius * Math.sqrt(Math.random());
            double x = center.x + r * Math.cos(angle);
            double z = center.z + r * Math.sin(angle);
            double y = center.y;
            if (r <= radius / 3) {
                this.level().addParticle(ParticleTypes.SMOKE, false, x, y, z, 0, 0.025, 0);
            } else {
                this.level().addParticle(ParticleTypes.LAVA, false, x, y, z, 0, 0.025, 0);
            }
        }
        ShockWaveParticleOption particleOption = new ShockWaveParticleOption(7);
        this.level().addAlwaysVisibleParticle(particleOption, false, this.getX(), this.getY() + 0.25, this.getZ(), 0.0D, 0.0D, 0.0D);

        for (int i = 0; i < 8; ++i) {
            float dx = 0;
            float dy = 0;
            float dz = 0;
            if (i >= 1) {
                if (i <= 6) {
                    dx = (float) Math.sin(i);
                    dz = (float) Math.cos(i);
                } else {
                    dy = 0.6f;
                }
            }
            this.level().addParticle(ImmortalersDelightParticleTypes.HUGE_SMOKE.get(), pPos.x + dx, pPos.y + dy, pPos.z + dz, randomsource.nextGaussian() * 0.15D, randomsource.nextDouble() * 0.2D, randomsource.nextGaussian() * 0.15D);
        }

        ParticleOptions particleoptions = ParticleTypes.LAVA;
        for (int k2 = 0; k2 < 100; ++k2) {
            double d13 = randomsource.nextDouble() * 4.0D;
            double d19 = randomsource.nextDouble() * Math.PI * 2.0D;
            double d25 = Math.cos(d19) * d13;
            double d30 = 0.01D + randomsource.nextDouble() * 0.5D;
            double d31 = Math.sin(d19) * d13;
            this.level().addParticle(particleoptions,
                    pPos.x + d25 * 0.1D, pPos.y + 0.3D, pPos.z + d31 * 0.1D,
                    d25, d30, d31);
        }

        this.level().playLocalSound(BlockPos.containing(pPos), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.NEUTRAL, 1.0F, randomsource.nextFloat() * 0.1F + 0.9F, false);
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putByte("ExplosionPower", (byte) this.explosionPower);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("ExplosionPower", 99)) {
            this.explosionPower = pCompound.getByte("ExplosionPower");
        }
    }
}
