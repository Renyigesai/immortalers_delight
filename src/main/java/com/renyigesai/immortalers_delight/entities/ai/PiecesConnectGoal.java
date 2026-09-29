package com.renyigesai.immortalers_delight.entities.ai;


import net.minecraft.world.entity.ai.goal.Goal;

//实现棋子之间连线的AI
public class PiecesConnectGoal extends Goal {
    @Override
    public boolean canUse() {
        return false;
    }
}
