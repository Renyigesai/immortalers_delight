package com.renyigesai.immortalers_delight.block.soul_infuser;

import com.renyigesai.immortalers_delight.api.IActions;

//灵魂熔炉的行为，作用类似于生物的Goal系统。
public class SoulInfuserAttackAction implements IActions {
    // ===================== 服务端行为 ===================== //
    protected int nextReadyTime;                    // 下一次就绪的时间，用于冷却
    protected int cooldown;                         // 冷却时间
    protected int actionTime;                       // 当前行为已经进行的时间（服务端）
    protected int duration;                         // 持续时间
    protected int minDuration;                      // 技能结束时锁定其他技能发动的最小冷却时间，这是为了避免出现无缝连招
    protected SoulInfuserAttackHelper attackHelper; // 行为驱动器，注意以上字段都是仅服务端，这个是双端

    public SoulInfuserAttackAction(SoulInfuserAttackHelper attackHelper) {
        this.attackHelper = attackHelper;
        this.nextReadyTime = 0;
        this.actionTime = 0;
        this.cooldown = 99999;
        this.duration = 99;
        this.minDuration = 1;
    }

    //判断行为是否可以启动的方法，用于检查冷却以及其他条件（注意出场爆炸没有目标也执行，所以这里没有放目标判定）
    @Override
    public boolean isReady() {
        return this.attackHelper.getAttackTime() >= this.nextReadyTime;
    }

    //行为开始时执行的方法
    @Override
    public void startAction() {

    }

    //行为期间应当每tick执行的方法
    @Override
    public void actionTick() {
        this.actionTime++;
    }

    //判断是否应该结束冷却的方法，当前用于检查行为是否达到最大持续时间
    @Override
    public boolean needStop() {
        return this.actionTime >= this.duration;
    }

    //行为结束时的方法，用于重置冷却以及其他行为
    @Override
    public void endAction() {
        this.nextReadyTime = this.attackHelper.getAttackTime() + cooldown;
        this.actionTime = 0;
        this.attackHelper.weakTime = this.minDuration;
    }

    // =================== 客户端行为 =================== //
    public void animationTick() {
    }

}
