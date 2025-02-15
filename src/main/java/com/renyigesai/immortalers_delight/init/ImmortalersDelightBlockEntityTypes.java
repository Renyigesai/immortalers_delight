package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.himekaido_sign.HimekaidoSignBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmortalersDelightBlockEntityTypes{

    public static final DeferredRegister<BlockEntityType<?>> TILES =  DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ImmortalersDelightMod.MODID);
    public static final RegistryObject<BlockEntityType<HimekaidoSignBlockEntity>> SIGN = TILES.register("sign", () ->
            BlockEntityType.Builder.of(HimekaidoSignBlockEntity::new, new Block[]{ImmortalersDelightBlocks.HIMEKAIDO_SIGN.get()}).build(null));

}
