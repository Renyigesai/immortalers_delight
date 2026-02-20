package com.renyigesai.immortalers_delight.block.warped_lantern;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.block.sextlotus_lantern.SextlotusLightHelper;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class WarpedLanternBlockEntity extends BlockEntity {

    protected boolean useExtraTick = true;
    private static List<BlockPos> allThisblock = new ArrayList<>();
    private static Map<BlockPos, String> blocksNeedTick = new HashMap<>();
    public Map<BlockPos, String> getBlocksNeedTick() {return blocksNeedTick;}
    public WarpedLanternBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ImmortalersDelightBlocks.WARPED_LANTERN_ENTITY.get(), pPos, pBlockState);
    }
    public WarpedLanternBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    //=======================基础功能部分，实现方块实体的通用逻辑并管理数据==========================//

    public void load(@NotNull CompoundTag pTag) {
        this.addToPoses();
        super.load(pTag);
    }

    public void setRemoved() {
        this.removeFromPoses();
        super.setRemoved();
    }
    public void clearRemoved() {
        this.addToPoses();
        super.clearRemoved();
    }
    @Override
    public void onChunkUnloaded() {
        this.removeFromPoses();
        super.onChunkUnloaded();
    }
    public void addToPoses() {
        allThisblock.add(this.getBlockPos());
    }
    public void removeFromPoses() {
        allThisblock.remove(this.getBlockPos());
    }

    //=======================需求实现部分，实现当前方块实体的功能=========================//


    @Mod.EventBusSubscriber(
            modid = ImmortalersDelightMod.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class WarpedLanternBlockEvents {
        @SubscribeEvent
        public static void onBlockGrow(BlockEvent.CropGrowEvent.Pre event) {
            //检测是否在正确的世界
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                //每次随机刻有25%概率额外进行随机刻，额外的随机刻也会触发25%概率，因此实际概率约为30%
                if (serverLevel.random.nextInt(4) == 0) {
                    List<BlockPos> allPos = new ArrayList<>(allThisblock);
                    if (allPos.size() > 0) for (BlockPos thisPos : allPos) {
                        //通过判断当前发布事件的方块与本方块的X，Y，Z三个轴的偏移量均不超过2，检测是否在5*5*5范围内
                        if (Math.abs(event.getPos().getX() - thisPos.getX()) <= 2
                                && Math.abs(event.getPos().getY() - thisPos.getY()) <= 2
                                && Math.abs(event.getPos().getZ() - thisPos.getZ()) <= 2) {
                            //进行最终校验，确保方块实体存在且是对应类型的实体
                            if (serverLevel.getBlockEntity(thisPos) != null && serverLevel.getBlockEntity(thisPos) instanceof WarpedLanternBlockEntity) {
                                spawnParticle(serverLevel, thisPos, 0);
                                //存Map
                                blocksNeedTick.put(event.getPos(), serverLevel.getLevel().dimension().toString());
                            } else {
                                allThisblock.remove(thisPos);
                            }
                        }
                    }
                }
            }
        }
        @SubscribeEvent
        public static void doExtraTick(@Nonnull TickEvent.LevelTickEvent event) {

            if (event.level instanceof ServerLevel serverLevel && event.phase.equals(TickEvent.Phase.START)) {
                if (blocksNeedTick.size() > 0) {
                    //复制一个列表，避免影响原列表
                    Map<BlockPos, String> tickingBlocks = new HashMap<>(blocksNeedTick);
                    //遍历所有需要处理的方块
                    for (BlockPos pos : tickingBlocks.keySet()) {
                        //仅在相同的维度时处理
                        if (event.level.dimension().toString().equals(tickingBlocks.get(pos))) {
                            //仅当区块加载时才处理，避免强制加载区块
                            BlockState state = SextlotusLightHelper.getBlockStateIfLoaded(serverLevel, pos);
                            if (!state.isAir() && state.getBlock().isRandomlyTicking(state)) {
                                blocksNeedTick.remove(pos);
                                spawnParticle(serverLevel, pos, 1);
                                state.randomTick(serverLevel, pos, serverLevel.getRandom());
                            }
                        }
                    }
                }
            }
        }

        private static void spawnParticle(Level level, BlockPos pPos, int type) {
            if (level instanceof ServerLevel serverLevel) {
                Vec3 center = new Vec3(pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5);
                double radius = 1.1;
                for (int i = 0; i < 11; i++) {
                    double angle = 2 * Math.PI * Math.random();
                    double r = radius * Math.sqrt(Math.random());
                    double x = center.x + r * Math.cos(angle);
                    double z = center.z + r * Math.sin(angle);
                    double y = center.y;
                    if (type == 0) {
                        serverLevel.sendParticles(
                                ParticleTypes.SCULK_SOUL, x, y, z, 1, 0, 0, 0, 0.025
                        );
                    } else serverLevel.sendParticles(
                            ParticleTypes.EGG_CRACK, x, y, z, 1, 0, 0, 0, 0.025
                    );
                }
            }
        }
    }

}
