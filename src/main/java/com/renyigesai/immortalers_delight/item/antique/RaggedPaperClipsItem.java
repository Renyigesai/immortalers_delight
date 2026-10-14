package com.renyigesai.immortalers_delight.item.antique;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public class RaggedPaperClipsItem extends AbstractAntiqueItem{
    public RaggedPaperClipsItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
//        builder.put(Attributes.JUMP_STRENGTH,new AttributeModifier(UUID.fromString("bae24391-f451-4b7c-ac71-9c362f581499"),"Tool modifier",0.2,AttributeModifier.Operation.ADDITION));
        builder.put(ForgeMod.STEP_HEIGHT_ADDITION.get(),new AttributeModifier(UUID.fromString("7ba2fe98-395e-4c61-95d1-8fcf7e45a9a9"),"Tool modifier",0.4,AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public void onPlace(Player player, Level level) {

    }

    @Override
    public void onTake(Player player, Level level) {

    }

    @Override
    public void onTick(Player player, Level level) {

    }
}
