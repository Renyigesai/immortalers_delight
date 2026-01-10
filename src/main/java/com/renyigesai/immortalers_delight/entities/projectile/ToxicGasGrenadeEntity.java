package com.renyigesai.immortalers_delight.entities.projectile;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.potion.GasPoisonMobEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ToxicGasGrenadeEntity extends ThrowableItemProjectile {
    public ToxicGasGrenadeEntity(EntityType<? extends ToxicGasGrenadeEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ToxicGasGrenadeEntity(Level pLevel, LivingEntity pShooter) {
        super(ImmortalersDelightEntities.CAUSTIC_ESSENTIAL_OIL.get(), pShooter, pLevel);
    }

    public ToxicGasGrenadeEntity(Level pLevel, double pX, double pY, double pZ) {
        super(ImmortalersDelightEntities.CAUSTIC_ESSENTIAL_OIL.get(), pX, pY, pZ, pLevel);
    }

    protected Item getDefaultItem() {
        return ImmortalersDelightItems.CAUSTIC_ESSENTIAL_OIL.get();
    }

    private ParticleOptions getParticle() {
        ItemStack itemstack = this.getItemRaw();
        return (ParticleOptions)(itemstack.isEmpty() ? ImmortalersDelightParticleTypes.KWAT.get() : new ItemParticleOption(ParticleTypes.ITEM, itemstack));
    }

    public void handleEntityEvent(byte pId) {
        if (pId == 3) {
            ParticleOptions particleoptions = this.getParticle();
            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity entity = pResult.getEntity();
        int i = entity instanceof LivingEntity living && living.getItemBySlot(EquipmentSlot.HEAD).is(ImmortalersDelightItems.GOLDEN_FABRIC_VEIL.get()) ? 0 : 3;
        entity.hurt(GasPoisonMobEffect.getDamageSource(entity, this.getOwner()), (float)i);
        makeAreaOfEffectCloud(this.level(), entity.blockPosition());
    }

    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            makeAreaOfEffectCloud(this.level(), this.blockPosition());
            this.discard();
        }

    }

    private void makeAreaOfEffectCloud(Level level, BlockPos pPos) {
        if (level.isClientSide()) return;
        EffectCloudBaseEntity effectCloud = new GasCloudEntity(level, pPos.getX(), pPos.getY(), pPos.getZ());

        effectCloud.setDangerous(true);
        effectCloud.setRadius(3.5F);
        effectCloud.setRadiusOnUse(-0.1F);
        effectCloud.setWaitTime(10);
        effectCloud.setRadiusPerTick(-(effectCloud.getRadius() / (float)effectCloud.getDuration()) * 2.0f);
        effectCloud.setParticle(ImmortalersDelightParticleTypes.KWAT.get());

        effectCloud.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.GAS_POISON.get(),600,0));

        level.addFreshEntity(effectCloud);
    }
}
