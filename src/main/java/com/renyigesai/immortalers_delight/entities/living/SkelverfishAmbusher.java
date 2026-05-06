package com.renyigesai.immortalers_delight.entities.living;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class SkelverfishAmbusher extends SkelverfishBase{
    private static final ResourceLocation HARD_HEALTH_ID = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "skelverfish_ambusher_hard_health");
    private static final ResourceLocation NORMAL_HEALTH_ID = ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "skelverfish_ambusher_normal_health");

    public SkelverfishAmbusher(EntityType<? extends Silverfish> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.@NotNull Builder createSkelverfishAmbusherAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

//    public boolean doHurtTarget(Entity pEntity) {
//        if (!super.doHurtTarget(pEntity)) {
//            return false;
//        } else {
//            if (pEntity instanceof LivingEntity livingEntity) {
//                livingEntity.setHealth((LivingEntity))
//            }
//
//            return true;
//        }
//    }
    @Override
    public void tick() {
        super.tick();

        List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(0.2F, -0.01F, 0.2F), EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            boolean flag = !this.level().isClientSide && !(this.getControllingPassenger() instanceof Player);

            for (Entity entity : list) {
                if (!entity.hasPassenger(this)) {
                    if (flag && this.getPassengers().size() < 4 && !entity.isPassenger() && entity.getBbWidth() < 1.375F && entity instanceof Silverfish) {
                        entity.startRiding(this);
                    } else {
                        this.push(entity);
                    }
                }
            }
        }
    }
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
        this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY,-1,0));
        if (pDifficulty.getDifficulty().getId() >= Difficulty.HARD.getId()) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(
                    new AttributeModifier(HARD_HEALTH_ID, 8.0D, AttributeModifier.Operation.ADD_VALUE)
            );
            this.setHealth(this.getMaxHealth());
        } else if (pDifficulty.getDifficulty().getId() >= Difficulty.NORMAL.getId()) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(
                    new AttributeModifier(NORMAL_HEALTH_ID, 4.0D, AttributeModifier.Operation.ADD_VALUE)
            );
            this.setHealth(this.getMaxHealth());
        }
        return pSpawnData;
    }

}
