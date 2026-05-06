package com.renyigesai.immortalers_delight.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.renyigesai.immortalers_delight.api.event.SnifferDropSeedEvent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.ArrayList;

/**
 * Global loot modifier: after {@code minecraft:gameplay/sniffer_digging} resolves, apply biome-specific replacement via
 * {@link SnifferDropSeedEvent}. {@link LootContextParams#THIS_ENTITY} is set by vanilla Sniffer digging when present.
 */
public class SnifferBiomeDigLootModifier extends LootModifier {

    public static final MapCodec<SnifferBiomeDigLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            LootModifier.codecStart(inst).apply(inst, SnifferBiomeDigLootModifier::new));

    public SnifferBiomeDigLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext) {
        BlockPos pos = resolveDigPos(lootContext);
        if (pos == null) {
            return generatedLoot;
        }
        Sniffer sniffer = null;
        Entity entity = lootContext.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity instanceof Sniffer s) {
            sniffer = s;
        }
        SnifferDropSeedEvent event = new SnifferDropSeedEvent(
                lootContext.getLevel(),
                pos,
                new ArrayList<>(generatedLoot),
                sniffer
        );
        NeoForge.EVENT_BUS.post(event);
        generatedLoot.clear();
        generatedLoot.addAll(event.getStacks());
        return generatedLoot;
    }

    private static BlockPos resolveDigPos(LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin != null) {
            return BlockPos.containing(origin);
        }
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity != null) {
            return BlockPos.containing(entity.position());
        }
        return null;
    }
}
