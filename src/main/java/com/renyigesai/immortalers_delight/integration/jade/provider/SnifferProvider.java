package com.renyigesai.immortalers_delight.integration.jade.provider;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.event.SnifferEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.impl.theme.ThemeHelper;

public enum SnifferProvider implements IEntityComponentProvider , IServerDataProvider<EntityAccessor> {
    INSTANCE;
    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        if (entityAccessor.getServerData().contains(SnifferEvent.SNIFFER_TAIL_REGENERATION_COOLDOWN)){
            int tailTime = entityAccessor.getServerData().getInt(SnifferEvent.SNIFFER_TAIL_REGENERATION_COOLDOWN);
            if (tailTime > 0){
                iTooltip.add(Component.translatable("tooltip.immortalers_delight.sniffer_tail_regeneration").withStyle(ChatFormatting.GRAY).append(ThemeHelper.INSTANCE.seconds(tailTime)));
            }
        }
        if (entityAccessor.getServerData().contains(SnifferEvent.SNIFFER_BRUSHING_COOLDOWN)){
            int brushingTime = entityAccessor.getServerData().getInt(SnifferEvent.SNIFFER_BRUSHING_COOLDOWN);
            if (brushingTime > 0){
                iTooltip.add(Component.translatable("tooltip.immortalers_delight.sniffer_brushing").withStyle(ChatFormatting.GRAY).append(ThemeHelper.INSTANCE.seconds(brushingTime)));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(ImmortalersDelightMod.MODID,"sniffer");
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, EntityAccessor entityAccessor) {
        Sniffer sniffer = (Sniffer)entityAccessor.getEntity();
        if (sniffer.getPersistentData().contains(SnifferEvent.SNIFFER_TAIL_REGENERATION_COOLDOWN, Tag.TAG_INT)){
            compoundTag.putInt(SnifferEvent.SNIFFER_TAIL_REGENERATION_COOLDOWN,sniffer.getPersistentData().getInt(SnifferEvent.SNIFFER_TAIL_REGENERATION_COOLDOWN));
        }
        if (sniffer.getPersistentData().contains(SnifferEvent.SNIFFER_BRUSHING_COOLDOWN, Tag.TAG_INT)){
            compoundTag.putInt(SnifferEvent.SNIFFER_BRUSHING_COOLDOWN,sniffer.getPersistentData().getInt(SnifferEvent.SNIFFER_BRUSHING_COOLDOWN));
        }
    }
}
