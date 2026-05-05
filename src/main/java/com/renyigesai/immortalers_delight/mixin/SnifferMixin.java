package com.renyigesai.immortalers_delight.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.renyigesai.immortalers_delight.api.event.SnifferDropSeedEvent;
import com.renyigesai.immortalers_delight.event.SnifferEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mixin(value = Sniffer.class, remap = false)
public abstract class SnifferMixin extends Animal {

    protected SnifferMixin(EntityType<? extends Animal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }

    @Invoker(value = "getHeadBlock", remap = false)
    protected abstract BlockPos immortalers_delight$invokeGetHeadBlock();

    @WrapOperation(
            method = "dropSeed",
            remap = false,
            require = 0,
            at = @At(
                    value = "INVOKE",
                    remap = false,
                    target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;JLjava/util/function/Consumer;)V"
            )
    )
    private void immortalers$wrapDropSeedSeeded(Operation<Void> original, LootTable lootTable, LootParams lootParams, long seed, Consumer<ItemStack> consumer) {
        List<ItemStack> buffer = new ArrayList<>();
        original.call(new Object[] { lootTable, lootParams, seed, (Consumer<ItemStack>) buffer::add });
        immortalers$applyDropSeedEvent(buffer, consumer);
    }

    @WrapOperation(
            method = "dropSeed",
            remap = false,
            require = 0,
            at = @At(
                    value = "INVOKE",
                    remap = false,
                    target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;Ljava/util/function/Consumer;)V"
            )
    )
    private void immortalers$wrapDropSeedTwoArg(Operation<Void> original, LootTable lootTable, LootParams lootParams, Consumer<ItemStack> consumer) {
        List<ItemStack> buffer = new ArrayList<>();
        original.call(new Object[] { lootTable, lootParams, (Consumer<ItemStack>) buffer::add });
        immortalers$applyDropSeedEvent(buffer, consumer);
    }

    private void immortalers$applyDropSeedEvent(List<ItemStack> buffer, Consumer<ItemStack> consumer) {
        Sniffer self = (Sniffer) (Object) this;
        SnifferDropSeedEvent event = new SnifferDropSeedEvent(self.level(), immortalers_delight$invokeGetHeadBlock(), new ArrayList<>(buffer));
        NeoForge.EVENT_BUS.post(event);
        for (ItemStack stack : event.getStacks()) {
            consumer.accept(stack);
        }
    }

    @Override
    public boolean canFallInLove() {
        CompoundTag tag = this.getPersistentData();
        if (!tag.contains(SnifferEvent.SNIFFER_TAIL_REGENERATION_COOLDOWN, Tag.TAG_INT)) {
            return super.canFallInLove();
        }
        if (tag.contains(SnifferEvent.SNIFFER_TAIL_REGENERATION_COOLDOWN, Tag.TAG_INT) && tag.getInt(SnifferEvent.SNIFFER_TAIL_REGENERATION_COOLDOWN) == 0) {
            return super.canFallInLove();
        }
        return false;
    }
}
