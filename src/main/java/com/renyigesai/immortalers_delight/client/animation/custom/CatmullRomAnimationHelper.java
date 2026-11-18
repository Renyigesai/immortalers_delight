package com.renyigesai.immortalers_delight.client.animation.custom;


import net.minecraft.world.phys.Vec3;

import java.util.*;

public class CatmullRomAnimationHelper {
    // 排序后的时间点列表
    private final List<Integer> sortedTimes;
    // 原始时间-位置映射
    private final Map<Integer, Vec3> trails;

    public CatmullRomAnimationHelper(Map<Integer, Vec3> trails) {
        if (trails == null || trails.isEmpty()) {
            throw new IllegalArgumentException("Trails map cannot be null or empty!");
        }
        this.trails = new HashMap<>(trails);
        this.sortedTimes = new ArrayList<>(trails.keySet());
        Collections.sort(this.sortedTimes);
    }

    /**
     * 计算任意时刻 t 的平滑样条曲线插值位置 (Catmull-Rom)
     * 利用二分查找以 O(log N) 复杂度定位区间
     * @param t 目标时刻
     * @return 经过平滑过渡后的 Vec3 坐标
     */
    public Vec3 getPositionAtTime(float t) {
        int size = sortedTimes.size();

        // 1. 处理单点退化情况
        if (size == 1) {
            return trails.get(sortedTimes.get(0));
        }

        // 2. 处理上/下边界情况
        int minTime = sortedTimes.get(0);
        if (t <= minTime) {
            return trails.get(minTime);
        }

        int maxTime = sortedTimes.get(size - 1);
        if (t >= maxTime) {
            return trails.get(maxTime);
        }

        // 3. 利用二分查找定位时间所在的帧索引
        // 将浮点数转换为向下取整的整数进行快速二分（因为时间均为整数点）
        int searchKey = (int) Math.floor(t);
        int searchResult = Collections.binarySearch(sortedTimes, searchKey);

        int currentIdx;
        if (searchResult >= 0) {
            // 精确命中已有的整点时间帧
            if (t == (float) sortedTimes.get(searchResult)) {
                return trails.get(sortedTimes.get(searchResult));
            }
            // 例如 t = 9.4，searchKey = 9，命中了索引，当前区间起点即为命中项
            currentIdx = searchResult;
        } else {
            // 未命中，计算插入点: ~searchResult 等价于 -searchResult - 1
            int insertPoint = ~searchResult;
            currentIdx = insertPoint - 1;
        }

        int nextIdx = currentIdx + 1;

        int prevTime = sortedTimes.get(currentIdx);
        int nextTime = sortedTimes.get(nextIdx);

        // 若因精度原因刚好对齐终点帧
        if (t >= nextTime) {
            return trails.get(nextTime);
        }

        // 4. 计算局部进度 delta (0.0F ~ 1.0F)
        float delta = (t - prevTime) / (float) (nextTime - prevTime);

        // 5. 按照原版 Catmull-Rom 规则夹紧索引，选定 4 个控制点
        Vec3 p0 = trails.get(sortedTimes.get(Math.max(0, currentIdx - 1)));
        Vec3 p1 = trails.get(sortedTimes.get(currentIdx));
        Vec3 p2 = trails.get(sortedTimes.get(nextIdx));
        Vec3 p3 = trails.get(sortedTimes.get(Math.min(size - 1, nextIdx + 1)));

        // 6. 分三轴进行样条插值
        double x = catmullrom(delta, p0.x, p1.x, p2.x, p3.x);
        double y = catmullrom(delta, p0.y, p1.y, p2.y, p3.y);
        double z = catmullrom(delta, p0.z, p1.z, p2.z, p3.z);

        return new Vec3(x, y, z);
    }

    /**
     * 对标原版 Mth.catmullrom 的 double 精度实现
     */
    private double catmullrom(float delta, double p0, double p1, double p2, double p3) {
        return 0.5D * (
                2.0D * p1 +
                        (p2 - p0) * delta +
                        (2.0D * p0 - 5.0D * p1 + 4.0D * p2 - p3) * delta * delta +
                        (3.0D * p1 - p0 - 3.0D * p2 + p3) * delta * delta * delta
        );
    }
}
