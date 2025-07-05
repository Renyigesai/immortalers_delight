package com.renyigesai.immortalers_delight.entities.living;

import net.minecraft.client.model.SilverfishModel;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class SkelverfishAmbusher extends SkelverfishBase{
    public static final UUID HARD_HEALTH = UUID.fromString("86aa5919-69b0-d91e-e51f-e746753f87cd");
    public static final UUID NORMAL_HEALTH = UUID.fromString("e5e40a66-ef57-1490-5ca6-23e44bdf3970");

    public SkelverfishAmbusher(EntityType<? extends Silverfish> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.@NotNull Builder createSkelverfishAmbusherAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY,-1,0));
        if (pDifficulty.getDifficulty().getId() >= Difficulty.HARD.getId()) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(
                    new AttributeModifier(HARD_HEALTH,
                            "hard_difficulty_extra_health",
                            8.0F,
                            AttributeModifier.Operation.ADDITION)
            );
            this.setHealth(this.getMaxHealth());
        } else if (pDifficulty.getDifficulty().getId() >= Difficulty.NORMAL.getId()) {
            this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(
                    new AttributeModifier(NORMAL_HEALTH,
                            "normal_difficulty_extra_health",
                            4.0F,
                            AttributeModifier.Operation.ADDITION)
            );
            this.setHealth(this.getMaxHealth());
        }
        return pSpawnData;
    }

}
