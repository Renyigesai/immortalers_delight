package com.renyigesai.immortalers_delight.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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

    public InfestedOreBlock(Block pHostBlock, Properties pProperties) {
        super(pHostBlock, pProperties);
    }
    private void spawnInfestation(ServerLevel pLevel, BlockPos pPos) {
        Silverfish silverfish = EntityType.SILVERFISH.create(pLevel);
        if (silverfish != null) {
            silverfish.moveTo((double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, 0.0F, 0.0F);

            // 1. 添加移动速度修饰符（+30%）
            UUID speedUuid = UUID.fromString("662a6b8d-662a-6b8d-662a-6b8d662a6b8d"); // 自定义唯一UUID
            AttributeModifier speedModifier = new AttributeModifier(
                    speedUuid,
                    "Silverfish Speed Boost",
                    0.3,
                    AttributeModifier.Operation.MULTIPLY_BASE
            );
            silverfish.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(speedModifier);

            // 2. 添加击退抗性修饰符（25%）
            UUID knockbackUuid = UUID.fromString("772a7b8d-772a-7b8d-772a-7b8d772a7b8d");
            AttributeModifier knockbackModifier = new AttributeModifier(
                    knockbackUuid,
                    "Silverfish Knockback Resistance",
                    0.9,
                    AttributeModifier.Operation.ADDITION
            );
            silverfish.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(knockbackModifier);

            // 3. 装备石剑
            silverfish.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));

            pLevel.addFreshEntity(silverfish);
            silverfish.spawnAnim();
        }
    }

    @Override
    public void spawnAfterBreak(BlockState pState, ServerLevel pLevel, BlockPos pPos, ItemStack pStack, boolean pDropExperience) {
        super.spawnAfterBreak(pState, pLevel, pPos, pStack, pDropExperience);
        if (pLevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, pStack) == 0) {
            this.spawnInfestation(pLevel, pPos);
        }

    }
}
