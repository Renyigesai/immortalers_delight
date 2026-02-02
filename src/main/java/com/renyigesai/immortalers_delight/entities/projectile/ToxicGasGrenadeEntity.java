package com.renyigesai.immortalers_delight.entities.projectile;

import com.renyigesai.immortalers_delight.init.*;
import com.renyigesai.immortalers_delight.potion.GasPoisonMobEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

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

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide) {
            this.makeAreaOfEffectCloud(this.level(),
                    pResult.getType() == HitResult.Type.ENTITY ?
                            ((EntityHitResult)pResult).getEntity().blockPosition()
                            : this.blockPosition());
            this.level().levelEvent(2002, this.blockPosition(), PotionUtils.getColor(this.getItem()));
            this.discard();
        }
    }

    private void makeAreaOfEffectCloud(Level level, BlockPos pPos) {
        if (level.isClientSide()) return;

        EffectCloudBaseEntity effectCloud = new GasCloudEntity(level, pPos.getX() + 0.5D, pPos.getY() + 0.2D, pPos.getZ() + 0.5D);

        effectCloud.setDangerous(true);
        effectCloud.setRadius(3.5F);
        effectCloud.setRadiusOnUse(-0.1F);
        effectCloud.setWaitTime(15);
        effectCloud.setRadiusPerTick(-(effectCloud.getRadius() / (float)effectCloud.getDuration()) * 2.0f);
        effectCloud.setParticle(ImmortalersDelightParticleTypes.KWAT.get());

        effectCloud.setPotion(ImmortalersDelightPotions.GAS.get());
        if (this.getOwner() != null && this.getOwner() instanceof LivingEntity livingEntity) effectCloud.setOwner(livingEntity);

        level.addFreshEntity(effectCloud);
    }
}
