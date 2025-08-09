package com.renyigesai.immortalers_delight.mixin;

import com.renyigesai.immortalers_delight.api.event.SnifferDropSeedEvent;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import jdk.jfr.Label;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(Sniffer.class)
public abstract class SnifferMixin extends Animal {


    @Shadow @Final private static EntityDataAccessor<Integer> DATA_DROP_SEED_AT_TICK;

    @Shadow protected abstract BlockPos getHeadBlock();

    protected SnifferMixin(EntityType<? extends Animal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }

    @Inject(method = "dropSeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/sniffer/Sniffer;getHeadBlock()Lnet/minecraft/core/BlockPos;", shift = At.Shift.AFTER),locals = LocalCapture.CAPTURE_FAILHARD)
    private void dropSeed(CallbackInfo ci, ServerLevel serverlevel, LootTable loottable, LootParams lootparams, List<ItemStack> list){
        Level level = this.level();
        BlockPos headBlock = getHeadBlock();
        SnifferDropSeedEvent snifferDropSeedEvent = new SnifferDropSeedEvent(level,headBlock,new ArrayList<>(list));
        MinecraftForge.EVENT_BUS.post(snifferDropSeedEvent);
        list.clear();
        list.addAll(snifferDropSeedEvent.getStacks());
    }
}
