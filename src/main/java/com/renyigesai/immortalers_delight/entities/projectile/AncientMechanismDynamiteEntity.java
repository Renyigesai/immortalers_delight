package com.renyigesai.immortalers_delight.entities.projectile;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

public class AncientMechanismDynamiteEntity extends ThrowableItemProjectile {
    public AncientMechanismDynamiteEntity(EntityType<? extends AncientMechanismDynamiteEntity> type, Level level) {
        super(type, level);
    }

    public AncientMechanismDynamiteEntity(Level level, LivingEntity owner) {
        super(ImmortalersDelightEntities.ANCIENT_MECHANISM_DYNAMITE.get(), owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.TNT;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide) {
            level().explode(this, getX(), getY(), getZ(), 3.0F, Level.ExplosionInteraction.NONE);
            for (LivingEntity target : level().getEntitiesOfClass(LivingEntity.class,
                    new AABB(getX() - 3, getY() - 3, getZ() - 3, getX() + 3, getY() + 3, getZ() + 3))) {
                if (target instanceof AbstractIllager || target == getOwner()) continue;
                double distance = Math.sqrt(distanceToSqr(target));
                target.hurt(damageSources().explosion(this, getOwner() instanceof LivingEntity living ? living : this),
                        (float) Math.max(0.0D, 12.0D * (1.0D - distance / 3.0D)));
            }
            discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) level().addParticle(ParticleTypes.SMOKE, getX(), getY() + 0.2D, getZ(), 0, 0.01D, 0);
    }
}
