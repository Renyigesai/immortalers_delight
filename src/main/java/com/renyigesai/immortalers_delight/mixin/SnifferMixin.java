package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.api.ISnifferSaddleData;
import com.renyigesai.immortalers_delight.api.SnifferAnimations;
import com.renyigesai.immortalers_delight.api.event.SnifferDropSeedEvent;
import com.renyigesai.immortalers_delight.event.SnifferEvent;
import com.renyigesai.immortalers_delight.item.SnifferSaddleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;

@Mixin(Sniffer.class)
public abstract class SnifferMixin extends Animal implements ISnifferSaddleData {

    @Shadow @Final private static EntityDataAccessor<Integer> DATA_DROP_SEED_AT_TICK;

    @Shadow @Final public AnimationState feelingHappyAnimationState;

    @Shadow protected abstract BlockPos getHeadBlock();

    @Shadow public abstract void tick();

    protected SnifferMixin(EntityType<? extends Animal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }
    
    public boolean isSaddled() {
        return SnifferSaddleItem.hasSaddle((Sniffer)(Object)this);
    }
    
    
    @Inject(method = "tick", at = @At("TAIL"))
    private void immortalersDelight$tickHappyAnimation(CallbackInfo ci) {
        if (!this.level().isClientSide()) {
            int remaining = this.entityData.get(SnifferAnimations.HAPPY_TICKS);
            if (remaining > 0) {
                this.entityData.set(SnifferAnimations.HAPPY_TICKS, remaining - 1);
            }
        }
    }
    
    @Override
    public LivingEntity getControllingPassenger() {
        if (this.getPassengers().isEmpty()) {
            return null;
        }
        // 只有装备鞍座时，第一个乘客才是控制者
        if (SnifferSaddleItem.hasSaddle((Sniffer)(Object)this) && this.getPassengers().get(0) instanceof LivingEntity living) {
            return living;
        }
        return null;
    }
    
    @Override
    public void travel(Vec3 travelVector) {
        if (this.isAlive()) {
            LivingEntity rider = this.getControllingPassenger();
            if (this.isVehicle() && rider != null) {
                // 设置嗅探兽朝向跟随玩家视角
                this.setYRot(rider.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(rider.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                
                // 获取玩家输入
                float forward = rider.zza;
                float strafe = rider.xxa;
                
                // 设置移动速度（嗅探兽基础速度的60%）
                float speed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.6F;
                
                // 后退速度减半
                if (forward <= 0.0F) {
                    forward *= 0.5F;
                }
                
                // 应用速度
                this.setSpeed(speed);
                
                // 处理移动逻辑
                super.travel(new Vec3(strafe, travelVector.y, forward));
                
                // 同步位置到客户端
                if (!this.level().isClientSide && (this.getDeltaMovement().lengthSqr() > 1.0E-6D || this.tickCount % 20 == 0)) {
                    this.hasImpulse = true;
                }
            } else {
                // 没有骑乘时恢复正常移动
                super.travel(travelVector);
            }
        }
    }
    


    @Inject(method = "onSyncedDataUpdated", at = @At("TAIL"))
    private void immortalersDelight$receiveHappyAnimation(EntityDataAccessor<?> key, CallbackInfo ci) {
        if (key == SnifferAnimations.HAPPY_TICKS && this.level().isClientSide()) {
            if (this.entityData.get(SnifferAnimations.HAPPY_TICKS) > 0) {
                this.feelingHappyAnimationState.startIfStopped(this.tickCount);
            } else {
                this.feelingHappyAnimationState.stop();
            }
        }
    }

    @Inject(method = "dropSeed",  at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/sniffer/Sniffer;getHeadBlock()Lnet/minecraft/core/BlockPos;", shift = At.Shift.AFTER),locals = LocalCapture.CAPTURE_FAILHARD)
    private void dropSeed(CallbackInfo ci, ServerLevel serverlevel, LootTable loottable, LootParams lootparams, List<ItemStack> list){
        Level level = this.level();
        BlockPos headBlock = getHeadBlock();
        SnifferDropSeedEvent snifferDropSeedEvent = new SnifferDropSeedEvent(level,headBlock,new ArrayList<>(list));
        MinecraftForge.EVENT_BUS.post(snifferDropSeedEvent);
        list.clear();
        list.addAll(snifferDropSeedEvent.getStacks());
    }

    @Override
    public boolean canFallInLove() {
        CompoundTag tag = this.getPersistentData();
        if (!tag.contains(SnifferEvent.SNIFFER_TAIL_REGENERATION_COOLDOWN,Tag.TAG_INT)){
            return super.canFallInLove();
        }
        if (tag.contains(SnifferEvent.SNIFFER_TAIL_REGENERATION_COOLDOWN,Tag.TAG_INT) && tag.getInt(SnifferEvent.SNIFFER_TAIL_REGENERATION_COOLDOWN) == 0){
            return super.canFallInLove();
        }
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected boolean canRide(net.minecraft.world.entity.Entity entity) {
        return false;
    }

}

//opus5 牛逼