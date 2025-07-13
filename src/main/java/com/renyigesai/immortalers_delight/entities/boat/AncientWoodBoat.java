package com.renyigesai.immortalers_delight.entities.boat;

import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AncientWoodBoat extends ImmortalersBoat {

    public AncientWoodBoat(EntityType<? extends Boat> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public AncientWoodBoat(Level level, double pX, double pY, double pZ) {
        this(ImmortalersDelightEntities.ANCIENT_WOOD_BOAT.get(), level);
        this.setPos(pX, pY, pZ);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
    }
    @Override
    public void destroy(DamageSource damageSource) {
        this.spawnAtLocation(new ItemStack(ImmortalersDelightItems.ANCIENT_WOOD_LOG.get(),5));
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.25D;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        if (this.getPassengers().size() >= this.getMaxPassengers() - 1 && BigPassenger(passenger)) {
            return false;
        } else {
            return passenger.getBbWidth() < 2.5F && super.canAddPassenger(passenger);
        }
    }
    @Override
    public boolean hasEnoughSpaceFor(Entity pEntity) {
        return pEntity.getBbWidth() < 0.8 * this.getBbWidth();
    }
    @Override
    protected int getMaxPassengers() {
        return this.getBigPassengerNumber() == 1 ? 3 : getBigPassengerNumber() == 2 ? 2 : 5;
    }

    private static boolean BigPassenger(Entity passenger) {
        return passenger.getBbWidth() >= 1.375F;
    }

    private int getBigPassengerNumber() {
        int i = 0;
        for (Entity passenger : this.getPassengers()) {
            if (BigPassenger(passenger))
                i += 1;
        }
        return i;
    }

    public static boolean isAnimalEsque(Entity passenger) {
        return passenger instanceof Animal || passenger instanceof HoglinBase || (BigPassenger(passenger) && passenger instanceof Spider);
    }
    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction function) {
        if (this.hasPassenger(passenger)) {
            float x = -0.2F;
            float z = 0.0F;
            int index = this.getPassengers().indexOf(passenger);
            int bigPassengerNumber = this.getBigPassengerNumber();

            boolean rotate = false;

            if (bigPassengerNumber > 0) {
                if (this.getPassengers().size() == 3){
                    if (bigPassengerNumber == 1) {
                    int bigIndex = 0;
                    for (int i = 0; i < this.getPassengers().size(); i++) {
                        if (BigPassenger(this.getPassengers().get(i))) {
                            bigIndex = i;
                            break;
                        }
                    }
                    if (BigPassenger(passenger)) {
                        rotate = isAnimalEsque(passenger);
                        x -= 0.5F;
                    } else {
                        x += 0.8F;
                        if (bigIndex == 0 && index == 1 || bigIndex > 0 && index == 0) {
                            z += 0.5F;
                        } else {
                            z -= 0.5F;
                        }
                    }
                }
                } else if (this.getPassengers().size() == 2) {
                    if (bigPassengerNumber == 1) {
                        rotate = isAnimalEsque(passenger);
                        if (index == 0) {
                            x += 0.8F;
                        } else {
                            x -= 0.6F;
                        }
                    }
                    if (bigPassengerNumber == 2) {
                        if (index == 0) {
                            x += 0.8F;
                        } else {
                            rotate = isAnimalEsque(passenger);
                            x -= 0.8F;
                        }
                    }
                } else if (this.getPassengers().size() == 1) {
                    x -= 0.4F;
                }
            } else {
                if (this.getPassengers().size() > 4) {
                    x = 1.35F - index * 0.7F;
                }
                else if (this.getPassengers().size() == 4) {
                    x = 1.2F - index * 0.8F;
                }
                else if (this.getPassengers().size() == 3) {
                    x = 1.0F - index * 1.0F;
                }
                else if (this.getPassengers().size() == 2) {
                    x += 0.6F - index * 1.4F;
                } else if (this.getPassengers().size() == 1) {
                    x += -0.8F;
                }
            }

            float f1 = (float) ((this.isRemoved() ? (double) 0.01F : this.getPassengersRidingOffset()) + passenger.getMyRidingOffset());
            Vec3 vector3d = (new Vec3(x, 0.0D, z)).yRot(-this.getYRot() * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
            function.accept(passenger, this.getX() + vector3d.x, this.getY() + (double) f1, this.getZ() + vector3d.z);
            passenger.setYRot(passenger.getYRot() + this.deltaRotation);
            passenger.setYHeadRot(passenger.getYHeadRot() + this.deltaRotation);
            this.clampRotation(passenger);
            if (passenger instanceof LivingEntity living && (rotate || (isAnimalEsque(passenger) && !BigPassenger(passenger) && this.getPassengers().size() > 1))) {
                int j = passenger.getId() % 2 == 0 ? 90 : 270;
                passenger.setYBodyRot(living.yBodyRot + (float) j);
                passenger.setYHeadRot(passenger.getYHeadRot() + (float) j);
            }
        }
    }

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
            ItemStack hand = pPlayer.getItemInHand(pHand);
            if (hand.is(Items.CHEST) && hand.getCount() >= 5){
                AncientWoodChestBoat boat = new AncientWoodChestBoat(this.level(), this.getX(), this.getY(), this.getZ());
                if (!pPlayer.getAbilities().instabuild) {
                    hand.shrink(5);
                }
                boat.setVariant(ImmortalersChestBoat.Type.ANCIENT_WOOD);
                this.level().addFreshEntity(boat);
                this.level().playLocalSound(this.getX(),this.getY(),this.getZ(), SoundEvents.WOOD_BREAK, SoundSource.BLOCKS,0.8F,0.8F,false);
                this.discard();
                return InteractionResult.SUCCESS;
            }else {
                return super.imm$Interact(pPlayer,pHand);
            }
    }

}
