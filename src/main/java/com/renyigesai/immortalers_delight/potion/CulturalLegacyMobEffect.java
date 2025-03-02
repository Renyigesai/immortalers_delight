package com.renyigesai.immortalers_delight.potion;

import com.renyigesai.immortalers_delight.block.CulturalLegacyEffectToolBlock;
import com.renyigesai.immortalers_delight.block.HimekaidoLeavesGrowing;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.CULTURAL_LEGACY;
import static com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect.WEAK_POISON;

public class CulturalLegacyMobEffect extends MobEffect {
    private static final int[] LEVEL_UP = new int[] {5, 10, 15, 18, 20, 22, 23, 24};

    public CulturalLegacyMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -39424);
    }
    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier) {
        if (this == CULTURAL_LEGACY.get()) {
            int lv = amplifier > 7 ? 7 : amplifier;
            if (pEntity.isAlive() || !pEntity.level().isClientSide) {
                if (pEntity.level().isEmptyBlock(pEntity.getOnPos().above())) {
                    pEntity.level().setBlock(pEntity.getOnPos().above(), ImmortalersDelightBlocks.CULTURAL_LEGACY.get().defaultBlockState().setValue(CulturalLegacyEffectToolBlock.AGE,lv),2);
                }
            }
            if (pEntity instanceof Player player) {
                if (player.getEffect(CULTURAL_LEGACY.get()) != null
                        && Objects.requireNonNull(player.getEffect(CULTURAL_LEGACY.get())).getDuration() == 1) {
                    player.giveExperienceLevels(LEVEL_UP[lv]);
                }
            }
        }
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration == 1 || duration % 160 == 0;
    }
}
