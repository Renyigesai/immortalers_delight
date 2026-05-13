package com.renyigesai.immortalers_delight.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.level.block.state.properties.Property;

public class PropertyCollectionUtils {

    /**
     * 获取【只在 collectionA 中存在，不在 collectionB 中存在】的 Property
     */
    public static Collection<Property<?>> getOnlyInA(Collection<Property<?>> collectionA, Collection<Property<?>> collectionB) {
        Set<Property<?>> setB = new HashSet<>(collectionB);
        Collection<Property<?>> result = new ArrayList<>();

        for (Property<?> property : collectionA) {
            if (!setB.contains(property)) {
                result.add(property);
            }
        }
        return result;
    }

    /**
     * 获取【只在 collectionB 中存在，不在 collectionA 中存在】的 Property
     */
    public static Collection<Property<?>> getOnlyInB(Collection<Property<?>> collectionA, Collection<Property<?>> collectionB) {
        return getOnlyInA(collectionB, collectionA);
    }

    /**
     * 获取【两个集合所有不相同的项】（对称差集）
     * = 只在A有 + 只在B有
     */
    public static Collection<Property<?>> getAllDifferences(Collection<Property<?>> collectionA, Collection<Property<?>> collectionB) {
        Collection<Property<?>> onlyA = getOnlyInA(collectionA, collectionB);
        Collection<Property<?>> onlyB = getOnlyInB(collectionA, collectionB);

        Collection<Property<?>> allDiff = new ArrayList<>(onlyA);
        allDiff.addAll(onlyB);
        return allDiff;
    }
    // 比较两个 PropertyValue 列表，返回不同的项
    public static <T extends Comparable<T>> Collection<Property.Value<T>> getDifferentValues(
            Collection<Property.Value<T>> valuesA,
            Collection<Property.Value<T>> valuesB
    ) {
        Set<Property.Value<T>> setB = new HashSet<>(valuesB);
        Collection<Property.Value<T>> diff = new ArrayList<>();

        for (Property.Value<T> v : valuesA) {
            if (!setB.contains(v)) {
                diff.add(v);
            }
        }

        Set<Property.Value<T>> setA = new HashSet<>(valuesA);
        for (Property.Value<T> v : valuesB) {
            if (!setA.contains(v)) {
                diff.add(v);
            }
        }

        return diff;
    }
}
