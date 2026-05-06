package com.renyigesai.immortalers_delight.item;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class ImmortalersKnifeItem extends KnifeItem {
    public static final int ANCIENT_KNIFE_TYPE = 1;
    public static final int NEW_ANCIENT_KNIFE_TYPE = 2;
    public static final int PILLAGER_KNIFE_TYPE = 3;
    public static final int BONE_KNIFE_TYPE = 4;
    public static final String ANCIENT_KNIFE_COMBO_SKILL = ImmortalersDelightMod.MODID + "_ancient_knife_combo_skill";
    protected int type_id;
    protected final float attackDamage;
    protected final float attackSpeed;
    protected final float extra_attackDamage;
    protected final float extra_attackSpeed;
    private final float attackDamageModifier;
    private final float attackSpeedModifier;
    public int getTypeId() {return this.type_id;}
    public ImmortalersKnifeItem(int type, Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, ImmortalersDelightItems.withTierToolAttributes(properties, tier, attackDamage, attackSpeed));
        this.type_id = type;
        this.attackDamageModifier = attackDamage;
        this.attackSpeedModifier = attackSpeed;
        this.attackDamage = attackDamage + tier.getAttackDamageBonus();
        this.attackSpeed = attackSpeed;
        this.extra_attackDamage = 0;
        this.extra_attackSpeed = 0;
    }

    public ImmortalersKnifeItem(int type, Tier tier, float attackDamage, float attackSpeed, float extra_attackDamage, float extra_attackSpeed, Properties properties) {
        super(tier, ImmortalersDelightItems.withTierToolAttributes(properties, tier, attackDamage, attackSpeed));
        this.type_id = type;
        this.attackDamageModifier = attackDamage;
        this.attackSpeedModifier = attackSpeed;
        this.attackDamage = attackDamage + tier.getAttackDamageBonus();
        this.attackSpeed = attackSpeed;
        this.extra_attackDamage = extra_attackDamage;
        this.extra_attackSpeed = extra_attackSpeed;
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Holder<Enchantment> enchantment) {
        int level = EnchantmentHelper.getTagEnchantmentLevel(enchantment, stack);
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        int type = this.type_id;
        if (type == PILLAGER_KNIFE_TYPE) {
            if (enchantment.is(Enchantments.LOOTING)) level += isPowerful ? 4 : 2;
        }
        if (type == BONE_KNIFE_TYPE) {
            if (enchantment.is(Enchantments.LOOTING) && level > 0) level -= 1;
        }
        return level;
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        ItemAttributeModifiers base = ImmortalersDelightItems.tierToolModifiers(getTier(), attackDamageModifier, attackSpeedModifier);
        boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
        if (!isPowerful || (extra_attackDamage == 0 && extra_attackSpeed == 0)) {
            return base;
        }
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        for (ItemAttributeModifiers.Entry entry : base.modifiers()) {
            builder.add(entry.attribute(), entry.modifier(), entry.slot());
        }
        if (extra_attackDamage != 0) {
            builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "knife_power_damage"), extra_attackDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        }
        if (extra_attackSpeed != 0) {
            builder.add(Attributes.ATTACK_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "knife_power_speed"), extra_attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        }
        return builder.build();
    }

    /** Farmer's Delight {@code TextUtils} expects {@code tooltip.<path>}, not {@code tooltip.<namespace:path>}. */
    private String farmersDelightTooltipPath() {
        return BuiltInRegistries.ITEM.getKey(this).getPath();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (this.type_id == ANCIENT_KNIFE_TYPE) {
            MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + farmersDelightTooltipPath(), new Object[0]);
            tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
        }
        if (this.type_id == NEW_ANCIENT_KNIFE_TYPE) {
            for (int i = 0; i < 3; i++) {
                MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + farmersDelightTooltipPath() + "." + ((i == 2 && DifficultyModeUtil.isPowerBattleMode()) ? "power" : i), new Object[0]);
                if(i == 1) {
                    tooltip.add(textEmpty.withStyle(ChatFormatting.GRAY));
                } else if (i == 2) {
                    tooltip.add(textEmpty.withStyle(ChatFormatting.DARK_GREEN));
                } else {
                    tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
                }
            }
        }
        if (this.type_id == BONE_KNIFE_TYPE) {
            MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + farmersDelightTooltipPath(), new Object[0]);
            tooltip.add(textEmpty.withStyle(ChatFormatting.YELLOW));
            MutableComponent textEmpty1 = TextUtils.getTranslation("tooltip." + farmersDelightTooltipPath() + ".1", new Object[0]);
            tooltip.add(textEmpty1.withStyle(ChatFormatting.RED));
        }

        super.appendHoverText(stack, context, tooltip, isAdvanced);
    }

    @EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID)
    public static class ImmortalersKnifeEvents {
        @SubscribeEvent
        public static void ImmortalersKnifeAttack(LivingDamageEvent.Pre event) {

            boolean isPowerful = DifficultyModeUtil.isPowerBattleMode();
            LivingEntity hurtOne = event.getEntity();
            if (hurtOne.level().isClientSide) return;

            if (event.getSource().getEntity() instanceof LivingEntity attacker){
                ItemStack toolStack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
                if (!toolStack.isEmpty() && toolStack.getItem() instanceof ImmortalersKnifeItem knife) {
                    if (knife.getTypeId() == 1) {
                        hurtOne.addEffect(new MobEffectInstance(ImmortalersDelightMobEffect.WEAK_WITHER, 320, isPowerful ? 2 : 1));
                    }
                    if (knife.getTypeId() == 2) {
                        ItemStack toolStack2 = attacker.getItemInHand(InteractionHand.OFF_HAND);
                        if (!toolStack2.isEmpty() && toolStack2.is(ImmortalersDelightItems.ANCIENT_BLADE.get())) {
                            float buffer = event.getNewDamage() * 0.6F;
                            if (!isPowerful && buffer > 4) buffer = 4;
                            event.setNewDamage(event.getNewDamage() + buffer);
                            if (attacker instanceof ServerPlayer serverPlayer && !serverPlayer.getAbilities().instabuild) {
                                toolStack2.hurtAndBreak(1, serverPlayer, EquipmentSlot.OFFHAND);
                            }
                        }
                    }
                }
            }

        }

        @SubscribeEvent
        public static void onCakeInteraction(PlayerInteractEvent.RightClickBlock event) {
            ItemStack toolStack = event.getEntity().getItemInHand(event.getHand());
            if (toolStack.is(ImmortalersDelightTags.IMMORTAL_KNIVES)) {
                Level level = event.getLevel();
                BlockPos pos = event.getPos();
                BlockState state = event.getLevel().getBlockState(pos);
                Block block = state.getBlock();
                if (state.is(ImmortalersDelightTags.FARMERSDELIGHT_DROPS_CAKE_SLICE)) {
                    level.setBlock(pos, (BlockState) Blocks.CAKE.defaultBlockState().setValue(CakeBlock.BITES, 1), 3);
                    Block.dropResources(state, level, pos);
                    ItemUtils.spawnItemEntity(level, new ItemStack((ItemLike) ModItems.CAKE_SLICE.get()), (double)pos.getX(), (double)pos.getY() + 0.2, (double)pos.getZ() + 0.5, -0.05, 0.0, 0.0);
                    level.playSound((Player)null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                }

                if (block == Blocks.CAKE) {
                    int bites = (Integer)state.getValue(CakeBlock.BITES);
                    if (bites < 6) {
                        level.setBlock(pos, (BlockState)state.setValue(CakeBlock.BITES, bites + 1), 3);
                    } else {
                        level.removeBlock(pos, false);
                    }

                    ItemUtils.spawnItemEntity(level, new ItemStack((ItemLike)ModItems.CAKE_SLICE.get()), (double)pos.getX() + (double)bites * 0.1, (double)pos.getY() + 0.2, (double)pos.getZ() + 0.5, -0.05, 0.0, 0.0);
                    level.playSound((Player)null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                }

            }
        }

    }
}
