package com.renyigesai.immortalers_delight.client.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class SoulInfuserAnimation {
    public static final AnimationDefinition idle = AnimationDefinition.Builder.withLength(2.0F).looping()
            .addAnimation("all", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 14.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -90.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 270.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -90.0F, 6.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(6.0F, -180.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, -270.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(-6.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.48F, KeyframeAnimations.posVec(0.0F, 21.04F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 22.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.48F, KeyframeAnimations.posVec(0.0F, 21.14F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts0", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("base", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition die = AnimationDefinition.Builder.withLength(1.5F)
            .addAnimation("all", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, 6.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("all", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.scaleVec(1.2F, 1.3F, 1.2F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2F, KeyframeAnimations.scaleVec(0.1F, 0.1F, 0.1F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(0.0F, -720.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, 22.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 30.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.scaleVec(1.6F, 1.6F, 1.6F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, -8.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.scaleVec(1.5F, 1.5F, 1.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 25.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.scaleVec(1.8F, 1.8F, 1.8F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, -12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts0", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("base", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition awaken = AnimationDefinition.Builder.withLength(4.0F)
            .addAnimation("all", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.scaleVec(0.9F, 0.85F, 0.9F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.4F, KeyframeAnimations.scaleVec(1.15F, 1.2F, 1.15F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2F, KeyframeAnimations.scaleVec(0.98F, 0.98F, 0.98F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 18.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2F, KeyframeAnimations.posVec(0.0F, 15.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, -5.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2F, KeyframeAnimations.posVec(0.0F, -3.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.degreeVec(0.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, -120.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(-0.3F, 0.0F, -0.2F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.3F, 0.0F, 0.3F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(-0.4F, 0.0F, -0.3F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3F, KeyframeAnimations.posVec(-0.2F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.2F, 5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9F, KeyframeAnimations.posVec(-0.3F, 4.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 6.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 21.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2F, KeyframeAnimations.posVec(0.0F, 17.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.4F, KeyframeAnimations.degreeVec(-12.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(-6.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.3F, 0.0F, 0.3F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(-0.3F, 0.0F, -0.2F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.4F, 0.0F, 0.4F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3F, KeyframeAnimations.posVec(0.2F, 1.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(-0.2F, 3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9F, KeyframeAnimations.posVec(0.2F, 2.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 4.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 23.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2F, KeyframeAnimations.posVec(0.0F, 19.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.degreeVec(0.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, -30.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 270.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(-0.2F, 0.0F, 0.3F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.2F, 0.0F, -0.3F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(-0.3F, 0.0F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2F, KeyframeAnimations.posVec(0.0F, 9.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.posVec(-14.0F, 0.0F, -14.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2F, KeyframeAnimations.posVec(-10.5F, 0.0F, -10.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.posVec(14.0F, 0.0F, 14.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2F, KeyframeAnimations.posVec(10.5F, 0.0F, 10.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.posVec(-14.0F, 0.0F, 14.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2F, KeyframeAnimations.posVec(-10.5F, 0.0F, 10.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts0", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.posVec(14.0F, 0.0F, -14.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2F, KeyframeAnimations.posVec(10.5F, 0.0F, -10.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.degreeVec(100.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.posVec(0.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2F, KeyframeAnimations.posVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.degreeVec(-100.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.posVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -100.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.posVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2F, KeyframeAnimations.posVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 100.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.posVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2F, KeyframeAnimations.posVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 100.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.posVec(4.0F, 0.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.degreeVec(-50.0F, 0.0F, -100.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.posVec(-4.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.degreeVec(50.0F, 0.0F, -100.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.posVec(-4.0F, 0.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.degreeVec(-50.0F, 0.0F, 100.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.45F, KeyframeAnimations.posVec(4.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("base", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.28F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition attack_fire_rain = AnimationDefinition.Builder.withLength(1.6F).looping()
            .addAnimation("all", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8F, KeyframeAnimations.degreeVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.degreeVec(-12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("all", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 16.0F, -1.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8F, KeyframeAnimations.posVec(0.0F, 17.5F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(0.0F, 18.5F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.44F, KeyframeAnimations.posVec(0.0F, 13.0F, -1.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.0F, 16.0F, -1.5F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.6F, KeyframeAnimations.degreeVec(0.0F, -540.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 18.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -90.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.6F, KeyframeAnimations.degreeVec(0.0F, 450.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 10.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.6F, KeyframeAnimations.degreeVec(-10.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 20.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8F, KeyframeAnimations.posVec(0.0F, -4.5F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.0F, -4.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts0", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("base", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition attack_explode = AnimationDefinition.Builder.withLength(4.0F)
            .addAnimation("all", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2F, KeyframeAnimations.posVec(0.0F, 4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6F, KeyframeAnimations.posVec(0.0F, 6.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.76F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.36F, KeyframeAnimations.posVec(0.0F, -2.19F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2F, KeyframeAnimations.posVec(0.0F, 22.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6F, KeyframeAnimations.posVec(0.0F, 24.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.76F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.36F, KeyframeAnimations.posVec(0.0F, 10.12F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6F, KeyframeAnimations.scaleVec(1.15F, 1.25F, 1.15F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.76F, KeyframeAnimations.scaleVec(0.9F, 0.85F, 0.9F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.36F, KeyframeAnimations.scaleVec(0.9008F, 0.8495F, 0.9008F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2F, KeyframeAnimations.degreeVec(-35.0F, -180.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6F, KeyframeAnimations.degreeVec(-35.0F, -1080.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.76F, KeyframeAnimations.degreeVec(0.0F, -1120.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.36F, KeyframeAnimations.degreeVec(2.36F, -1179.36F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, -1440.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2F, KeyframeAnimations.posVec(0.0F, 22.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.76F, KeyframeAnimations.posVec(0.0F, 14.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.36F, KeyframeAnimations.posVec(0.0F, 14.17F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -90.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2F, KeyframeAnimations.degreeVec(35.0F, 90.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6F, KeyframeAnimations.degreeVec(35.0F, 990.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.76F, KeyframeAnimations.degreeVec(0.0F, 1020.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.36F, KeyframeAnimations.degreeVec(-2.36F, 1080.45F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 1350.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2F, KeyframeAnimations.posVec(0.0F, 14.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.4F, KeyframeAnimations.posVec(0.0F, 14.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.76F, KeyframeAnimations.posVec(0.0F, 6.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.36F, KeyframeAnimations.posVec(0.0F, 6.17F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.76F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.36F, KeyframeAnimations.posVec(0.0F, 11.23F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2F, KeyframeAnimations.scaleVec(1.3F, 1.3F, 1.3F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6F, KeyframeAnimations.scaleVec(1.6F, 1.6F, 1.6F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.76F, KeyframeAnimations.scaleVec(0.9F, 0.9F, 0.9F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.36F, KeyframeAnimations.scaleVec(0.8704F, 0.8704F, 0.8704F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts0", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("base", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition attack_summon = AnimationDefinition.Builder.withLength(2.6F).looping()
            .addAnimation("all", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.64F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.96F, KeyframeAnimations.posVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.28F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.16F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.36F, KeyframeAnimations.degreeVec(2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.88F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.96F, KeyframeAnimations.degreeVec(-2.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.12F, KeyframeAnimations.posVec(0.0F, 14.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.36F, KeyframeAnimations.posVec(0.0F, 11.78F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.72F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.88F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.64F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.96F, KeyframeAnimations.posVec(0.0F, 14.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.28F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.16F, KeyframeAnimations.degreeVec(0.0F, -90.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.32F, KeyframeAnimations.degreeVec(0.0F, -180.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.52F, KeyframeAnimations.degreeVec(0.0F, -270.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.96F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.12F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6F, KeyframeAnimations.degreeVec(0.0F, -450.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.12F, KeyframeAnimations.posVec(0.0F, 14.67F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.36F, KeyframeAnimations.posVec(0.0F, 11.33F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.72F, KeyframeAnimations.posVec(0.0F, 8.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.88F, KeyframeAnimations.posVec(0.0F, 8.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.16F, KeyframeAnimations.scaleVec(1.125F, 1.0F, 1.125F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.32F, KeyframeAnimations.scaleVec(1.25F, 1.0F, 1.25F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.52F, KeyframeAnimations.scaleVec(1.375F, 1.0F, 1.375F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.96F, KeyframeAnimations.scaleVec(1.5F, 1.0F, 1.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.04F, KeyframeAnimations.scaleVec(1.5F, 1.0F, 1.5F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.12F, KeyframeAnimations.scaleVec(0.8F, 1.0F, 0.8F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.28F, KeyframeAnimations.scaleVec(0.9F, 1.0F, 0.9F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.44F, KeyframeAnimations.scaleVec(0.9667F, 1.0F, 0.9667F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -90.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -90.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.16F, KeyframeAnimations.degreeVec(0.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.48F, KeyframeAnimations.degreeVec(0.0F, 60.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.96F, KeyframeAnimations.degreeVec(0.0F, 135.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.04F, KeyframeAnimations.degreeVec(0.0F, 135.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.2F, KeyframeAnimations.degreeVec(0.0F, 180.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6F, KeyframeAnimations.degreeVec(0.0F, 270.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.16F, KeyframeAnimations.degreeVec(0.0F, -90.0F, 6.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.32F, KeyframeAnimations.degreeVec(6.0F, -180.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.52F, KeyframeAnimations.degreeVec(0.0F, -270.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.96F, KeyframeAnimations.degreeVec(-6.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.12F, KeyframeAnimations.degreeVec(-6.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6F, KeyframeAnimations.degreeVec(0.0F, -450.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.12F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.36F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.72F, KeyframeAnimations.posVec(0.0F, 14.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.88F, KeyframeAnimations.posVec(0.0F, 14.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(0.0F, 21.04F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.56F, KeyframeAnimations.posVec(0.0F, 22.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.96F, KeyframeAnimations.posVec(0.0F, 21.14F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.04F, KeyframeAnimations.posVec(0.0F, 21.14F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.28F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.12F, KeyframeAnimations.posVec(0.0F, -5.33F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.36F, KeyframeAnimations.posVec(0.0F, -6.67F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.72F, KeyframeAnimations.posVec(0.0F, -8.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.88F, KeyframeAnimations.posVec(0.0F, -8.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.16F, KeyframeAnimations.posVec(0.0F, 0.0F, -9.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(0.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.52F, KeyframeAnimations.posVec(0.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.96F, KeyframeAnimations.posVec(0.0F, 0.0F, -12.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.04F, KeyframeAnimations.posVec(0.0F, 0.0F, -12.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.12F, KeyframeAnimations.posVec(0.0F, 0.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.28F, KeyframeAnimations.posVec(0.0F, 0.0F, -7.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.44F, KeyframeAnimations.posVec(0.0F, 0.0F, -7.67F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6F, KeyframeAnimations.posVec(0.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.16F, KeyframeAnimations.posVec(0.0F, 0.0F, 9.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(0.0F, 0.0F, 10.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.52F, KeyframeAnimations.posVec(0.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.96F, KeyframeAnimations.posVec(0.0F, 0.0F, 12.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.04F, KeyframeAnimations.posVec(0.0F, 0.0F, 12.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.12F, KeyframeAnimations.posVec(0.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.28F, KeyframeAnimations.posVec(0.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.44F, KeyframeAnimations.posVec(0.0F, 0.0F, 7.67F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6F, KeyframeAnimations.posVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.16F, KeyframeAnimations.posVec(-9.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.52F, KeyframeAnimations.posVec(-11.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.96F, KeyframeAnimations.posVec(-12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.04F, KeyframeAnimations.posVec(-12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.12F, KeyframeAnimations.posVec(-6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.28F, KeyframeAnimations.posVec(-7.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.44F, KeyframeAnimations.posVec(-7.67F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6F, KeyframeAnimations.posVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.posVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.16F, KeyframeAnimations.posVec(9.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.52F, KeyframeAnimations.posVec(11.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.96F, KeyframeAnimations.posVec(12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.04F, KeyframeAnimations.posVec(12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.12F, KeyframeAnimations.posVec(6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.28F, KeyframeAnimations.posVec(7.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.44F, KeyframeAnimations.posVec(7.67F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.6F, KeyframeAnimations.posVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts0", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("base", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition attack_column = AnimationDefinition.Builder.withLength(2.4F)
            .addAnimation("all", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.88F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.52F, KeyframeAnimations.posVec(0.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.68F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.4F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.24F, KeyframeAnimations.posVec(0.0F, 13.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.48F, KeyframeAnimations.posVec(0.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.72F, KeyframeAnimations.posVec(0.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.88F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.52F, KeyframeAnimations.posVec(0.0F, 6.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.68F, KeyframeAnimations.posVec(0.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.4F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.88F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.32F, KeyframeAnimations.scaleVec(0.9F, 1.2F, 0.9F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.52F, KeyframeAnimations.scaleVec(1.35F, 0.65F, 1.35F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.72F, KeyframeAnimations.scaleVec(0.95F, 1.05F, 0.95F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.4F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.88F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(0.0F, 24.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.56F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.4F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.88F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.32F, KeyframeAnimations.scaleVec(0.8F, 0.8F, 0.8F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.56F, KeyframeAnimations.scaleVec(1.6F, 1.0F, 1.6F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0F, KeyframeAnimations.scaleVec(1.05F, 1.0F, 1.05F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.4F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.88F, KeyframeAnimations.degreeVec(0.0F, -90.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.88F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(0.0F, 14.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.52F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.4F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.88F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.32F, KeyframeAnimations.scaleVec(0.85F, 0.85F, 0.85F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.56F, KeyframeAnimations.scaleVec(1.5F, 0.8F, 1.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.4F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.2F, KeyframeAnimations.degreeVec(0.0F, -84.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.44F, KeyframeAnimations.degreeVec(0.0F, -180.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.8F, KeyframeAnimations.degreeVec(0.0F, -276.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.8F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.56F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.8F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.16F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.24F, KeyframeAnimations.posVec(0.0F, 19.6F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.4F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.88F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.88F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.16F, KeyframeAnimations.posVec(0.0F, 0.0F, -12.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.36F, KeyframeAnimations.posVec(0.0F, 0.0F, -14.67F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.48F, KeyframeAnimations.posVec(0.0F, 0.0F, -16.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6F, KeyframeAnimations.posVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.96F, KeyframeAnimations.posVec(0.0F, 0.0F, -6.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2F, KeyframeAnimations.posVec(0.0F, 0.0F, -7.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(0.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.16F, KeyframeAnimations.posVec(0.0F, 0.0F, 12.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.36F, KeyframeAnimations.posVec(0.0F, 0.0F, 14.67F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.48F, KeyframeAnimations.posVec(0.0F, 0.0F, 16.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6F, KeyframeAnimations.posVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.96F, KeyframeAnimations.posVec(0.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 7.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.16F, KeyframeAnimations.posVec(-12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.36F, KeyframeAnimations.posVec(-14.67F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.48F, KeyframeAnimations.posVec(-16.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6F, KeyframeAnimations.posVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.96F, KeyframeAnimations.posVec(-6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2F, KeyframeAnimations.posVec(-7.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.16F, KeyframeAnimations.posVec(12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.36F, KeyframeAnimations.posVec(14.67F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.48F, KeyframeAnimations.posVec(16.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.6F, KeyframeAnimations.posVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.96F, KeyframeAnimations.posVec(6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2F, KeyframeAnimations.posVec(7.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.32F, KeyframeAnimations.posVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts0", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("base", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition attack_fire_ring = AnimationDefinition.Builder.withLength(1.6F)
            .addAnimation("all", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.52F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.72F, KeyframeAnimations.posVec(0.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.88F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.24F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.52F, KeyframeAnimations.posVec(0.0F, 21.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.72F, KeyframeAnimations.posVec(0.0F, 6.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.88F, KeyframeAnimations.posVec(0.0F, 12.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.52F, KeyframeAnimations.scaleVec(0.9F, 1.2F, 0.9F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.72F, KeyframeAnimations.scaleVec(1.35F, 0.65F, 1.35F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.92F, KeyframeAnimations.scaleVec(0.95F, 1.05F, 0.95F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.52F, KeyframeAnimations.degreeVec(0.0F, -240.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.76F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.6F, KeyframeAnimations.degreeVec(0.0F, -540.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.52F, KeyframeAnimations.posVec(0.0F, 25.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.76F, KeyframeAnimations.posVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.52F, KeyframeAnimations.scaleVec(0.8F, 0.8F, 0.8F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.76F, KeyframeAnimations.scaleVec(1.7F, 0.6F, 1.7F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.52F, KeyframeAnimations.degreeVec(0.0F, 180.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.76F, KeyframeAnimations.degreeVec(0.0F, 270.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.6F, KeyframeAnimations.degreeVec(0.0F, 360.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.52F, KeyframeAnimations.posVec(0.0F, 24.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.76F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.2F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.52F, KeyframeAnimations.scaleVec(0.8F, 0.8F, 0.8F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.76F, KeyframeAnimations.scaleVec(1.6F, 1.0F, 1.6F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2F, KeyframeAnimations.scaleVec(1.05F, 1.0F, 1.05F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 14.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.65F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.1F, KeyframeAnimations.posVec(0.0F, 8.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.45F, KeyframeAnimations.scaleVec(0.85F, 0.85F, 0.85F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.68F, KeyframeAnimations.scaleVec(1.5F, 0.8F, 1.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.45F, KeyframeAnimations.scaleVec(0.75F, 1.15F, 0.75F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.66F, KeyframeAnimations.scaleVec(1.55F, 0.6F, 1.55F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.85F, KeyframeAnimations.scaleVec(0.9F, 1.05F, 0.9F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts0", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("base", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.52F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.72F, KeyframeAnimations.posVec(0.0F, -7.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.92F, KeyframeAnimations.posVec(0.0F, -4.8F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition attack_fireball = AnimationDefinition.Builder.withLength(1.6F)
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.95F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.05F, KeyframeAnimations.degreeVec(18.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3F, KeyframeAnimations.degreeVec(-3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 14.5F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.95F, KeyframeAnimations.posVec(0.0F, 13.8F, -2.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.05F, KeyframeAnimations.posVec(0.0F, 18.0F, 3.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3F, KeyframeAnimations.posVec(0.0F, 15.5F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9F, KeyframeAnimations.degreeVec(-8.0F, -180.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.05F, KeyframeAnimations.degreeVec(12.0F, -240.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8F, KeyframeAnimations.posVec(0.0F, 15.5F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.05F, KeyframeAnimations.posVec(0.0F, 21.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.35F, KeyframeAnimations.posVec(0.0F, 17.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9F, KeyframeAnimations.degreeVec(-10.0F, 90.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.05F, KeyframeAnimations.degreeVec(15.0F, 120.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.degreeVec(0.0F, 180.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8F, KeyframeAnimations.posVec(0.0F, 18.5F, -1.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.05F, KeyframeAnimations.posVec(0.0F, 23.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.35F, KeyframeAnimations.posVec(0.0F, 19.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8F, KeyframeAnimations.posVec(0.0F, 9.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.05F, KeyframeAnimations.posVec(0.0F, 11.5F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.6F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts0", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("base", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    public static final AnimationDefinition defult = AnimationDefinition.Builder.withLength(0.0F)
            .addAnimation("ring_up", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 18.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -90.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_down", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 16.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts11", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts10", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts9", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-45.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts8", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, 3.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts7", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 8.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-11.0F, 0.0F, 11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("upper_body_parts0", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(11.0F, 0.0F, -11.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("ring_center", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
}
