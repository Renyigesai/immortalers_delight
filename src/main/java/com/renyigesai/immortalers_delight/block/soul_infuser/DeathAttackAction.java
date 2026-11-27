package com.renyigesai.immortalers_delight.block.soul_infuser;

import com.renyigesai.immortalers_delight.entities.projectile.HatredfireBoltEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import com.renyigesai.immortalers_delight.util.DifficultyModeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DeathAttackAction extends SoulInfuserAttackAction{
    public DeathAttackAction(SoulInfuserAttackHelper attackHelper) {
        super(attackHelper);
        this.nextReadyTime = 99999;
        this.duration = 30;
    }

}
