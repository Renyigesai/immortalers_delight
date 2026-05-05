package com.renyigesai.immortalers_delight.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;

import java.util.Optional;

public record ImmDelightCriterionInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
    public static final Codec<ImmDelightCriterionInstance> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(ImmDelightCriterionInstance::player))
                    .apply(instance, ImmDelightCriterionInstance::new));
}
