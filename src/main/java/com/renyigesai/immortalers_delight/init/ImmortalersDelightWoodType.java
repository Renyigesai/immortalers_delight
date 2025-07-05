package com.renyigesai.immortalers_delight.init;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import static net.minecraft.world.level.block.state.properties.WoodType.register;

public class ImmortalersDelightWoodType {
    public static final WoodType HIMEKAIDO = register(new WoodType("himekaido", ImmortalersDelightWoodSetType.HIMEKAIDO));
    public static final WoodType LEISAMBOO = register(new WoodType("leisamboo", ImmortalersDelightWoodSetType.LEISAMBOO));
    public static final WoodType PEARLIP_SHELL = register(new WoodType("pearlip_shell", ImmortalersDelightWoodSetType.PEARLIP_SHELL));
    public static final WoodType ANCIENT_WOOD = register(new WoodType("ancient_wood", ImmortalersDelightWoodSetType.ANCIENT_WOOD));

}
