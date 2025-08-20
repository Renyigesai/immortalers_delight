package com.renyigesai.immortalers_delight.fluid;

import com.renyigesai.immortalers_delight.recipe.EnchantalCoolerRecipe;
import com.renyigesai.immortalers_delight.recipe.HotSpringRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

//待优化
public class HotSpringFluidsBlock extends LiquidBlock {

    public HotSpringFluidsBlock() {
        super(ImmortalersDelightFluids.HOT_SPRING,
                Properties.of().mapColor(MapColor.WATER).strength(100f)
                        .noCollission().noLootTable().liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable().randomTicks());
    }


    private Optional<HotSpringRecipe> getCurrentRecipe(Level level, List<ItemStack> list) {
        SimpleContainer inventory = new SimpleContainer(4);
        List<ItemStack> inputs = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
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

//    @Override
//    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
//        super.randomTick(pState, pLevel, pPos, pRandom);
//        craftTick(pLevel,pPos);
//    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState pState) {
        return true;
    }

    private void craftTick(Level level,BlockPos pos){
        Vec3 vec3 = new Vec3(pos.getX(),pos.getY(),pos.getZ());
//        List<ItemEntity> itemEntityList = level.getEntitiesOfClass(ItemEntity.class, new AABB(vec3, vec3).inflate(1 / 2d), e
//                -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(vec3))).toList();
//        System.out.println(itemEntityList);
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(vec3, vec3).inflate(1 / 2d), e
                -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(vec3))).toList();
        System.out.println(entities);
//        if (itemEntityList.isEmpty())
//            return;
//        List<ItemStack> stackList = new ArrayList<>();
//        for (ItemEntity item : itemEntityList) {
//            if (!item.getItem().isEmpty()) {
//                stackList.add(item.getItem());
//            }
//        }
//        if (stackList.isEmpty())
//            return;
//        Optional<HotSpringRecipe> recipeOptional = getCurrentRecipe(level,stackList);
//        if (recipeOptional.isEmpty()){
//            return;
//        }
//        System.out.println("Yes");

    }
}
