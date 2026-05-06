package com.renyigesai.immortalers_delight.advancement;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

public class PassSnifferCoolDownTrigger extends SimpleCriterionTrigger<ImmDelightCriterionInstance> {
    @Override
    public Codec<ImmDelightCriterionInstance> codec() {
        return ImmDelightCriterionInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, instance -> true);
    }
}
