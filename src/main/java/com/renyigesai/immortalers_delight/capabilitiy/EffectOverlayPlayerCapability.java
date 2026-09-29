package com.renyigesai.immortalers_delight.capabilitiy;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class EffectOverlayPlayerCapability implements INBTSerializable<CompoundTag> {
    private float deathlessData;
    private float infernalForgingData;
    private float crispFortitudeData;
    private float stunData;

    public float getDeathlessData() {
        return deathlessData;
    }

    public float getInfernalForgingData() {
        return infernalForgingData;
    }

    public float getCrispFortitudeData() {
        return crispFortitudeData;
    }
    public float getStunData() {return stunData;}

    public void setDeathlessData(float deathlessData) {
        this.deathlessData = deathlessData;
    }

    public void setInfernalForgingData(float infernalForgingData) {
        this.infernalForgingData = infernalForgingData;
    }

    public void setCrispFortitudeData(float crispFortitudeData) {
        this.crispFortitudeData = crispFortitudeData;
    }

    public void setStunData(float stunData) {this.stunData = stunData;}
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag= new CompoundTag();
        compoundTag.putFloat("DeathlessData",this.deathlessData);
        compoundTag.putFloat("InfernalForgingData",this.infernalForgingData);
        compoundTag.putFloat("CrispFortitudeData",this.crispFortitudeData);
        compoundTag.putFloat("StunData", this.stunData);
        return compoundTag;}
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.deathlessData = nbt.getFloat("DeathlessData");
        this.infernalForgingData = nbt.getFloat("InfernalForgingData");
        this.crispFortitudeData = nbt.getFloat("CrispFortitudeData");
        this.stunData = nbt.getFloat("StunData");
    }
}
