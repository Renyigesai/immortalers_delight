package com.renyigesai.immortalers_delight.capabilitiy;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ImmortalersCapabilities {
    public static final Capability<KeyAuxiliaryPlayerCapability> PLAYER_KEY_AUXILIARY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<EffectOverlayPlayerCapability> PLAYER_EFFECT_OVERLAY = CapabilityManager.get(new CapabilityToken<>() {
    });

    @Mod.EventBusSubscriber
    public static class AdditionCapabilities {
        @SubscribeEvent
        public static void attachCapabilityToEntityHandler(AttachCapabilitiesEvent<Entity> event){
            if (event.getObject() instanceof Player) {
                //玩家注册按键能力
                final KeyAuxiliaryPlayerCapability playerKeyCap = new KeyAuxiliaryPlayerCapability();
                final LazyOptional<KeyAuxiliaryPlayerCapability> capOptional = LazyOptional.of(() -> playerKeyCap);
                ICapabilityProvider provider = new ICapabilitySerializable<CompoundTag>(){
                    @Nonnull
                    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction){
                        return cap ==PLAYER_KEY_AUXILIARY?capOptional.cast():LazyOptional.empty();
                    }
                    public CompoundTag serializeNBT(){return playerKeyCap.serializeNBT();}
                    public void deserializeNBT(CompoundTag nbt){playerKeyCap.deserializeNBT(nbt);};
                };
                event.addCapability(new ResourceLocation(ImmortalersDelightMod.MODID,"key"), provider);

                //玩家注册效果显示数据能力
                final EffectOverlayPlayerCapability playerEffectOverlayCap = new EffectOverlayPlayerCapability();
                final LazyOptional<EffectOverlayPlayerCapability> capOptional2 = LazyOptional.of(() -> playerEffectOverlayCap);
                ICapabilityProvider provider2 = new ICapabilitySerializable<CompoundTag>(){
                    @Nonnull
                    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction){
                        return cap ==PLAYER_EFFECT_OVERLAY?capOptional2.cast():LazyOptional.empty();
                    }
                    public CompoundTag serializeNBT(){return playerEffectOverlayCap.serializeNBT();}
                    public void deserializeNBT(CompoundTag nbt){playerEffectOverlayCap.deserializeNBT(nbt);};
                };
                event.addCapability(new ResourceLocation(ImmortalersDelightMod.MODID,"ime"), provider2);
            }
        }
    }
}