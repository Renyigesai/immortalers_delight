package com.renyigesai.immortalers_delight.item.weapon;

import com.google.common.collect.Sets;
import com.renyigesai.immortalers_delight.client.renderer.special_item.WakimayaTantoRender;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

public class WakimayaTantoItem extends DiggerItem {
    public static final Set<ToolAction> KNIFE_ACTIONS = Set.of(ToolActions.SHEARS_CARVE);
    public static final float RATE = 0.025f;
    public static final String NBT_BLADE_ENERGY_COOLING = "BladeEnergyCooling";
    public static final String NBT_BLADE_ENERGY_COOLING_OLD = "BladeEnergyCoolingOld";
    public static final String NBT_RELEASE = "Release";
    public WakimayaTantoItem(Properties pProperties) {
        super(0, 0, new Tier() {
            @Override
            public int getUses() {
                return 250;
            }

            @Override
            public float getSpeed() {
                return -2;
            }

            @Override
            public float getAttackDamageBonus() {
                return 8f;
            }

            @Override
            public int getLevel() {
                return 4;
            }

            @Override
            public int getEnchantmentValue() {
                return 15;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return Ingredient.EMPTY;
            }
        }, BlockTags.MINEABLE_WITH_AXE, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        boolean hurt = super.hurtEnemy(pStack, pTarget, pAttacker);
        if (pAttacker instanceof Player player && player.getAttackStrengthScale(0.0f) == 1.0f){
            float bladeEnergyCooling = getBladeEnergyCooling(pStack);
            CompoundTag tag = pStack.getOrCreateTag();
            if (bladeEnergyCooling == 0f){
                tag.putFloat(NBT_BLADE_ENERGY_COOLING,0.005f);
            }
        }
        return hurt;
    }

    public static void fanShapedAttack(LivingEntity pAttacker, double radius, double totalAngle, float damage) {
        Level level = pAttacker.level();
        if (level.isClientSide){
            return;
        }
        if (!(pAttacker instanceof Player player)){
            return;
        }
        Vec3 origin = pAttacker.getEyePosition();
        Vec3 lookVec = pAttacker.getLookAngle().normalize();

        double cosThreshold = Math.cos(Math.toRadians(totalAngle / 2.0));

        AABB searchBox = pAttacker.getBoundingBox().inflate(radius);

        DamageSource source = level.damageSources().playerAttack(player);

        for (Entity entity : level.getEntities(pAttacker, searchBox)) {
            if (entity == pAttacker || !entity.isAlive() || !(entity instanceof LivingEntity)) {
                continue;
            }

            Vec3 target = entity.position().add(0, entity.getBbHeight() / 2.0, 0);
            Vec3 toTarget = target.subtract(origin);
            double distSq = toTarget.lengthSqr();

            if (distSq > radius * radius || distSq < 1.0E-6) {
                continue;
            }

            double dot = toTarget.normalize().dot(lookVec);
            if (dot < cosThreshold) {
                continue;
            }

            Random random = new Random();
            player.attack(entity);
            Vec3 vec3 = entity.getDeltaMovement();
            double s = random.nextDouble(0.35d,0.85d);
            Vec3 vec31 = (new Vec3(Mth.sin(pAttacker.getYRot() * ((float)Math.PI / 180F)), 0.8D,(-Mth.cos(pAttacker.getYRot() * ((float)Math.PI / 180F))))).normalize().scale(s);
            entity.setDeltaMovement(vec3.x / 2.0D - vec31.x, entity.onGround() ? Math.min(0.4D, vec3.y / 2.0 + s) : vec3.y, vec3.z / 2.0D - vec31.z);
        }
    }

    public float getBladeEnergyDamage(LivingEntity pAttacker){
        double attributeValue = pAttacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float damage = (float) attributeValue * 0.8f;
        if (DifficultyModeUtil.isPowerBattleMode()){
            damage = damage * 1.4f;
        }
        return damage;
    }

    public static float getBladeEnergyCooling(ItemStack stack){
        if (!stack.getOrCreateTag().contains(NBT_BLADE_ENERGY_COOLING)){
            return 0f;
        }
        return stack.getOrCreateTag().getFloat(NBT_BLADE_ENERGY_COOLING);
    }

    public static float getBladeEnergyCoolingOld(ItemStack stack){
        if (!stack.getOrCreateTag().contains(NBT_BLADE_ENERGY_COOLING_OLD)){
            return 0f;
        }
        return stack.getOrCreateTag().getFloat(NBT_BLADE_ENERGY_COOLING_OLD);
    }

    public static boolean getRelease(ItemStack stack){
        if (!stack.getOrCreateTag().contains(NBT_RELEASE)){
            return false;
        }
        return stack.getOrCreateTag().getBoolean(NBT_RELEASE);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (!(pEntity instanceof Player player) || !isProgress(player)) {
            return;
        }
        float cooling = getBladeEnergyCooling(pStack);
        if (cooling >= 0.35f && cooling <= 0.45f){
            boolean release = getRelease(pStack);
            if (!release){
                float damage = getBladeEnergyDamage(player);
                fanShapedAttack(player,10,45,damage);
                spawnParticle(player,10,45);
                pStack.getOrCreateTag().putBoolean(NBT_RELEASE,true);
            }
        }
        nbtTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new WakimayaTantoRender(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
            }
        });
    }

    public void nbtTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected){
        CompoundTag tag = pStack.getOrCreateTag();
        float cooling = getBladeEnergyCooling(pStack);
        tag.putFloat(NBT_BLADE_ENERGY_COOLING_OLD,cooling);
        if (cooling > 0f){
            if (cooling <= 1f){
                tag.putFloat(NBT_BLADE_ENERGY_COOLING, cooling + RATE);
            }else {
                tag.putFloat(NBT_BLADE_ENERGY_COOLING,0f);
                tag.putFloat(NBT_BLADE_ENERGY_COOLING_OLD, 0f);
                tag.putBoolean(NBT_RELEASE,false);
            }
        }
    }

    public static float getProgress(ItemStack stack,float pPartialTicks) {
        return Mth.lerp(pPartialTicks, getBladeEnergyCoolingOld(stack), getBladeEnergyCooling(stack));
    }

    public static boolean isProgress(Player player){
        return player.getAttackStrengthScale(0.0f) == 1.0f;
    }

    public static void spawnParticle(Player player, double radius, double totalAngle) {
        Level level = player.level();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        Vec3 origin = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle().normalize();
        Vec3 flatLook = new Vec3(lookVec.x, 0, lookVec.z);
        if (flatLook.lengthSqr() < 1e-6) {
            flatLook = new Vec3(0, 0, 1);
        }
        flatLook = flatLook.normalize();
        double halfAngle = Math.toRadians(totalAngle / 2.0);
        double speed = 0.05;
        int smokeCount = 30;
        for (int i = 0; i < smokeCount; i++) {
            double angleOffset = (serverLevel.random.nextDouble() * 2 - 1) * halfAngle;

            double cos = Math.cos(angleOffset);
            double sin = Math.sin(angleOffset);
            double dx = flatLook.x * cos - flatLook.z * sin;
            double dz = flatLook.x * sin + flatLook.z * cos;

            double dist = radius * (0.3 + serverLevel.random.nextDouble() * 0.7);

            double px = origin.x + dx * dist;
            double py = origin.y - 0.4;
            double pz = origin.z + dz * dist;

            ParticleType<?> particleType = new Random().nextDouble() < 0.15 ? ImmortalersDelightParticleTypes.BLADE_LIGHT.get() : ParticleTypes.LARGE_SMOKE;
            serverLevel.sendParticles((ParticleOptions) particleType, px, py, pz, 1, dx * speed, 0.02, dz * speed, 0.02);
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        Set<Enchantment> ALLOWED_ENCHANTMENTS = Sets.newHashSet(new Enchantment[]{Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.MOB_LOOTING});
        if (ALLOWED_ENCHANTMENTS.contains(enchantment)) {
            return true;
        } else {
            Set<Enchantment> DENIED_ENCHANTMENTS = Sets.newHashSet(new Enchantment[]{Enchantments.BLOCK_FORTUNE});
            return DENIED_ENCHANTMENTS.contains(enchantment) ? false : enchantment.category.canEnchant(stack.getItem());
        }
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return KNIFE_ACTIONS.contains(toolAction);
    }

    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }
}
