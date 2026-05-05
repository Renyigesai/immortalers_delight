package com.renyigesai.immortalers_delight.item.weapon;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.BoneKnifeBakedModel;
import com.renyigesai.immortalers_delight.client.model.ItemTESRBakedModel;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.item.ImmortalersKnifeItem;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
public class BoneKnifeItem extends ImmortalersKnifeItem {
    public static final String TAG_USE_TIME = "UseTime";
    public static final String TAG_PREV_USE_TIME = "PrevUseTime";
    private static final Map<UUID, Long> entityLastUsed = new ConcurrentHashMap<>();

    public BoneKnifeItem(int type, Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(type, tier, attackDamage, attackSpeed, properties);
    }

    public BoneKnifeItem(int type, Tier tier, float attackDamage, float attackSpeed, float extra_attackDamage, float extra_attackSpeed, Properties properties) {
        super(type, tier, attackDamage, attackSpeed, extra_attackDamage, extra_attackSpeed, properties);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        ItemAttributeModifiers base = super.getDefaultAttributeModifiers(stack);
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        float baseDamage = this.attackDamage + (isPowerful ? extra_attackDamage : 0);
        int useTime = getUseTime(stack);
        int maxLoadTime = getMaxLoadTime();
        float buffer = 1 + Math.min((float) useTime / maxLoadTime, 1.0F);
        double damage = buffer > 1.5f ? (baseDamage + (buffer > 1.8f ? 0.5f : -0.5f)) * buffer : baseDamage;
        double speed = this.attackSpeed + (isPowerful ? extra_attackSpeed : 0);

        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        for (ItemAttributeModifiers.Entry entry : base.modifiers()) {
            if (entry.attribute().is(Attributes.ATTACK_DAMAGE)) {
                builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, damage, AttributeModifier.Operation.ADD_VALUE), entry.slot());
            } else if (entry.attribute().is(Attributes.ATTACK_SPEED)) {
                builder.add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, speed, AttributeModifier.Operation.ADD_VALUE), entry.slot());
            } else {
                builder.add(entry.attribute(), entry.modifier(), entry.slot());
            }
        }
        return builder.build();
    }
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        boolean b = super.onLeftClickEntity(stack, player, entity);
        int useTime = getUseTime(stack);
        if (!b && useTime > 0.0F) {
            setUseTime(stack, 0);
        }
        return b;
    }
    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        boolean b = super.hurtEnemy(stack, target, attacker);

        entityLastUsed.put(attacker.getUUID(), TimekeepingTask.getImmortalTickTime());
        return b;
    }


    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean held) {
        boolean var10000;
        label30: {
            super.inventoryTick(stack, level, entity, i, held);
            if (entity instanceof LivingEntity living) {
                if (living.getItemInHand(InteractionHand.MAIN_HAND) == stack) {
                    var10000 = true;
                    break label30;
                }
            }

            var10000 = false;
        }

        boolean holding = var10000;
        int useTime = getUseTime(stack);
        if (level.isClientSide()) {
            CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            if (tag.getInt(TAG_PREV_USE_TIME) != tag.getInt(TAG_USE_TIME)) {
                tag.putInt(TAG_PREV_USE_TIME, getUseTime(stack));
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            }

            int maxLoadTime = getMaxLoadTime();
            if (holding && useTime < maxLoadTime) {
                setUseTime(stack, useTime + 1);
            }
        }

        if (!holding && (float)useTime > 0.0F) {
            setUseTime(stack, Math.max(0, useTime - 5));
        }

    }

    private static int getMaxLoadTime() {
        return 20;
    }

    public static int getUseTime(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt(TAG_USE_TIME);
    }

    public static void setUseTime(ItemStack stack, int useTime) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.putInt(TAG_PREV_USE_TIME, getUseTime(stack));
        tag.putInt(TAG_USE_TIME, useTime);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static float getLerpedUseTime(ItemStack stack, float f) {
        CompoundTag compoundtag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        float prev = (float) compoundtag.getInt(TAG_PREV_USE_TIME);
        float current = (float) compoundtag.getInt(TAG_USE_TIME);
        return prev + f * (current - prev);
    }

    public static float getPullingAmount(ItemStack itemStack, float partialTicks){
        if (getLerpedUseTime(itemStack, partialTicks) > 0) {
            return Math.min(getLerpedUseTime(itemStack, partialTicks) / (float) getMaxLoadTime(), 1F);
        }

        return 0;
    }

    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID)
    public static class BoneKnifeEvents {
        @SubscribeEvent
        public static void BoneKnifeAttack(LivingDamageEvent.Pre event) {

            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            LivingEntity hurtOne = event.getEntity();
            if (hurtOne.level().isClientSide) return;

            if (event.getSource().getEntity() instanceof LivingEntity attacker){
                ItemStack toolStack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
                if (!toolStack.isEmpty() && toolStack.getItem() instanceof BoneKnifeItem knife) {

                    if (entityLastUsed.containsKey(attacker.getUUID())) {
                        long lastUsedTime = entityLastUsed.get(attacker.getUUID());
                        float buffer = 1 + Math.min((float) (TimekeepingTask.getImmortalTickTime() - lastUsedTime) / getMaxLoadTime() * 50, 1F);
                        float damage = buffer > 1.5f ? (event.getNewDamage() + (buffer > 1.8f ? 0.5f : 0f)) * buffer : event.getNewDamage();
                        float knifeBase = knife.attackDamage + (isPowerful ? knife.extra_attackDamage : 0);
                        event.setNewDamage(Math.min(event.getNewDamage() + 1.5f * knifeBase, damage));
                    }

                }
            }

        }

    }
//
//    @EventBusSubscriber(modid = ImmortalersDelightMod.MODID, value = net.neoforged.api.distmarker.Dist.CLIENT)
//    public static class ClientEventBus{
//        @SubscribeEvent
//        public static void onModelBaked(ModelEvent.ModifyBakingResult event){
//            // 在这里替换烘焙模型
//            // 注意，一旦替换BakedModel，ItemRenderer所做的变换将全部失效，所以你需要自己在自定义渲染器中从头处理整套流程
//            Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();
//            if (true) {
//                ModelResourceLocation location = new ModelResourceLocation(BuiltInRegistries.ITEM.getKey(ImmortalersDelightItems.BONE_KNIFE.get()), "inventory");
//                BakedModel existingModel = modelRegistry.get(location);
//                if (existingModel == null) {
//                    throw new RuntimeException("Did not find Obsidian Hidden in registry");
//                } else if (existingModel instanceof BoneKnifeBakedModel) {
//                    throw new RuntimeException("Tried to replaceObsidian Hidden twice");
//                } else {
//                    BoneKnifeBakedModel obsidianWrenchBakedModel = new BoneKnifeBakedModel(existingModel);
//                    event.getModels().put(location, obsidianWrenchBakedModel);
//                }
//            }
////            if (true) {
////                ModelResourceLocation location = new ModelResourceLocation(BuiltInRegistries.ITEM.getKey(ImmortalersDelightItems.JENG_NANU.get()), "inventory");
////                BakedModel existingModel = modelRegistry.get(location);
////                if (existingModel == null) {
////                    throw new RuntimeException("Did not find Obsidian Hidden in registry");
////                } else if (existingModel instanceof ItemTESRBakedModel) {
////                    throw new RuntimeException("Tried to replaceObsidian Hidden twice");
////                } else {
////                    ItemTESRBakedModel obsidianWrenchBakedModel = new ItemTESRBakedModel(existingModel);
////                    event.getModels().put(location, obsidianWrenchBakedModel);
////                }
////            }
//        }
//    }
}
