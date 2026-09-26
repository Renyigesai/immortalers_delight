package com.renyigesai.immortalers_delight.entities.living;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class BlackPiecesHitboxEntity extends PiecesHitboxEntity{

    private boolean isSummons;//判定是否是召唤物（也就是触发时向两边扩散的）
    public BlackPiecesHitboxEntity(EntityType<? extends BlackPiecesHitboxEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.attackDuration = 45; // 等待结束后的最大存活时间（游戏刻）
        this.attackTriggerTicks = 18; // 等待状态结束后，到造成伤害前的前摇时间（游戏刻）
        this.findRange = 2f;//默认索敌半径，实为自身半径的乘数
        this.attackRange = 3f;//默认伤害半径，实为自身半径的乘数
    }

    public BlackPiecesHitboxEntity(Level pLevel, double pX, double pY, double pZ, float pYRot, boolean isWaiting) {
        this(ImmortalersDelightEntities.BLACK_PIECES.get(), pLevel);
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
        this.setWaiting(isWaiting);
        if (!isWaiting) {
            this.warmupDelayTicks = 1;
        }
    }

    public BlackPiecesHitboxEntity(Level pLevel, double pX, double pY, double pZ, float pYRot, LivingEntity pOwner) {
        this(ImmortalersDelightEntities.BLACK_PIECES.get(), pLevel);
        this.setOwner(pOwner);
        this.setYRot(pYRot * (180F / (float)Math.PI));
        this.setPos(pX, pY, pZ);
    }

    @Override
    public boolean isWaiting() {
        if (!this.level().isClientSide() && this.isSummons) return this.warmupDelayTicks <= 0;
        return super.isWaiting();
    }
    @Override
    public float getAnimationProgress(float pPartialTicks) {
        if (this.isWaiting()) {return 0.0F;}
        if (this.animationTicks > 30) return -1;
        return (float) this.animationTicks + pPartialTicks;
    }
    /*===========================下面是实现棋子之间连线的AI==============================*/
}
