package com.renyigesai.immortalers_delight.item.food;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.AntiFeedingFoodItem;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import com.renyigesai.immortalers_delight.item.DrinkItem;
import com.renyigesai.immortalers_delight.item.ImmortalersHammerItem;
import com.renyigesai.immortalers_delight.potion.VulnerableMobEffect;
import com.renyigesai.immortalers_delight.potion.immortaleffects.FreezeEffect;
import com.renyigesai.immortalers_delight.potion.immortaleffects.StunEffect;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.datautil.EffectData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;
import vectorwing.farmersdelight.data.EntityTags;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FrostyCrownMousseItem extends EdibleBlockFoodItem {
    public static final String EAT_THIS = ImmortalersDelightMod.MODID+ "_frosty_crown_mousse_eater";
    public FrostyCrownMousseItem(Block pBlock, Properties pProperties, FoodProperties poweredFoodProperties, boolean hasCustomTooltip) {
        super(pBlock, pProperties,poweredFoodProperties,hasCustomTooltip);
    }

    public FrostyCrownMousseItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    protected void addAheadFoodEffect(ItemStack stack, Level level, LivingEntity livingEntity) {
        livingEntity.setIsInPowderSnow(true);
        if (!level.isClientSide()) {
            livingEntity.setSharedFlagOnFire(false);
        }
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (Configuration.FOOD_EFFECT_TOOLTIP.get()) {
            MutableComponent textValue = Component.translatable("tooltip." + ImmortalersDelightMod.MODID+ ".frosty_crown_mousse");
            tooltip.add(textValue.withStyle(ChatFormatting.RED));
        }
        super.appendHoverText(stack, level, tooltip, isAdvanced);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity livingEntity, ItemStack pStack, int pRemainingUseDuration) {
        //判断在使用物品
        if (this.getUseDuration(pStack) - pRemainingUseDuration > 32) {
            addAheadFoodEffect(pStack, livingEntity.level(), livingEntity);
        }
        //结束使用物品
        if (pRemainingUseDuration == 1) {
            this.addEatEffect(pStack, pLevel, livingEntity);
            //特殊持续状态本身是没有持久化的，所以我们为这个慕斯实现一个寒冷状态的存盘。
            //慕斯切片的冷冻效果在事件类
            if (!livingEntity.isAlive() || livingEntity.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES) || livingEntity.level().isClientSide()) return;
            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            if (pStack.is(ImmortalersDelightItems.FROSTY_CROWN_MOUSSE.get())) {
                FreezeEffect.applyImmortalEffect(livingEntity, isPowerful ? 3600 : 1200, 2);
                if (livingEntity instanceof Player player) {
                    CompoundTag tag = player.getPersistentData();
                    tag.putInt(EAT_THIS, isPowerful ? 3600 : 1200);
                }
            }
        }
    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class FrostyCrownMousseEvents {
        //存了寒冷nbt，怎么判断寒冷时间?搞一个冷却计时嘛。
        @SubscribeEvent
        public static void eatPeiCooldown(LivingEvent.LivingTickEvent event) {
            LivingEntity entity = event.getEntity();
            if (entity.isAlive() && entity.tickCount % 5 == 0
                    && !entity.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                    && !entity.level().isClientSide()) {
                CompoundTag tag = entity.getPersistentData();
                if (tag.contains(EAT_THIS, Tag.TAG_INT) && tag.getInt(EAT_THIS) > 0) {

                    Map<UUID, EffectData> freezedEntitys = new HashMap<>(FreezeEffect.getEntityMap());
                    //如果玩家因为重启游戏等原因冻结状态丢了，从这里补一个
                    if (!freezedEntitys.containsKey(entity.getUUID())) FreezeEffect.applyImmortalEffect(entity, tag.getInt(EAT_THIS), 2);

                    if (tag.getInt(EAT_THIS) - 5 <= 0) {
                        tag.remove(EAT_THIS);
                    } else {
                        tag.putInt(EAT_THIS, tag.getInt(EAT_THIS) - 5);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void removeOnDeath(LivingDeathEvent event) {
            LivingEntity entity = event.getEntity();
            CompoundTag tag = entity.getPersistentData();
            if (tag.contains(EAT_THIS, Tag.TAG_INT) && tag.getInt(EAT_THIS) > 0) {
                tag.remove(EAT_THIS);
                FreezeEffect.removeImmortalEffect(entity);
            }
        }

    }
}
