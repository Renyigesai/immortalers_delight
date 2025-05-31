package com.renyigesai.immortalers_delight.entities.living;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;

public class SkelverfishThrasher extends SkelverfishBase{
    public AnimationState attackAnimationState = new AnimationState();
    public int idleAnimationTimeOut = 0;
    public SkelverfishThrasher(EntityType<? extends Silverfish> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    private void setupAnimationStates() {
        attackAnimationState.start(this.tickCount);
    }
    //启动动画状态
//    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
//        if (DATA_POSE.equals(pKey)) {
//            switch (this.getPose()) {
//                // 可以添加新的 Pose 来触发新动画
//                case NEW_POSE:
//                    attackAnimationState.start(this.tickCount);
//                    break;
//            }
//        }
//    }

    //重写实体事件
//    @Override
//    public void handleEntityEvent(byte pId) {
//        if (pId == YOUR_NEW_EVENT_ID) { // 替换 YOUR_NEW_EVENT_ID 为一个未被使用的事件 ID
//            newAnimationState.start(this.tickCount);
//        } else {
//            super.handleEntityEvent(pId);
//        }
//    }
//
//    //触发事件
//    public boolean doHurtTarget(Entity pEntity) {
//        if (this.level() instanceof ServerLevel serverLevel) {
//            serverLevel.broadcastEntityEvent(this, (byte)YOUR_NEW_EVENT_ID); // 发送新的事件 ID
//        }
//        return super.doHurtTarget(pEntity);
//    }

}
