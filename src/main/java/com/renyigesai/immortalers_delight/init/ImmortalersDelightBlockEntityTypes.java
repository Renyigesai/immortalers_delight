package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.hanging_sign.ImmortalersDelightHangingSignBlockEntity;
import com.renyigesai.immortalers_delight.block.sign.ImmortalersDelightSignBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ImmortalersDelightBlockEntityTypes{

    public static final DeferredRegister<BlockEntityType<?>> TILES =  DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ImmortalersDelightMod.MODID);
    public static final RegistryObject<BlockEntityType<ImmortalersDelightSignBlockEntity>> SIGN = TILES.register("sign", () ->
            BlockEntityType.Builder.of(ImmortalersDelightSignBlockEntity::new,
                    new Block[]{
                            ImmortalersDelightBlocks.HIMEKAIDO_SIGN.get(),
                            ImmortalersDelightBlocks.HIMEKAIDO_WALL_SIGN.get(),
                            ImmortalersDelightBlocks.LEISAMBOO_SIGN.get(),
                            ImmortalersDelightBlocks.LEISAMBOO_WALL_SIGN.get()
            }).build(null));
    public static final RegistryObject<BlockEntityType<ImmortalersDelightHangingSignBlockEntity>> HANGING_SIGN = TILES.register("hanging_sign", () ->
            BlockEntityType.Builder.of(ImmortalersDelightHangingSignBlockEntity::new,
                    new Block[]{
                            ImmortalersDelightBlocks.HIMEKAIDO_HANGING_SIGN.get(),
                            ImmortalersDelightBlocks.HIMEKAIDO_WALL_HANGING_SIGN.get(),
                            ImmortalersDelightBlocks.LEISAMBOO_HANGING_SIGN.get(),
                            ImmortalersDelightBlocks.LEISAMBOO_WALL_HANGING_SIGN.get()
            }).build(null));


}
