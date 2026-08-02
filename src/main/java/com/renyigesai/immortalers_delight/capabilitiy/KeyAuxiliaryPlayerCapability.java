package com.renyigesai.immortalers_delight.capabilitiy;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class KeyAuxiliaryPlayerCapability implements INBTSerializable<CompoundTag> {

    public boolean key = false;
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag= new CompoundTag();
        compoundTag.putBoolean("key",this.key);
        return compoundTag;}
    @Override
    public void deserializeNBT(CompoundTag tag) {this.key = tag.getBoolean("key");}
    public void setKey(boolean key){this.key = key;}
    public boolean isKey() {return key;}
}
