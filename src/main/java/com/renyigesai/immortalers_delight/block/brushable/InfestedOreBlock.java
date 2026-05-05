package com.renyigesai.immortalers_delight.block.brushable;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
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
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;

public class InfestedOreBlock extends InfestedBlock {

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
        if (pDropExperience) {
            int baseXp = pState.getBlock().getExpDrop(pState, pLevel, pPos, pLevel.getBlockEntity(pPos), null, pStack);
            int xp = EnchantmentHelper.processBlockExperience(pLevel, pStack, baseXp);
            if (xp > 0) {
                pState.getBlock().popExperience(pLevel, pPos, xp);
            }
        }
        if (pLevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && EnchantmentHelper.getItemEnchantmentLevel(pLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), pStack) == 0) {
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
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_coal_extra_attack"),
                                10.0D,
                                AttributeModifier.Operation.ADD_VALUE)
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
                        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_copper_extra_attack"),
                                10.0D,
                                AttributeModifier.Operation.ADD_VALUE)
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
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_iron_extra_speed"),
                                    0.3D,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
                    ambusher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_iron_extra_knockback_resistance"),
                                    0.25D,
                                    AttributeModifier.Operation.ADD_VALUE)
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
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_iron_extra_speed"),
                                    0.3D,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
                    thrasher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_iron_extra_knockback_resistance"),
                                    0.25D,
                                    AttributeModifier.Operation.ADD_VALUE)
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
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_gold_extra_speed"),
                                    0.5D,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
                    ambusher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_gold_extra_knockback_resistance"),
                                    0.44D,
                                    AttributeModifier.Operation.ADD_VALUE)
                    );

                    ambusher.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                } else {
                    ambusher.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_gold_extra_attack"),
                                    3.0D,
                                    AttributeModifier.Operation.ADD_VALUE)
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
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_gold_extra_speed"),
                                    0.5D,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
                    thrasher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_gold_extra_knockback_resistance"),
                                    0.44D,
                                    AttributeModifier.Operation.ADD_VALUE)
                    );

                    thrasher.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                } else {
                    thrasher.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_gold_extra_attack"),
                                    3.0D,
                                    AttributeModifier.Operation.ADD_VALUE)
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
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_iron_extra_speed"),
                                    0.3D,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
                    ambusher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_iron_extra_knockback_resistance"),
                                    0.3D,
                                    AttributeModifier.Operation.ADD_VALUE)
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
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_iron_extra_speed"),
                                    0.3D,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
                    thrasher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_iron_extra_knockback_resistance"),
                                    0.3D,
                                    AttributeModifier.Operation.ADD_VALUE)
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
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_iron_extra_speed"),
                                    0.3D,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
                    ambusher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_iron_extra_knockback_resistance"),
                                    0.3D,
                                    AttributeModifier.Operation.ADD_VALUE)
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
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_iron_extra_speed"),
                                    0.3D,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
                    thrasher.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ImmortalersDelightMod.MODID, "infested_iron_extra_knockback_resistance"),
                                    0.3D,
                                    AttributeModifier.Operation.ADD_VALUE)
                    );
                    thrasher.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                }
                pLevel.addFreshEntity(thrasher);
                thrasher.spawnAnim();
            }
        }
    }
}
