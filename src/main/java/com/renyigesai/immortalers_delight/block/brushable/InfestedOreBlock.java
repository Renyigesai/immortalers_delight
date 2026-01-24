package com.renyigesai.immortalers_delight.block.brushable;

import com.renyigesai.immortalers_delight.entities.living.SkelverfishAmbusher;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishBomber;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishThrasher;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class InfestedOreBlock extends InfestedBlock {
    public static final UUID COAL_ATTACK = UUID.fromString("2530111d-6536-7a8e-7618-4c088d7bb51f");
    public static final UUID COPPER_ATTACK = UUID.fromString("2ec93708-353a-d4f5-e8da-fd7be506d818");
    public static final UUID IRON_SPEED = UUID.fromString("4803ed96-b783-e670-dcef-825a2cc1dac8");
    public static final UUID IRON_KNOCKBACK_RESISTANCE = UUID.fromString("ad3ef751-8bcd-e3b6-0d7c-f65071453614");
    public static final UUID GOLD_ATTACK = UUID.fromString("e7ae4383-6c10-7d59-e019-3a1429a60dcd");
    public static final UUID GOLD_SPEED = UUID.fromString("bd04f084-221c-bee6-3c6a-85f1af90a472");
    public static final UUID GOLD_KNOCKBACK_RESISTANCE = UUID.fromString("1485c2b4-50f8-21c5-5e59-0499daf30fef");
    public static final UUID EMERALD_ATTACK = UUID.fromString("c3eece36-e9e3-685f-58a7-d7ab467b04ce");
    public static final UUID EMERALD_SPEED = UUID.fromString("06984096-4dd8-1e13-fdde-62f331b90b57");
    public static final UUID EMERALD_KNOCKBACK_RESISTANCE = UUID.fromString("82fbe076-6d1a-9f4c-b4df-8ce31661532a");
    public static final UUID REDSTONE_SPEED = UUID.fromString("0314adbc-a5da-5c60-9a08-9040c21578a9");
    public static final UUID REDSTONE_KNOCKBACK_RESISTANCE = UUID.fromString("8c08f698-8838-db9d-704f-81b1e9a21bb0");
    public static final UUID LAPIS_SPEED = UUID.fromString("d8c7a3e0-e035-6ee3-685b-0729e4ac5c53");
    public static final UUID LAPIS_KNOCKBACK_RESISTANCE = UUID.fromString("4d876d4c-e99a-e98d-0bc2-30f8b3bb3ab3");
    public static final UUID DIAMOND_SPEED = UUID.fromString("f79a04f0-86ee-c2b7-a55b-cfc6977d3654");
    public static final UUID DIAMOND_ATTACK = UUID.fromString("7c053c50-1287-fc90-f432-c3c824ed655b");

    public InfestedOreBlock(Block pHostBlock, Properties pProperties) {
        super(pHostBlock, pProperties);
    }

    private void spawnInfestation(ServerLevel pLevel, BlockPos pPos) {
        Silverfish silverfish = EntityType.SILVERFISH.create(pLevel);
        if (silverfish != null) {
            silverfish.moveTo((double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, 0.0F, 0.0F);

            pLevel.addFreshEntity(silverfish);
            silverfish.spawnAnim();
        }
    }

    @Override
    public void spawnAfterBreak(BlockState pState, ServerLevel pLevel, BlockPos pPos, ItemStack pStack, boolean pDropExperience) {
        if (pDropExperience) net.minecraftforge.common.ForgeHooks.dropXpForBlock(pState, pLevel, pPos, pStack);
        if (pLevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, pStack) == 0) {
            //this.spawnInfestation(pLevel, pPos);
            this.spawnIronBlockInfestation(pLevel, pPos);
        }
    }

    private void spawnCoalBlockInfestation(ServerLevel pLevel, BlockPos pPos) {
        SkelverfishBomber bomber = ImmortalersDelightEntities.SKELVERFISH_BOMBER.get().create(pLevel);
        if (bomber != null) {
            bomber.moveTo((double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, 0.0F, 0.0F);
            //超凡模式额外加强：攻击力+10
            if (DifficultyModeUtil.isPowerBattleMode()) {
                bomber.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(
                        new AttributeModifier(COAL_ATTACK,
                                "infested_coal_extra_attack",
                                10.0F,
                                AttributeModifier.Operation.ADDITION)
                );
            }
            pLevel.addFreshEntity(bomber);
            bomber.spawnAnim();
        }
    }
    private void spawnCopperBlockInfestation(ServerLevel pLevel, BlockPos pPos) {
        SkelverfishThrasher thrasher = ImmortalersDelightEntities.SKELVERFISH_THRASHER.get().create(pLevel);
        if (thrasher != null) {
            thrasher.moveTo((double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, 0.0F, 0.0F);
            //超凡模式额外加强：攻击力+20%，每秒回复0.3生命
            if (DifficultyModeUtil.isPowerBattleMode()) {
                thrasher.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(
                        new AttributeModifier(COPPER_ATTACK,
                                "infested_copper_extra_attack",
                                10.0F,
                                AttributeModifier.Operation.ADDITION)
                );
                thrasher.addEffect(new MobEffectInstance(MobEffects.REGENERATION, -1,1));
                thrasher.addEffect(new MobEffectInstance(MobEffects.WITHER, -1));
            }
            pLevel.addFreshEntity(thrasher);
            thrasher.spawnAnim();
        }
    }
    private void spawnIronBlockInfestation(ServerLevel pLevel, BlockPos pPos) {
        SkelverfishAmbusher ambusher = null;
        SkelverfishThrasher thrasher = null;
        for (int i = 0; i < (DifficultyModeUtil.isPowerBattleMode() ? 4 : 2); i++) {
            ambusher = ImmortalersDelightEntities.SKELVERFISH_AMBUSHER.get().create(pLevel);
            if (ambusher != null) {
                ambusher.moveTo((double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, 0.0F, 0.0F);
                if (DifficultyModeUtil.isPowerBattleMode() && i >= 2) {
                    ambusher.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(
                            new AttributeModifier(IRON_SPEED,
                                    "infested_iron_extra_speed",
                                    0.3F,
                                    AttributeModifier.Operation.MULTIPLY_BASE)
                    );
                    ambusher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(IRON_KNOCKBACK_RESISTANCE,
                                    "infested_iron_extra_knockback_resistance",
                                    0.25F,
                                    AttributeModifier.Operation.ADDITION)
                    );
                    ambusher.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                }
                pLevel.addFreshEntity(ambusher);
                ambusher.spawnAnim();
            }
        }
        for (int i = 0; i < (DifficultyModeUtil.isPowerBattleMode() ? 4 : 2); i++) {
            thrasher = ImmortalersDelightEntities.SKELVERFISH_THRASHER.get().create(pLevel);
            if (thrasher != null) {
                thrasher.moveTo((double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, 0.0F, 0.0F);
                if (DifficultyModeUtil.isPowerBattleMode() && i >= 2) {
                    thrasher.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(
                            new AttributeModifier(IRON_SPEED,
                                    "infested_iron_extra_speed",
                                    0.3F,
                                    AttributeModifier.Operation.MULTIPLY_BASE)
                    );
                    thrasher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(IRON_KNOCKBACK_RESISTANCE,
                                    "infested_iron_extra_knockback_resistance",
                                    0.25F,
                                    AttributeModifier.Operation.ADDITION)
                    );
                    thrasher.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                }
                pLevel.addFreshEntity(thrasher);
                thrasher.spawnAnim();
            }
        }
    }

    private void spawnGoldBlockInfestation(ServerLevel pLevel, BlockPos pPos) {
        SkelverfishAmbusher ambusher = null;
        SkelverfishThrasher thrasher = null;
        for (int i = 0; i < (DifficultyModeUtil.isPowerBattleMode() ? 5 : 2); i++) {
            ambusher = ImmortalersDelightEntities.SKELVERFISH_AMBUSHER.get().create(pLevel);
            if (ambusher != null) {
                ambusher.moveTo((double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, 0.0F, 0.0F);
                if (DifficultyModeUtil.isPowerBattleMode() && i >= 2) {
                    ambusher.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(
                            new AttributeModifier(GOLD_SPEED,
                                    "infested_gold_extra_speed",
                                    0.5F,
                                    AttributeModifier.Operation.MULTIPLY_BASE)
                    );
                    ambusher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(GOLD_KNOCKBACK_RESISTANCE,
                                    "infested_gold_extra_knockback_resistance",
                                    0.44F,
                                    AttributeModifier.Operation.ADDITION)
                    );

                    ambusher.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                } else {
                    ambusher.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(
                            new AttributeModifier(GOLD_ATTACK,
                                    "infested_gold_extra_knockback_resistance",
                                    3.0F,
                                    AttributeModifier.Operation.ADDITION)
                    );
                }
                pLevel.addFreshEntity(ambusher);
                ambusher.spawnAnim();
            }
        }
        for (int i = 0; i < (DifficultyModeUtil.isPowerBattleMode() ? 5 : 2); i++) {
            thrasher = ImmortalersDelightEntities.SKELVERFISH_THRASHER.get().create(pLevel);
            if (thrasher != null) {
                thrasher.moveTo((double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, 0.0F, 0.0F);
                if (DifficultyModeUtil.isPowerBattleMode() && i >= 2) {
                    thrasher.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(
                            new AttributeModifier(GOLD_SPEED,
                                    "infested_gold_extra_speed",
                                    0.5F,
                                    AttributeModifier.Operation.MULTIPLY_BASE)
                    );
                    thrasher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(GOLD_KNOCKBACK_RESISTANCE,
                                    "infested_gold_extra_knockback_resistance",
                                    0.44F,
                                    AttributeModifier.Operation.ADDITION)
                    );

                    thrasher.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                } else {
                    thrasher.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(
                            new AttributeModifier(GOLD_ATTACK,
                                    "infested_gold_extra_knockback_resistance",
                                    3.0F,
                                    AttributeModifier.Operation.ADDITION)
                    );
                }
                pLevel.addFreshEntity(thrasher);
                thrasher.spawnAnim();
            }
        }
    }

    private void spawnEmeraldBlockInfestation(ServerLevel pLevel, BlockPos pPos) {
        SkelverfishAmbusher ambusher = null;
        SkelverfishThrasher thrasher = null;
        for (int i = 0; i < (DifficultyModeUtil.isPowerBattleMode() ? 4 : 2); i++) {
            ambusher = ImmortalersDelightEntities.SKELVERFISH_AMBUSHER.get().create(pLevel);
            if (ambusher != null) {
                ambusher.moveTo((double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, 0.0F, 0.0F);
                if (DifficultyModeUtil.isPowerBattleMode() && i >= 2) {
                    ambusher.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(
                            new AttributeModifier(IRON_SPEED,
                                    "infested_iron_extra_speed",
                                    0.3F,
                                    AttributeModifier.Operation.MULTIPLY_BASE)
                    );
                    ambusher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(IRON_KNOCKBACK_RESISTANCE,
                                    "infested_iron_extra_knockback_resistance",
                                    0.3F,
                                    AttributeModifier.Operation.ADDITION)
                    );
                    ambusher.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                }
                pLevel.addFreshEntity(ambusher);
                ambusher.spawnAnim();
            }
        }
        for (int i = 0; i < (DifficultyModeUtil.isPowerBattleMode() ? 4 : 2); i++) {
            thrasher = ImmortalersDelightEntities.SKELVERFISH_THRASHER.get().create(pLevel);
            if (thrasher != null) {
                thrasher.moveTo((double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, 0.0F, 0.0F);
                if (DifficultyModeUtil.isPowerBattleMode() && i >= 2) {
                    thrasher.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(
                            new AttributeModifier(IRON_SPEED,
                                    "infested_iron_extra_speed",
                                    0.3F,
                                    AttributeModifier.Operation.MULTIPLY_BASE)
                    );
                    thrasher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(IRON_KNOCKBACK_RESISTANCE,
                                    "infested_iron_extra_knockback_resistance",
                                    0.3F,
                                    AttributeModifier.Operation.ADDITION)
                    );
                    thrasher.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                }
                pLevel.addFreshEntity(thrasher);
                thrasher.spawnAnim();
            }
        }
    }
    private void spawnDiamondBlockInfestation(ServerLevel pLevel, BlockPos pPos) {
        SkelverfishAmbusher ambusher = null;
        SkelverfishThrasher thrasher = null;
        for (int i = 0; i < (DifficultyModeUtil.isPowerBattleMode() ? 4 : 2); i++) {
            ambusher = ImmortalersDelightEntities.SKELVERFISH_AMBUSHER.get().create(pLevel);
            if (ambusher != null) {
                ambusher.moveTo((double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, 0.0F, 0.0F);
                if (DifficultyModeUtil.isPowerBattleMode() && i >= 2) {
                    ambusher.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(
                            new AttributeModifier(IRON_SPEED,
                                    "infested_iron_extra_speed",
                                    0.3F,
                                    AttributeModifier.Operation.MULTIPLY_BASE)
                    );
                    ambusher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(IRON_KNOCKBACK_RESISTANCE,
                                    "infested_iron_extra_knockback_resistance",
                                    0.3F,
                                    AttributeModifier.Operation.ADDITION)
                    );
                    ambusher.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                }
                pLevel.addFreshEntity(ambusher);
                ambusher.spawnAnim();
            }
        }
        for (int i = 0; i < (DifficultyModeUtil.isPowerBattleMode() ? 4 : 2); i++) {
            thrasher = ImmortalersDelightEntities.SKELVERFISH_THRASHER.get().create(pLevel);
            if (thrasher != null) {
                thrasher.moveTo((double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, 0.0F, 0.0F);
                if (DifficultyModeUtil.isPowerBattleMode() && i >= 2) {
                    thrasher.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(
                            new AttributeModifier(IRON_SPEED,
                                    "infested_iron_extra_speed",
                                    0.3F,
                                    AttributeModifier.Operation.MULTIPLY_BASE)
                    );
                    thrasher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(IRON_KNOCKBACK_RESISTANCE,
                                    "infested_iron_extra_knockback_resistance",
                                    0.3F,
                                    AttributeModifier.Operation.ADDITION)
                    );
                    thrasher.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                }
                pLevel.addFreshEntity(thrasher);
                thrasher.spawnAnim();
            }
        }
    }
}
