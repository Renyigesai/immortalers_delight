package com.renyigesai.immortalers_delight.init;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.hanging_sign.ImmortalersDelightHangingSignBlockEntity;
import com.renyigesai.immortalers_delight.block.sign.ImmortalersDelightSignBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ImmortalersDelightBlockEntityTypes{

    public static final DeferredRegister<BlockEntityType<?>> TILES =  DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ImmortalersDelightMod.MODID);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ImmortalersDelightSignBlockEntity>> SIGN = TILES.register("sign", () ->
            BlockEntityType.Builder.of(ImmortalersDelightSignBlockEntity::new,
                    new Block[]{
                            ImmortalersDelightBlocks.HIMEKAIDO_SIGN.get(),
                            ImmortalersDelightBlocks.HIMEKAIDO_WALL_SIGN.get(),
                            ImmortalersDelightBlocks.LEISAMBOO_SIGN.get(),
                            ImmortalersDelightBlocks.LEISAMBOO_WALL_SIGN.get(),
                            ImmortalersDelightBlocks.PEARLIP_SHELL_SIGN.get(),
                            ImmortalersDelightBlocks.PEARLIP_SHELL_WALL_SIGN.get()
            }).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ImmortalersDelightHangingSignBlockEntity>> HANGING_SIGN = TILES.register("hanging_sign", () ->
            BlockEntityType.Builder.of(ImmortalersDelightHangingSignBlockEntity::new,
                    new Block[]{
                            ImmortalersDelightBlocks.HIMEKAIDO_HANGING_SIGN.get(),
                            ImmortalersDelightBlocks.HIMEKAIDO_WALL_HANGING_SIGN.get(),
                            ImmortalersDelightBlocks.LEISAMBOO_HANGING_SIGN.get(),
                            ImmortalersDelightBlocks.LEISAMBOO_WALL_HANGING_SIGN.get(),
                            ImmortalersDelightBlocks.PEARLIP_SHELL_HANGING_SIGN.get(),
                            ImmortalersDelightBlocks.PEARLIP_SHELL_WALL_HANGING_SIGN.get()
            }).build(null));


}
