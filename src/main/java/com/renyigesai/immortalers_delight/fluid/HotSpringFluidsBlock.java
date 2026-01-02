package com.renyigesai.immortalers_delight.fluid;

import com.renyigesai.immortalers_delight.recipe.HotSpringRecipe;
import com.renyigesai.immortalers_delight.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;

import java.util.*;

//待优化
public class HotSpringFluidsBlock extends LiquidBlock {

    public HotSpringFluidsBlock() {
        super(ImmortalersDelightFluids.HOT_SPRING,
                Properties.of().mapColor(MapColor.WATER).strength(100f)
                        .noCollission().noLootTable().liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable().randomTicks());
    }


    private Optional<HotSpringRecipe> getCurrentRecipe(Level level, List<ItemStack> list) {
        SimpleContainer inventory = new SimpleContainer(6);
        List<ItemStack> inputs = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = list.get(i);
            if (!stack.isEmpty()) {
                inputs.add(stack);
            }
        }

        for (int i = 0; i < inputs.size(); i++) {
            inventory.setItem(i, inputs.get(i));
        }

        return level.getRecipeManager()
                .getRecipeFor(HotSpringRecipe.Type.INSTANCE, inventory, level);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);
        if (isHeatSources(pLevel,pPos)){
            double x = pPos.getX() + 0.5;
            double y = pPos.above().getY();
            double z = pPos.getZ() + 0.5;
            Random random = new Random();
            pLevel.addParticle(ParticleTypes.POOF,x + random.nextDouble(-0.5,0.5),y,z + random.nextDouble(-0.5,0.5),0,0,0);
            pLevel.addParticle(ModParticleTypes.STEAM.get(),x + random.nextDouble(-0.5,0.5),y,z + random.nextDouble(-0.5,0.5),0,0,0);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        craftTick(pLevel,pPos);
    }

    public boolean isHeatSources(Level level, BlockPos pos){
        BlockState state = level.getBlockState(pos.below());
        return state.is(BlockTags.create(new ResourceLocation("farmersdelight:heat_sources"))) && state.getBlock().getStateDefinition().getProperty("lit") instanceof BooleanProperty booleanProperty && state.getValue(booleanProperty);
    }

    private void craftTick(Level level, BlockPos pos){
        List<ItemEntity> itemEntityList = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos,pos).inflate(1,1,1));
        if (itemEntityList.isEmpty())
            return;
        List<ItemStack> stackList = new ArrayList<>();
        for (int i = 0; i < itemEntityList.size(); i++) {
            ItemStack item = itemEntityList.get(i).getItem();
            if (!item.isEmpty() && item.getCount() == 1) {
                stackList.add(i,item);
            }
        }
        if (stackList.isEmpty())
            return;
        Optional<HotSpringRecipe> recipeOptional = getCurrentRecipe(level,stackList);
        if (recipeOptional.isEmpty()){
            return;
        }
        HotSpringRecipe recipe = recipeOptional.get();
        ItemStack resultItem = recipe.getResultItem(level.registryAccess()).copy();
        for (ItemEntity itemEntity : itemEntityList) {
            itemEntity.remove(Entity.RemovalReason.DISCARDED);
        }
        ItemUtils.spawnItemEntity(level,resultItem,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,0,0,0);
        if (level instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(ParticleTypes.POOF,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,8,0.0,0.0,0.0,0);
            level.playSound(null,pos,SoundEvents.FIRE_EXTINGUISH,SoundSource.BLOCKS);
        }
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        super.entityInside(pState, pLevel, pPos, pEntity);
        if (!isHeatSources(pLevel,pPos))
            return;
        if (pEntity instanceof ItemEntity){
            pLevel.scheduleTick(pPos,this,5);
        }
    }
}
