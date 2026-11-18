package com.renyigesai.immortalers_delight.client.animation.custom;


import net.minecraft.world.phys.Vec3;

import java.util.*;

public class AnimationHelper {
    // 排序后的时间点列表（保持不可变性与有序）
    private final List<Integer> sortedTimes;
    // 原始时间-位置映射
    private final Map<Integer, Vec3> trails;

    /**
     * 构造方法：初始化并排序关键帧时间
     * @param trails 原始时间-位置映射（不可为空）
     */
    public AnimationHelper(Map<Integer, Vec3> trails) {
        if (trails == null || trails.isEmpty()) {
            throw new IllegalArgumentException("Trails map cannot be null or empty!");
        }
        this.trails = new HashMap<>(trails);
        this.sortedTimes = new ArrayList<>(trails.keySet());
        Collections.sort(this.sortedTimes);
    }

    /**
     * 计算任意时刻 t 的线性插值位置 (Linear Interpolation)
     * 利用二分查找以 O(log N) 复杂度定位区间
     * @param t 目标时刻（float）
     * @return t 时刻线性插值得到的 Vec3
     */
    public Vec3 getPositionAtTime(float t) {
        int size = sortedTimes.size();

        // 1. 处理单关键帧退化情况
        if (size == 1) {
            return trails.get(sortedTimes.get(0));
        }

        // 2. 下边界：t 小于等于最小时间 -> 保持初始帧
        int minTime = sortedTimes.get(0);
        if (t <= minTime) {
            return trails.get(minTime);
        }

        // 3. 上边界：t 大于等于最大时间 -> 保持末尾帧
        int maxTime = sortedTimes.get(size - 1);
        if (t >= maxTime) {
            return trails.get(maxTime);
        }

        // 4. 二分查找定位所在区间
        int searchKey = (int) Math.floor(t);
        int searchResult = Collections.binarySearch(sortedTimes, searchKey);

        int currentIdx;
        if (searchResult >= 0) {
            // 精确命中已存在的整点时间
            if (t == (float) sortedTimes.get(searchResult)) {
                return trails.get(sortedTimes.get(searchResult));
            }
            currentIdx = searchResult;
        } else {
            // 未命中整点，按插入点回退一位找到左端点
            int insertPoint = ~searchResult;
            currentIdx = insertPoint - 1;
        }

        int nextIdx = currentIdx + 1;
        int prevTime = sortedTimes.get(currentIdx);
        int nextTime = sortedTimes.get(nextIdx);

        // 浮点精度保护
        if (t >= nextTime) {
            return trails.get(nextTime);
        }

        Vec3 prevPos = trails.get(prevTime);
        Vec3 nextPos = trails.get(nextTime);

        // 5. 计算线性插值比例 alpha，并执行 lerp
        double alpha = (t - prevTime) / (double) (nextTime - prevTime);
        return prevPos.lerp(nextPos, alpha);
    }

    /**
     * 获取当前通道的最大时间点（可辅助上层进行全局动画时长统计）
     */
    public int getMaxTime() {
        return sortedTimes.get(sortedTimes.size() - 1);
    }

    /**
     * 获取当前通道的最小时间点
     */
    public int getMinTime() {
        return sortedTimes.get(0);
    }
}
//
//import net.minecraft.world.phys.Vec3;
//
//import java.util.*;
//
//public class AnimationHelper {
//    // 排序后的时间点列表（避免Map无序导致的错误）
//    private final List<Integer> sortedTimes;
//    // 原始时间-位置映射
//    private final Map<Integer, Vec3> trails;
//
//    /**
//     * 构造方法：初始化并排序时间点
//     * @param trails 原始时间-位置映射
//     */
//    public AnimationHelper(Map<Integer, Vec3> trails) {
//        this.trails = new HashMap<>(trails);
//        // 将Map的键（时间）排序，方便后续查找相邻点
//        this.sortedTimes = new ArrayList<>(trails.keySet());
//        Collections.sort(this.sortedTimes);
//    }
//
//    /**
//     * 计算任意时刻t的位置（折线插值）
//     * @param t 目标时刻（0.0f ~ 25.0f）
//     * @return t时刻的位置Vec3；若t超出范围，返回边界点
//     */
//    public Vec3 getPositionAtTime(float t) {
//        // 处理下边界情况：t小于最小时间 → 返回最小时间的位置
//        int minTime = sortedTimes.get(0);
//        if (t <= minTime) {
//            return trails.get(minTime);
//        }
//
//        // 处理上边界情况：t大于最大时间 → 返回最大时间的位置
//        int maxTime = sortedTimes.get(sortedTimes.size() - 1);
//        if (t >= maxTime) {
//            return trails.get(maxTime);
//        }
//
//        // 找到t所在的两个相邻时间点（prevTime < t < nextTime）
//        int prevTime = minTime;
//        int nextTime = maxTime;
//        for (int time : sortedTimes) {
//            if (time < t) {
//                prevTime = time;
//            } else if (time > t) {
//                nextTime = time;
//                break; // 找到后立即退出循环，提升效率
//            } else {
//                // t恰好等于离散时间点 → 直接返回对应位置
//                return trails.get(time);
//            }
//        }
//
//        // 获取相邻时间点的位置
//        Vec3 prevPos = trails.get(prevTime);
//        Vec3 nextPos = trails.get(nextTime);
//
//        // 计算插值比例（alpha）：(t - prevTime) / (nextTime - prevTime)
//        double alpha = (t - prevTime) / (double) (nextTime - prevTime);
//
//        // 线性插值得到t时刻的位置
//        return prevPos.lerp(nextPos, alpha);
//    }
//
//    /**
//     * 测试方法：输出每隔1.0f的位置
//     */
////    public static void main(String[] args) {
////        // 需要测试的原始时间-位置映射
////        Map<Integer, Vec3> trails = Map.of(
////                0, new Vec3(0, -24, 0),
////                9, new Vec3(0, 0, 0),
////                10, new Vec3(0, -26, 0),
////                15, new Vec3(0, 0, 0),
////                25, new Vec3(0, -10, 0)
////        );
////
////        AnimationHelper interpolator = new AnimationHelper(trails);
////
////        // 输出0.0f到25.0f之间每个整数时刻的位置（也可输出小数时刻）
////        for (float t = 0.0f; t <= 25.0f; t += 1.0f) {
////            Vec3 pos = interpolator.getPositionAtTime(t);
////            System.out.printf("t=%.1f → %s%n", t, pos);
////        }
////    }
//}
