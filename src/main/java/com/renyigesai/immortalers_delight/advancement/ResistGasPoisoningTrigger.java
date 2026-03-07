package com.renyigesai.immortalers_delight.advancement;

import com.google.gson.JsonObject;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ResistGasPoisoningTrigger extends SimpleCriterionTrigger<ResistGasPoisoningTrigger.Instance> {

    // 触发器唯一ID（需与进度JSON中的trigger字段一致）
    private static final ResourceLocation ID = new ResourceLocation(ImmortalersDelightMod.MODID, "resist_gas_poisoning");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected ResistGasPoisoningTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate playerPredicate, DeserializationContext context) {
        return new ResistGasPoisoningTrigger.Instance(playerPredicate);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, instance -> true);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ContextAwarePredicate playerPredicate) {
            super(ID, playerPredicate);
        }
    }
}
