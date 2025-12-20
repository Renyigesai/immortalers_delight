package com.renyigesai.immortalers_delight.event;


import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.entities.living.illager_archaeological_team.Scavenger;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishBase;
import com.renyigesai.immortalers_delight.entities.living.SkelverfishBomber;
import com.renyigesai.immortalers_delight.init.*;
import com.renyigesai.immortalers_delight.util.WorldUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber
public class SkelverfishEventHelper {

    @SubscribeEvent
    public static void onExplosionDamage(LivingHurtEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_EXPLOSION) && event.getSource().getEntity() instanceof SkelverfishBomber bomber) {
            float f = event.getEntity().getRandom().nextFloat();
            float extraDamage = (bomber.getMaxFireDamage() +
                    (float)(bomber.getAttribute(Attributes.ATTACK_DAMAGE) == null ? 0.0F : bomber.getAttribute(Attributes.ATTACK_DAMAGE).getValue())) *
                    (bomber.isPowered() ? 4.0F : 2.0F) +
                    (bomber.level().getDifficulty() == Difficulty.EASY ? 2.0F + f * 1.5F : 0.0F) +
                    (bomber.level().getDifficulty() == Difficulty.NORMAL ? 5.0F + f * 4.0F : 0.0F) +
                    (bomber.level().getDifficulty() == Difficulty.HARD ? 6.0F + f * 3.0F : 0.0F);
            event.setAmount(event.getAmount() + extraDamage); // 提升爆炸伤害，造成攻击力200%或400%爆炸伤害
        }
    }

    @SubscribeEvent
    public static void onAmbusherAttack(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(attacker.getType());
            if (entityId != null && !attacker.level().isClientSide) {
                String idString = entityId.toString();
                if (idString.equals(ImmortalersDelightEntities.SKELVERFISH_AMBUSHER.getId().toString())) {
                    int hurtArmor = event.getEntity().getArmorValue();
                    if (hurtArmor > 0) {
                        if (hurtArmor > 20) hurtArmor = 20;
                        float damageBuffer = (1/(1-(hurtArmor * 0.04f))) > 3.0f ? 3.0f : (1/(1-(hurtArmor * 0.04f)));
                        event.setAmount(event.getAmount() * damageBuffer);
                    }
                }
            }
        }
    }
    public static final String PILLAGER_HAS_KNIFE = ImmortalersDelightMod.MODID + "_has_pillager_knife";

    @SubscribeEvent
    public static void onThrasherJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Sniffer sniffer) {
            sniffer.goalSelector.addGoal(3, new TemptGoal(sniffer, 3.0D, Ingredient.of(ImmortalersDelightItems.SACHETS.get()), false));
        }
        if (event.getEntity() instanceof Pillager pillager) {
            if (pillager.hasActiveRaid() && pillager.level() instanceof ServerLevel serverLevel) {
                int r = pillager.getRandom().nextInt(3);
                if (r == 0) {
                    Scavenger scavenger = new Scavenger(ImmortalersDelightEntities.SCAVENGER.get(), pillager.level());
                    scavenger.setCurrentRaid(pillager.getCurrentRaid());
                    scavenger.finalizeSpawn(serverLevel, pillager.level().getCurrentDifficultyAt(pillager.blockPosition()), MobSpawnType.SPAWNER, (SpawnGroupData)null, (CompoundTag)null);
                    scavenger.moveTo(pillager.getX(), pillager.getY(), pillager.getZ(), pillager.getYRot(), pillager.getXRot());
                    pillager.level().addFreshEntity(scavenger);
                }
                if (r == 1 && pillager.getItemInHand(InteractionHand.OFF_HAND).isEmpty()) {
                    //掠夺者装备匕首
                    ItemStack knife = new ItemStack(ImmortalersDelightItems.PILLAGER_KNIFE.get());
                    int i = pillager.getRandom().nextInt(4);
                    if (i == 0) PotionUtils.setPotion(knife, Potions.POISON);
                    if (i == 1) PotionUtils.setPotion(knife, Potions.WEAKNESS);
                    if (i == 2) PotionUtils.setPotion(knife, Potions.HARMING);
                    if (i == 3) PotionUtils.setPotion(knife, Potions.SLOWNESS);
                    ItemStack mainHand = pillager.getItemInHand(InteractionHand.MAIN_HAND);
                    if (mainHand.is(Items.CROSSBOW)){
                        pillager.setItemSlot(EquipmentSlot.MAINHAND, knife);
                        pillager.setItemSlot(EquipmentSlot.OFFHAND, mainHand);
                    } else pillager.setItemSlot(EquipmentSlot.OFFHAND, knife);
                    //掠夺者额外掉落
                    CompoundTag entityTag = pillager.getPersistentData();
                    entityTag.putInt(PILLAGER_HAS_KNIFE,1);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPillagerDeath(LivingDropsEvent event) {
        if (event.isCanceled()) return;
        LivingEntity deceased = event.getEntity();
        if (deceased.level() instanceof ServerLevel serverLevel && event.getSource().getEntity() instanceof Player player) {
            CompoundTag entityTag = deceased.getPersistentData();
            if (entityTag.contains(PILLAGER_HAS_KNIFE, Tag.TAG_INT) && entityTag.getInt(PILLAGER_HAS_KNIFE) > 0) {
                ResourceLocation resourcelocation = new ResourceLocation(ImmortalersDelightMod.MODID, "entities/pillager_knife_loot");
                if (!resourcelocation.equals(BuiltInLootTables.EMPTY)) {
                    LootParams.Builder lootparams$builder = (new LootParams.Builder(serverLevel))
                            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(deceased.getOnPos()))
                            .withParameter(LootContextParams.DAMAGE_SOURCE, event.getSource())
                            .withParameter(LootContextParams.THIS_ENTITY, event.getEntity())
                            // 关键：添加LAST_DAMAGE_PLAYER参数（killed_by_player条件需要）
                            .withOptionalParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                            // 可选：击杀者实体
                            .withOptionalParameter(LootContextParams.KILLER_ENTITY, player)
                            // 可选：直接击杀者（如箭、三叉戟）
                            .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, event.getSource().getDirectEntity());


                    LootParams lootparams = lootparams$builder
                            .create(LootContextParamSets.ENTITY);
                    LootTable loottable = serverLevel.getServer().getLootData().getLootTable(resourcelocation);
                    for (int i = 0; i < entityTag.getInt(PILLAGER_HAS_KNIFE); ++i) {
                        List<ItemStack> items = loottable.getRandomItems(lootparams);
                        if (items.isEmpty()) items = WorldUtils.getFromLootTableItemStack(WorldUtils.getLootTables("immortalers_delight:entities/pillager_knife_loot",serverLevel),serverLevel,deceased.getOnPos().above());
                        for (ItemStack itemstack : items) {
                            event.getDrops().add(new ItemEntity(
                                            deceased.level(),
                                            deceased.getX(),
                                            deceased.getY(),
                                            deceased.getZ(),
                                            itemstack
                                    )
                            );
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPillagerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Pillager hurtOne && !hurtOne.level().isClientSide()) {
            if (event.getSource().getEntity() instanceof LivingEntity attacker
                    && attacker.getMobType() != MobType.ILLAGER
                    && hurtOne.distanceTo(attacker) <= 3.5) {
                if (hurtOne.getItemInHand(InteractionHand.MAIN_HAND).is(ImmortalersDelightItems.PILLAGER_KNIFE.get())
                        || hurtOne.getItemInHand(InteractionHand.MAIN_HAND).is(ImmortalersDelightItems.PILLAGER_KNIFE.get())) {
                    float f = (float)(hurtOne.getAttribute(Attributes.ATTACK_DAMAGE) == null ? 0.0F : hurtOne.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
                    f += 3.0F;
                    hurtOne.swing(InteractionHand.MAIN_HAND);
                    Vec3 hurtEntityPos = hurtOne.position();
                    Vec3 sourceEntityPos = attacker.position();
                    Vec3 directionVector = sourceEntityPos.subtract(hurtEntityPos);
                    for(int i = 1; i <= 3; i++) {
                        attacker.level().addParticle(ParticleTypes.SWEEP_ATTACK,
                                (hurtEntityPos.x + sourceEntityPos.x) / 2,
                                (hurtEntityPos.y + sourceEntityPos.y) / 2,
                                (hurtEntityPos.z + sourceEntityPos.z) / 2,
                                0.2 * i,
                                0.2 * i,
                                0.2 * i
                        );
                    }
                    attacker.hurt(hurtOne.damageSources().mobAttack(hurtOne), f);
                    attacker.setDeltaMovement(hurtOne.getDeltaMovement().add(directionVector));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSilverfishsIntoStone(EntityMobGriefingEvent event) {
        if (event.getEntity() instanceof SkelverfishBase skelverfishBase) {
            event.setResult(Event.Result.DENY);
        }
//        if (event.getEntity() instanceof SkelverfishThrasher skelverfishThrasher) {
//            skelverfishThrasher.goalSelector.removeGoal(new MeleeAttackGoal(skelverfishThrasher, 1.0D, false));
//        }
    }

}
