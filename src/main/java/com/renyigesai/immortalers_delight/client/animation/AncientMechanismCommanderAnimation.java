package com.renyigesai.immortalers_delight.client.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

/** Animation clips matching the action names in the Blockbench model. */
public final class AncientMechanismCommanderAnimation {

    public static final AnimationDefinition IDLE = AnimationDefinition.Builder.withLength(2.0F).looping()
            .addAnimation("waist", rotation(0.0F, -6.0F, -4.0F, -6.0F))
            .build();

    public static final AnimationDefinition WALK = AnimationDefinition.Builder.withLength(1.0F).looping()
            .addAnimation("right_leg", rotation(0.0F, -11.0F, 10.0F, -11.0F))
            .addAnimation("left_leg", rotation(0.0F, 10.0F, -10.0F, 10.0F))
            .addAnimation("right_arm", rotation(0.0F, -10.0F, 18.0F, -10.0F))
            .addAnimation("left_arm", rotation(0.0F, 12.0F, -8.0F, 12.0F))
            .build();

    public static final AnimationDefinition ATTACK_START = attack(0.6F, -35.0F, 15.0F);
    public static final AnimationDefinition ATTACK_1 = attack(1.52F, -105.0F, 35.0F);
    public static final AnimationDefinition ATTACK_2 = attack(1.36F, -75.0F, 70.0F);
    public static final AnimationDefinition ATTACK_3 = attack(2.8F, -120.0F, 45.0F);
    public static final AnimationDefinition ATTACK_4 = attack(2.0F, -60.0F, 25.0F);
    public static final AnimationDefinition SUMMON = attack(4.0F, -90.0F, 0.0F);
    public static final AnimationDefinition SHOOT = attack(9.92F, -35.0F, 0.0F);
    /** Standalone machine-crossbow animation1, layered over the commander shoot clip. */
    public static final AnimationDefinition CROSSBOW_SHOOT = AnimationDefinition.Builder.withLength(0.48F).looping()
            .addAnimation("bow_limb_left", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.24F, KeyframeAnimations.degreeVec(0.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.36F, KeyframeAnimations.degreeVec(0.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.44F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("bow_limb_right", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.24F, KeyframeAnimations.degreeVec(0.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.36F, KeyframeAnimations.degreeVec(0.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.44F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("bowstring_left", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.24F, KeyframeAnimations.degreeVec(0.0F, -25.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.36F, KeyframeAnimations.degreeVec(0.0F, -25.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.44F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("bowstring_right", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.24F, KeyframeAnimations.degreeVec(0.0F, 25.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.36F, KeyframeAnimations.degreeVec(0.0F, 25.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.44F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("flywheel", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.24F, KeyframeAnimations.degreeVec(0.0F, 180.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.48F, KeyframeAnimations.degreeVec(0.0F, 360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("arrow", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.24F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.LINEAR)))
            .build();
    public static final AnimationDefinition THROW = attack(1.4F, -100.0F, 20.0F);
    public static final AnimationDefinition REVIVE = attack(2.68F, 25.0F, 0.0F);

    private static AnimationDefinition attack(float length, float rightArmStart, float rightArmEnd) {
        return AnimationDefinition.Builder.withLength(length)
                .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(rightArmStart, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(length * 0.35F, KeyframeAnimations.degreeVec(rightArmEnd, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(length, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)))
                .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(length * 0.35F, KeyframeAnimations.degreeVec(-35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(length, KeyframeAnimations.degreeVec(12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)))
                .build();
    }

    private static AnimationChannel rotation(float time, float start, float peak, float end) {
        return new AnimationChannel(AnimationChannel.Targets.ROTATION,
                new Keyframe(time, KeyframeAnimations.degreeVec(start, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(time + 0.5F, KeyframeAnimations.degreeVec(peak, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                new Keyframe(time + 1.0F, KeyframeAnimations.degreeVec(end, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM));
    }

    private AncientMechanismCommanderAnimation() {}
}
