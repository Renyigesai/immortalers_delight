package com.renyigesai.immortalers_delight.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

/**
 * Global loot modifier: after {@code minecraft:gameplay/sniffer_digging} resolves, apply biome-specific replacement via
 * {@link com.renyigesai.immortalers_delight.api.event.SnifferDropSeedEvent}.
 * <p>
 * When More Sniffer Flowers is present, its modifier may clear this result; see
 * {@link com.renyigesai.immortalers_delight.mixin.MoreSnifferFlowersAddItemsModifierMixin}.
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
        return SnifferBiomeSeedHandler.applyFromLootContext(generatedLoot, lootContext);
    }
}
