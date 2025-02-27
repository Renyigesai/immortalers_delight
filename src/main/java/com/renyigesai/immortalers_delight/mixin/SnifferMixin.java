package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Sniffer.class)
public abstract class SnifferMixin extends Animal {


    @Shadow @Final private static EntityDataAccessor<Integer> DATA_DROP_SEED_AT_TICK;

    @Shadow protected abstract BlockPos getHeadBlock();

    protected SnifferMixin(EntityType<? extends Animal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }
    @Inject(method = "dropSeed",at = @At("HEAD"))
    private void dropSeed(CallbackInfo ci){
        if (!this.level().isClientSide() && this.entityData.get(DATA_DROP_SEED_AT_TICK) == this.tickCount){
            BlockPos blockpos = this.getHeadBlock();
            Holder<Biome> biomeHolder = this.level().getBiome(blockpos);
            ServerLevel serverlevel = (ServerLevel)this.level();
            if (biomeHolder.is(BiomeTags.IS_JUNGLE)){
                serverlevel.addFreshEntity(getSeedEntity(serverlevel,blockpos,0));
            } else if (biomeHolder.is(Tags.Biomes.IS_PLAINS)) {
                serverlevel.addFreshEntity(getSeedEntity(serverlevel,blockpos,1));
            } else if (biomeHolder.is(BiomeTags.IS_FOREST)) {
                serverlevel.addFreshEntity(getSeedEntity(serverlevel,blockpos,2));
            } else if (biomeHolder.is(BiomeTags.IS_RIVER)) {
                serverlevel.addFreshEntity(getSeedEntity(serverlevel,blockpos,3));
            }
        }
    }

    @Unique
    private ItemEntity getSeedEntity(ServerLevel serverlevel, BlockPos blockpos, int i){
        List<ItemStack> itemStacks = new ArrayList<>();
        ItemStack stack1 = new ItemStack(ImmortalersDelightItems.PEARLIPEARL.get());
        ItemStack stack2 = new ItemStack(ImmortalersDelightItems.EVOLUTCORN_GRAINS.get());
        ItemStack stack3 = new ItemStack(ImmortalersDelightItems.HIMEKAIDO_SEED.get());
        ItemStack stack4 = new ItemStack(ImmortalersDelightItems.CONTAINS_TEA_LEISAMBOO.get());
        itemStacks.add(stack1);
        itemStacks.add(stack2);
        itemStacks.add(stack3);
        itemStacks.add(stack4);
        return new ItemEntity(serverlevel, blockpos.getX(), blockpos.getY(), blockpos.getZ(),
                new ItemStack(itemStacks.get(i).getItem()));

    }
}
