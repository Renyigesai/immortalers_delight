package com.renyigesai.immortalers_delight.event;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.item.food.AlfalfaPopsicleItem;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.util.ReflectionUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ImmortalersDelightMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PiglinBarterWeaponEvents {

    private static final String NBT_KEY = "CustomBarterType";

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {

            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            // 普通模式防连点器
            if (!isPowerful) {
                AttributeInstance instance = attacker.getAttribute(Attributes.ATTACK_DAMAGE);
                if (instance == null || event.getAmount() < (attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.6)) return;
            }

            ItemStack weapon = attacker.getMainHandItem();
            if (!weapon.hasTag() || !weapon.getOrCreateTag().contains(NBT_KEY)) return;

            String type = weapon.getOrCreateTag().getString(NBT_KEY);
            LivingEntity target = event.getEntity();
            float bonusDamage = 0.0f;

            switch (type) {
                case "cursed_gold_sword_t1" -> {
                    // 对生命值大于持有者的目标伤害 +15
                    if (target.getHealth() > attacker.getHealth()) {
                        bonusDamage += 15.0f;
                    }
                    // 对 Boss 或 玩家 造成的伤害 +10
                    if (isBossOrPlayer(target)) {
                        bonusDamage += 10.0f;
                    }
                }
                case "blazing_gold_sword_t1", "blazing_gold_sword_t2" -> {
                    // 对生命值大于持有者的目标伤害 +7.5
                    if (target.getHealth() > attacker.getHealth()) {
                        bonusDamage += 7.5f;
                    }
                    // 对 Boss 或 玩家 造成的伤害 +10
                    if (isBossOrPlayer(target)) {
                        bonusDamage += 10.0f;
                    }
                }
                case "executioner_gold_pickaxe" -> {
                    boolean used = false;
                    // 检查 4 个盔甲槽是否全空
                    boolean noArmor = target.getItemBySlot(EquipmentSlot.HEAD).isEmpty() &&
                            target.getItemBySlot(EquipmentSlot.CHEST).isEmpty() &&
                            target.getItemBySlot(EquipmentSlot.LEGS).isEmpty() &&
                            target.getItemBySlot(EquipmentSlot.FEET).isEmpty();
                    if (noArmor) {
                        if (isPowerful) bonusDamage += 10 + (attacker.getAttribute(Attributes.ATTACK_DAMAGE) == null ? 0 : attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5F);
                        else bonusDamage += 5.0f;
                        used = true;
                    }

                    // 对护甲值大于 10 的生物伤害 +7.5
                    if (target.getArmorValue() > 10) {
                        if (isPowerful) bonusDamage += 15 + (attacker.getAttribute(Attributes.ATTACK_DAMAGE) == null ? 0 : attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.75F);
                        else bonusDamage += 7.5f;
                        used = true;
                    }

                    if (isPowerful && used && attacker.getHealth() > 0.7 * attacker.getMaxHealth()) {
                        if (attacker.getMaxHealth() < 20) {
                            attacker.addEffect(new MobEffectInstance(MobEffects.HARM ,1));
                        } else {
                            attacker.hurt(attacker.level().damageSources().magic(), (float) (attacker.getHealth() - 0.7 * attacker.getMaxHealth()));
                        }
                    }
                }
                case "crushing_gold_axe" -> {
                    // 额外提升 3+1.5d6 的伤害
                    if (isPowerful) {
                        bonusDamage += 4.5f + attacker.getRandom().nextFloat() * 7.5f;
                    }
                }
            }

            if (bonusDamage > 0.0f) {
                event.setAmount(event.getAmount() + bonusDamage);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {

        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            if (!weapon.hasTag() || !weapon.getTag().contains(NBT_KEY)) return;

            String type = weapon.getTag().getString(NBT_KEY);
            LivingEntity target = event.getEntity();
            if (target.level().isClientSide()) return;

            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();

            if (isPowerful) {
                double attack = attacker.getAttribute(Attributes.ATTACK_DAMAGE) == null ? 0 : attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
                double oldDamage = event.getAmount();
                double useAmount = Math.max(attack,oldDamage);

                switch (type) {
                    case "cursed_gold_sword_t1" -> {
                        // 2 + 50%攻击力 秒 凋零 II
                        int time = (int) (useAmount * 20) + 40;
                        target.addEffect(new MobEffectInstance(MobEffects.WITHER, time, 1), attacker);
                    }
                    case "blazing_gold_sword_t1" -> {
                        // 2 + 50%攻击力 秒 燃烧
                        int time = (int) useAmount + 2;
                        target.setSecondsOnFire(time);
                    }
                    case "blazing_gold_sword_t2" -> {
                        // 1 + 50%攻击力 秒 燃烧
                        int time = (int) useAmount + 1;
                        target.setSecondsOnFire(time);
                    }
                    case "crushing_gold_axe" -> {
                        // 2.5~3.4秒 [脆弱] 效果 (30 ticks)
                        if (ImmortalersDelightMobEffect.VULNERABLE != null) {
                            target.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.VULNERABLE.get(), 50 + attacker.getRandom().nextInt(17), 0), attacker);
                        }
                    }
                }
            } else {
                switch (type) {
                    case "cursed_gold_sword_t1" -> {
                        // 16秒 凋零 II (320 ticks, amplifier 1)
                        target.addEffect(new MobEffectInstance(MobEffects.WITHER, 320, 1), attacker);
                    }
                    case "blazing_gold_sword_t1" -> {
                        // 16秒 燃烧
                        target.setSecondsOnFire(16);
                    }
                    case "blazing_gold_sword_t2" -> {
                        // 10秒 燃烧
                        target.setSecondsOnFire(10);
                    }
                    case "crushing_gold_axe" -> {
                        // 1.5秒 [脆弱] 效果 (30 ticks)
                        if (ImmortalersDelightMobEffect.VULNERABLE != null) {
                            target.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.VULNERABLE.get(), 30, 0), attacker);
                        }
                    }
                }
            }
        }
    }

    //实现超凡模式的金斧1.5倍伤害无视脆弱免疫,以及刷新附魔
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {

        if (!DifficultyModeUtil.isPowerBattleMode()) return;
        LivingEntity attacker = null;
        if (event.getSource().getEntity() instanceof LivingEntity living) {
            attacker = living;
        }
        if (attacker != null) {
            //判断物品是否符合要求
            ItemStack weapon = attacker.getMainHandItem();
            if (!weapon.hasTag() || !weapon.getTag().contains(NBT_KEY)) return;

            //刷新附魔
            reEnch(attacker.level(), weapon);

            String type = weapon.getTag().getString(NBT_KEY);
            if (type.equals("crushing_gold_axe")) {
                if (!event.getEntity().hasEffect(ImmortalersDelightMobEffect.VULNERABLE.get())) event.setAmount(event.getAmount() * 1.5f);
            }
        }
    }

    /**
     * 3. 挖掘速度提升
     */
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        if (!heldItem.hasTag() || !heldItem.getTag().contains(NBT_KEY)) return;

        String type = heldItem.getTag().getString(NBT_KEY);
        BlockState state = event.getState();

        switch (type) {
            case "crushing_gold_axe" -> {
                // 对原木类方块挖掘速度 +10
                if (state.is(BlockTags.LOGS)) {
                    event.setNewSpeed(event.getNewSpeed() + 10.0f);
                }
            }
            case "executioner_gold_pickaxe" -> {
                // 对需要挖掘等级的方块挖掘速度双倍 (requiresCorrectToolForDrops)
                if (state.requiresCorrectToolForDrops()) {
                    event.setNewSpeed(event.getNewSpeed() * 2.0f);
                }
            }
        }
    }

    /**
     * 辅助判断：是否为 Boss 或 玩家
     */
    private static boolean isBossOrPlayer(LivingEntity entity) {
        if (entity instanceof Player) return true;
        // 判断实体是否属于 boss 标签（Forge 标签体系）或没有被正常牵引/具备特殊属性
        return entity.getType().is(Tags.EntityTypes.BOSSES);
    }


    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.hasTag() || !stack.getOrCreateTag().contains(NBT_KEY)) return;

        String type = stack.getOrCreateTag().getString(NBT_KEY);
        List<Component> tooltips = event.getToolTip();

        // 1. 替换标题 (第 0 行)
        if (!tooltips.isEmpty()) {
            tooltips.set(0, Component.translatable("item.immortalers_delight." + type + ".name"));
        }

        // 2. 追加技能说明 Lore (插在Tooltip末尾)
        tooltips.add(Component.empty());
        tooltips.add(Component.translatable("tooltip.immortalers_delight.skill_header").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));

        switch (type) {
            case "cursed_gold_sword_t1" -> {
                tooltips.add(Component.translatable("tooltip.immortalers_delight.cursed_sword_1").withStyle(ChatFormatting.RED));
                tooltips.add(Component.translatable("tooltip.immortalers_delight.cursed_sword_2").withStyle(ChatFormatting.DARK_PURPLE));
            }
            case "blazing_gold_sword_t1" -> {
                tooltips.add(Component.translatable("tooltip.immortalers_delight.blazing_sword_t1_1").withStyle(ChatFormatting.RED));
                tooltips.add(Component.translatable("tooltip.immortalers_delight.blazing_sword_t1_2").withStyle(ChatFormatting.GOLD));
            }
            case "blazing_gold_sword_t2" -> {
                tooltips.add(Component.translatable("tooltip.immortalers_delight.blazing_sword_t2_1").withStyle(ChatFormatting.YELLOW));
                tooltips.add(Component.translatable("tooltip.immortalers_delight.blazing_sword_t2_2").withStyle(ChatFormatting.GOLD));
            }
            case "crushing_gold_axe" -> {
                tooltips.add(Component.translatable("tooltip.immortalers_delight.crushing_axe_1").withStyle(ChatFormatting.DARK_AQUA));
                tooltips.add(Component.translatable("tooltip.immortalers_delight.crushing_axe_2").withStyle(ChatFormatting.GREEN));
            }
            case "executioner_gold_pickaxe" -> {
                tooltips.add(Component.translatable("tooltip.immortalers_delight.executioner_pickaxe_1").withStyle(ChatFormatting.AQUA));
                tooltips.add(Component.translatable("tooltip.immortalers_delight.executioner_pickaxe_2").withStyle(ChatFormatting.DARK_GREEN));
            }
        }
    }

    //这一部分用于让特殊武器在开启超凡模式时获得更强的附魔，同时防止部分模组道具刷附魔
    private static final String IS_AWAKEN = "usedCustomTypeEnchant";
    @SubscribeEvent
    public static void onPickUpItem(EntityItemPickupEvent event) {
        ItemStack stack = event.getItem().getItem();
        reEnch(event.getItem().level(), stack);
    }

    public static boolean reEnch(Level level, ItemStack stack) {
        if (!DifficultyModeUtil.isPowerBattleMode()) return false;
        if (level.isClientSide()) return false;

        if (!stack.hasTag() || !stack.getOrCreateTag().contains(NBT_KEY)) return false;

        if (stack.getOrCreateTag().contains(IS_AWAKEN)) return false;

        stack.removeTagKey("Enchantments");
        String type = stack.getOrCreateTag().getString(NBT_KEY);
        switch (type) {
            case "cursed_gold_sword_t1", "blazing_gold_sword_t1" -> {
                stack.enchant(Enchantments.KNOCKBACK, 5);
                stack.enchant(Enchantments.MOB_LOOTING, 7);
                stack.getOrCreateTag().putBoolean(IS_AWAKEN,true);
            }
            case "blazing_gold_sword_t2" -> {
                stack.enchant(Enchantments.KNOCKBACK, 3);
                stack.enchant(Enchantments.MOB_LOOTING, 5);
                stack.getOrCreateTag().putBoolean(IS_AWAKEN,true);
            }
            case "crushing_gold_axe" -> {
                stack.enchant(Enchantments.BLOCK_FORTUNE, 5);
                stack.enchant(Enchantments.UNBREAKING, 4);
                stack.enchant(Enchantments.MOB_LOOTING, 4);
                stack.getOrCreateTag().putBoolean(IS_AWAKEN,true);
            }
            case "executioner_gold_pickaxe" -> {
                stack.enchant(Enchantments.KNOCKBACK, 4);
                stack.enchant(Enchantments.BLOCK_FORTUNE, 10);
                stack.enchant(Enchantments.MOB_LOOTING, 7);
                stack.getOrCreateTag().putBoolean(IS_AWAKEN,true);
            }
        }
        return true;
    }

    private static ItemStack removeNonCurses(ItemStack pStack, int pDamage) {
        ItemStack itemstack = pStack.copy();
        itemstack.removeTagKey("Enchantments");
        itemstack.removeTagKey("StoredEnchantments");
        if (pDamage > 0) {
            itemstack.setDamageValue(pDamage);
        } else {
            itemstack.removeTagKey("Damage");
        }

        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(pStack).entrySet().stream().filter((p_39584_) -> {
            return p_39584_.getKey().isCurse();
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        EnchantmentHelper.setEnchantments(map, itemstack);
        itemstack.setRepairCost(0);
        if (itemstack.is(Items.ENCHANTED_BOOK) && map.size() == 0) {
            itemstack = new ItemStack(Items.BOOK);
            if (pStack.hasCustomHoverName()) {
                itemstack.setHoverName(pStack.getHoverName());
            }
        }

        for(int i = 0; i < map.size(); ++i) {
            itemstack.setRepairCost(AnvilMenu.calculateIncreasedRepairCost(itemstack.getBaseRepairCost()));
        }

        return itemstack;
    }


    private static final UUID HEALTH_PENALTY_MAINHAND = UUID.fromString("71A35473-5F11-4563-AB92-817FDE4724A1");
    private static final UUID HEALTH_PENALTY_OFFHAND  = UUID.fromString("71A35473-5F11-4563-AB92-817FDE4724A2");
    //这一部分用于让特殊工具武器类似原版般地显示绿色属性（而不是蓝色或红色）
    @SubscribeEvent
    public static void onItemAttributeModifiers(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.hasTag() || !stack.getOrCreateTag().contains(NBT_KEY)) return;

        String type = stack.getOrCreateTag().getString(NBT_KEY);
        EquipmentSlot slot = event.getSlotType();

        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        // 1. 主手槽位的通用属性修饰
        if (slot == EquipmentSlot.MAINHAND) {
            // 先移除可能存在的默认攻击力、攻速修饰符，防止叠加
            event.removeAttribute(Attributes.ATTACK_DAMAGE);
            event.removeAttribute(Attributes.ATTACK_SPEED);

            switch (type) {
                case "cursed_gold_sword_t1" -> {
                    event.removeAttribute(Attributes.MAX_HEALTH);
                    if (isPowerful) {
                        // 最终面板：28 攻击力 (27 增量), 1.6 攻速 (-2.4 增量)
                        event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ReflectionUtil.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 27D, AttributeModifier.Operation.ADDITION));
                        event.addModifier(Attributes.ATTACK_SPEED, new AttributeModifier(ReflectionUtil.BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4D, AttributeModifier.Operation.ADDITION));
                        // -30% 最大生命值
                        event.addModifier(Attributes.MAX_HEALTH, new AttributeModifier(HEALTH_PENALTY_MAINHAND, "Health penalty", -0.3D, AttributeModifier.Operation.MULTIPLY_TOTAL));
                    }
                    else {
                        // 最终面板：8.5 攻击力 (7.5 增量), 1.6 攻速 (-2.4 增量)
                        event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ReflectionUtil.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 7.5D, AttributeModifier.Operation.ADDITION));
                        event.addModifier(Attributes.ATTACK_SPEED, new AttributeModifier(ReflectionUtil.BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4D, AttributeModifier.Operation.ADDITION));
                        // -30% 最大生命值
                        event.addModifier(Attributes.MAX_HEALTH, new AttributeModifier(HEALTH_PENALTY_MAINHAND, "Health penalty", -0.3D, AttributeModifier.Operation.MULTIPLY_TOTAL));
                    }

                }
                case "blazing_gold_sword_t1" -> {
                    if (isPowerful) {
                        // 8.5 攻击力, 1.6 攻速
                        event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ReflectionUtil.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 27D, AttributeModifier.Operation.ADDITION));
                    } else {
                        // 8.5 攻击力, 1.6 攻速
                        event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ReflectionUtil.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 7.5D, AttributeModifier.Operation.ADDITION));
                    }
                    event.addModifier(Attributes.ATTACK_SPEED, new AttributeModifier(ReflectionUtil.BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4D, AttributeModifier.Operation.ADDITION));
                }
                case "blazing_gold_sword_t2" -> {
                    if (isPowerful) {
                        // 18 攻击力 (17 增量), 1.6 攻速 (-2.4 增量)
                        event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ReflectionUtil.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 17D, AttributeModifier.Operation.ADDITION));
                    } else  {
                        // 6.5 攻击力 (5.5 增量), 1.6 攻速 (-2.4 增量)
                        event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ReflectionUtil.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 5.5D, AttributeModifier.Operation.ADDITION));
                    }
                    event.addModifier(Attributes.ATTACK_SPEED, new AttributeModifier(ReflectionUtil.BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4D, AttributeModifier.Operation.ADDITION));
                }
                case "crushing_gold_axe" -> {
                    if (isPowerful) {
                        // 14.0 攻击力 (13.0 增量), 1.6 攻速 (-2.4 增量)
                        event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ReflectionUtil.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 13.0D, AttributeModifier.Operation.ADDITION));
                        event.addModifier(Attributes.ATTACK_SPEED, new AttributeModifier(ReflectionUtil.BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4D, AttributeModifier.Operation.ADDITION));
                    } else {
                        // 12.0 攻击力 (11.0 增量), 1.0 攻速 (-3.0 增量)
                        event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ReflectionUtil.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 11.0D, AttributeModifier.Operation.ADDITION));
                        event.addModifier(Attributes.ATTACK_SPEED, new AttributeModifier(ReflectionUtil.BASE_ATTACK_SPEED_UUID, "Weapon modifier", -3.0D, AttributeModifier.Operation.ADDITION));
                    }
                }
                case "executioner_gold_pickaxe" -> {
                    if (isPowerful) {
                        // 9.0 攻击力 (8.0 增量), 1.6 攻速 (-2.4 增量)
                        event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ReflectionUtil.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 8.0D, AttributeModifier.Operation.ADDITION));
                    } else {
                        // 5.0 攻击力 (4.0 增量), 1.6 攻速 (-2.4 增量)
                        event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ReflectionUtil.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 4.0D, AttributeModifier.Operation.ADDITION));
                    }
                    event.addModifier(Attributes.ATTACK_SPEED, new AttributeModifier(ReflectionUtil.BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4D, AttributeModifier.Operation.ADDITION));
                }
            }
        }

        // 2. 副手槽位的属性修饰 (针对嗜血金剑的 -30% 生命)
        if (slot == EquipmentSlot.OFFHAND) {
            if ("cursed_gold_sword_t1".equals(type)) {
                event.removeAttribute(Attributes.MAX_HEALTH);
                event.addModifier(Attributes.MAX_HEALTH, new AttributeModifier(HEALTH_PENALTY_OFFHAND, "Health penalty offhand", -0.3D, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
        }
    }
}
