package com.renyigesai.immortalers_delight.fluid;

import com.google.common.collect.ImmutableMap;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.tag.ModTags;

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
        double x = pPos.getX() + 0.5;
        double y = pPos.above().getY();
        double z = pPos.getZ() + 0.5;
        Random random = new Random();
        boolean isNether = pLevel.dimension() == Level.NETHER;
        if (isHeatSources(pLevel,pPos) || (isNether && pLevel.getGameTime() % 5 == 0)){
            pLevel.addParticle(ParticleTypes.POOF,x + random.nextDouble(-0.5,0.5),y,z + random.nextDouble(-0.5,0.5),0,0,0);
            pLevel.addParticle(ModParticleTypes.STEAM.get(),x + random.nextDouble(-0.5,0.5),y,z + random.nextDouble(-0.5,0.5),0,0,0);
        } else if (pLevel.getGameTime() % 20 == 0) pLevel.addParticle(ParticleTypes.POOF,x + random.nextDouble(-0.5,0.5),y,z + random.nextDouble(-0.5,0.5),0,0,0);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        craftTick(pLevel,pPos);
    }

    //温泉热源逻辑，在下界以外同厨锅，补充了温泉在下界沸腾的设定，在下界则是单独的温泉方块即可(避免出现大片温泉高频查配方导致卡顿)
    public boolean isHeatSources(Level level, BlockPos pos){
        BlockState stateBelow = level.getBlockState(pos.below());
        if (stateBelow.is(ModTags.HEAT_SOURCES)) {
            return stateBelow.hasProperty(BlockStateProperties.LIT) ? (Boolean)stateBelow.getValue(BlockStateProperties.LIT) : true;
        } else if (stateBelow.is(ModTags.HEAT_CONDUCTORS)) {
            BlockState stateFurtherBelow = level.getBlockState(pos.below(2));
            if (stateFurtherBelow.is(ModTags.HEAT_SOURCES)) {
                if (stateFurtherBelow.hasProperty(BlockStateProperties.LIT)) {
                    return (Boolean)stateFurtherBelow.getValue(BlockStateProperties.LIT);
                }
                return true;
            }
        }
        if (stateBelow.isSolid() && level.dimension() == Level.NETHER) {
            Map<Integer,BlockPos> posMap = (new ImmutableMap.Builder<Integer,BlockPos>())
                    .put(0,pos.north())
                    .put(1,pos.south())
                    .put(2,pos.west())
                    .put(3,pos.east())
                    .build();
            for (int i = 0;i < 4 && posMap.get(i) != null; i++) {
                BlockState stateAround = level.getBlockState(Objects.requireNonNull(posMap.get(i)));
                if (stateAround.getFluidState().is(ImmortalersDelightFluids.HOT_SPRING.get())) return false;
            }
            return true;
        }
        return false;
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
            for (int i = 0; i < 4; i++) {
                Vec3 vec3 = new Vec3((double)((level.random.nextFloat() * 2.0F - 1.0F) * 0.4F), level.random.nextInt(4) * 0.25D - 0.25D, (double)((level.random.nextFloat() * 2.0F - 1.0F) * 0.4F));
                for (int j = 1; j <= 5; j++) {
                    double x = pos.getX() + vec3.x * j / 5;
                    double y = pos.getY() + vec3.y * j / 5;
                    double z = pos.getZ() + vec3.z * j / 5;
                    serverLevel.sendParticles(ParticleTypes.SOUL,x,y,z,8,0.0,0.0,0.0,0);
                }
            }
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
