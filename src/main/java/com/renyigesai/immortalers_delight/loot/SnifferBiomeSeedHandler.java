package com.renyigesai.immortalers_delight.loot;

import com.renyigesai.immortalers_delight.api.event.SnifferDropSeedEvent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared entry for biome-specific sniffer dig seeds (GLM + More Sniffer Flowers compat).
 */
public final class SnifferBiomeSeedHandler {
    private SnifferBiomeSeedHandler() {
    }

    public static ObjectArrayList<ItemStack> applyFromLootContext(
            ObjectArrayList<ItemStack> generatedLoot,
            LootContext lootContext
    ) {
        BlockPos pos = resolveDigPos(lootContext);
        if (pos == null) {
            return generatedLoot;
        }
        Sniffer sniffer = null;
        Entity entity = lootContext.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity instanceof Sniffer s) {
            sniffer = s;
        }
        List<ItemStack> applied = apply(lootContext.getLevel(), pos, sniffer, generatedLoot);
        if (applied == generatedLoot) {
            return generatedLoot;
        }
        ObjectArrayList<ItemStack> out = new ObjectArrayList<>(applied.size());
        out.addAll(applied);
        return out;
    }

    public static List<ItemStack> apply(
            net.minecraft.world.level.Level level,
            BlockPos pos,
            @Nullable Sniffer sniffer,
            List<ItemStack> current
    ) {
        SnifferDropSeedEvent event = new SnifferDropSeedEvent(
                level,
                pos,
                new ArrayList<>(current),
                sniffer
        );
        NeoForge.EVENT_BUS.post(event);
        return event.getStacks();
    }

    @Nullable
    public static BlockPos resolveDigPos(LootContext context) {
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
