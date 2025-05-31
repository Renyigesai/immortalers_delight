package com.renyigesai.immortalers_delight.item;

import com.mojang.datafixers.util.Pair;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.GasPoisonEffect;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.InebriatedEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.item.DrinkableItem;

@Mod.EventBusSubscriber
public class InebriatedToxicFoodItem extends DrinkableItem {

    private final boolean foil;
    public InebriatedToxicFoodItem(Properties properties, boolean hasFoodEffectTooltip) {
        super(properties,hasFoodEffectTooltip,false);
        foil = false;
    }
    public InebriatedToxicFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties,hasFoodEffectTooltip,hasCustomTooltip);
        foil = false;
    }
    public InebriatedToxicFoodItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip, boolean isFoil, boolean canFeed) {
        super(properties,hasFoodEffectTooltip,hasCustomTooltip);
        foil = isFoil;
        if (canFeed) MinecraftForge.EVENT_BUS.register(this);
    }
    @Override
    public @NotNull ItemStack finishUsingItem (@NotNull ItemStack stack, @NotNull Level level, LivingEntity livingEntity) {
        addInebriatedEffect(stack,level,livingEntity);
        return super.finishUsingItem(stack, level, livingEntity);
    }
    /**
     * 该方法用于处理实体食用物品后添加对应的药水效果。
     * 当实体食用某个可食用物品时，会根据物品的属性尝试为实体添加相应的药水效果。
     *
     * @param stack 被食用的物品栈，包含了具体的物品及其数量等信息。
     * @param level 实体所在的游戏世界，用于判断是否为客户端，以及获取随机数生成器。
     * @param livingEntity 食用物品的实体，即要添加药水效果的对象。
     */
    private void addInebriatedEffect(ItemStack stack, Level level, LivingEntity livingEntity) {
        // 从物品栈中获取具体的物品
        Item item = stack.getItem();
        // 检查该物品是否为可食用物品
        if (item.isEdible()) {
            // 遍历物品的食物属性中定义的所有药水效果及其概率
            for (Pair<MobEffectInstance, Float> pair : stack.getFoodProperties(livingEntity).getEffects()) {
                // 条件判断：
                // 1. 当前不是客户端，因为药水效果的添加通常在服务器端处理，以保证数据一致性。
                // 2. 药水效果实例不为空，确保有有效的药水效果。
                // 3. 药水效果为我们指定的酒精效果
                if (!level.isClientSide && pair.getFirst() != null) {
                    if (pair.getFirst().getEffect() == ImmortalersDelightMobEffect.INEBRIATED.get()) {
                        // 创建一个新的药水效果实例，使用原有的药水效果实例作为模板。
                        // 然后将该药水效果添加到食用物品的实体上。
                        int oldLv = livingEntity.hasEffect(ImmortalersDelightMobEffect.INEBRIATED.get()) ? livingEntity.getEffect(ImmortalersDelightMobEffect.INEBRIATED.get()).getAmplifier() : 0;
                        int oldTime = livingEntity.hasEffect(ImmortalersDelightMobEffect.INEBRIATED.get()) ? livingEntity.getEffect(ImmortalersDelightMobEffect.INEBRIATED.get()).getDuration() : 0;
                        int time = pair.getFirst().getDuration() + oldTime;
                        int lv = pair.getFirst().getAmplifier() > oldLv ? pair.getFirst().getAmplifier() : oldLv;
                        livingEntity.addEffect(new MobEffectInstance(pair.getFirst().getEffect(),time,lv));
                        //InebriatedEffect.applyImmortalEffect(livingEntity,(double) time / 20 + 0.1,lv);
                    }
                    else  livingEntity.addEffect(pair.getFirst());
                }
            }
        }
    }
    @SubscribeEvent
    public void onPlayerFeed(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getEntity() != null && event.getTarget() instanceof LivingEntity target){
            Player player = event.getEntity();
            Level level = player.level();
            ItemStack itemStack = event.getItemStack();
            if (!(level instanceof ServerLevel serverLevel) || itemStack == ItemStack.EMPTY) return;
            if (itemStack.getItem() == ImmortalersDelightItems.CLEAR_WATER_VODKA.get()) {
                addInebriatedEffect(itemStack,serverLevel,target);
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                    BlockPos pos = player.getOnPos().above();
                    vectorwing.farmersdelight.common.utility.ItemUtils.spawnItemEntity(level,new ItemStack(Items.GLASS_BOTTLE),
                            pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5,0.0,0.0,0.0);
                }
            }
            //AidSupportAbility.onItemUse(player, itemStack);
        }
    }
}
