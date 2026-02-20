package com.renyigesai.immortalers_delight.client;

import net.minecraft.world.phys.Vec3;

import java.util.*;

public class AnimationHelper {
    // 排序后的时间点列表（避免Map无序导致的错误）
    private final List<Integer> sortedTimes;
    // 原始时间-位置映射
    private final Map<Integer, Vec3> trails;

    /**
     * 构造方法：初始化并排序时间点
     * @param trails 原始时间-位置映射
     */
    public AnimationHelper(Map<Integer, Vec3> trails) {
        this.trails = new HashMap<>(trails);
        // 将Map的键（时间）排序，方便后续查找相邻点
        this.sortedTimes = new ArrayList<>(trails.keySet());
        Collections.sort(this.sortedTimes);
    }

    /**
     * 计算任意时刻t的位置（折线插值）
     * @param t 目标时刻（0.0f ~ 25.0f）
     * @return t时刻的位置Vec3；若t超出范围，返回边界点
     */
    public Vec3 getPositionAtTime(float t) {
        // 处理下边界情况：t小于最小时间 → 返回最小时间的位置
        int minTime = sortedTimes.get(0);
        if (t <= minTime) {
            return trails.get(minTime);
        }

        // 处理上边界情况：t大于最大时间 → 返回最大时间的位置
        int maxTime = sortedTimes.get(sortedTimes.size() - 1);
        if (t >= maxTime) {
            return trails.get(maxTime);
        }

        // 找到t所在的两个相邻时间点（prevTime < t < nextTime）
        int prevTime = minTime;
        int nextTime = maxTime;
        for (int time : sortedTimes) {
            if (time < t) {
                prevTime = time;
            } else if (time > t) {
                nextTime = time;
                break; // 找到后立即退出循环，提升效率
            } else {
                // t恰好等于离散时间点 → 直接返回对应位置
                return trails.get(time);
            }
        }

        // 获取相邻时间点的位置
        Vec3 prevPos = trails.get(prevTime);
        Vec3 nextPos = trails.get(nextTime);

        // 计算插值比例（alpha）：(t - prevTime) / (nextTime - prevTime)
        double alpha = (t - prevTime) / (double) (nextTime - prevTime);

        // 线性插值得到t时刻的位置
        return prevPos.lerp(nextPos, alpha);
    }

    /**
     * 测试方法：输出每隔1.0f的位置
     */
//    public static void main(String[] args) {
//        // 需要测试的原始时间-位置映射
//        Map<Integer, Vec3> trails = Map.of(
//                0, new Vec3(0, -24, 0),
//                9, new Vec3(0, 0, 0),
//                10, new Vec3(0, -26, 0),
//                15, new Vec3(0, 0, 0),
//                25, new Vec3(0, -10, 0)
//        );
//
//        AnimationHelper interpolator = new AnimationHelper(trails);
//
//        // 输出0.0f到25.0f之间每个整数时刻的位置（也可输出小数时刻）
//        for (float t = 0.0f; t <= 25.0f; t += 1.0f) {
//            Vec3 pos = interpolator.getPositionAtTime(t);
//            System.out.printf("t=%.1f → %s%n", t, pos);
//        }
//    }
}
