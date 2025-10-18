package com.renyigesai.immortalers_delight.api.mobbase;

public interface AntiCheesedMob extends ImmortalersMob{

    //判断生物是否可以受伤，用于独立无敌帧
    boolean canBeHurt();

    //判断生物是否可以损失生命值，用于独立无敌帧
    boolean canLoseHealth();


}
