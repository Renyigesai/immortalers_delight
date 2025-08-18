
package com.renyigesai.immortalers_delight.fluid;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;

public abstract class HotSpringFluid extends ForgeFlowingFluid {
    public static final Properties PROPERTIES = new Properties(ImmortalersDelightFluidTypes.HOT_SPRING_TYPE, ImmortalersDelightFluids.HOT_SPRING,
            ImmortalersDelightFluids.FLOWING_HOT_SPRING).explosionResistance(100f).bucket(ImmortalersDelightItems.HOT_SPRING_BUCKET)
            .block(ImmortalersDelightBlocks.HOT_SPRING_BLOCK);

    private HotSpringFluid() {
        super(PROPERTIES);
    }

    public static class Source extends HotSpringFluid {
        public int getAmount(@NotNull FluidState state) {
            return 8;
        }

        public boolean isSource(@NotNull FluidState state) {
            return true;
        }
    }

    public static class Flowing extends HotSpringFluid {
        protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(@NotNull FluidState state) {
            return false;
        }
    }

}
