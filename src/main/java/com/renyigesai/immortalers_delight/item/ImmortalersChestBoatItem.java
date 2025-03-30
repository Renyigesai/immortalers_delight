package com.renyigesai.immortalers_delight.item;

import com.renyigesai.immortalers_delight.entities.ImmortalersChestBoat;
import com.renyigesai.immortalers_delight.entities.ImmortalersChestBoat;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class ImmortalersChestBoatItem extends Item {
    private static final Predicate<Entity> ENTITY_PREDICATE;
    private final ImmortalersChestBoat.Type type;
    private final boolean hasChest;

    public ImmortalersChestBoatItem(ImmortalersChestBoat.Type pType, Properties pProperties) {
        super(pProperties);
        this.hasChest = true;
        this.type = pType;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack $$3 = pPlayer.getItemInHand(pHand);
        HitResult $$4 = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.ANY);
        if ($$4.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass($$3);
        } else {
            Vec3 $$5 = pPlayer.getViewVector(1.0F);
            List<Entity> $$7 = pLevel.getEntities(pPlayer, pPlayer.getBoundingBox().expandTowards($$5.scale(5.0)).inflate(1.0), ENTITY_PREDICATE);
            if (!$$7.isEmpty()) {
                Vec3 $$8 = pPlayer.getEyePosition();

                for (Entity $$9 : $$7) {
                    AABB $$10 = $$9.getBoundingBox().inflate($$9.getPickRadius());
                    if ($$10.contains($$8)) {
                        return InteractionResultHolder.pass($$3);
                    }
                }
            }

            if ($$4.getType() == HitResult.Type.BLOCK) {
                ImmortalersChestBoat boat = (ImmortalersChestBoat) this.getBoat(pLevel, $$4);
                ((ImmortalersChestBoat)boat).setVariant(this.type);
                boat.setYRot(pPlayer.getYRot());
                if (!pLevel.noCollision(boat, boat.getBoundingBox())) {
                    return InteractionResultHolder.fail($$3);
                } else {
                    if (!pLevel.isClientSide) {
                        pLevel.addFreshEntity(boat);
                        pLevel.gameEvent(pPlayer, GameEvent.ENTITY_PLACE, $$4.getLocation());
                        if (!pPlayer.getAbilities().instabuild) {
                            $$3.shrink(1);
                        }
                    }

                    pPlayer.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess($$3, pLevel.isClientSide());
                }
            } else {
                return InteractionResultHolder.pass($$3);
            }
        }
    }

    private ImmortalersChestBoat getBoat(Level pLevel, HitResult pHitResult) {
        return /*this.hasChest ? new ImmortalersChestBoat(pLevel, pHitResult.getLocation().x, pHitResult.getLocation().y, pHitResult.getLocation().z) : */new ImmortalersChestBoat(pLevel, pHitResult.getLocation().x, pHitResult.getLocation().y, pHitResult.getLocation().z);
    }

    static {
        ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
    }
}

