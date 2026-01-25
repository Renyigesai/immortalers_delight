package com.renyigesai.immortalers_delight.item.weapon;

import com.google.common.collect.Lists;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class RepeatingCrossbowItem extends CrossbowItem {
    private static final String MOD_TAG_CHARGED = ImmortalersDelightMod.MODID + "_charged";
    private static final String MOD_TAG_CHARGED_PROJECTILES = ImmortalersDelightMod.MODID + "_charged_projectiles";
    private static final String BULLET_COUNT = ImmortalersDelightMod.MODID + "remainder_bullet";
    private static int maxChargeDuration = 35;
    private static int maxCoolDown = 70;
    public int defaultRange = 8;
    /** Set to {@code true} when the crossbow is 20% charged. */
    private boolean startSoundPlayed = false;
    /** Set to {@code true} when the crossbow is 50% charged. */
    private boolean midLoadSoundPlayed = false;
    private float startSoundPercent = 0.2F;
    private float midSoundPercent = 0.5F;
    private static float arrowPower = 3.15F;
    private static float fireworkPower = 1.6F;
    public RepeatingCrossbowItem(Properties pProperties) {
        super(pProperties);
    }

    /**=================================数据值管理部分，实际处理装填与弹药相关的方法====================================**/
    public static int getBulletCount(ItemStack pCrossbowStack) {
        CompoundTag compoundtag = pCrossbowStack.getOrCreateTag();
        return compoundtag.contains(BULLET_COUNT, Tag.TAG_INT) ? compoundtag.getInt(BULLET_COUNT) : 0;
    }

    public static void setBulletCount(ItemStack pCrossbowStack, int count) {
        CompoundTag compoundtag = pCrossbowStack.getOrCreateTag();
        compoundtag.putInt(BULLET_COUNT, count);
    }

    public static boolean isModCharged(ItemStack pCrossbowStack) {
        CompoundTag compoundtag = pCrossbowStack.getTag();
        return compoundtag != null && compoundtag.getBoolean(MOD_TAG_CHARGED);
    }

    public static void setModCharged(ItemStack pCrossbowStack, boolean pIsCharged) {
        CompoundTag compoundtag = pCrossbowStack.getOrCreateTag();
        compoundtag.putBoolean(MOD_TAG_CHARGED, pIsCharged);
    }

    private static void addChargedModProjectile(ItemStack pCrossbowStack, ItemStack pAmmoStack) {
        CompoundTag compoundtag = pCrossbowStack.getOrCreateTag();
        ListTag listtag;
        if (compoundtag.contains(MOD_TAG_CHARGED_PROJECTILES, 9)) {
            listtag = compoundtag.getList(MOD_TAG_CHARGED_PROJECTILES, 10);
        } else {
            listtag = new ListTag();
        }

        CompoundTag compoundtag1 = new CompoundTag();
        pAmmoStack.save(compoundtag1);
        listtag.add(compoundtag1);
        compoundtag.put(MOD_TAG_CHARGED_PROJECTILES, listtag);
    }

    private static List<ItemStack> getChargedModProjectiles(ItemStack pCrossbowStack) {
        List<ItemStack> list = Lists.newArrayList();
        CompoundTag compoundtag = pCrossbowStack.getTag();
        if (compoundtag != null && compoundtag.contains(MOD_TAG_CHARGED_PROJECTILES, 9)) {
            ListTag listtag = compoundtag.getList(MOD_TAG_CHARGED_PROJECTILES, 10);
            if (listtag != null) {
                for(int i = 0; i < listtag.size(); ++i) {
                    CompoundTag compoundtag1 = listtag.getCompound(i);
                    list.add(ItemStack.of(compoundtag1));
                }
            }
        }

        return list;
    }

    private static void clearChargedModProjectiles(ItemStack pCrossbowStack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, pCrossbowStack) > 0 ? 3 : 1;
        CompoundTag compoundtag = pCrossbowStack.getTag();
        if (compoundtag != null) {
            ListTag listtag = compoundtag.getList(MOD_TAG_CHARGED_PROJECTILES, 10);
            for (int j = 0; j < i; ++j) {
                if (!listtag.isEmpty()) {
                    listtag.remove(listtag.size() - 1);
                }
            }
            compoundtag.put(MOD_TAG_CHARGED_PROJECTILES, listtag);
        }

    }

    public static boolean containsChargedModProjectile(ItemStack pCrossbowStack, Item pAmmoItem) {
        return getChargedModProjectiles(pCrossbowStack).stream().anyMatch((p_40870_) -> {
            return p_40870_.is(pAmmoItem);
        });
    }


    /**============================发射流程部分，处理短按发射逻辑=============================**/
    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return ARROW_OR_FIREWORK;
    }

    /**
     * Get the predicate to match ammunition when searching the player's inventory, not their main/offhand
     */
    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (isModCharged(itemstack)) {
            modPerformShooting(pLevel, pPlayer, pHand, itemstack, getShootingModPower(itemstack), 1.0F);
            if (getChargedModProjectiles(itemstack).isEmpty()) {
                setModCharged(itemstack, false);
                pPlayer.getCooldowns().addCooldown(itemstack.getItem(), getModCoolDown(itemstack));
            }
            return InteractionResultHolder.consume(itemstack);
        } else if (!pPlayer.getProjectile(itemstack).isEmpty()) {
            if (!isModCharged(itemstack)) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
                pPlayer.startUsingItem(pHand);
            }

            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    private static float getShootingModPower(ItemStack pCrossbowStack) {
        return containsChargedModProjectile(pCrossbowStack, Items.FIREWORK_ROCKET) ? fireworkPower : arrowPower;
    }

    /**
     * 处理单次射击。
     * Performs the action of shooting a projectile.
     */
    public static void modPerformShooting(Level pLevel, LivingEntity pShooter, InteractionHand pUsedHand, ItemStack pCrossbowStack, float pVelocity, float pInaccuracy) {
        if (pShooter instanceof Player player && net.minecraftforge.event.ForgeEventFactory.onArrowLoose(pCrossbowStack, pShooter.level(), player, 1, true) < 0) return;
        boolean flag1 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, pCrossbowStack) > 0;
        List<ItemStack> list = getChargedModProjectiles(pCrossbowStack);
        float[] afloat = getModShotPitches(pShooter.getRandom());

        for(int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = list.get(i);
            if (!itemstack.isEmpty()) {
                boolean flag = (pShooter instanceof Player player && player.getAbilities().instabuild) || itemstack != list.get(0);

                if (i == list.size() - 1) {
                    shootModProjectile(pLevel, pShooter, pUsedHand, pCrossbowStack, itemstack, afloat[(i % 3)], flag, pVelocity, pInaccuracy, 0.0F);
                }
                if(flag1) {
                    if (i == list.size() - 2) {
                        shootModProjectile(pLevel, pShooter, pUsedHand, pCrossbowStack, itemstack, afloat[(i % 3)], flag, pVelocity, pInaccuracy, -10.0F);
                    } else if (i == list.size() - 3) {
                        shootModProjectile(pLevel, pShooter, pUsedHand, pCrossbowStack, itemstack, afloat[(i % 3)], flag, pVelocity, pInaccuracy, 10.0F);
                    }
                }
            }
        }

        onModCrossbowShot(pLevel, pShooter, pCrossbowStack);
    }

    /**
     * 处理单支箭矢的射出，这里是箭矢实际生成的方法。
     * 沟槽的ojng要把怪物弩AI的远程攻击也TM放在这里处理，实在是烂活中的烂活。
     **/
    private static void shootModProjectile(Level pLevel, LivingEntity pShooter, InteractionHand pHand, ItemStack pCrossbowStack, ItemStack pAmmoStack, float pSoundPitch, boolean pNoTrueArrow, float pVelocity, float pInaccuracy, float pProjectileAngle) {
        if (!pLevel.isClientSide) {
            boolean flag = pAmmoStack.is(Items.FIREWORK_ROCKET);
            Projectile projectile;
            if (flag) {
                projectile = new FireworkRocketEntity(pLevel, pAmmoStack, pShooter, pShooter.getX(), pShooter.getEyeY() - (double)0.15F, pShooter.getZ(), true);
            } else {
                projectile = getModArrow(pLevel, pShooter, pCrossbowStack, pAmmoStack);
                if (pNoTrueArrow) {
                    ((AbstractArrow)projectile).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
            }

            if (pShooter instanceof CrossbowAttackMob crossbowattackmob && crossbowattackmob.getTarget() != null) {
                crossbowattackmob.shootCrossbowProjectile(crossbowattackmob.getTarget(), pCrossbowStack, projectile, pProjectileAngle);
            } else {
                Vec3 vec31 = pShooter.getUpVector(1.0F);
                Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((double)(pProjectileAngle * ((float)Math.PI / 180F)), vec31.x, vec31.y, vec31.z);
                Vec3 vec3 = pShooter.getViewVector(1.0F);
                Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
                if (projectile instanceof AbstractArrow abstractarrow) {
                    if (DifficultyModeUtil.isPowerBattleMode()) abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + 3.0D + pLevel.getDifficulty().getId());
                    else abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (pLevel.getDifficulty() == Difficulty.HARD ? 1.5D : 2.0D));
                }
                projectile.shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), pVelocity, pInaccuracy);
            }

            pCrossbowStack.hurtAndBreak(flag ? 3 : 1, pShooter, (p_40858_) -> {
                p_40858_.broadcastBreakEvent(pHand);
            });
            pLevel.addFreshEntity(projectile);
            pLevel.playSound((Player)null, pShooter.getX(), pShooter.getY(), pShooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, pSoundPitch);
        }
    }

    /**
     * 获取单支箭矢
     **/
    private static AbstractArrow getModArrow(Level pLevel, LivingEntity pLivingEntity, ItemStack pCrossbowStack, ItemStack pAmmoStack) {
        ArrowItem arrowitem = (ArrowItem)(pAmmoStack.getItem() instanceof ArrowItem ? pAmmoStack.getItem() : Items.ARROW);
        AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, pAmmoStack, pLivingEntity);
        if (pLivingEntity instanceof Player) {
            abstractarrow.setCritArrow(true);
        }

        abstractarrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        abstractarrow.setShotFromCrossbow(true);
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, pCrossbowStack);
        if (i > 0) {
            abstractarrow.setPierceLevel((byte)i);
        }

        return abstractarrow;
    }

    private static float[] getModShotPitches(RandomSource pRandom) {
        boolean flag = pRandom.nextBoolean();
        return new float[]{1.0F, getModRandomShotPitch(flag, pRandom), getModRandomShotPitch(!flag, pRandom)};
    }

    private static float getModRandomShotPitch(boolean pIsHighPitched, RandomSource pRandom) {
        float f = pIsHighPitched ? 0.63F : 0.43F;
        return 1.0F / (pRandom.nextFloat() * 0.5F + 1.8F) + f;
    }

    private static void onModCrossbowShot(Level pLevel, LivingEntity pShooter, ItemStack pCrossbowStack) {
        if (pShooter instanceof ServerPlayer serverplayer) {
            if (!pLevel.isClientSide) {
                CriteriaTriggers.SHOT_CROSSBOW.trigger(serverplayer, pCrossbowStack);
            }

            serverplayer.awardStat(Stats.ITEM_USED.get(pCrossbowStack.getItem()));
        }

        clearChargedModProjectiles(pCrossbowStack);
    }

    /**=======================装填流程部分，处理长按右键以及松开右键时装填箭矢的方法========================**/

    /**
     * 松开右键时触发的方法，处理上膛的逻辑
     * Called when the player stops using an Item (stops holding the right mouse button).
     */
    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        int i = this.getUseDuration(pStack) - pTimeLeft;
        float f = getModPowerForTime(i, pStack);
        if (f >= 1.0F && !isModCharged(pStack) && tryLoadModProjectiles(pEntityLiving, pStack)) {
            setModCharged(pStack, true);
            SoundSource soundsource = pEntityLiving instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            pLevel.playSound((Player)null, pEntityLiving.getX(), pEntityLiving.getY(), pEntityLiving.getZ(), SoundEvents.CROSSBOW_LOADING_END, soundsource, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }

    }

    /**
     * 处理单次装弹，如果有三重射击，会装填3个
     **/
    private static boolean tryLoadModProjectiles(LivingEntity pShooter, ItemStack pCrossbowStack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, pCrossbowStack);
        int j = i == 0 ? 3 : 9;
        boolean flag = pShooter instanceof Player && ((Player)pShooter).getAbilities().instabuild;
        ItemStack itemstack = pShooter.getProjectile(pCrossbowStack);
        ItemStack itemstack1 = itemstack.copy();

        for(int k = 0; k < j; ++k) {
            if (k > 0) {
                itemstack = itemstack1.copy();
            }

            if (itemstack.isEmpty() && flag) {
                itemstack = new ItemStack(Items.ARROW);
                itemstack1 = itemstack.copy();
            }

            if (!loadModProjectile(pShooter, pCrossbowStack, itemstack, k > 0, flag)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 装填单支箭矢
     **/
    private static boolean loadModProjectile(LivingEntity pShooter, ItemStack pCrossbowStack, ItemStack pAmmoStack, boolean pHasAmmo, boolean pIsCreative) {
        if (pAmmoStack.isEmpty()) {
            return false;
        } else {
            boolean flag = pIsCreative && pAmmoStack.getItem() instanceof ArrowItem;
            ItemStack itemstack;
            if (!flag && !pIsCreative && !pHasAmmo) {
                itemstack = pAmmoStack.split(1);
                if (pAmmoStack.isEmpty() && pShooter instanceof Player) {
                    ((Player)pShooter).getInventory().removeItem(pAmmoStack);
                }
            } else {
                itemstack = pAmmoStack.copy();
            }

            addChargedModProjectile(pCrossbowStack, itemstack);
            return true;
        }
    }

    /**
     * 根据使用进度播放不同音效
     * Called as the item is being used by an entity.
     */
    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pCount) {
        if (!pLevel.isClientSide) {
            int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, pStack);
            SoundEvent soundevent = this.getStartSound(i);
            SoundEvent soundevent1 = i == 0 ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;
            float f = (float)(pStack.getUseDuration() - pCount) / (float) getModChargeDuration(pStack);
            if (f < 0.2F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }

            if (f >= this.startSoundPercent && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                pLevel.playSound((Player)null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), soundevent, SoundSource.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= this.midSoundPercent && soundevent1 != null && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                pLevel.playSound((Player)null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), soundevent1, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
        }

    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getUseDuration(ItemStack pStack) {
        return getModChargeDuration(pStack) + 3;
    }

    /**
     * The time the crossbow must be used to reload it
     */
    public static int getModChargeDuration(ItemStack pCrossbowStack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, pCrossbowStack);
        return i == 0 ? maxChargeDuration : maxChargeDuration - 5 * i;
    }

    public static int getModCoolDown(ItemStack pCrossbowStack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, pCrossbowStack);
        return i == 0 ? maxCoolDown : maxCoolDown - 10 * i;
    }

    /**
     * Returns the action that specifies what animation to play when the item is being used.
     */
    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.CROSSBOW;
    }

    private SoundEvent getStartSound(int pEnchantmentLevel) {
        switch (pEnchantmentLevel) {
            case 1:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_1;
            case 2:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_2;
            case 3:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_3;
            default:
                return SoundEvents.CROSSBOW_LOADING_START;
        }
    }

    private static float getModPowerForTime(int pUseTime, ItemStack pCrossbowStack) {
        float f = (float)pUseTime / (float) getModChargeDuration(pCrossbowStack);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    /**
     * Allows items to add custom lines of information to the mouseover description.
     */
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        List<ItemStack> list = getChargedModProjectiles(pStack);
        if (isModCharged(pStack) && !list.isEmpty()) {
            MutableComponent textValue = Component.translatable(
                    "tooltip." +ImmortalersDelightMod.MODID+ ".charged_projectiles",
                    list.size()
            );
            pTooltip.add(textValue.withStyle(ChatFormatting.WHITE));
            ItemStack itemstack = list.get(0);
            pTooltip.add(Component.translatable("item.minecraft.crossbow.projectile").append(CommonComponents.SPACE).append(itemstack.getDisplayName()));
            if (pFlag.isAdvanced() && itemstack.is(Items.FIREWORK_ROCKET)) {
                List<Component> list1 = Lists.newArrayList();
                Items.FIREWORK_ROCKET.appendHoverText(itemstack, pLevel, list1, pFlag);
                if (!list1.isEmpty()) {
                    for(int i = 0; i < list1.size(); ++i) {
                        list1.set(i, Component.literal("  ").append(list1.get(i)).withStyle(ChatFormatting.GRAY));
                    }

                    pTooltip.addAll(list1);
                }
            }
        }
    }

    /**
     * If this stack's item is a crossbow
     */
    @Override
    public boolean useOnRelease(ItemStack pStack) {
        return pStack.is(this);
    }

    @Override
    public int getDefaultProjectileRange() {
        return this.defaultRange;
    }
}
