package com.renyigesai.immortalers_delight.item.weapon;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.client.model.BoneKnifeBakedModel;
import com.renyigesai.immortalers_delight.client.model.ItemTESRBakedModel;
import com.renyigesai.immortalers_delight.client.renderer.special_item.BoneKnifeItemRenderer;
import com.renyigesai.immortalers_delight.client.renderer.special_item.ItemTESRenderer;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.item.ImmortalersKnifeItem;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.task.TimekeepingTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

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
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack)
    {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.<Attribute, AttributeModifier>create();
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            float baseDamage = this.attackDamage + (isPowerful ? extra_attackDamage : 0);
            int useTime = getUseTime(stack);
            int maxLoadTime = getMaxLoadTime();
            float buffer = 1 + Math.min((float) useTime / maxLoadTime, 1.0F); // 0.0~1.0 的蓄力比例;
            double damage = buffer > 1.5f ? (baseDamage + (buffer > 1.8f ? 0.5f : -0.5f)) * buffer : baseDamage;
            multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)attackSpeed + (isPowerful ? extra_attackSpeed : 0), AttributeModifier.Operation.ADDITION));
            multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", damage, AttributeModifier.Operation.ADDITION));
            return multimap;
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public float getAttackDamage() {
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        return this.attackDamage + (isPowerful ? extra_attackDamage : 0);
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
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.getInt(TAG_PREV_USE_TIME) != tag.getInt(TAG_USE_TIME)) {
                tag.putInt(TAG_PREV_USE_TIME, getUseTime(stack));
            }

            int maxLoadTime = getMaxLoadTime();
            if (holding && useTime < maxLoadTime) {
                int set = useTime + 1;
                setUseTime(stack, set);
            }
        }

        if (!holding && (float)useTime > 0.0F) {
            setUseTime(stack, Math.max(0, useTime - 5));
        }

    }

    //绑定特殊渲染器。要注意，启用渲染器需要烘焙模型的支持，因此不要漏。
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new ItemTESRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
            }
        });
    }


    private static int getMaxLoadTime() {
        return 20;
    }

    public static int getUseTime(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        return compoundtag != null ? compoundtag.getInt(TAG_USE_TIME) : 0;
    }

    public static void setUseTime(ItemStack stack, int useTime) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(TAG_PREV_USE_TIME, getUseTime(stack));
        tag.putInt(TAG_USE_TIME, useTime);
    }

    public static float getLerpedUseTime(ItemStack stack, float f) {
        CompoundTag compoundtag = stack.getTag();
        float prev = compoundtag != null ? (float) compoundtag.getInt(TAG_PREV_USE_TIME) : 0F;
        float current = compoundtag != null ? (float) compoundtag.getInt(TAG_USE_TIME) : 0F;
        return prev + f * (current - prev);
    }

    public static float getPullingAmount(ItemStack itemStack, float partialTicks){
        if (getLerpedUseTime(itemStack, partialTicks) > 0) {
            return Math.min(getLerpedUseTime(itemStack, partialTicks) / (float) getMaxLoadTime(), 1F);
        }

        return 0;
    }

    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class BoneKnifeEvents {
        @SubscribeEvent
        public static void BoneKnifeAttack(LivingHurtEvent event) {
            if (event.isCanceled()) return;

            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            LivingEntity hurtOne = event.getEntity();
            if (hurtOne.level().isClientSide) return;

            if (event.getSource().getEntity() instanceof LivingEntity attacker){
                ItemStack toolStack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
                if (!toolStack.isEmpty() && toolStack.getItem() instanceof BoneKnifeItem knife) {

                    if (entityLastUsed.containsKey(attacker.getUUID())) {
                        long lastUsedTime = entityLastUsed.get(attacker.getUUID());
                        float buffer = 1 + Math.min((float) (TimekeepingTask.getImmortalTickTime() - lastUsedTime) / getMaxLoadTime() * 50, 1F);
                        float damage = buffer > 1.5f ? (event.getAmount() + (buffer > 1.8f ? 0.5f : 0f)) * buffer : event.getAmount();
                        event.setAmount(Math.min(event.getAmount() + 1.5f * knife.getAttackDamage(), damage));
                    }

                }
            }

        }

    }
//
//    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
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
