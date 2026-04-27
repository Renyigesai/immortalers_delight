package com.renyigesai.immortalers_delight.fluid;

import com.google.common.collect.ImmutableMap;
import com.renyigesai.immortalers_delight.recipe.HotSpringRecipe;
import com.renyigesai.immortalers_delight.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.*;

public class HotSpringFluidsBlock extends LiquidBlock {
    private static final class RecipeMatchResult {
        private final HotSpringRecipe recipe;
        private final Map<ItemEntity, Integer> consumeCounts;

        private RecipeMatchResult(HotSpringRecipe recipe, Map<ItemEntity, Integer> consumeCounts) {
            this.recipe = recipe;
            this.consumeCounts = consumeCounts;
        }
    }

    public HotSpringFluidsBlock() {
        super(ImmortalersDelightFluids.HOT_SPRING,
                Properties.of().mapColor(MapColor.WATER).strength(100f)
                        .noCollission().noLootTable().liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable().randomTicks());
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);
        // 优化性能，如果顶上是完整方块，则不生成粒子。
        if (isFaceFull(pLevel.getBlockState(pPos.above()).getCollisionShape(pLevel, pPos.above()), Direction.DOWN)) {
            return;
        }
        double x = pPos.getX() + 0.5;
        double y = pPos.above().getY();
        double z = pPos.getZ() + 0.5;
        boolean isNether = pLevel.dimension() == Level.NETHER;
        if ((isNether && pLevel.getGameTime() % 10 == 0) || isHeatSources(pLevel, pPos)) {
            pLevel.addParticle(ParticleTypes.POOF, x + pRandom.nextDouble(-0.5D, 0.5D), y, z + pRandom.nextDouble(-0.5D, 0.5D), 0, 0, 0);
            pLevel.addParticle(ModParticleTypes.STEAM.get(), x + pRandom.nextDouble(-0.5D, 0.5D), y, z + pRandom.nextDouble(-0.5D, 0.5D), 0, 0, 0);
        } else if (pLevel.getGameTime() % 20 == 0) {
            pLevel.addParticle(ParticleTypes.POOF, x + pRandom.nextDouble(-0.5D, 0.5D), y, z + pRandom.nextDouble(-0.5D, 0.5D), 0, 0, 0);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        craftTick(pLevel,pPos);
    }

