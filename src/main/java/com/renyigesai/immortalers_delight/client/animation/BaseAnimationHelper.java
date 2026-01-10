package com.renyigesai.immortalers_delight.client.animation;

public class BaseAnimationHelper {
    // 二次方缓动曲线
    public static float easeOutQuad(float t) {
        return 1 - (1 - t) * (1 - t);
    }
    // 三次方缓动曲线
    public static float easeOutCubic(float t) {
        return 1 - (float) Math.pow(1 - t, 3);
    }
    //指数缓动曲线（爆发动画）
    public static float easeOutExpo(float t) {
        return t >= 1.0f ? 1.0f : 1 - (float) Math.pow(2, -10 * t);
    }
    // 平滑缓动曲线
    public static float easeOutSine(float t) {
        return (float) Math.sin(t * Math.PI / 2);
    }
    //弹性缓动曲线
    public static float easeOutElastic(float t) {
        if (t == 0 || t == 1) return t;
        float c4 = (2 * (float) Math.PI) / 0.3f;
        return (float) (Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * c4) + 1);
    }
}
