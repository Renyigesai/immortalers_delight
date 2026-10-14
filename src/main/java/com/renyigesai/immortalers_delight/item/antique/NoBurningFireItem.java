package com.renyigesai.immortalers_delight.item.antique;

import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class NoBurningFireItem extends AbstractAntiqueItem{
    public NoBurningFireItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers() {
        return EMPTY.build();
    }

    @Override
    public void onPlace(Player player, Level level) {

    }

    @Override
    public void onTake(Player player, Level level) {

    }

    @Override
    public boolean isTick(Player player, Level level) {
        return true;
    }

    @Override
    public void onTick(Player player, Level level) {
        if (player.tickCount % 300 == 0){
            float maxHealth = player.getMaxHealth();
            if (player.getHealth() < maxHealth && player.getHealth() > 0) {
                player.heal(maxHealth * 0.1F);
            }
        }

        if (player.tickCount % 100 == 0){
            if (player.isInWater()) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 0));
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 120, 0));
            }
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 120, 0, false, false));
        }

    }
}
