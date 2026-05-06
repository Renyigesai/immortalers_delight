package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.event.SnifferEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Sniffer.class)
public abstract class SnifferMixin extends Animal {

    protected SnifferMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean canFallInLove() {
        CompoundTag tag = this.getPersistentData();
        if (!tag.contains(SnifferEvent.SNIFFER_TAIL_REGENERATION_COOLDOWN, Tag.TAG_INT)) {
            return super.canFallInLove();
        }
        if (tag.getInt(SnifferEvent.SNIFFER_TAIL_REGENERATION_COOLDOWN) == 0) {
            return super.canFallInLove();
        }
        return false;
    }
}
