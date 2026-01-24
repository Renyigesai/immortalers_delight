package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.BaseImmortalEffect;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.BaseImmortalEffectTask;
//import com.renyigesai.immortalers_delight.potion.immortaleffects.GasPoisonEffect;
//import com.renyigesai.immortalers_delight.util.datautil.EffectData;
//import com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper.ExitTimeSaveLoadHelper;
//import com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper.MagicalReverseMapSaveLoadHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.data.EntityTags;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

public class DebugItem extends Item {
    public DebugItem(Properties p_41383_) {
        super(p_41383_);
    }

    public static final int DEBUG_ITEM_MODEL = 0;
    public static final int KI_BLAST_MODEL = 1;
    public static final int LARGE_COLUMN_MODEL = 2;
    public static final int BONE_KNIFE_MODEL = 3;
    public static final int JENG_NANU_MODEL = 4;

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        Class<BlockEntity> blockEntityClass = BlockEntity.class;
        Field[] fields = blockEntityClass.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public ItemStack finishUsingItem (ItemStack pStack, Level level, LivingEntity pLivingEntity) {
//        //CustomDataUsageExample.saveCustomInfo(level, "Hello, Minecraft!");
//        // 读取自定义信息
//        ExitTimeSaveLoadHelper.saveExitTime(level,System.currentTimeMillis());
//        Long info = ExitTimeSaveLoadHelper.loadExitTime(level);
//        System.out.println("Loaded info: " + info);
//
//        UUID playerID = pLivingEntity.getUUID();
//        System.out.println("Players UUID: " + playerID);
//
//        Map<UUID, EffectData> map = MagicalReverseMapSaveLoadHelper.loadEntityHasEffect(level);
//        // 输出读取到的 Map
//        for (Map.Entry<UUID, EffectData> entry : map.entrySet()) {
//            UUID uuid = entry.getKey();
//            EffectData effectData = entry.getValue();
//            System.out.println("UUID: " + uuid + ", Effect Level: " + effectData.getAmplifier() +
//                    ", Duration: " + effectData.getTime() + ", Task ID " + effectData.getTaskId());
//        }
//        MobEffectInstance gas = new MobEffectInstance(ImmortalersDelightMobEffect.GAS_POISON.get(),100,0);
//        pLivingEntity.addEffect(gas);
        //GasPoisonEffect.applyImmortalEffect(pLivingEntity,5.0,0);
//        MobEffectInstance gas1 = new MobEffectInstance(ImmortalersDelightMobEffect.LINGERING_FLAVOR.get(),1200,0);
//        pLivingEntity.addEffect(gas1);
//        BaseImmortalEffect.applyImmortalEffect(pLivingEntity,50.0,0);
        return pStack;
    }
}
