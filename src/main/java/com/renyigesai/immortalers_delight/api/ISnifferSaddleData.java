package com.renyigesai.immortalers_delight.api;

import net.minecraft.network.syncher.EntityDataAccessor;

public interface ISnifferSaddleData {
    EntityDataAccessor<Boolean> immortalersDelight$getHasSaddleAccessor();
    EntityDataAccessor<Boolean> immortalersDelight$getSaddleUpgradedAccessor();
}