    //温泉热源逻辑，在下界以外同厨锅，补充了温泉在下界沸腾的设定，在下界则是单独的温泉方块即可(避免出现大片温泉高频查配方导致卡顿)
    public boolean isHeatSources(Level level, BlockPos pos){
        BlockState stateBelow = level.getBlockState(pos.below());
        if (stateBelow.is(ModTags.HEAT_SOURCES)) {
            return stateBelow.hasProperty(BlockStateProperties.LIT) ? stateBelow.getValue(BlockStateProperties.LIT) : !stateBelow.getFluidState().is(ImmortalersDelightFluids.HOT_SPRING.get());
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

    private List<ItemEntity> getCandidateItemEntities(List<ItemEntity> itemEntityList) {
        List<ItemEntity> craftableItems = new ArrayList<>();
        for (ItemEntity entity : itemEntityList) {
            ItemStack itemStack = entity.getItem();
            if (!itemStack.isEmpty()) {
                craftableItems.add(entity);
            }
        }
        return craftableItems;
    }

    private Optional<RecipeMatchResult> findMatchingRecipe(Level level, List<ItemEntity> itemEntityList) {
        List<HotSpringRecipe> recipes = level.getRecipeManager().getAllRecipesFor(HotSpringRecipe.Type.INSTANCE);
        for (HotSpringRecipe recipe : recipes) {
            Map<ItemEntity, Integer> consumeCounts = matchRecipe(recipe, itemEntityList);
            if (!consumeCounts.isEmpty()) {
                return Optional.of(new RecipeMatchResult(recipe, consumeCounts));
            }
        }
        return Optional.empty();
    }

    private Map<ItemEntity, Integer> matchRecipe(HotSpringRecipe recipe, List<ItemEntity> itemEntityList) {
        List<Ingredient> ingredients = new ArrayList<>(recipe.getIngredients());
        if (ingredients.isEmpty()) {
            return Collections.emptyMap();
        }

        int[] remainingCounts = new int[itemEntityList.size()];
        for (int i = 0; i < itemEntityList.size(); i++) {
            remainingCounts[i] = itemEntityList.get(i).getItem().getCount();
        }

        LinkedHashMap<ItemEntity, Integer> consumeCounts = new LinkedHashMap<>();
        boolean matched = matchIngredients(ingredients, itemEntityList, remainingCounts, 0, consumeCounts);
        return matched ? consumeCounts : Collections.emptyMap();
    }

    private boolean matchIngredients(List<Ingredient> ingredients, List<ItemEntity> itemEntityList, int[] remainingCounts,
                                     int ingredientIndex, Map<ItemEntity, Integer> consumeCounts) {
        if (ingredientIndex >= ingredients.size()) {
            return true;
        }

        Ingredient ingredient = ingredients.get(ingredientIndex);
        for (int i = 0; i < itemEntityList.size(); i++) {
            ItemEntity itemEntity = itemEntityList.get(i);
            if (remainingCounts[i] <= 0 || !ingredient.test(itemEntity.getItem())) {
                continue;
            }

            remainingCounts[i]--;
            consumeCounts.merge(itemEntity, 1, Integer::sum);
            if (matchIngredients(ingredients, itemEntityList, remainingCounts, ingredientIndex + 1, consumeCounts)) {
                return true;
            }

            remainingCounts[i]++;
            int currentCount = consumeCounts.getOrDefault(itemEntity, 0) - 1;
            if (currentCount <= 0) {
                consumeCounts.remove(itemEntity);
            } else {
                consumeCounts.put(itemEntity, currentCount);
            }
        }

        return false;
    }

    private void consumeMatchedItems(Map<ItemEntity, Integer> consumeCounts) {
        for (Map.Entry<ItemEntity, Integer> entry : consumeCounts.entrySet()) {
            ItemEntity itemEntity = entry.getKey();
            int consumeCount = entry.getValue();
            ItemStack itemStack = itemEntity.getItem();
            itemStack.shrink(consumeCount);
            if (itemStack.isEmpty()) {
                itemEntity.remove(Entity.RemovalReason.DISCARDED);
            } else {
                itemEntity.setItem(itemStack);
            }
        }
    }

    private void craftTick(Level level, BlockPos pos) {
        List<ItemEntity> itemEntityList = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos,pos).inflate(1,1,1));
        if (itemEntityList.isEmpty()) {
            return;
        }

        List<ItemEntity> candidateItems = getCandidateItemEntities(itemEntityList);
        if (candidateItems.isEmpty()) {
            return;
        }

        Optional<RecipeMatchResult> matchResultOptional = findMatchingRecipe(level, candidateItems);
        if (matchResultOptional.isEmpty()) {
            return;
        }

        RecipeMatchResult matchResult = matchResultOptional.get();
        HotSpringRecipe recipe = matchResult.recipe;
        ItemStack resultItem = recipe.getResultItem(level.registryAccess()).copy();
        consumeMatchedItems(matchResult.consumeCounts);
        ItemUtils.spawnItemEntity(level, resultItem, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0, 0, 0);
        if (level instanceof ServerLevel serverLevel) {
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
        //平静的温泉会对生物施加恢复效果(自然对亡灵生物无效)
        if (!isHeatSources(pLevel,pPos)) {
            if (pEntity instanceof LivingEntity living){
                if (living.tickCount % 25 == 0) {
                    living.addEffect(new MobEffectInstance(MobEffects.REGENERATION,50,0,false,false));
                }
            }
            return;
        }
        if (pEntity instanceof LivingEntity living) {
            //沸腾状态下的温泉如灵魂火造成伤害，但对亡灵生物造成治疗效果
            if (!living.getMobType().equals(MobType.UNDEAD) && !living.fireImmune()) {
//                living.setRemainingFireTicks(pEntity.getRemainingFireTicks() + 1);
//                if (living.getRemainingFireTicks() == 0) {pEntity.setSecondsOnFire(8);}
                living.hurt(pLevel.damageSources().inFire(), 2.0f);
            }
            if (living.getMobType().equals(MobType.UNDEAD) && living.tickCount % 25 == 0) living.heal(1.0f);
        } else if (pEntity instanceof ItemEntity){
            pLevel.scheduleTick(pPos,this,5);
        }
    }
}
