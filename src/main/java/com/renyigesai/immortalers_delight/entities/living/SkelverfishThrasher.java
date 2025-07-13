package com.renyigesai.immortalers_delight.entities.living;

import com.renyigesai.immortalers_delight.entities.ai.SkelverfishThrasherAttackGoal;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class SkelverfishThrasher extends SkelverfishBase{
    public static final UUID HARD_ATTACK = UUID.fromString("1023c998-0635-c985-eeff-d30fbfb8d26f");
    public static final UUID NORMAL_ATTACK = UUID.fromString("d7ce190e-b28e-9617-db38-7beba96bbe40");
    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationDuration = 0;
    public SkelverfishThrasher(EntityType<? extends Silverfish> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.5F));
        this.goalSelector.addGoal(3, new SkelverfishThrasherAttackGoal(this,  1.0D, false));
    }
    /**
     * 创建属性，此方法在类构造时调用，
     * 需要注意的是如果继承了其他类，这个东西不要沿用父类的命名，否则导向的是父类的同名函数。
     */
    public static AttributeSupplier.@NotNull Builder createSkelverfishThrasherAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }
    /**
     * 在生物生被初始化时调用，即在实体被创建后、在实体被加入世界之前被调用。
     * 此处主要用于实现类似原版怪物不同难度下属性的变化。
     */
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        if (pDifficulty.getDifficulty().getId() >= Difficulty.HARD.getId()) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(
                    new AttributeModifier(HARD_ATTACK,
                            "hard_difficulty_extra_attack",
                            3.0F,
                            AttributeModifier.Operation.ADDITION)
            );
        } else if (pDifficulty.getDifficulty().getId() >= Difficulty.NORMAL.getId()) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(
                    new AttributeModifier(NORMAL_ATTACK,
                            "normal_difficulty_extra_attack",
                            1.5F,
                            AttributeModifier.Operation.ADDITION)
            );
        }
        return pSpawnData;
    }
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            setUpAnimationState();
        }
    }

    private void setUpAnimationState(){
        if (attackAnimationDuration >= 0) /*System.out.println("当前骷髅虫的ID为："+this.getId()+"，attackAnimationDuration:"+attackAnimationDuration)*/;
        if (this.attackAnimationDuration < 0){
            /*System.out.println("当前重击虫,ID为 "+this.getId()+" 将结束攻击动画");*/
            attackAnimationState.stop();
        }
        if (this.isAggressive() && this.attackAnimationDuration <= 0){
            attackAnimationDuration = 10;
            attackAnimationState.start(this.tickCount);
        }
        --this.attackAnimationDuration;
    }
    @Override
    public boolean doHurtTarget(Entity pEntity) {
        //boolean flag = super.doHurtTarget(pEntity);
        float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f1 = (int)f > 0 ? f / 2.0F + (float)this.random.nextInt((int)f) : f;
        boolean flag = pEntity.hurt(this.damageSources().mobAttack(this), f1);
        //if (f1 > f) flag = pEntity.hurt(this.damageSources().mobAttack(this), f1);
        if (flag) {
            double d2;
            if (pEntity instanceof LivingEntity livingentity) {
                d2 = livingentity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            } else {
                d2 = 0.0D;
            }

            double d0 = d2;
            double d1 = Math.max(0.0D, 1.0D - d0);
            ((LivingEntity)pEntity).knockback((double)((1.0F + this.level().getDifficulty().getId()) * 0.25F), (double) Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.getYRot() * ((float)Math.PI / 180F))));
            pEntity.setDeltaMovement(pEntity.getDeltaMovement().add(0.0D, (double)0.4F * d1, 0.0D));
            this.doEnchantDamageEffects(this, pEntity);
        }

        this.playSound(SoundEvents.STONE_BREAK, 1.0F, 1.0F);
        return flag;
    }

}
