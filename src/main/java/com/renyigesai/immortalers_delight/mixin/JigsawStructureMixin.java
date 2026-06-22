package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.world.structure.StructureGroundChecks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(JigsawStructure.class)
public abstract class JigsawStructureMixin {
    @Shadow(remap = false)
    @Final
    private Holder<StructureTemplatePool> startPool;

    @Inject(method = "findGenerationPoint", at = @At("RETURN"), cancellable = true, remap = false)
    private void immortalers_delight$validateSolidGround(
            Structure.GenerationContext context,
            CallbackInfoReturnable<Optional<Structure.GenerationStub>> cir
    ) {
        Optional<ResourceLocation> poolId = this.startPool.unwrapKey().map(key -> key.location());
        if (poolId.isEmpty()) {
            return;
        }

        StructureGroundChecks.GroundCheck check = StructureGroundChecks.groundCheckForPool(poolId.get());
        if (check == null) {
            return;
        }

        Optional<Structure.GenerationStub> stub = cir.getReturnValue();
        if (stub.isEmpty()) {
            return;
        }

        BlockPos pos = stub.get().position();
        if (!StructureGroundChecks.hasSolidGround(context, pos, check)) {
            cir.setReturnValue(Optional.empty());
        }
    }
}
