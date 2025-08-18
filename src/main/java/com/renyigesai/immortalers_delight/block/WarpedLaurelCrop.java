package com.renyigesai.immortalers_delight.block;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;

public class WarpedLaurelCrop extends ReapCropBlock{
    public WarpedLaurelCrop(Properties p_52247_) {
        super(p_52247_);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ImmortalersDelightItems.WARPED_LAUREL_SEEDS.get();
    }

    @Override
    public int getMaxAge() {
        return 6;
    }
}
