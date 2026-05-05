package com.renyigesai.immortalers_delight.block.ancient_stove;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;

public class AncientStoveBlockEntity extends AbstractStoveBlockEntity {


    public AncientStoveBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ImmortalersDelightBlocks.ANCIENT_STOVE_ENTITY.get(), blockPos, blockState, RecipeType.CAMPFIRE_COOKING);
    }

    public static void particleTick(Level level, BlockPos pos, BlockState state, AncientStoveBlockEntity stoveEntity) {
        if (!stoveEntity.isEmpty()) {
            stoveEntity.addSmokeParticles();
        }
    }

    public boolean shouldDropItems() {
        return this.level != null && AncientStoveBlock.isStoveTopCovered(this.level, this.worldPosition, this.getBlockState());
    }

    public void addSmokeParticles() {
        assert this.level != null;

        ItemStackHandler items = this.getItems();

        for(int i = 0; i < items.getSlots(); ++i) {
            if (!items.getStackInSlot(i).isEmpty() && !(this.level.random.nextFloat() >= 0.2F)) {
                Vec2 itemOffset = this.getStoveItemOffset(i);
                Direction direction = (Direction)this.getBlockState().getValue(AbstractStoveBlock.FACING);
                if (direction.get2DDataValue() % 2 != 0) {
                    itemOffset = new Vec2(itemOffset.y, itemOffset.x);
                }

                double x = (double)this.worldPosition.getX() + 0.5 - (double)((float)direction.getStepX() * itemOffset.x) + (double)((float)direction.getClockWise().getStepX() * itemOffset.x);
                double y = (double)this.worldPosition.getY() + 1.0;
                double z = (double)this.worldPosition.getZ() + 0.5 - (double)((float)direction.getStepZ() * itemOffset.y) + (double)((float)direction.getClockWise().getStepZ() * itemOffset.y);

                for(int k = 0; k < 3; ++k) {
                    this.level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
                }
            }
        }

    }

    protected int getInventorySlotCount() {
        return 6;
    }

    public Vec2 getStoveItemOffset(int index) {
        float X_OFFSET = 0.3F;
        float Y_OFFSET = 0.2F;
        Vec2[] OFFSETS = new Vec2[]{new Vec2(0.3F, 0.2F), new Vec2(0.0F, 0.2F), new Vec2(-0.3F, 0.2F), new Vec2(0.3F, -0.2F), new Vec2(0.0F, -0.2F), new Vec2(-0.3F, -0.2F)};
        return OFFSETS[index];
    }
}
