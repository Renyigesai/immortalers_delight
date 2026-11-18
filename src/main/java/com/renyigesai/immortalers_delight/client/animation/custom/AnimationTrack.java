package com.renyigesai.immortalers_delight.client.animation.custom;

import net.minecraft.world.phys.Vec3;
import java.util.*;

public class AnimationTrack {
    private final boolean isCatmullRom;
    private final AnimationHelper linearHelper;
    private final CatmullRomAnimationHelper catmullRomHelper;
    private final Vec3 defaultValue;

    private AnimationTrack(Map<Integer, Vec3> keyframes, boolean forceCatmullRom, Vec3 defaultValue) {
        this.defaultValue = defaultValue;
        if (keyframes.isEmpty()) {
            this.isCatmullRom = false;
            this.linearHelper = null;
            this.catmullRomHelper = null;
        } else {
            this.isCatmullRom = forceCatmullRom;
            if (this.isCatmullRom) {
                this.catmullRomHelper = new CatmullRomAnimationHelper(keyframes);
                this.linearHelper = null;
            } else {
                this.linearHelper = new AnimationHelper(keyframes);
                this.catmullRomHelper = null;
            }
        }
    }

    public Vec3 get(float timeInTicks) {
        if (linearHelper == null && catmullRomHelper == null) {
            return defaultValue;
        }
        return isCatmullRom ? catmullRomHelper.getPositionAtTime(timeInTicks)
                : linearHelper.getPositionAtTime(timeInTicks);
    }

    /**
     * 关键帧链式构造器，规避 Map.of 限制并自动判断缓动
     */
    public static class Builder {
        private final Map<Integer, Vec3> frames = new HashMap<>();
        private boolean hasCatmullRom = false;
        private final Vec3 defaultValue;

        public Builder(Vec3 defaultValue) {
            this.defaultValue = defaultValue;
        }

        /**
         * @param timeSeconds Blockbench 中的时间（秒）
         */
        public Builder frame(double timeSeconds, double x, double y, double z, boolean isCatmullRom) {
            int tick = (int) Math.round(timeSeconds * 25.0); // 0.04s -> 1 tick
            frames.put(tick, new Vec3(x, y, z));
            if (isCatmullRom) {
                this.hasCatmullRom = true;
            }
            return this;
        }

        public Builder linear(double timeSeconds, double x, double y, double z) {
            return frame(timeSeconds, x, y, z, false);
        }

        public Builder catmull(double timeSeconds, double x, double y, double z) {
            return frame(timeSeconds, x, y, z, true);
        }

        public AnimationTrack build() {
            return new AnimationTrack(frames, hasCatmullRom, defaultValue);
        }
    }
}
