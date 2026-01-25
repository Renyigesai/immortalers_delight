package com.renyigesai.immortalers_delight.advancement;

import com.google.gson.JsonObject;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

// ===================== 自定义 CriteriaTrigger 实现 =====================
public class LevelUpEnchantmentTrigger extends SimpleCriterionTrigger<LevelUpEnchantmentTrigger.Instance> {
    // 触发器唯一ID（需与进度JSON中的trigger字段一致）
    private static final ResourceLocation ID = new ResourceLocation(ImmortalersDelightMod.MODID, "level_up_enchantment");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    // 反序列化：从进度JSON读取自定义条件（无额外条件则返回空实例）
    @Override
    protected Instance createInstance(JsonObject json, ContextAwarePredicate playerPredicate, DeserializationContext context) {
        return new Instance(playerPredicate);
    }

    // 触发方法：在业务逻辑中调用，通知进度系统完成条件
    public void trigger(ServerPlayer player) {
        // 第二个参数是“条件校验器”，true表示无条件触发
        this.trigger(player, instance -> true);
        System.out.println("触发条件");
    }

    // ===================== 触发条件实例（CriterionInstance） =====================
    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ContextAwarePredicate playerPredicate) {
            // 必须传入触发器ID和玩家谓词
            super(ID, playerPredicate);
        }

        // 序列化：将条件写入JSON（无额外条件则返回空对象）
//        @Override
//        public JsonObject serializeToJson(DeserializationContext context) {
//            return new JsonObject();
//        }
//        public @NotNull JsonObject serializeToJson(SerializationContext pConditions) {
//            JsonObject jsonobject = new JsonObject();
//            jsonobject.add("player", this.player.toJson(pConditions));
//            return jsonobject;
//        }
    }
}
