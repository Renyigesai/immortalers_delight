package com.renyigesai.immortalers_delight.fluid;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

//待优化
public class HotSpringFluidsBlock extends LiquidBlock {

    public HotSpringFluidsBlock() {
        super(ImmortalersDelightFluids.HOT_SPRING,
                Properties.of().mapColor(MapColor.WATER).strength(100f)
                        .noCollission().noLootTable().liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable());
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState pState) {
        return true;
    }
}
