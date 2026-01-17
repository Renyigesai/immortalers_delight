package com.renyigesai.immortalers_delight.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BlockEntity.class)
public abstract class BrushableBlockEntityMixin extends net.minecraftforge.common.capabilities.CapabilityProvider<BlockEntity> implements net.minecraftforge.common.extensions.IForgeBlockEntity {
    protected BrushableBlockEntityMixin(Class<BlockEntity> baseClass) {
        super(baseClass);
    }

    protected BrushableBlockEntityMixin(Class<BlockEntity> baseClass, boolean isLazy) {
        super(baseClass, isLazy);
    }




    //    @ModifyArg(
//            method = "<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BlockEntity;<init>(Lnet/minecraft/world/level/block/entity/BlockEntityType;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"), index = 0
//    )
//    private BlockEntityType<?> modifyBlockEntityType(BlockEntityType<?> originalType, BlockPos pos, BlockState state) {
//        return getCustomType(pos, state);
//    }
}
