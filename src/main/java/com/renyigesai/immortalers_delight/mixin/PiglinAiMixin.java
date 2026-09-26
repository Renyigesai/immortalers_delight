package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PiglinAi.class)
public class PiglinAiMixin {

    // Shadow 原版的私有投掷方法
    @Shadow
    private static void throwItems(Piglin piglin, List<ItemStack> items) {}
    @Unique
    private static final ResourceLocation BETTER_BARTER_LOOT_TABLE =
            new ResourceLocation(ImmortalersDelightMod.MODID, "gameplay/better_piglin_bartering");

    @Inject(method = "stopHoldingOffHandItem", at = @At("HEAD"), cancellable = true)
    private static void stopHoldingOffHandItem(Piglin piglin, boolean pShouldBarter, CallbackInfo ci) {
        // 获取猪灵当前副手里端详的物品
        ItemStack offhandItem = piglin.getItemInHand(InteractionHand.OFF_HAND);

        // 判定是目标物品、处于服务端且确实需要结算交易（pShouldBarter 为 true）
        if (pShouldBarter && offhandItem.is(ImmortalersDelightItems.GOLDEN_CAKE.get()) && piglin.level() instanceof ServerLevel serverLevel) {

            // 清空副手拿着的蛋糕（正常消耗）
            piglin.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);

            // 获取自定义的高级战利品表
            LootTable customTable = serverLevel.getServer().getLootData().getLootTable(BETTER_BARTER_LOOT_TABLE);

            // 构建交易战利品参数
            LootParams lootParams = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.THIS_ENTITY, piglin)
                    .create(LootContextParamSets.PIGLIN_BARTER);

            // 产生战利品并返回，提前结束原版逻辑
            List<ItemStack> result = customTable.getRandomItems(lootParams);
            throwItems(piglin, result);
            ci.cancel();
        }
    }
}
