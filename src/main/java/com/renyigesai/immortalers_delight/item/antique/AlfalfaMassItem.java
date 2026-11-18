package com.renyigesai.immortalers_delight.item.antique;

import com.google.common.collect.Multimap;
import com.renyigesai.immortalers_delight.api.IPlayerLastAntiqueSlot;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class AlfalfaMassItem extends AbstractAntiqueItem {
    public AlfalfaMassItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers() {
        return EMPTY.build();
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

    @Mod.EventBusSubscriber
    public static class AlfalfaMassHandler{

        public static final float HEAL_MULTIPLIER = 1.2F;

        @SubscribeEvent
        public static void onLivingHeal(LivingHealEvent event) {
            if (!(event.getEntity() instanceof Player player)) {
                return;
            }

            if (player.level().isClientSide) {
                return;
            }

            if (!shouldBoostHealing(player)) {
                return;
            }

            event.setAmount(event.getAmount() * HEAL_MULTIPLIER);
        }

        private static boolean shouldBoostHealing(Player player) {
            AntiqueBagHandler.BagRecord lastAntiqueSlot = ((IPlayerLastAntiqueSlot) player).getLastAntiqueSlot();
            if (lastAntiqueSlot != null){
                if (lastAntiqueSlot.stack().getItem() instanceof AntiqueBagItem bagItem){
                    return bagItem.containsAntique(lastAntiqueSlot.stack(), ImmortalersDelightItems.ALFALFA_MASS.get());
                }
            }
            return false;
        }
    }
}
