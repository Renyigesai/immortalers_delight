package com.renyigesai.immortalers_delight.advancement;

import com.google.gson.JsonObject;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class GameplayTrigger extends SimpleCriterionTrigger<GameplayTrigger.Instance> {
    private final ResourceLocation id;

    public GameplayTrigger(String name) {
        this.id = new ResourceLocation(ImmortalersDelightMod.MODID, name);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    protected Instance createInstance(JsonObject json, ContextAwarePredicate playerPredicate, DeserializationContext context) {
        return new Instance(id, playerPredicate);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, instance -> true);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ResourceLocation id, ContextAwarePredicate playerPredicate) {
            super(id, playerPredicate);
        }
    }
}
