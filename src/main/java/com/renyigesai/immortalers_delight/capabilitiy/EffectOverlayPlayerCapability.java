package com.renyigesai.immortalers_delight.capabilitiy;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class EffectOverlayPlayerCapability implements INBTSerializable<CompoundTag> {
    private float deathlessData;
    private float infernalForgingData;
    private float purplePower;

    public float getDeathlessData() {
        return deathlessData;
    }

    public float getInfernalForgingData() {
        return infernalForgingData;
    }

    public float getPurplePower() {
        return purplePower;
    }

    public void setDeathlessData(float deathlessData) {
        this.deathlessData = deathlessData;
    }

    public void setInfernalForgingData(float infernalForgingData) {
        this.infernalForgingData = infernalForgingData;
    }

    public void setPurplePower(float purplePower) {
        this.purplePower = purplePower;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag= new CompoundTag();
        compoundTag.putFloat("DeathlessData",this.deathlessData);
        compoundTag.putFloat("InfernalForgingData",this.infernalForgingData);
        compoundTag.putFloat("PurpleMP",this.purplePower);

        return compoundTag;}
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.deathlessData = nbt.getFloat("DeathlessData");
        this.infernalForgingData = nbt.getFloat("InfernalForgingData");
        this.purplePower = nbt.getFloat("PurpleMP");
    }
}
