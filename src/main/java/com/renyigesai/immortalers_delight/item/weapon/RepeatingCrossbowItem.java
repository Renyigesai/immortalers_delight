package com.renyigesai.immortalers_delight.item.weapon;//package com.renyigesai.immortalers_delight.item.weapon;
//
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import org.apache.commons.lang3.mutable.MutableFloat;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
public class RepeatingCrossbowItem extends CrossbowItem {

    private static int maxCoolDown = 70;

    private static final ChargingSounds DEFAULT_SOUNDS;
    public RepeatingCrossbowItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return super.use(level, player, hand);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        int i = this.getUseDuration(stack, entityLiving) - timeLeft;
        float f = getPowerForTime(i, stack, entityLiving);
        if (f >= 1.0F && !isCharged(stack) && tryLoadProjectiles(entityLiving, stack)) {
            ChargingSounds crossbowitem$chargingsounds = this.getChargingSounds(stack);
            crossbowitem$chargingsounds.end().ifPresent((p_352852_) -> level.playSound((Player)null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), (SoundEvent)p_352852_.value(), entityLiving.getSoundSource(), 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F));
        }
    }

    ChargingSounds getChargingSounds(ItemStack stack) {
        return EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.CROSSBOW_CHARGING_SOUNDS).orElse(DEFAULT_SOUNDS);
    }

    private static boolean tryLoadProjectiles(LivingEntity shooter, ItemStack crossbowStack) {
        if (shooter.level() instanceof ServerLevel level){
            List<ItemStack> projectiles = getProjectile(level,shooter,crossbowStack);
            boolean flag = true;
            int projectileCount = EnchantmentHelper.processProjectileCount(level, crossbowStack, shooter, 1);
            if (shooter instanceof Player player){
                if (!player.getAbilities().instabuild) {
                    flag = false;
                }else {
                    ArrayList<ItemStack> stacks = new ArrayList<>();
                    if (!projectiles.isEmpty()){
                        ItemStack first = projectiles.getFirst();
                        for (int i = 0; i < 3 * projectileCount; i++) {
                            ItemStack ammo = first.copy();
                            ammo.set(DataComponents.INTANGIBLE_PROJECTILE,Unit.INSTANCE);
                            stacks.add(ammo);
                        }
                    }else {
                        for (int i = 0; i < 3 * projectileCount; i++) {
                            ItemStack ammo = new ItemStack(Items.ARROW);
                            ammo.set(DataComponents.INTANGIBLE_PROJECTILE,Unit.INSTANCE);
                            stacks.add(ammo);
                        }
                    }
                    crossbowStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(stacks));
                    return true;
                }
            }
            if (!flag){
                if (projectiles.isEmpty()){
                    return false;
                }
                crossbowStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(projectiles));
                return true;
            }
        }
        return false;
    }

    private static List<ItemStack> getProjectile(ServerLevel level,LivingEntity livingEntity,ItemStack crossbowStack){
        if (!(crossbowStack.getItem() instanceof ProjectileWeaponItem)){
            return List.of();
        }else {
            ArrayList<ItemStack> stacks = new ArrayList<>();
            if (livingEntity instanceof Player player){
                boolean instabuild = player.getAbilities().instabuild;
                Predicate<ItemStack> predicate = ((ProjectileWeaponItem)crossbowStack.getItem()).getSupportedHeldProjectiles(crossbowStack);
                ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(player, predicate);
                if (!itemstack.isEmpty() && itemstack.getCount() >= 3){
                    stacksToList(level,itemstack.copy(),crossbowStack,livingEntity,stacks);
                    if (!instabuild){
                        itemstack.shrink(3);
                    }
                    return stacks;
                }else {
                    predicate = ((ProjectileWeaponItem)crossbowStack.getItem()).getAllSupportedProjectiles(crossbowStack);
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack itemstack1 = player.getInventory().getItem(i);
                        if (predicate.test(itemstack1)){
                            stacksToList(level,itemstack1,crossbowStack,livingEntity,stacks);
                            if (itemstack1.getCount() >= 3){
                                if (!instabuild){
                                    itemstack1.shrink(3);
                                }
                                break;
                            }else {
                                if (!instabuild){
                                    itemstack1.shrink(itemstack1.getCount());
                                }
                            }
                        }
                    }
                }
            }
            if (livingEntity instanceof Monster){

            }
            return stacks;
        }
    }

    private static void stacksToList(ServerLevel level,ItemStack stacks,ItemStack weapon,LivingEntity shooter,List<ItemStack> list){
        ArrayList<ItemStack> oleStacks = new ArrayList<>();
        int min = Math.min(stacks.getCount(), 3);
        for (int i = 0; i < min; i++) {//将堆叠的物品一个一个放入List
            ItemStack copy = stacks.copy();
            copy.setCount(1);
            oleStacks.add(copy);
        }
        //此时oleStacks应含有3个弹射物
        int projectileCount = EnchantmentHelper.processProjectileCount(level, weapon, shooter, 1);//返回1或3
        if (projectileCount == 1){//如果没有多重射击直接返回
            list.addAll(oleStacks);
            return;
        }
        for (ItemStack ammo : oleStacks) {
            for (int j = 0; j < projectileCount; j++) {//将弹射物数量全部翻三倍
                ItemStack newAmmo = ammo.copy();
                if (j < projectileCount - 1) {//将最后一个以外的弹射物设置为INTANGIBLE_PROJECTILE
                    newAmmo.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
                }
                list.add(newAmmo);
            }
        }
    }

    public void performShooting(Level level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, float velocity, float inaccuracy, @Nullable LivingEntity target) {
        if (level instanceof ServerLevel serverlevel) {
            if (shooter instanceof Player player) {
                if (EventHooks.onArrowLoose(weapon, shooter.level(), player, 1, true) < 0) {
                    return;
                }
            }
            ChargedProjectiles chargedProjectiles = weapon.get(DataComponents.CHARGED_PROJECTILES);
            if (chargedProjectiles != null && !chargedProjectiles.isEmpty()){
                int projectileCount = EnchantmentHelper.processProjectileCount(serverlevel, weapon, shooter, 1);
                List<ItemStack> stacks = popChargedProjectiles(weapon, projectileCount);
                if (!stacks.isEmpty()){
                    this.shoot(serverlevel, shooter, hand, weapon, stacks, velocity, inaccuracy, shooter instanceof Player, target);
                    if (shooter instanceof ServerPlayer serverPlayer) {
                        System.out.println(chargedProjectiles.getItems());
                        if (chargedProjectiles.getItems().size() <= projectileCount){
                            serverPlayer.getCooldowns().addCooldown(weapon.getItem(), getModCoolDown(serverlevel,serverPlayer,weapon));
                        }
                        ServerPlayer serverplayer = (ServerPlayer)shooter;
                        CriteriaTriggers.SHOT_CROSSBOW.trigger(serverplayer, weapon);
                        serverplayer.awardStat(Stats.ITEM_USED.get(weapon.getItem()));
                    }
                }
            }
        }
    }

    /**
     * 从弩中弹出最后 count 支箭，返回弹出的箭矢列表，并更新武器组件
     */
    private List<ItemStack> popChargedProjectiles(ItemStack weapon, int count) {
        ChargedProjectiles charged = weapon.get(DataComponents.CHARGED_PROJECTILES);
        if (charged == null) return List.of();
        List<ItemStack> items = charged.getItems();
        int size = items.size();
        int toRemove = Math.min(count, size);
        if (toRemove <= 0) return List.of();
        int fromIndex = size - toRemove;
        List<ItemStack> popped = new ArrayList<>(items.subList(fromIndex, size));
        List<ItemStack> remaining = items.subList(0, fromIndex);
        if (remaining.isEmpty()) {
            weapon.remove(DataComponents.CHARGED_PROJECTILES);
        } else {
            weapon.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(remaining));
        }
        return popped;
    }

    public static int getModCoolDown(ServerLevel level,Entity entity,ItemStack pCrossbowStack) {
        int i = processProjectileCount(level,pCrossbowStack,entity);
        return i == 0 ? maxCoolDown : maxCoolDown - 10 * i;
    }

    public static int processProjectileCount(ServerLevel level, ItemStack stack, Entity entity) {
        MutableFloat mutablefloat = new MutableFloat((float)3);
        EnchantmentHelper.runIterationOnItem(stack, (p_344593_, p_344594_) -> p_344593_.value().modifyProjectileCount(level, p_344594_, stack,entity,mutablefloat));
        return mutablefloat.intValue();
    }

    private static float getPowerForTime(int timeLeft, ItemStack stack, LivingEntity shooter) {
        float f = (float)timeLeft / (float)getChargeDuration(stack, shooter);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    /**
     * If this stack's item is a crossbow
     */
    @Override
    public boolean useOnRelease(ItemStack pStack) {
        return pStack.is(this);
    }

    static {
        DEFAULT_SOUNDS = new ChargingSounds(Optional.of(SoundEvents.CROSSBOW_LOADING_START), Optional.of(SoundEvents.CROSSBOW_LOADING_MIDDLE), Optional.of(SoundEvents.CROSSBOW_LOADING_END));
    }

}
