package com.renyigesai.immortalers_delight.item.antique;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractAntiqueItem extends Item implements IAntiqueItem {
    public boolean customTooltip = false;
    public static final ImmutableMultimap.Builder<Attribute, AttributeModifier> EMPTY = ImmutableMultimap.builder();
    private static final DecimalFormat ATTRIBUTE_MODIFIER_FORMAT = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ROOT));

    public AbstractAntiqueItem(Properties pProperties) {
        super(pProperties);
    }

    public AbstractAntiqueItem(Properties pProperties,boolean customTooltip) {
        super(pProperties);
        this.customTooltip = customTooltip;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pSlot) {
        return EMPTY.build();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (this.customTooltip) {
            pTooltipComponents.add(Component.translatable(ImmortalersDelightMod.MODID + ".tooltip." + this));
        }

        Multimap<Attribute, AttributeModifier> modifiers = this.getDefaultAttributeModifiers();
        if (!modifiers.isEmpty()) {
            pTooltipComponents.add(CommonComponents.EMPTY);
            pTooltipComponents.add(Component.translatable("tooltip.immortalers_delight.antique").withStyle(ChatFormatting.GRAY));

            for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()) {
                Attribute attribute = entry.getKey();
                AttributeModifier modifier = entry.getValue();
                double amount = modifier.getAmount();
                double display;

                if (modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE
                        || modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_TOTAL) {
                    display = amount * 100.0;
                } else if (attribute.equals(Attributes.KNOCKBACK_RESISTANCE)) {
                    display = amount * 10.0;
                } else {
                    display = amount;
                }

                if (amount > 0.0) {
                    pTooltipComponents.add(Component.translatable(
                            "attribute.modifier.plus." + modifier.getOperation().toValue(),
                            ATTRIBUTE_MODIFIER_FORMAT.format(display),
                            Component.translatable(attribute.getDescriptionId())
                    ).withStyle(ChatFormatting.BLUE));
                } else if (amount < 0.0) {
                    display *= -1.0;
                    pTooltipComponents.add(Component.translatable(
                            "attribute.modifier.take." + modifier.getOperation().toValue(),
                            ATTRIBUTE_MODIFIER_FORMAT.format(display),
                            Component.translatable(attribute.getDescriptionId())
                    ).withStyle(ChatFormatting.RED));
                }
            }
        }

    }
}
