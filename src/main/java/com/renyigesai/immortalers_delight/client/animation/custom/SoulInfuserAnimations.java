package com.renyigesai.immortalers_delight.client.animation.custom;

import com.renyigesai.immortalers_delight.client.animation.custom.AnimationTrack;
import com.renyigesai.immortalers_delight.client.model.SoulInfuserModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.phys.Vec3;

public class SoulInfuserAnimations {

    public static final float DEG_TO_RAD = (float) (Math.PI / 180.0);

    // =========================================================================
    // 0~11 号部件标准“展开激活姿态”（在非觉醒状态下，这些部件处于展开位置）
    // =========================================================================
    public static void applyDefaultOpenParts(SoulInfuserModel<?> m) {
        // 外部四个角块 (upper_body_parts0~3) -> 散开至 +/- 11
        m.upper_body_parts3.setPos(11.0F, 0.0F, -11.0F);
        m.upper_body_parts2.setPos(-11.0F, 0.0F, 11.0F);
        m.upper_body_parts1.setPos(11.0F, 0.0F, 11.0F);
        m.upper_body_parts0.setPos(-11.0F, 0.0F, -11.0F);

        // 中间四个十字块 (upper_body_parts4~7) -> 散开至 +/- 8 且旋转 90 度
        m.upper_body_parts7.setPos(0.0F, 0.0F, -8.0F);
        m.upper_body_parts7.xRot = -90.0F * DEG_TO_RAD;

        m.upper_body_parts6.setPos(0.0F, 0.0F, 8.0F);
        m.upper_body_parts6.xRot = 90.0F * DEG_TO_RAD;

        m.upper_body_parts5.setPos(8.0F, 0.0F, 0.0F);
        m.upper_body_parts5.zRot = -90.0F * DEG_TO_RAD;

        m.upper_body_parts4.setPos(-8.0F, 0.0F, 0.0F);
        m.upper_body_parts4.zRot = 90.0F * DEG_TO_RAD;

        // 下层四个斜角块 (upper_body_parts8~11) -> 偏移 +/- 3 并倾斜 45/90 度
        m.upper_body_parts11.setPos(-3.0F, 0.0F, -3.0F);
        m.upper_body_parts11.xRot = -45.0F * DEG_TO_RAD;
        m.upper_body_parts11.zRot = 90.0F * DEG_TO_RAD;

        m.upper_body_parts10.setPos(3.0F, 0.0F, 3.0F);
        m.upper_body_parts10.xRot = 45.0F * DEG_TO_RAD;
        m.upper_body_parts10.zRot = -90.0F * DEG_TO_RAD;

        m.upper_body_parts9.setPos(3.0F, 0.0F, -3.0F);
        m.upper_body_parts9.xRot = -45.0F * DEG_TO_RAD;
        m.upper_body_parts9.zRot = -90.0F * DEG_TO_RAD;

        m.upper_body_parts8.setPos(-3.0F, 0.0F, 3.0F);
        m.upper_body_parts8.xRot = 45.0F * DEG_TO_RAD;
        m.upper_body_parts8.zRot = 90.0F * DEG_TO_RAD;

        // 底座
        m.base.setPos(0.0F, -4.0F, 0.0F);
        m.body.setPos(0.0F, -4.0F, 0.0F);
    }

    // =========================================================================
    // ID 0: AWAKEN (length: 4.0s -> 100 ticks, once) - 包含从闭合到弹开的全过程
    // =========================================================================
    public static class Awaken {
        public static final float LENGTH = 100.0F;

        public static final AnimationTrack ALL_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
                .catmull(0.0, 1, 1, 1).catmull(2.2, 0.9, 0.85, 0.9)
                .catmull(2.4, 1.15, 1.2, 1.15).catmull(3.2, 0.98, 0.98, 0.98).catmull(4.0, 1, 1, 1).build();

        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(1.0, 0, 0, 0).catmull(1.6, 0, 2, 0)
                .catmull(2.2, 0, 0.5, 0).catmull(2.5, 0, 18.5, 0).catmull(3.2, 0, 15.5, 0).catmull(4.0, 0, 16, 0).build();

        public static final AnimationTrack BODY_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, -0.5, 0).catmull(2.5, 0, -5.5, 0)
                .catmull(3.2, 0, -3.8, 0).catmull(4.0, 0, -4, 0).build();

        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 15, 0).catmull(2.5, 0, 120, 0).catmull(4.0, 0, 360, 0).build();
        public static final AnimationTrack RING_UP_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(0.25, 0.3, 0, -0.2).catmull(0.5, -0.3, 0, 0.3)
                .catmull(0.75, 0.4, 0, -0.3).catmull(1.0, 0, 0, 0).catmull(1.3, 0.2, 2.5, 0)
                .catmull(1.6, -0.2, 5.0, 0).catmull(1.9, 0.3, 4.2, 0).catmull(2.2, 0, 6.0, 0)
                .catmull(2.5, 0, 21.0, 0).catmull(3.2, 0, 17.5, 0).catmull(4.0, 0, 18, 0).build();

        public static final AnimationTrack RING_CENTER_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.4, 12, 45, 0).catmull(4.0, 6, 360, 0).build();
        public static final AnimationTrack RING_CENTER_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(0.25, -0.3, 0, 0.3).catmull(0.5, 0.3, 0, -0.2)
                .catmull(0.75, -0.4, 0, 0.4).catmull(1.0, 0, 0, 0).catmull(1.3, -0.2, 1.5, 0)
                .catmull(1.6, 0.2, 3.5, 0).catmull(1.9, -0.2, 2.8, 0).catmull(2.2, 0, 4.5, 0)
                .catmull(2.5, 0, 23.0, 0).catmull(3.2, 0, 19.5, 0).catmull(4.0, 0, 20, 0).build();

        public static final AnimationTrack RING_DOWN_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, -15, 0).catmull(2.5, 0, 30, 0).catmull(4.0, 0, -270, 0).build();
        public static final AnimationTrack RING_DOWN_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(0.25, 0.2, 0, 0.3).catmull(0.5, -0.2, 0, -0.3)
                .catmull(0.75, 0.3, 0, 0.2).catmull(1.0, 0, 0, 0).catmull(1.6, 0, 1.0, 0)
                .catmull(2.2, 0, 2.0, 0).catmull(2.5, 0, 12.0, 0).catmull(3.2, 0, 9.5, 0).catmull(4.0, 0, 10, 0).build();

        public static final AnimationTrack BASE_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .linear(0.0, 0, 0, 0).linear(2.2, 0, 0, 0).linear(2.28, 0, -4, 0).build();

        public static final AnimationTrack P3_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 14, 0, -14).catmull(3.2, 10.5, 0, -10.5).catmull(4.0, 11, 0, -11).build();
        public static final AnimationTrack P2_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -14, 0, 14).catmull(3.2, -10.5, 0, 10.5).catmull(4.0, -11, 0, 11).build();
        public static final AnimationTrack P1_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 14, 0, 14).catmull(3.2, 10.5, 0, 10.5).catmull(4.0, 11, 0, 11).build();
        public static final AnimationTrack P0_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -14, 0, -14).catmull(3.2, -10.5, 0, -10.5).catmull(4.0, -11, 0, -11).build();

        public static final AnimationTrack P7_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -100, 0, 0).catmull(4.0, -90, 0, 0).build();
        public static final AnimationTrack P7_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 0, 0, -10).catmull(3.2, 0, 0, -7.5).catmull(4.0, 0, 0, -8).build();
        public static final AnimationTrack P6_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 100, 0, 0).catmull(4.0, 90, 0, 0).build();
        public static final AnimationTrack P6_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 0, 0, 10).catmull(3.2, 0, 0, 7.5).catmull(4.0, 0, 0, 8).build();
        public static final AnimationTrack P5_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 0, 0, -100).catmull(4.0, 0, 0, -90).build();
        public static final AnimationTrack P5_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 10, 0, 0).catmull(3.2, 7.5, 0, 0).catmull(4.0, 8, 0, 0).build();
        public static final AnimationTrack P4_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 0, 0, 100).catmull(4.0, 0, 0, 90).build();
        public static final AnimationTrack P4_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -10, 0, 0).catmull(3.2, -7.5, 0, 0).catmull(4.0, -8, 0, 0).build();

        public static final AnimationTrack P11_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -50, 0, 100).catmull(4.0, -45, 0, 90).build();
        public static final AnimationTrack P11_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -4, 0, -4).catmull(4.0, -3, 0, -3).build();
        public static final AnimationTrack P10_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 50, 0, -100).catmull(4.0, 45, 0, -90).build();
        public static final AnimationTrack P10_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 4, 0, 4).catmull(4.0, 3, 0, 3).build();
        public static final AnimationTrack P9_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -50, 0, -100).catmull(4.0, -45, 0, -90).build();
        public static final AnimationTrack P9_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 4, 0, -4).catmull(4.0, 3, 0, -3).build();
        public static final AnimationTrack P8_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 50, 0, 100).catmull(4.0, 45, 0, 90).build();
        public static final AnimationTrack P8_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -4, 0, 4).catmull(4.0, -3, 0, 3).build();

        public static void apply(SoulInfuserModel<?> m, float ticks) {
            float t = Math.min(ticks, LENGTH);
            Vec3 scale = ALL_SCALE.get(t);
            m.all.xScale = (float) scale.x; m.all.yScale = (float) scale.y; m.all.zScale = (float) scale.z;

            Vec3 hp = HEAD_POS.get(t);
            m.head.setPos((float) hp.x, (float) hp.y, (float) hp.z);

            Vec3 bp = BASE_POS.get(t);
            m.base.setPos((float) bp.x, (float) bp.y, (float) bp.z);
            Vec3 bodyP = BODY_POS.get(t);
            m.body.setPos((float) bodyP.x, (float) bodyP.y, (float) bodyP.z);

            Vec3 rur = RING_UP_ROT.get(t); Vec3 rup = RING_UP_POS.get(t);
            m.ring_up.yRot = (float) rur.y * DEG_TO_RAD;
            m.ring_up.setPos((float) rup.x, (float) rup.y, (float) rup.z);

            Vec3 rcr = RING_CENTER_ROT.get(t); Vec3 rcp = RING_CENTER_POS.get(t);
            m.ring_center.xRot = (float) rcr.x * DEG_TO_RAD; m.ring_center.yRot = (float) rcr.y * DEG_TO_RAD;
            m.ring_center.setPos((float) rcp.x, (float) rcp.y, (float) rcp.z);

            Vec3 rdr = RING_DOWN_ROT.get(t); Vec3 rdp = RING_DOWN_POS.get(t);
            m.ring_down.yRot = (float) rdr.y * DEG_TO_RAD;
            m.ring_down.setPos((float) rdp.x, (float) rdp.y, (float) rdp.z);

            // 直接赋值 Blockbench 关键帧给定的绝对偏移坐标
            setPos(m.upper_body_parts3, P3_POS.get(t));
            setPos(m.upper_body_parts2, P2_POS.get(t));
            setPos(m.upper_body_parts1, P1_POS.get(t));
            setPos(m.upper_body_parts0, P0_POS.get(t));

            setPosRot(m.upper_body_parts7, P7_POS.get(t), P7_ROT.get(t));
            setPosRot(m.upper_body_parts6, P6_POS.get(t), P6_ROT.get(t));
            setPosRot(m.upper_body_parts5, P5_POS.get(t), P5_ROT.get(t));
            setPosRot(m.upper_body_parts4, P4_POS.get(t), P4_ROT.get(t));

            setPosRot(m.upper_body_parts11, P11_POS.get(t), P11_ROT.get(t));
            setPosRot(m.upper_body_parts10, P10_POS.get(t), P10_ROT.get(t));
            setPosRot(m.upper_body_parts9, P9_POS.get(t), P9_ROT.get(t));
            setPosRot(m.upper_body_parts8, P8_POS.get(t), P8_ROT.get(t));
        }
    }

    /**
     * 忠实于 Blockbench modded_entity (flip_y = true) 规则应用位置与旋转：
     * 1. X 保持原值
     * 2. Y 轴必须取反：在 Blockbench 中 +Y 是向上，而在 Minecraft 中 +Y 是向下！
     * 3. Z 保持原值
     */
    public static void applyBone(ModelPart part, float defaultPivotX, float defaultPivotY, float defaultPivotZ,
                                 Vec3 animPos, Vec3 animRot, Vec3 animScale) {
        if (part == null) return;

        // 位置映射：基准点 + 动画偏移 (注意 Y 轴取反)
        if (animPos != null) {
            part.setPos(
                    defaultPivotX + (float) animPos.x,
                    defaultPivotY - (float) animPos.y, // 关键：减去 Y，让它向上飘而不是掉入地底！
                    defaultPivotZ + (float) animPos.z
            );
        } else {
            part.setPos(defaultPivotX, defaultPivotY, defaultPivotZ);
        }

        // 旋转映射 (Blockbench 度数转弧度，X/Y 轴方向依导出规则)
        if (animRot != null) {
            part.xRot = (float) animRot.x * DEG_TO_RAD;
            part.yRot = (float) animRot.y * DEG_TO_RAD;
            part.zRot = (float) animRot.z * DEG_TO_RAD;
        } else {
            part.xRot = 0.0F;
            part.yRot = 0.0F;
            part.zRot = 0.0F;
        }

        // 缩放映射
        if (animScale != null) {
            part.xScale = (float) animScale.x;
            part.yScale = (float) animScale.y;
            part.zScale = (float) animScale.z;
        } else {
            part.xScale = 1.0F;
            part.yScale = 1.0F;
            part.zScale = 1.0F;
        }
    }
    // =========================================================================
    // ID 1: IDLE (length: 2.0s -> 50 ticks, loop)
    // =========================================================================

    public static class Idle {
        public static final float LENGTH = 50.0F; // 2.0s * 25 ticks/s = 50 ticks

        // ---------------------------------------------------------------------
        // 关键帧轨道定义（100% 对应 JSON 原文）
        // ---------------------------------------------------------------------

        // all: position (catmullrom)
        public static final AnimationTrack ALL_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0)
                .catmull(0.5, 0, 2.5, 0)
                .catmull(1.0, 0, 0, 0)
                .catmull(1.5, 0, -2.5, 0)
                .catmull(2.0, 0, 0, 0)
                .build();

        // head: position & rotation (catmullrom)
        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(new Vec3(0, 16, 0))
                .catmull(0.0, 0, 16, 0)
                .catmull(0.5, 0, 18.0, 0)
                .catmull(1.0, 0, 16, 0)
                .catmull(1.5, 0, 14.0, 0)
                .catmull(2.0, 0, 16, 0)
                .build();

        public static final AnimationTrack HEAD_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 2, 0, 0)
                .catmull(0.5, 0, 0, 2)
                .catmull(1.0, -2, 0, 0)
                .catmull(1.5, 0, 0, -2)
                .catmull(2.0, 2, 0, 0)
                .build();

        // ring_up: rotation (linear, 0 -> 360)
        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .linear(0.0, 0, 0, 0)
                .linear(2.0, 0, 360, 0)
                .build();

        // ring_down: rotation (linear, 90 -> -270)
        public static final AnimationTrack RING_DOWN_ROT = new AnimationTrack.Builder(new Vec3(0, 90, 0))
                .linear(0.0, 0, 90, 0)
                .linear(2.0, 0, -270, 0)
                .build();

        // ring_center: position & rotation (linear)
        public static final AnimationTrack RING_CENTER_POS = new AnimationTrack.Builder(new Vec3(0, 20, 0))
                .linear(0.0, 0, 20, 0)
                .linear(0.48, 0, 21.04, 0)
                .linear(1.0, 0, 22, 0)
                .linear(1.48, 0, 21.14, 0)
                .linear(2.0, 0, 20, 0)
                .build();

        public static final AnimationTrack RING_CENTER_ROT = new AnimationTrack.Builder(new Vec3(6, 0, 0))
                .linear(0.0, 6, 0, 0)
                .linear(0.5, 0, 90, 6)
                .linear(1.0, -6, 180, 0)
                .linear(1.5, 0, 270, -6)
                .linear(2.0, 6, 360, 0)
                .build();

        // ---------------------------------------------------------------------
        // 动画应用逻辑
        // ---------------------------------------------------------------------
        public static void apply(SoulInfuserModel<?> m, float ticks) {
            float t = ticks % LENGTH;

            // 1. all
            Vec3 ap = ALL_POS.get(t);
            m.all.setPos((float) ap.x, (float) ap.y, (float) ap.z);

            // 2. head
            Vec3 hp = HEAD_POS.get(t);
            Vec3 hr = HEAD_ROT.get(t);
            m.head.setPos((float) hp.x, (float) hp.y, (float) hp.z);
            m.head.xRot = (float) hr.x * DEG_TO_RAD;
            m.head.yRot = (float) hr.y * DEG_TO_RAD;
            m.head.zRot = (float) hr.z * DEG_TO_RAD;

            // 3. ring_up: position 原文为 (0, 18, 0)
            m.ring_up.setPos(0.0F, 18.0F, 0.0F);
            m.ring_up.xRot = 0.0F;
            m.ring_up.yRot = (float) RING_UP_ROT.get(t).y * DEG_TO_RAD;
            m.ring_up.zRot = 0.0F;

            // 4. ring_down: position 原文为 (0, 10, 0)
            m.ring_down.setPos(0.0F, 10.0F, 0.0F);
            m.ring_down.xRot = 0.0F;
            m.ring_down.yRot = (float) RING_DOWN_ROT.get(t).y * DEG_TO_RAD;
            m.ring_down.zRot = 0.0F;

            // 5. ring_center
            Vec3 rcp = RING_CENTER_POS.get(t);
            Vec3 rcr = RING_CENTER_ROT.get(t);
            m.ring_center.setPos((float) rcp.x, (float) rcp.y, (float) rcp.z);
            m.ring_center.xRot = (float) rcr.x * DEG_TO_RAD;
            m.ring_center.yRot = (float) rcr.y * DEG_TO_RAD;
            m.ring_center.zRot = (float) rcr.z * DEG_TO_RAD;

            // 6. body & base: position 原文均为 (0, -4, 0)
            m.body.setPos(0.0F, -4.0F, 0.0F);
            m.base.setPos(0.0F, -4.0F, 0.0F);

            // 7. upper_body_parts0 ~ 3: 只有 position，严禁设置任何旋转！
            m.upper_body_parts0.setPos(-11.0F, 0.0F, -11.0F);
            m.upper_body_parts0.xRot = 0.0F; m.upper_body_parts0.yRot = 0.0F; m.upper_body_parts0.zRot = 0.0F;

            m.upper_body_parts1.setPos(11.0F, 0.0F, 11.0F);
            m.upper_body_parts1.xRot = 0.0F; m.upper_body_parts1.yRot = 0.0F; m.upper_body_parts1.zRot = 0.0F;

            m.upper_body_parts2.setPos(-11.0F, 0.0F, 11.0F);
            m.upper_body_parts2.xRot = 0.0F; m.upper_body_parts2.yRot = 0.0F; m.upper_body_parts2.zRot = 0.0F;

            m.upper_body_parts3.setPos(11.0F, 0.0F, -11.0F);
            m.upper_body_parts3.xRot = 0.0F; m.upper_body_parts3.yRot = 0.0F; m.upper_body_parts3.zRot = 0.0F;

            // 8. upper_body_parts4 ~ 7: 十字方向
            m.upper_body_parts4.setPos(-8.0F, 0.0F, 0.0F);
            m.upper_body_parts4.xRot = 0.0F;
            m.upper_body_parts4.yRot = 0.0F;
            m.upper_body_parts4.zRot = 90.0F * DEG_TO_RAD;

            m.upper_body_parts5.setPos(8.0F, 0.0F, 0.0F);
            m.upper_body_parts5.xRot = 0.0F;
            m.upper_body_parts5.yRot = 0.0F;
            m.upper_body_parts5.zRot = -90.0F * DEG_TO_RAD;

            m.upper_body_parts6.setPos(0.0F, 0.0F, 8.0F);
            m.upper_body_parts6.xRot = 90.0F * DEG_TO_RAD;
            m.upper_body_parts6.yRot = 0.0F;
            m.upper_body_parts6.zRot = 0.0F;

            m.upper_body_parts7.setPos(0.0F, 0.0F, -8.0F);
            m.upper_body_parts7.xRot = -90.0F * DEG_TO_RAD;
            m.upper_body_parts7.yRot = 0.0F;
            m.upper_body_parts7.zRot = 0.0F;

            // 9. upper_body_parts8 ~ 11: 倾斜四角
            m.upper_body_parts8.setPos(-3.0F, 0.0F, 3.0F);
            m.upper_body_parts8.xRot = 45.0F * DEG_TO_RAD;
            m.upper_body_parts8.yRot = 0.0F;
            m.upper_body_parts8.zRot = 90.0F * DEG_TO_RAD;

            m.upper_body_parts9.setPos(3.0F, 0.0F, -3.0F);
            m.upper_body_parts9.xRot = -45.0F * DEG_TO_RAD;
            m.upper_body_parts9.yRot = 0.0F;
            m.upper_body_parts9.zRot = -90.0F * DEG_TO_RAD;

            m.upper_body_parts10.setPos(3.0F, 0.0F, 3.0F);
            m.upper_body_parts10.xRot = 45.0F * DEG_TO_RAD;
            m.upper_body_parts10.yRot = 0.0F;
            m.upper_body_parts10.zRot = -90.0F * DEG_TO_RAD;

            m.upper_body_parts11.setPos(-3.0F, 0.0F, -3.0F);
            m.upper_body_parts11.xRot = -45.0F * DEG_TO_RAD;
            m.upper_body_parts11.yRot = 0.0F;
            m.upper_body_parts11.zRot = 90.0F * DEG_TO_RAD;
        }
    }
//    public static class Idle {
//        public static final float LENGTH = 50.0F;
//
//        public static final AnimationTrack ALL_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.5, 0, 2.5, 0).catmull(1.0, 0, 0, 0).catmull(1.5, 0, -2.5, 0).catmull(2.0, 0, 0, 0).build();
//
//        public static final AnimationTrack HEAD_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 2, 0, 0).catmull(0.5, 0, 0, 2).catmull(1.0, -2, 0, 0).catmull(1.5, 0, 0, -2).catmull(2.0, 2, 0, 0).build();
//        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(new Vec3(0, 16, 0))
//                .catmull(0.0, 0, 16, 0).catmull(0.5, 0, 18.0, 0).catmull(1.0, 0, 16, 0).catmull(1.5, 0, 14.0, 0).catmull(2.0, 0, 16, 0).build();
//        public static final AnimationTrack RING_UP_POS = new AnimationTrack.Builder(new Vec3(0, 18, 0))
//                .linear(0.0, 0, 18, 0).linear(1.0, 0, 18, 0).build();
//
//        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .linear(0.0, 0, 0, 0).linear(2.0, 0, 360, 0).build();
//        public static final AnimationTrack RING_DOWN_POS = new AnimationTrack.Builder(new Vec3(0, 18, 0))
//                .linear(0.0, 0, 10, 0).linear(1.0, 0, 10, 0).build();
//
//        public static final AnimationTrack RING_DOWN_ROT = new AnimationTrack.Builder(new Vec3(0, 90, 0))
//                .linear(0.0, 0, 90, 0).linear(2.0, 0, -270, 0).build();
//
//        public static final AnimationTrack RING_CENTER_ROT = new AnimationTrack.Builder(new Vec3(6, 0, 0))
//                .linear(0.0, 6, 0, 0).linear(0.5, 0, 90, 6).linear(1.0, -6, 180, 0).linear(1.5, 0, 270, -6).linear(2.0, 6, 360, 0).build();
//        public static final AnimationTrack RING_CENTER_POS = new AnimationTrack.Builder(new Vec3(0, 20, 0))
//                .linear(0.0, 0, 20, 0).linear(0.48, 0, 21.04, 0).linear(1.0, 0, 22, 0).linear(1.48, 0, 21.14, 0).linear(2.0, 0, 20, 0).build();
//
//        public static void apply(SoulInfuserModel<?> m, float ticks) {
//            // 先散开外壳
//            applyDefaultOpenParts(m);
//
//            float t = ticks % LENGTH;
//
//            // all (根部): 动画中 y 在 [-2.5, 2.5] 波动，Y 取反后向上漂浮
//            Vec3 ap = ALL_POS.get(t);
//            applyBone(m.all, 0.0F, 24.0F, 0.0F, ap, null, null);
//
//            // head: JSON 中 y 从 14 到 18 波动，取反后位于上方 16 像素处
//            Vec3 hp = HEAD_POS.get(t);
//            Vec3 hr = HEAD_ROT.get(t);
//            applyBone(m.head, 0.0F, 9.0F, 0.0F, hp, hr, null);
//
//            // ring_up: 动画中 y = 18，取反后正确处于上半身悬空位置
//            Vec3 rup = RING_UP_POS.get(t);
//            Vec3 rur = RING_UP_ROT.get(t);
//            applyBone(m.ring_up, 0.0F, 10.0F, 0.0F, rup, rur, null);
//
//            // ring_down: 动画中 y = 10，取反后处于下半身悬空位置
//            Vec3 rdp = RING_DOWN_POS.get(t);
//            Vec3 rdr = RING_DOWN_ROT.get(t);
//            applyBone(m.ring_down, 0.0F, 2.0F, 0.0F, rdp, rdr, null);
//
//            // ring_center: 动画中 y 在 [20, 22] 波动
//            Vec3 rcp = RING_CENTER_POS.get(t);
//            Vec3 rcr = RING_CENTER_ROT.get(t);
//            applyBone(m.ring_center, 0.0F, 2.0F, 0.0F, rcp, rcr, null);
//        }
////        public static void apply(SoulInfuserModel<?> m, float ticks) {
////            // 确保 12 个外壳部件散开！
////            applyDefaultOpenParts(m);
////
////            float t = ticks % LENGTH;
////            Vec3 ap = ALL_POS.get(t);
////            m.all.setPos((float) ap.x, (float) ap.y, (float) ap.z);
////
////            Vec3 hr = HEAD_ROT.get(t); Vec3 hp = HEAD_POS.get(t);
////            m.head.xRot = (float) hr.x * DEG_TO_RAD; m.head.yRot = (float) hr.y * DEG_TO_RAD; m.head.zRot = (float) hr.z * DEG_TO_RAD;
////            m.head.setPos((float) hp.x, (float) hp.y, (float) hp.z);
////
////            m.ring_up.yRot = (float) RING_UP_ROT.get(t).y * DEG_TO_RAD;
////            m.ring_up.setPos(0.0F, 18.0F, 0.0F);
////
////            m.ring_down.yRot = (float) RING_DOWN_ROT.get(t).y * DEG_TO_RAD;
////            m.ring_down.setPos(0.0F, 10.0F, 0.0F);
////
////            Vec3 rcr = RING_CENTER_ROT.get(t); Vec3 rcp = RING_CENTER_POS.get(t);
////            m.ring_center.xRot = (float) rcr.x * DEG_TO_RAD; m.ring_center.yRot = (float) rcr.y * DEG_TO_RAD; m.ring_center.zRot = (float) rcr.z * DEG_TO_RAD;
////            m.ring_center.setPos((float) rcp.x, (float) rcp.y, (float) rcp.z);
////        }
//    }

    // =========================================================================
    // ID 2: ATTACK_FIRE_RAIN (length: 1.6s -> 40 ticks, loop)
    // =========================================================================
    public static class AttackFireRain {
        public static final float LENGTH = 40.0F;

        public static final AnimationTrack ALL_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 12, 0, 0).catmull(0.8, 8, 0, 0).catmull(1.6, 12, 0, 0).build();
        public static final AnimationTrack ALL_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(0.8, 0, 2.0, 0).catmull(1.6, 0, 0, 0).build();

        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(new Vec3(0, 16, -1.5))
                .catmull(0.0, 0, 16, -1.5).catmull(0.8, 0, 17.5, -1.0).catmull(1.32, 0, 18.5, -0.5).catmull(1.44, 0, 13, -1.5).catmull(1.6, 0, 16, -1.5).build();

        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .linear(0.0, 0, 0, 0).linear(1.6, 0, 540, 0).build();
        public static final AnimationTrack RING_DOWN_ROT = new AnimationTrack.Builder(new Vec3(0, 90, 0))
                .linear(0.0, 0, 90, 0).linear(1.6, 0, -450, 0).build();
        public static final AnimationTrack RING_CENTER_ROT = new AnimationTrack.Builder(new Vec3(10, 0, 0))
                .linear(0.0, 10, 0, 0).linear(1.6, 10, 360, 0).build();

        public static void apply(SoulInfuserModel<?> m, float ticks) {
            applyDefaultOpenParts(m);

            float t = ticks % LENGTH;
            Vec3 ar = ALL_ROT.get(t); Vec3 ap = ALL_POS.get(t);
            m.all.xRot = (float) ar.x * DEG_TO_RAD;
            m.all.setPos((float) ap.x, (float) ap.y, (float) ap.z);

            Vec3 hp = HEAD_POS.get(t);
            m.head.setPos((float) hp.x, (float) hp.y, (float) hp.z);

            m.ring_up.yRot = (float) RING_UP_ROT.get(t).y * DEG_TO_RAD;
            m.ring_up.setPos(0.0F, 18.0F, -2.0F);

            m.ring_down.yRot = (float) RING_DOWN_ROT.get(t).y * DEG_TO_RAD;
            m.ring_down.setPos(0.0F, 10.0F, 1.0F);

            Vec3 rcr = RING_CENTER_ROT.get(t);
            m.ring_center.xRot = (float) rcr.x * DEG_TO_RAD; m.ring_center.yRot = (float) rcr.y * DEG_TO_RAD;
            m.ring_center.setPos(0.0F, 20.0F, -1.0F);
        }
    }

    // =========================================================================
    // ID 3: ATTACK_EXPLODE (length: 4.0s -> 100 ticks, once)
    // =========================================================================
    public static class AttackExplode {
        public static final float LENGTH = 100.0F;

        public static final AnimationTrack ALL_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(1.2, 0, 4, 0).catmull(2.6, 0, 6, 0).catmull(2.76, 0, -2, 0).catmull(3.36, 0, -2.19, 0).catmull(4.0, 0, 0, 0).build();

        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(new Vec3(0, 16, 0))
                .catmull(0.0, 0, 16, 0).catmull(1.2, 0, 22, 0).catmull(2.6, 0, 24, 0).catmull(2.76, 0, 10, 0).catmull(3.36, 0, 10.12, 0).catmull(4.0, 0, 16, 0).build();
        public static final AnimationTrack HEAD_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
                .catmull(0.0, 1, 1, 1).catmull(2.6, 1.15, 1.25, 1.15).catmull(2.76, 0.9, 0.85, 0.9).catmull(3.36, 0.9, 0.85, 0.9).catmull(4.0, 1, 1, 1).build();

        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(1.2, 35, 180, 0).catmull(2.6, 35, 1080, 0).catmull(2.76, 0, 1120, 0).catmull(3.36, -2.36, 1179.36, 0).catmull(4.0, 0, 1440, 0).build();
        public static final AnimationTrack RING_UP_POS = new AnimationTrack.Builder(new Vec3(0, 18, 0))
                .catmull(0.0, 0, 18, 0).catmull(1.2, 0, 22, 0).catmull(2.76, 0, 14, 0).catmull(3.36, 0, 14.17, 0).catmull(4.0, 0, 18, 0).build();

        public static final AnimationTrack RING_CENTER_POS = new AnimationTrack.Builder(new Vec3(0, 20, 0))
                .catmull(0.0, 0, 20, 0).catmull(1.2, 0, 18, 0).catmull(2.76, 0, 10, 0).catmull(3.36, 0, 11.23, 0).catmull(4.0, 0, 20, 0).build();
        public static final AnimationTrack RING_CENTER_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
                .catmull(0.0, 1, 1, 1).catmull(1.2, 1.3, 1.3, 1.3).catmull(2.6, 1.6, 1.6, 1.6).catmull(2.76, 0.9, 0.9, 0.9).catmull(3.36, 0.87, 0.87, 0.87).catmull(4.0, 1, 1, 1).build();

        public static final AnimationTrack RING_DOWN_ROT = new AnimationTrack.Builder(new Vec3(0, 90, 0))
                .catmull(0.0, 0, 90, 0).catmull(1.2, -35, -90, 0).catmull(2.6, -35, -990, 0).catmull(2.76, 0, -1020, 0).catmull(3.36, 2.36, -1080.45, 0).catmull(4.0, 0, -1350, 0).build();
        public static final AnimationTrack RING_DOWN_POS = new AnimationTrack.Builder(new Vec3(0, 10, 0))
                .catmull(0.0, 0, 10, 0).catmull(1.2, 0, 14, 0).catmull(1.8, 0, 18, 0).catmull(2.4, 0, 14, 0).catmull(2.76, 0, 6, 0).catmull(3.36, 0, 6.17, 0).catmull(4.0, 0, 10, 0).build();

        public static void apply(SoulInfuserModel<?> m, float ticks) {
            applyDefaultOpenParts(m);

            float t = Math.min(ticks, LENGTH);
            Vec3 ap = ALL_POS.get(t);
            m.all.setPos((float) ap.x, (float) ap.y, (float) ap.z);

            Vec3 hp = HEAD_POS.get(t); Vec3 hs = HEAD_SCALE.get(t);
            m.head.setPos((float) hp.x, (float) hp.y, (float) hp.z);
            m.head.xScale = (float) hs.x; m.head.yScale = (float) hs.y; m.head.zScale = (float) hs.z;

            Vec3 rur = RING_UP_ROT.get(t); Vec3 rup = RING_UP_POS.get(t);
            m.ring_up.xRot = (float) rur.x * DEG_TO_RAD; m.ring_up.yRot = (float) rur.y * DEG_TO_RAD;
            m.ring_up.setPos((float) rup.x, (float) rup.y, (float) rup.z);

            Vec3 rcp = RING_CENTER_POS.get(t); Vec3 rcs = RING_CENTER_SCALE.get(t);
            m.ring_center.setPos((float) rcp.x, (float) rcp.y, (float) rcp.z);
            m.ring_center.xScale = (float) rcs.x; m.ring_center.yScale = (float) rcs.y; m.ring_center.zScale = (float) rcs.z;

            Vec3 rdr = RING_DOWN_ROT.get(t); Vec3 rdp = RING_DOWN_POS.get(t);
            m.ring_down.xRot = (float) rdr.x * DEG_TO_RAD; m.ring_down.yRot = (float) rdr.y * DEG_TO_RAD;
            m.ring_down.setPos((float) rdp.x, (float) rdp.y, (float) rdp.z);
        }
    }

    // =========================================================================
    // ID 4: ATTACK_COLUMN (length: 2.4s -> 60 ticks, once)
    // =========================================================================
    public static class AttackColumn {
        public static final float LENGTH = 60.0F;

        public static final AnimationTrack ALL_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(0.88, 0, 0, 0).catmull(1.32, 0, 10, 0).catmull(1.52, 0, -6, 0).catmull(1.68, 0, -2, 0).catmull(2.0, 0, 1, 0).catmull(2.4, 0, 0, 0).build();

        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(new Vec3(0, 16, 0))
                .linear(0.0, 0, 16, 0).catmull(0.24, 0, 13, 0).catmull(0.48, 0, 12, 0).catmull(0.72, 0, 12, 0)
                .catmull(0.88, 0, 16, 0).catmull(1.32, 0, 20, 0).linear(1.52, 0, 6, 0).catmull(1.68, 0, 12, 0).catmull(2.4, 0, 16, 0).build();
        public static final AnimationTrack HEAD_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
                .catmull(0.0, 1, 1, 1).catmull(0.88, 1, 1, 1).catmull(1.32, 0.9, 1.2, 0.9).linear(1.52, 1.35, 0.65, 1.35).catmull(1.72, 0.95, 1.05, 0.95).catmull(2.4, 1, 1, 1).build();

        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .linear(0.0, 0, 0, 0).linear(0.2, 0, 84, 0).linear(0.44, 0, 180, 0).linear(0.8, 0, 276, 0).linear(1.2, 0, 360, 0).linear(1.8, 0, 360, 0).build();
        public static final AnimationTrack RING_UP_POS = new AnimationTrack.Builder(new Vec3(0, 18, 0))
                .linear(0.0, 0, 18, 0).linear(1.32, 0, 18, 0).linear(1.56, 0, 0, 0).linear(1.8, 0, 0, 0).linear(2.16, 0, 20, 0).linear(2.24, 0, 19.6, 0).linear(2.4, 0, 18, 0).build();

        public static final AnimationTrack RING_CENTER_POS = new AnimationTrack.Builder(new Vec3(0, 20, 0))
                .catmull(0.0, 0, 20, 0).catmull(0.88, 0, 20, 0).catmull(1.32, 0, 24, 0).catmull(1.56, 0, 2, 0).catmull(2.0, 0, 18, 0).catmull(2.4, 0, 20, 0).build();
        public static final AnimationTrack RING_CENTER_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
                .catmull(0.0, 1, 1, 1).catmull(0.88, 1, 1, 1).catmull(1.32, 0.8, 0.8, 0.8).catmull(1.56, 1.6, 1.0, 1.6).catmull(2.0, 1.05, 1.0, 1.05).catmull(2.4, 1, 1, 1).build();

        public static final AnimationTrack RING_DOWN_POS = new AnimationTrack.Builder(new Vec3(0, 10, 0))
                .catmull(0.0, 0, 10, 0).catmull(0.88, 0, 10, 0).catmull(1.32, 0, 14, 0).linear(1.52, 0, -2, 0).catmull(2.4, 0, 10, 0).build();
        public static final AnimationTrack RING_DOWN_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
                .catmull(0.0, 1, 1, 1).catmull(0.88, 1, 1, 1).catmull(1.32, 0.85, 0.85, 0.85).catmull(1.56, 1.5, 0.8, 1.5).catmull(2.4, 1, 1, 1).build();

        public static void apply(SoulInfuserModel<?> m, float ticks) {
            applyDefaultOpenParts(m);

            float t = Math.min(ticks, LENGTH);
            Vec3 ap = ALL_POS.get(t);
            m.all.setPos((float) ap.x, (float) ap.y, (float) ap.z);

            Vec3 hp = HEAD_POS.get(t); Vec3 hs = HEAD_SCALE.get(t);
            m.head.setPos((float) hp.x, (float) hp.y, (float) hp.z);
            m.head.xScale = (float) hs.x; m.head.yScale = (float) hs.y; m.head.zScale = (float) hs.z;

            m.ring_up.yRot = (float) RING_UP_ROT.get(t).y * DEG_TO_RAD;
            m.ring_up.setPos(0.0F, (float) RING_UP_POS.get(t).y, 0.0F);

            Vec3 rcp = RING_CENTER_POS.get(t); Vec3 rcs = RING_CENTER_SCALE.get(t);
            m.ring_center.setPos((float) rcp.x, (float) rcp.y, (float) rcp.z);
            m.ring_center.xScale = (float) rcs.x; m.ring_center.yScale = (float) rcs.y; m.ring_center.zScale = (float) rcs.z;

            Vec3 rdp = RING_DOWN_POS.get(t); Vec3 rds = RING_DOWN_SCALE.get(t);
            m.ring_down.setPos((float) rdp.x, (float) rdp.y, (float) rdp.z);
            m.ring_down.xScale = (float) rds.x; m.ring_down.yScale = (float) rds.y; m.ring_down.zScale = (float) rds.z;
        }
    }

    // =========================================================================
    // ID 5: ATTACK_SUMMON (length: 2.6s -> 65 ticks, loop)
    // 带有上下 12 个外壳部件收放动作
    // =========================================================================
    public static class AttackSummon {
        public static final float LENGTH = 65.0F;

        public static final AnimationTrack ALL_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(0.2, 0, -2, 0).catmull(0.4, 0, 0, 0).catmull(0.6, 0, 2, 0).catmull(0.8, 0, 0, 0)
                .catmull(1.32, 0, 2.5, 0).catmull(1.64, 0, 0, 0).catmull(1.96, 0, -2.5, 0).catmull(2.28, 0, 0, 0).catmull(2.6, 0, 0, 0).build();

        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(new Vec3(0, 16, 0))
                .catmull(0.0, 0, 16, 0).catmull(0.12, 0, 14, 0).catmull(0.36, 0, 11.78, 0).catmull(0.72, 0, 10, 0).catmull(0.88, 0, 10, 0)
                .catmull(1.0, 0, 16, 0).catmull(1.32, 0, 18, 0).catmull(1.64, 0, 16, 0).catmull(1.96, 0, 14, 0).catmull(2.28, 0, 16, 0).catmull(2.6, 0, 16, 0).build();
        public static final AnimationTrack HEAD_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 2, 0, 0).catmull(1.0, 2, 0, 0).catmull(1.16, 0, 0, 2).catmull(1.36, -2, 0, 0).catmull(1.6, 0, 0, -2).catmull(1.88, 2, 0, 0).catmull(1.96, 2, 0, 0).build();

        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .linear(0.0, 0, 0, 0).linear(1.0, 0, 0, 0).linear(1.16, 0, 90, 0).linear(1.32, 0, 180, 0).linear(1.52, 0, 270, 0).linear(1.96, 0, 360, 0).linear(2.12, 0, 360, 0).linear(2.6, 0, 450, 0).build();
        public static final AnimationTrack RING_UP_POS = new AnimationTrack.Builder(new Vec3(0, 18, 0))
                .linear(0.0, 0, 18, 0).linear(0.12, 0, 14.67, 0).linear(0.36, 0, 11.33, 0).linear(0.72, 0, 8, 0).linear(0.88, 0, 8, 0).linear(1.0, 0, 18, 0).build();

        // 召唤时，十字外壳（4~7）收缩与张开
        public static final AnimationTrack P7_POS = new AnimationTrack.Builder(new Vec3(0, 0, -8))
                .linear(0.0, 0, 0, -8).linear(0.16, 0, 0, -12).linear(0.36, 0, 0, -14.67).linear(0.48, 0, 0, -16)
                .linear(0.6, 0, 0, -5).linear(0.96, 0, 0, -6).linear(1.2, 0, 0, -7).linear(1.32, 0, 0, -8).linear(1.96, 0, 0, -12)
                .linear(2.04, 0, 0, -12).linear(2.12, 0, 0, -6).linear(2.28, 0, 0, -7).linear(2.44, 0, 0, -7.67).linear(2.6, 0, 0, -8).build();
        public static final AnimationTrack P6_POS = new AnimationTrack.Builder(new Vec3(0, 0, 8))
                .linear(0.0, 0, 0, 8).linear(0.16, 0, 0, 12).linear(0.36, 0, 0, 14.67).linear(0.48, 0, 0, 16)
                .linear(0.6, 0, 0, 5).linear(0.96, 0, 0, 6).linear(1.2, 0, 0, 7).linear(1.32, 0, 0, 8).linear(1.96, 0, 0, 12)
                .linear(2.04, 0, 0, 12).linear(2.12, 0, 0, 6).linear(2.28, 0, 0, 7).linear(2.44, 0, 0, 7.67).linear(2.6, 0, 0, 8).build();
        public static final AnimationTrack P5_POS = new AnimationTrack.Builder(new Vec3(8, 0, 0))
                .linear(0.0, 8, 0, 0).linear(0.16, 12, 0, 0).linear(0.36, 14.67, 0, 0).linear(0.48, 16, 0, 0)
                .linear(0.6, 5, 0, 0).linear(0.96, 6, 0, 0).linear(1.2, 7, 0, 0).linear(1.32, 8, 0, 0).linear(1.96, 12, 0, 0)
                .linear(2.04, 12, 0, 0).linear(2.12, 6, 0, 0).linear(2.28, 7, 0, 0).linear(2.44, 7.67, 0, 0).linear(2.6, 8, 0, 0).build();
        public static final AnimationTrack P4_POS = new AnimationTrack.Builder(new Vec3(-8, 0, 0))
                .linear(0.0, -8, 0, 0).linear(0.16, -12, 0, 0).linear(0.36, -14.67, 0, 0).linear(0.48, -16, 0, 0)
                .linear(0.6, -5, 0, 0).linear(0.96, -6, 0, 0).linear(1.2, -7, 0, 0).linear(1.32, -8, 0, 0).linear(1.96, -12, 0, 0)
                .linear(2.04, -12, 0, 0).linear(2.12, -6, 0, 0).linear(2.28, -7, 0, 0).linear(2.44, -7.67, 0, 0).linear(2.6, -8, 0, 0).build();

        public static void apply(SoulInfuserModel<?> m, float ticks) {
            applyDefaultOpenParts(m);

            float t = ticks % LENGTH;
            Vec3 ap = ALL_POS.get(t);
            m.all.setPos((float) ap.x, (float) ap.y, (float) ap.z);

            Vec3 hp = HEAD_POS.get(t); Vec3 hr = HEAD_ROT.get(t);
            m.head.setPos((float) hp.x, (float) hp.y, (float) hp.z);
            m.head.xRot = (float) hr.x * DEG_TO_RAD; m.head.yRot = (float) hr.y * DEG_TO_RAD; m.head.zRot = (float) hr.z * DEG_TO_RAD;

            m.ring_up.yRot = (float) RING_UP_ROT.get(t).y * DEG_TO_RAD;
            m.ring_up.setPos(0.0F, (float) RING_UP_POS.get(t).y, 0.0F);

            // 动态驱动 4~7 号散开与收缩
            setPos(m.upper_body_parts7, P7_POS.get(t));
            setPos(m.upper_body_parts6, P6_POS.get(t));
            setPos(m.upper_body_parts5, P5_POS.get(t));
            setPos(m.upper_body_parts4, P4_POS.get(t));
        }
    }

    // =========================================================================
    // ID 6: ATTACK_FIRE_RING (length: 1.6s -> 40 ticks, once)
    // =========================================================================
    public static class AttackFireRing {
        public static final float LENGTH = 40.0F;

        public static final AnimationTrack ALL_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(0.52, 0, 10, 0).linear(0.72, 0, -6, 0).catmull(0.88, 0, -2, 0).catmull(1.24, 0, 1, 0).catmull(1.6, 0, 0, 0).build();

        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(new Vec3(0, 16, 0))
                .catmull(0.0, 0, 16, 0).catmull(0.52, 0, 21, 0).linear(0.72, 0, 6, 0).catmull(0.88, 0, 12, 0).catmull(1.6, 0, 16, 0).build();
        public static final AnimationTrack HEAD_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
                .catmull(0.0, 1, 1, 1).catmull(0.52, 0.9, 1.2, 0.9).linear(0.72, 1.35, 0.65, 1.35).catmull(0.92, 0.95, 1.05, 0.95).catmull(1.6, 1, 1, 1).build();

        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(0.52, 0, 240, 0).linear(0.76, 0, 360, 0).catmull(1.6, 0, 540, 0).build();
        public static final AnimationTrack RING_UP_POS = new AnimationTrack.Builder(new Vec3(0, 18, 0))
                .catmull(0.0, 0, 18, 0).catmull(0.52, 0, 25, 0).linear(0.76, 0, 5, 0).catmull(1.2, 0, 16, 0).catmull(1.6, 0, 18, 0).build();
        public static final AnimationTrack RING_UP_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
                .catmull(0.0, 1, 1, 1).catmull(0.52, 0.8, 0.8, 0.8).catmull(0.76, 1.7, 0.6, 1.7).catmull(1.6, 1, 1, 1).build();

        public static final AnimationTrack RING_CENTER_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(0.52, 0, -180, 0).linear(0.76, 0, -270, 0).catmull(1.6, 0, -360, 0).build();
        public static final AnimationTrack RING_CENTER_POS = new AnimationTrack.Builder(new Vec3(0, 20, 0))
                .catmull(0.0, 0, 20, 0).catmull(0.52, 0, 24, 0).linear(0.76, 0, 2, 0).catmull(1.2, 0, 18, 0).catmull(1.6, 0, 20, 0).build();
        public static final AnimationTrack RING_CENTER_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
                .catmull(0.0, 1, 1, 1).catmull(0.52, 0.8, 0.8, 0.8).catmull(0.76, 1.6, 1.0, 1.6).catmull(1.2, 1.05, 1.0, 1.05).catmull(1.6, 1, 1, 1).build();


        public static void apply(SoulInfuserModel<?> m, float ticks) {
            applyDefaultOpenParts(m);

            float t = Math.min(ticks, LENGTH);
            Vec3 ap = ALL_POS.get(t);
            m.all.setPos((float) ap.x, (float) ap.y, (float) ap.z);

            Vec3 hp = HEAD_POS.get(t); Vec3 hs = HEAD_SCALE.get(t);
            m.head.setPos((float) hp.x, (float) hp.y, (float) hp.z);
            m.head.xScale = (float) hs.x; m.head.yScale = (float) hs.y; m.head.zScale = (float) hs.z;

            m.ring_up.yRot = (float) RING_UP_ROT.get(t).y * DEG_TO_RAD;
            m.ring_up.setPos(0.0F, (float) RING_UP_POS.get(t).y, 0.0F);
            Vec3 rus = RING_UP_SCALE.get(t);
            m.ring_up.xScale = (float) rus.x; m.ring_up.yScale = (float) rus.y; m.ring_up.zScale = (float) rus.z;

            m.ring_center.yRot = (float) RING_CENTER_ROT.get(t).y * DEG_TO_RAD;
            m.ring_center.setPos(0.0F, (float) RING_CENTER_POS.get(t).y, 0.0F);
            Vec3 rcs = RING_CENTER_SCALE.get(t);
            m.ring_center.xScale = (float) rcs.x; m.ring_center.yScale = (float) rcs.y; m.ring_center.zScale = (float) rcs.z;
        }
    }

    // =========================================================================
    // ID 7: ATTACK_FIREBALL (length: 1.6s -> 40 ticks, once)
    // =========================================================================
    public static class AttackFireball {
        public static final float LENGTH = 40.0F;

        public static final AnimationTrack HEAD_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(0.75, 12, 0, 0).catmull(0.95, 15, 0, 0).catmull(1.05, -18, 0, 0).catmull(1.3, 3, 0, 0).catmull(1.6, 0, 0, 0).build();
        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(new Vec3(0, 16, 0))
                .catmull(0.0, 0, 16, 0).catmull(0.75, 0, 14.5, -2.0).catmull(0.95, 0, 13.8, -2.5).catmull(1.05, 0, 18.0, 3.5).catmull(1.3, 0, 15.5, -0.5).catmull(1.6, 0, 16, 0).build();

        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(0.9, 8, 180, 0).catmull(1.05, -12, 240, 0).catmull(1.6, 0, 360, 0).build();
        public static final AnimationTrack RING_UP_POS = new AnimationTrack.Builder(new Vec3(0, 18, 0))
                .catmull(0.0, 0, 18, 0).catmull(0.8, 0, 15.5, -1.0).catmull(1.05, 0, 21.0, 2.0).catmull(1.35, 0, 17.5, 0).catmull(1.6, 0, 18, 0).build();

        public static final AnimationTrack RING_CENTER_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(0.9, 10, -90, 0).catmull(1.05, -15, -120, 0).catmull(1.6, 0, -180, 0).build();
        public static final AnimationTrack RING_CENTER_POS = new AnimationTrack.Builder(new Vec3(0, 20, 0))
                .catmull(0.0, 0, 20, 0).catmull(0.8, 0, 18.5, -1.5).catmull(1.05, 0, 23.0, 2.5).catmull(1.35, 0, 19.5, 0).catmull(1.6, 0, 20, 0).build();

        public static final AnimationTrack RING_DOWN_POS = new AnimationTrack.Builder(new Vec3(0, 10, 0))
                .catmull(0.0, 0, 10, 0).catmull(0.8, 0, 9.0, -0.5).catmull(1.05, 0, 11.5, 1.0).catmull(1.6, 0, 10, 0).build();

        public static void apply(SoulInfuserModel<?> m, float ticks) {
            applyDefaultOpenParts(m);

            float t = Math.min(ticks, LENGTH);
            Vec3 hr = HEAD_ROT.get(t); Vec3 hp = HEAD_POS.get(t);
            m.head.xRot = (float) hr.x * DEG_TO_RAD;
            m.head.setPos((float) hp.x, (float) hp.y, (float) hp.z);

            Vec3 rur = RING_UP_ROT.get(t); Vec3 rup = RING_UP_POS.get(t);
            m.ring_up.xRot = (float) rur.x * DEG_TO_RAD; m.ring_up.yRot = (float) rur.y * DEG_TO_RAD;
            m.ring_up.setPos((float) rup.x, (float) rup.y, (float) rup.z);

            Vec3 rcr = RING_CENTER_ROT.get(t); Vec3 rcp = RING_CENTER_POS.get(t);
            m.ring_center.xRot = (float) rcr.x * DEG_TO_RAD; m.ring_center.yRot = (float) rcr.y * DEG_TO_RAD;
            m.ring_center.setPos((float) rcp.x, (float) rcp.y, (float) rcp.z);

            Vec3 rdp = RING_DOWN_POS.get(t);
            m.ring_down.setPos((float) rdp.x, (float) rdp.y, (float) rdp.z);
        }
    }

    // =========================================================================
    // ID 8: DIE (length: 1.5s -> 37.5 ticks, once)
    // =========================================================================
    public static class Die {
        public static final float LENGTH = 37.5F;

        public static final AnimationTrack ALL_POS = new AnimationTrack.Builder(Vec3.ZERO)
                .catmull(0.0, 0, 0, 0).catmull(0.4, 0, 6, 0).catmull(1.5, 0, -10, 0).build();
        public static final AnimationTrack ALL_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
                .catmull(0.0, 1, 1, 1).catmull(0.4, 1.2, 1.3, 1.2).catmull(1.2, 0.1, 0.1, 0.1).linear(1.5, 0, 0, 0).build();

        public static final AnimationTrack HEAD_ROT = new AnimationTrack.Builder(Vec3.ZERO)
                .linear(0.0, 0, 0, 0).linear(1.5, 0, 720, 0).build();
        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(new Vec3(0, 16, 0))
                .catmull(0.0, 0, 16, 0).catmull(0.4, 0, 22, 0).catmull(1.5, 0, 0, 0).build();

        public static final AnimationTrack RING_UP_POS = new AnimationTrack.Builder(new Vec3(0, 18, 0))
                .catmull(0.0, 0, 18, 0).catmull(0.5, 0, 30, 0).catmull(1.5, 0, 0, 0).build();
        public static final AnimationTrack RING_UP_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
                .catmull(0.0, 1, 1, 1).catmull(0.5, 1.6, 1.6, 1.6).catmull(1.5, 0, 0, 0).build();

        public static final AnimationTrack RING_DOWN_POS = new AnimationTrack.Builder(new Vec3(0, 10, 0))
                .catmull(0.0, 0, 10, 0).catmull(0.5, 0, 5, 0).catmull(1.5, 0, -8, 0).build();
        public static final AnimationTrack RING_DOWN_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
                .catmull(0.0, 1, 1, 1).catmull(0.5, 1.5, 1.5, 1.5).catmull(1.5, 0, 0, 0).build();

        public static final AnimationTrack RING_CENTER_POS = new AnimationTrack.Builder(new Vec3(0, 20, 0))
                .catmull(0.0, 0, 20, 0).catmull(0.5, 0, 25, 0).catmull(1.5, 0, 0, 0).build();
        public static final AnimationTrack RING_CENTER_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
                .catmull(0.0, 1, 1, 1).catmull(0.5, 1.8, 1.8, 1.8).catmull(1.5, 0, 0, 0).build();

        public static void apply(SoulInfuserModel<?> m, float ticks) {
            applyDefaultOpenParts(m);

            float t = Math.min(ticks, LENGTH);
            Vec3 ap = ALL_POS.get(t); Vec3 as = ALL_SCALE.get(t);
            m.all.setPos((float) ap.x, (float) ap.y, (float) ap.z);
            m.all.xScale = (float) as.x; m.all.yScale = (float) as.y; m.all.zScale = (float) as.z;

            Vec3 hp = HEAD_POS.get(t);
            m.head.yRot = (float) HEAD_ROT.get(t).y * DEG_TO_RAD;
            m.head.setPos((float) hp.x, (float) hp.y, (float) hp.z);

            Vec3 rup = RING_UP_POS.get(t); Vec3 rus = RING_UP_SCALE.get(t);
            m.ring_up.setPos((float) rup.x, (float) rup.y, (float) rup.z);
            m.ring_up.xScale = (float) rus.x; m.ring_up.yScale = (float) rus.y; m.ring_up.zScale = (float) rus.z;

            Vec3 rdp = RING_DOWN_POS.get(t); Vec3 rds = RING_DOWN_SCALE.get(t);
            m.ring_down.setPos((float) rdp.x, (float) rdp.y, (float) rdp.z);
            m.ring_down.xScale = (float) rds.x; m.ring_down.yScale = (float) rds.y; m.ring_down.zScale = (float) rds.z;

            Vec3 rcp = RING_CENTER_POS.get(t); Vec3 rcs = RING_CENTER_SCALE.get(t);
            m.ring_center.setPos((float) rcp.x, (float) rcp.y, (float) rcp.z);
            m.ring_center.xScale = (float) rcs.x; m.ring_center.yScale = (float) rcs.y; m.ring_center.zScale = (float) rcs.z;
        }
    }

    // =========================================================================
    // 辅助赋值函数
    // =========================================================================
    private static void setPos(ModelPart part, Vec3 pos) {
        if (pos != null) {
            part.setPos((float) pos.x, (float) pos.y, (float) pos.z);
        }
    }

    private static void setPosRot(ModelPart part, Vec3 pos, Vec3 rotDeg) {
        if (pos != null) {
            part.setPos((float) pos.x, (float) pos.y, (float) pos.z);
        }
        if (rotDeg != null) {
            part.xRot = (float) rotDeg.x * DEG_TO_RAD;
            part.yRot = (float) rotDeg.y * DEG_TO_RAD;
            part.zRot = (float) rotDeg.z * DEG_TO_RAD;
        }
    }
}
//
//import com.renyigesai.immortalers_delight.client.animation.custom.AnimationTrack;
//import com.renyigesai.immortalers_delight.client.model.SoulInfuserModel;
//import net.minecraft.client.model.geom.ModelPart;
//import net.minecraft.world.phys.Vec3;
//
//public class SoulInfuserAnimations {
//
//    public static final float DEG_TO_RAD = (float) (Math.PI / 180.0);
//
//    // =========================================================================
//    // ID 0: AWAKEN (length: 4.0s -> 100 ticks, once)
//    // =========================================================================
//    public static class Awaken {
//        public static final float LENGTH = 100.0F;
//
//        public static final AnimationTrack ALL_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
//                .catmull(0.0, 1, 1, 1).catmull(2.2, 0.9, 0.85, 0.9)
//                .catmull(2.4, 1.15, 1.2, 1.15).catmull(3.2, 0.98, 0.98, 0.98).catmull(4.0, 1, 1, 1).build();
//
//        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(1.0, 0, 0, 0).catmull(1.6, 0, 2, 0)
//                .catmull(2.2, 0, 0.5, 0).catmull(2.5, 0, 18.5, 0).catmull(3.2, 0, 15.5, 0).catmull(4.0, 0, 16, 0).build();
//
//        public static final AnimationTrack BODY_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, -0.5, 0).catmull(2.5, 0, -5.5, 0)
//                .catmull(3.2, 0, -3.8, 0).catmull(4.0, 0, -4, 0).build();
//
//        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 15, 0).catmull(2.5, 0, 120, 0).catmull(4.0, 0, 360, 0).build();
//        public static final AnimationTrack RING_UP_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.25, 0.3, 0, -0.2).catmull(0.5, -0.3, 0, 0.3)
//                .catmull(0.75, 0.4, 0, -0.3).catmull(1.0, 0, 0, 0).catmull(1.3, 0.2, 2.5, 0)
//                .catmull(1.6, -0.2, 5.0, 0).catmull(1.9, 0.3, 4.2, 0).catmull(2.2, 0, 6.0, 0)
//                .catmull(2.5, 0, 21.0, 0).catmull(3.2, 0, 17.5, 0).catmull(4.0, 0, 18, 0).build();
//
//        public static final AnimationTrack RING_CENTER_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.4, 12, 45, 0).catmull(4.0, 6, 360, 0).build();
//        public static final AnimationTrack RING_CENTER_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.25, -0.3, 0, 0.3).catmull(0.5, 0.3, 0, -0.2)
//                .catmull(0.75, -0.4, 0, 0.4).catmull(1.0, 0, 0, 0).catmull(1.3, -0.2, 1.5, 0)
//                .catmull(1.6, 0.2, 3.5, 0).catmull(1.9, -0.2, 2.8, 0).catmull(2.2, 0, 4.5, 0)
//                .catmull(2.5, 0, 23.0, 0).catmull(3.2, 0, 19.5, 0).catmull(4.0, 0, 20, 0).build();
//
//        public static final AnimationTrack RING_DOWN_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, -15, 0).catmull(2.5, 0, 30, 0).catmull(4.0, 0, -270, 0).build();
//        public static final AnimationTrack RING_DOWN_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.25, 0.2, 0, 0.3).catmull(0.5, -0.2, 0, -0.3)
//                .catmull(0.75, 0.3, 0, 0.2).catmull(1.0, 0, 0, 0).catmull(1.6, 0, 1.0, 0)
//                .catmull(2.2, 0, 2.0, 0).catmull(2.5, 0, 12.0, 0).catmull(3.2, 0, 9.5, 0).catmull(4.0, 0, 10, 0).build();
//
//        public static final AnimationTrack BASE_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .linear(0.0, 0, 0, 0).linear(2.2, 0, 0, 0).linear(2.28, 0, -4, 0).build();
//
//        // 外部 4 个角块 (upper_body_parts0~3)
//        public static final AnimationTrack P3_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 14, 0, -14).catmull(3.2, 10.5, 0, -10.5).catmull(4.0, 11, 0, -11).build();
//        public static final AnimationTrack P2_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -14, 0, 14).catmull(3.2, -10.5, 0, 10.5).catmull(4.0, -11, 0, 11).build();
//        public static final AnimationTrack P1_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 14, 0, 14).catmull(3.2, 10.5, 0, 10.5).catmull(4.0, 11, 0, 11).build();
//        public static final AnimationTrack P0_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -14, 0, -14).catmull(3.2, -10.5, 0, -10.5).catmull(4.0, -11, 0, -11).build();
//
//        // 中间 4 个十字块 (upper_body_parts4~7)
//        public static final AnimationTrack P7_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -100, 0, 0).catmull(4.0, -90, 0, 0).build();
//        public static final AnimationTrack P7_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 0, 0, -10).catmull(3.2, 0, 0, -7.5).catmull(4.0, 0, 0, -8).build();
//        public static final AnimationTrack P6_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 100, 0, 0).catmull(4.0, 90, 0, 0).build();
//        public static final AnimationTrack P6_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 0, 0, 10).catmull(3.2, 0, 0, 7.5).catmull(4.0, 0, 0, 8).build();
//        public static final AnimationTrack P5_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 0, 0, -100).catmull(4.0, 0, 0, -90).build();
//        public static final AnimationTrack P5_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 10, 0, 0).catmull(3.2, 7.5, 0, 0).catmull(4.0, 8, 0, 0).build();
//        public static final AnimationTrack P4_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 0, 0, 100).catmull(4.0, 0, 0, 90).build();
//        public static final AnimationTrack P4_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -10, 0, 0).catmull(3.2, -7.5, 0, 0).catmull(4.0, -8, 0, 0).build();
//
//        // 下层 4 个对角斜块 (upper_body_parts8~11)
//        public static final AnimationTrack P11_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -50, 0, 100).catmull(4.0, -45, 0, 90).build();
//        public static final AnimationTrack P11_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -4, 0, -4).catmull(4.0, -3, 0, -3).build();
//        public static final AnimationTrack P10_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 50, 0, -100).catmull(4.0, 45, 0, -90).build();
//        public static final AnimationTrack P10_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 4, 0, 4).catmull(4.0, 3, 0, 3).build();
//        public static final AnimationTrack P9_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -50, 0, -100).catmull(4.0, -45, 0, -90).build();
//        public static final AnimationTrack P9_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 4, 0, -4).catmull(4.0, 3, 0, -3).build();
//        public static final AnimationTrack P8_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, 50, 0, 100).catmull(4.0, 45, 0, 90).build();
//        public static final AnimationTrack P8_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(2.2, 0, 0, 0).catmull(2.45, -4, 0, 4).catmull(4.0, -3, 0, 3).build();
//
//        public static void apply(SoulInfuserModel<?> m, float ticks) {
//            float t = Math.min(ticks, LENGTH);
//            Vec3 scale = ALL_SCALE.get(t);
//            m.all.xScale = (float) scale.x; m.all.yScale = (float) scale.y; m.all.zScale = (float) scale.z;
//
//            Vec3 hp = HEAD_POS.get(t);
//            m.head.setPos((float) hp.x, -9.0F + (float) hp.y, (float) hp.z);
//
//            Vec3 bp = BASE_POS.get(t);
//            m.base.setPos((float) bp.x, -7.0F + (float) bp.y, (float) bp.z);
//
//            Vec3 rur = RING_UP_ROT.get(t); Vec3 rup = RING_UP_POS.get(t);
//            m.ring_up.yRot = (float) rur.y * DEG_TO_RAD;
//            m.ring_up.setPos((float) rup.x, -10.0F + (float) rup.y, (float) rup.z);
//
//            Vec3 rcr = RING_CENTER_ROT.get(t); Vec3 rcp = RING_CENTER_POS.get(t);
//            m.ring_center.xRot = (float) rcr.x * DEG_TO_RAD; m.ring_center.yRot = (float) rcr.y * DEG_TO_RAD;
//            m.ring_center.setPos((float) rcp.x, 5.0F + (float) rcp.y, (float) rcp.z);
//
//            Vec3 rdr = RING_DOWN_ROT.get(t); Vec3 rdp = RING_DOWN_POS.get(t);
//            m.ring_down.yRot = (float) rdr.y * DEG_TO_RAD;
//            m.ring_down.setPos((float) rdp.x, 5.0F + (float) rdp.y, (float) rdp.z);
//
//            // 部件位置与旋转映射
//            applyPart(m.upper_body_parts3, P3_POS.get(t), null, -4.5F, 0.0F, -4.5F);
//            applyPart(m.upper_body_parts2, P2_POS.get(t), null, 4.5F, 0.0F, 4.5F);
//            applyPart(m.upper_body_parts1, P1_POS.get(t), null, -4.5F, 0.0F, 4.5F);
//            applyPart(m.upper_body_parts0, P0_POS.get(t), null, 4.5F, 0.0F, -4.5F);
//
//            applyPart(m.upper_body_parts7, P7_POS.get(t), P7_ROT.get(t), 0.0F, 0.0F, -8.0F);
//            applyPart(m.upper_body_parts6, P6_POS.get(t), P6_ROT.get(t), 0.0F, 0.0F, 8.0F);
//            applyPart(m.upper_body_parts5, P5_POS.get(t), P5_ROT.get(t), -8.0F, 0.0F, 0.0F);
//            applyPart(m.upper_body_parts4, P4_POS.get(t), P4_ROT.get(t), 8.0F, 0.0F, 0.0F);
//
//            applyPart(m.upper_body_parts11, P11_POS.get(t), P11_ROT.get(t), 8.0F, 0.0F, -8.0F);
//            applyPart(m.upper_body_parts10, P10_POS.get(t), P10_ROT.get(t), -8.0F, 0.0F, 8.0F);
//            applyPart(m.upper_body_parts9, P9_POS.get(t), P9_ROT.get(t), -8.0F, 0.0F, -8.0F);
//            applyPart(m.upper_body_parts8, P8_POS.get(t), P8_ROT.get(t), 8.0F, 0.0F, 8.0F);
//        }
//    }
//
//    // =========================================================================
//    // ID 1: IDLE (length: 2.0s -> 50 ticks, loop)
//    // =========================================================================
//    public static class Idle {
//        public static final float LENGTH = 50.0F;
//
//        public static final AnimationTrack ALL_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.5, 0, 2.5, 0).catmull(1.0, 0, 0, 0).catmull(1.5, 0, -2.5, 0).catmull(2.0, 0, 0, 0).build();
//
//        public static final AnimationTrack HEAD_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 2, 0, 0).catmull(0.5, 0, 0, 2).catmull(1.0, -2, 0, 0).catmull(1.5, 0, 0, -2).catmull(2.0, 2, 0, 0).build();
//        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.5, 0, 2.0, 0).catmull(1.0, 0, 0, 0).catmull(1.5, 0, -2.0, 0).catmull(2.0, 0, 0, 0).build();
//
//        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .linear(0.0, 0, 0, 0).linear(2.0, 0, 360, 0).build();
//        public static final AnimationTrack RING_DOWN_ROT = new AnimationTrack.Builder(new Vec3(0, 90, 0))
//                .linear(0.0, 0, 90, 0).linear(2.0, 0, -270, 0).build();
//
//        public static final AnimationTrack RING_CENTER_ROT = new AnimationTrack.Builder(new Vec3(6, 0, 0))
//                .linear(0.0, 6, 0, 0).linear(0.5, 0, 90, 6).linear(1.0, -6, 180, 0).linear(1.5, 0, 270, -6).linear(2.0, 6, 360, 0).build();
//        public static final AnimationTrack RING_CENTER_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .linear(0.0, 0, 0, 0).linear(0.48, 0, 1.04, 0).linear(1.0, 0, 2, 0).linear(1.48, 0, 1.14, 0).linear(2.0, 0, 0, 0).build();
//
//        public static void apply(SoulInfuserModel<?> m, float ticks) {
//            float t = ticks % LENGTH;
//            Vec3 ap = ALL_POS.get(t);
//            m.all.setPos((float) ap.x, 24.0F + (float) ap.y, (float) ap.z);
//
//            Vec3 hr = HEAD_ROT.get(t); Vec3 hp = HEAD_POS.get(t);
//            m.head.xRot = (float) hr.x * DEG_TO_RAD; m.head.yRot = (float) hr.y * DEG_TO_RAD; m.head.zRot = (float) hr.z * DEG_TO_RAD;
//            m.head.setPos((float) hp.x, -9.0F + (float) hp.y, (float) hp.z);
//
//            m.ring_up.yRot = (float) RING_UP_ROT.get(t).y * DEG_TO_RAD;
//            m.ring_down.yRot = (float) RING_DOWN_ROT.get(t).y * DEG_TO_RAD;
//
//            Vec3 rcr = RING_CENTER_ROT.get(t); Vec3 rcp = RING_CENTER_POS.get(t);
//            m.ring_center.xRot = (float) rcr.x * DEG_TO_RAD; m.ring_center.yRot = (float) rcr.y * DEG_TO_RAD; m.ring_center.zRot = (float) rcr.z * DEG_TO_RAD;
//            m.ring_center.setPos((float) rcp.x, 5.0F + (float) rcp.y, (float) rcp.z);
//        }
//    }
//
//    // =========================================================================
//    // ID 2: ATTACK_FIRE_RAIN (length: 1.6s -> 40 ticks, loop)
//    // =========================================================================
//    public static class AttackFireRain {
//        public static final float LENGTH = 40.0F;
//
//        public static final AnimationTrack ALL_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 12, 0, 0).catmull(0.8, 8, 0, 0).catmull(1.6, 12, 0, 0).build();
//        public static final AnimationTrack ALL_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.8, 0, 2.0, 0).catmull(1.6, 0, 0, 0).build();
//
//        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, -1.5).catmull(0.8, 0, 1.5, -1.0).catmull(1.32, 0, 2.5, -0.5).catmull(1.44, 0, -3.0, -1.5).catmull(1.6, 0, 0, -1.5).build();
//
//        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .linear(0.0, 0, 0, 0).linear(1.6, 0, 540, 0).build();
//        public static final AnimationTrack RING_DOWN_ROT = new AnimationTrack.Builder(new Vec3(0, 90, 0))
//                .linear(0.0, 0, 90, 0).linear(1.6, 0, -450, 0).build();
//        public static final AnimationTrack RING_CENTER_ROT = new AnimationTrack.Builder(new Vec3(10, 0, 0))
//                .linear(0.0, 10, 0, 0).linear(1.6, 10, 360, 0).build();
//
//        public static void apply(SoulInfuserModel<?> m, float ticks) {
//            float t = ticks % LENGTH;
//            Vec3 ar = ALL_ROT.get(t); Vec3 ap = ALL_POS.get(t);
//            m.all.xRot = (float) ar.x * DEG_TO_RAD;
//            m.all.setPos((float) ap.x, 24.0F + (float) ap.y, (float) ap.z);
//
//            Vec3 hp = HEAD_POS.get(t);
//            m.head.setPos((float) hp.x, -9.0F + (float) hp.y, (float) hp.z);
//
//            m.ring_up.yRot = (float) RING_UP_ROT.get(t).y * DEG_TO_RAD;
//            m.ring_down.yRot = (float) RING_DOWN_ROT.get(t).y * DEG_TO_RAD;
//
//            Vec3 rcr = RING_CENTER_ROT.get(t);
//            m.ring_center.xRot = (float) rcr.x * DEG_TO_RAD; m.ring_center.yRot = (float) rcr.y * DEG_TO_RAD;
//        }
//    }
//
//    // =========================================================================
//    // ID 3: ATTACK_EXPLODE (length: 4.0s -> 100 ticks, once)
//    // =========================================================================
//    public static class AttackExplode {
//        public static final float LENGTH = 100.0F;
//
//        public static final AnimationTrack ALL_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(1.2, 0, 4, 0).catmull(2.6, 0, 6, 0).catmull(2.76, 0, -2, 0).catmull(3.36, 0, -2.19, 0).catmull(4.0, 0, 0, 0).build();
//
//        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(1.2, 0, 6, 0).catmull(2.6, 0, 8, 0).catmull(2.76, 0, -6, 0).catmull(3.36, 0, -5.88, 0).catmull(4.0, 0, 0, 0).build();
//        public static final AnimationTrack HEAD_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
//                .catmull(0.0, 1, 1, 1).catmull(2.6, 1.15, 1.25, 1.15).catmull(2.76, 0.9, 0.85, 0.9).catmull(3.36, 0.9, 0.85, 0.9).catmull(4.0, 1, 1, 1).build();
//
//        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(1.2, 35, 180, 0).catmull(2.6, 35, 1080, 0).catmull(2.76, 0, 1120, 0).catmull(3.36, -2.36, 1179.36, 0).catmull(4.0, 0, 1440, 0).build();
//        public static final AnimationTrack RING_UP_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(1.2, 0, 4, 0).catmull(2.76, 0, -4, 0).catmull(3.36, 0, -3.83, 0).catmull(4.0, 0, 0, 0).build();
//
//        public static final AnimationTrack RING_CENTER_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(1.2, 0, -2, 0).catmull(2.76, 0, -10, 0).catmull(3.36, 0, -8.77, 0).catmull(4.0, 0, 0, 0).build();
//        public static final AnimationTrack RING_CENTER_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
//                .catmull(0.0, 1, 1, 1).catmull(1.2, 1.3, 1.3, 1.3).catmull(2.6, 1.6, 1.6, 1.6).catmull(2.76, 0.9, 0.9, 0.9).catmull(3.36, 0.87, 0.87, 0.87).catmull(4.0, 1, 1, 1).build();
//
//        public static final AnimationTrack RING_DOWN_ROT = new AnimationTrack.Builder(new Vec3(0, 90, 0))
//                .catmull(0.0, 0, 90, 0).catmull(1.2, -35, -90, 0).catmull(2.6, -35, -990, 0).catmull(2.76, 0, -1020, 0).catmull(3.36, 2.36, -1080.45, 0).catmull(4.0, 0, -1350, 0).build();
//        public static final AnimationTrack RING_DOWN_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(1.2, 0, 4, 0).catmull(1.8, 0, 8, 0).catmull(2.4, 0, 4, 0).catmull(2.76, 0, -4, 0).catmull(3.36, 0, -3.83, 0).catmull(4.0, 0, 0, 0).build();
//
//        public static void apply(SoulInfuserModel<?> m, float ticks) {
//            float t = Math.min(ticks, LENGTH);
//            Vec3 ap = ALL_POS.get(t);
//            m.all.setPos((float) ap.x, 24.0F + (float) ap.y, (float) ap.z);
//
//            Vec3 hp = HEAD_POS.get(t); Vec3 hs = HEAD_SCALE.get(t);
//            m.head.setPos((float) hp.x, -9.0F + (float) hp.y, (float) hp.z);
//            m.head.xScale = (float) hs.x; m.head.yScale = (float) hs.y; m.head.zScale = (float) hs.z;
//
//            Vec3 rur = RING_UP_ROT.get(t); Vec3 rup = RING_UP_POS.get(t);
//            m.ring_up.xRot = (float) rur.x * DEG_TO_RAD; m.ring_up.yRot = (float) rur.y * DEG_TO_RAD;
//            m.ring_up.setPos((float) rup.x, -10.0F + (float) rup.y, (float) rup.z);
//
//            Vec3 rcp = RING_CENTER_POS.get(t); Vec3 rcs = RING_CENTER_SCALE.get(t);
//            m.ring_center.setPos((float) rcp.x, 5.0F + (float) rcp.y, (float) rcp.z);
//            m.ring_center.xScale = (float) rcs.x; m.ring_center.yScale = (float) rcs.y; m.ring_center.zScale = (float) rcs.z;
//
//            Vec3 rdr = RING_DOWN_ROT.get(t); Vec3 rdp = RING_DOWN_POS.get(t);
//            m.ring_down.xRot = (float) rdr.x * DEG_TO_RAD; m.ring_down.yRot = (float) rdr.y * DEG_TO_RAD;
//            m.ring_down.setPos((float) rdp.x, 5.0F + (float) rdp.y, (float) rdp.z);
//        }
//    }
//
//    // =========================================================================
//    // ID 4: ATTACK_COLUMN (length: 2.4s -> 60 ticks, once)
//    // =========================================================================
//    public static class AttackColumn {
//        public static final float LENGTH = 60.0F;
//
//        public static final AnimationTrack ALL_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.88, 0, 0, 0).catmull(1.32, 0, 10, 0).catmull(1.52, 0, -6, 0).catmull(1.68, 0, -2, 0).catmull(2.0, 0, 1, 0).catmull(2.4, 0, 0, 0).build();
//
//        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.24, 0, -3, 0).catmull(0.48, 0, -4, 0).catmull(0.72, 0, -4, 0)
//                .catmull(0.88, 0, 0, 0).catmull(1.32, 0, 4, 0).catmull(1.52, 0, -10, 0).catmull(1.68, 0, -4, 0).catmull(2.4, 0, 0, 0).build();
//        public static final AnimationTrack HEAD_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
//                .catmull(0.0, 1, 1, 1).catmull(0.88, 1, 1, 1).catmull(1.32, 0.9, 1.2, 0.9).catmull(1.52, 1.35, 0.65, 1.35).catmull(1.72, 0.95, 1.05, 0.95).catmull(2.4, 1, 1, 1).build();
//
//        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .linear(0.0, 0, 0, 0).linear(0.2, 0, 84, 0).linear(0.44, 0, 180, 0).linear(0.8, 0, 276, 0).linear(1.2, 0, 360, 0).linear(1.8, 0, 360, 0).build();
//        public static final AnimationTrack RING_UP_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .linear(0.0, 0, 0, 0).linear(1.32, 0, 0, 0).linear(1.56, 0, -18, 0).linear(1.8, 0, -18, 0).linear(2.16, 0, 2, 0).linear(2.24, 0, 1.6, 0).linear(2.4, 0, 0, 0).build();
//
//        public static final AnimationTrack RING_CENTER_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.88, 0, 0, 0).catmull(1.32, 0, 4, 0).catmull(1.56, 0, -18, 0).catmull(2.0, 0, -2, 0).catmull(2.4, 0, 0, 0).build();
//        public static final AnimationTrack RING_CENTER_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
//                .catmull(0.0, 1, 1, 1).catmull(0.88, 1, 1, 1).catmull(1.32, 0.8, 0.8, 0.8).catmull(1.56, 1.6, 1.0, 1.6).catmull(2.0, 1.05, 1.0, 1.05).catmull(2.4, 1, 1, 1).build();
//
//        public static final AnimationTrack RING_DOWN_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.88, 0, 0, 0).catmull(1.32, 0, 4, 0).catmull(1.52, 0, -12, 0).catmull(2.4, 0, 0, 0).build();
//        public static final AnimationTrack RING_DOWN_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
//                .catmull(0.0, 1, 1, 1).catmull(0.88, 1, 1, 1).catmull(1.32, 0.85, 0.85, 0.85).catmull(1.56, 1.5, 0.8, 1.5).catmull(2.4, 1, 1, 1).build();
//
//        public static void apply(SoulInfuserModel<?> m, float ticks) {
//            float t = Math.min(ticks, LENGTH);
//            Vec3 ap = ALL_POS.get(t);
//            m.all.setPos((float) ap.x, 24.0F + (float) ap.y, (float) ap.z);
//
//            Vec3 hp = HEAD_POS.get(t); Vec3 hs = HEAD_SCALE.get(t);
//            m.head.setPos((float) hp.x, -9.0F + (float) hp.y, (float) hp.z);
//            m.head.xScale = (float) hs.x; m.head.yScale = (float) hs.y; m.head.zScale = (float) hs.z;
//
//            m.ring_up.yRot = (float) RING_UP_ROT.get(t).y * DEG_TO_RAD;
//            m.ring_up.setPos(0.0F, -10.0F + (float) RING_UP_POS.get(t).y, 0.0F);
//
//            Vec3 rcp = RING_CENTER_POS.get(t); Vec3 rcs = RING_CENTER_SCALE.get(t);
//            m.ring_center.setPos((float) rcp.x, 5.0F + (float) rcp.y, (float) rcp.z);
//            m.ring_center.xScale = (float) rcs.x; m.ring_center.yScale = (float) rcs.y; m.ring_center.zScale = (float) rcs.z;
//
//            Vec3 rdp = RING_DOWN_POS.get(t); Vec3 rds = RING_DOWN_SCALE.get(t);
//            m.ring_down.setPos((float) rdp.x, 5.0F + (float) rdp.y, (float) rdp.z);
//            m.ring_down.xScale = (float) rds.x; m.ring_down.yScale = (float) rds.y; m.ring_down.zScale = (float) rds.z;
//        }
//    }
//
//    // =========================================================================
//    // ID 5: ATTACK_SUMMON (length: 2.6s -> 65 ticks, loop)
//    // =========================================================================
//    public static class AttackSummon {
//        public static final float LENGTH = 65.0F;
//
//        public static final AnimationTrack ALL_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.2, 0, -2, 0).catmull(0.4, 0, 0, 0).catmull(0.6, 0, 2, 0).catmull(0.8, 0, 0, 0)
//                .catmull(1.32, 0, 2.5, 0).catmull(1.64, 0, 0, 0).catmull(1.96, 0, -2.5, 0).catmull(2.28, 0, 0, 0).catmull(2.6, 0, 0, 0).build();
//
//        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.12, 0, -2, 0).catmull(0.36, 0, -4.22, 0).catmull(0.72, 0, -6, 0).catmull(0.88, 0, -6, 0)
//                .catmull(1.0, 0, 0, 0).catmull(1.32, 0, 2, 0).catmull(1.64, 0, 0, 0).catmull(1.96, 0, -2, 0).catmull(2.28, 0, 0, 0).catmull(2.6, 0, 0, 0).build();
//        public static final AnimationTrack HEAD_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 2, 0, 0).catmull(1.0, 2, 0, 0).catmull(1.16, 0, 0, 2).catmull(1.36, -2, 0, 0).catmull(1.6, 0, 0, -2).catmull(1.88, 2, 0, 0).catmull(1.96, 2, 0, 0).build();
//
//        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .linear(0.0, 0, 0, 0).linear(1.0, 0, 0, 0).linear(1.16, 0, 90, 0).linear(1.32, 0, 180, 0).linear(1.52, 0, 270, 0).linear(1.96, 0, 360, 0).linear(2.12, 0, 360, 0).linear(2.6, 0, 450, 0).build();
//        public static final AnimationTrack RING_UP_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
//                .linear(0.0, 1, 1, 1).linear(1.0, 1, 1, 1).linear(1.16, 1.125, 1, 1.125).linear(1.32, 1.25, 1, 1.25).linear(1.52, 1.375, 1, 1.375).linear(1.96, 1.5, 1, 1.5).linear(2.12, 0.8, 1, 0.8).linear(2.6, 1, 1, 1).build();
//
//        public static void apply(SoulInfuserModel<?> m, float ticks) {
//            float t = ticks % LENGTH;
//            Vec3 ap = ALL_POS.get(t);
//            m.all.setPos((float) ap.x, 24.0F + (float) ap.y, (float) ap.z);
//
//            Vec3 hp = HEAD_POS.get(t); Vec3 hr = HEAD_ROT.get(t);
//            m.head.setPos((float) hp.x, -9.0F + (float) hp.y, (float) hp.z);
//            m.head.xRot = (float) hr.x * DEG_TO_RAD; m.head.yRot = (float) hr.y * DEG_TO_RAD; m.head.zRot = (float) hr.z * DEG_TO_RAD;
//
//            m.ring_up.yRot = (float) RING_UP_ROT.get(t).y * DEG_TO_RAD;
//            Vec3 rus = RING_UP_SCALE.get(t);
//            m.ring_up.xScale = (float) rus.x; m.ring_up.yScale = (float) rus.y; m.ring_up.zScale = (float) rus.z;
//        }
//    }
//
//    // =========================================================================
//    // ID 6: ATTACK_FIRE_RING (length: 1.6s -> 40 ticks, once)
//    // =========================================================================
//    public static class AttackFireRing {
//        public static final float LENGTH = 40.0F;
//
//        public static final AnimationTrack ALL_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.52, 0, 10, 0).catmull(0.72, 0, -6, 0).catmull(0.88, 0, -2, 0).catmull(1.24, 0, 1, 0).catmull(1.6, 0, 0, 0).build();
//
//        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.52, 0, 5, 0).catmull(0.72, 0, -10, 0).catmull(0.88, 0, -4, 0).catmull(1.6, 0, 0, 0).build();
//        public static final AnimationTrack HEAD_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
//                .catmull(0.0, 1, 1, 1).catmull(0.52, 0.9, 1.2, 0.9).catmull(0.72, 1.35, 0.65, 1.35).catmull(0.92, 0.95, 1.05, 0.95).catmull(1.6, 1, 1, 1).build();
//
//        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.52, 0, 240, 0).catmull(0.76, 0, 360, 0).catmull(1.6, 0, 540, 0).build();
//        public static final AnimationTrack RING_UP_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.52, 0, 7, 0).catmull(0.76, 0, -13, 0).catmull(1.2, 0, -2, 0).catmull(1.6, 0, 0, 0).build();
//        public static final AnimationTrack RING_UP_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
//                .catmull(0.0, 1, 1, 1).catmull(0.52, 0.8, 0.8, 0.8).catmull(0.76, 1.7, 0.6, 1.7).catmull(1.6, 1, 1, 1).build();
//
//        public static final AnimationTrack RING_CENTER_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.52, 0, -180, 0).catmull(0.76, 0, -270, 0).catmull(1.6, 0, -360, 0).build();
//        public static final AnimationTrack RING_CENTER_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.52, 0, 4, 0).catmull(0.76, 0, -18, 0).catmull(1.2, 0, -2, 0).catmull(1.6, 0, 0, 0).build();
//        public static final AnimationTrack RING_CENTER_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
//                .catmull(0.0, 1, 1, 1).catmull(0.52, 0.8, 0.8, 0.8).catmull(0.76, 1.6, 1.0, 1.6).catmull(1.2, 1.05, 1.0, 1.05).catmull(1.6, 1, 1, 1).build();
//
//        public static void apply(SoulInfuserModel<?> m, float ticks) {
//            float t = Math.min(ticks, LENGTH);
//            Vec3 ap = ALL_POS.get(t);
//            m.all.setPos((float) ap.x, 24.0F + (float) ap.y, (float) ap.z);
//
//            Vec3 hp = HEAD_POS.get(t); Vec3 hs = HEAD_SCALE.get(t);
//            m.head.setPos((float) hp.x, -9.0F + (float) hp.y, (float) hp.z);
//            m.head.xScale = (float) hs.x; m.head.yScale = (float) hs.y; m.head.zScale = (float) hs.z;
//
//            m.ring_up.yRot = (float) RING_UP_ROT.get(t).y * DEG_TO_RAD;
//            m.ring_up.setPos(0.0F, -10.0F + (float) RING_UP_POS.get(t).y, 0.0F);
//            Vec3 rus = RING_UP_SCALE.get(t);
//            m.ring_up.xScale = (float) rus.x; m.ring_up.yScale = (float) rus.y; m.ring_up.zScale = (float) rus.z;
//
//            m.ring_center.yRot = (float) RING_CENTER_ROT.get(t).y * DEG_TO_RAD;
//            m.ring_center.setPos(0.0F, 5.0F + (float) RING_CENTER_POS.get(t).y, 0.0F);
//            Vec3 rcs = RING_CENTER_SCALE.get(t);
//            m.ring_center.xScale = (float) rcs.x; m.ring_center.yScale = (float) rcs.y; m.ring_center.zScale = (float) rcs.z;
//        }
//    }
//
//    // =========================================================================
//    // ID 7: ATTACK_FIREBALL (length: 1.6s -> 40 ticks, once)
//    // =========================================================================
//    public static class AttackFireball {
//        public static final float LENGTH = 40.0F;
//
//        public static final AnimationTrack HEAD_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.75, 12, 0, 0).catmull(0.95, 15, 0, 0).catmull(1.05, -18, 0, 0).catmull(1.3, 3, 0, 0).catmull(1.6, 0, 0, 0).build();
//        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.75, 0, -1.5, -2.0).catmull(0.95, 0, -2.2, -2.5).catmull(1.05, 0, 2.0, 3.5).catmull(1.3, 0, -0.5, -0.5).catmull(1.6, 0, 0, 0).build();
//
//        public static final AnimationTrack RING_UP_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.9, 8, 180, 0).catmull(1.05, -12, 240, 0).catmull(1.6, 0, 360, 0).build();
//        public static final AnimationTrack RING_UP_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.8, 0, -2.5, -1.0).catmull(1.05, 0, 3.0, 2.0).catmull(1.35, 0, -0.5, 0).catmull(1.6, 0, 0, 0).build();
//
//        public static final AnimationTrack RING_CENTER_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.9, 10, -90, 0).catmull(1.05, -15, -120, 0).catmull(1.6, 0, -180, 0).build();
//        public static final AnimationTrack RING_CENTER_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.8, 0, -1.5, -1.5).catmull(1.05, 0, 3.0, 2.5).catmull(1.35, 0, -0.5, 0).catmull(1.6, 0, 0, 0).build();
//
//        public static final AnimationTrack RING_DOWN_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.8, 0, -1.0, -0.5).catmull(1.05, 0, 1.5, 1.0).catmull(1.6, 0, 0, 0).build();
//
//        public static void apply(SoulInfuserModel<?> m, float ticks) {
//            float t = Math.min(ticks, LENGTH);
//            Vec3 hr = HEAD_ROT.get(t); Vec3 hp = HEAD_POS.get(t);
//            m.head.xRot = (float) hr.x * DEG_TO_RAD;
//            m.head.setPos((float) hp.x, -9.0F + (float) hp.y, (float) hp.z);
//
//            Vec3 rur = RING_UP_ROT.get(t); Vec3 rup = RING_UP_POS.get(t);
//            m.ring_up.xRot = (float) rur.x * DEG_TO_RAD; m.ring_up.yRot = (float) rur.y * DEG_TO_RAD;
//            m.ring_up.setPos((float) rup.x, -10.0F + (float) rup.y, (float) rup.z);
//
//            Vec3 rcr = RING_CENTER_ROT.get(t); Vec3 rcp = RING_CENTER_POS.get(t);
//            m.ring_center.xRot = (float) rcr.x * DEG_TO_RAD; m.ring_center.yRot = (float) rcr.y * DEG_TO_RAD;
//            m.ring_center.setPos((float) rcp.x, 5.0F + (float) rcp.y, (float) rcp.z);
//
//            Vec3 rdp = RING_DOWN_POS.get(t);
//            m.ring_down.setPos((float) rdp.x, 5.0F + (float) rdp.y, (float) rdp.z);
//        }
//    }
//
//    // =========================================================================
//    // ID 8: DIE (length: 1.5s -> 37.5 ticks, once)
//    // =========================================================================
//    public static class Die {
//        public static final float LENGTH = 37.5F;
//
//        public static final AnimationTrack ALL_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.4, 0, 6, 0).catmull(1.5, 0, -10, 0).build();
//        public static final AnimationTrack ALL_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
//                .catmull(0.0, 1, 1, 1).catmull(0.4, 1.2, 1.3, 1.2).catmull(1.2, 0.1, 0.1, 0.1).catmull(1.5, 0, 0, 0).build();
//
//        public static final AnimationTrack HEAD_ROT = new AnimationTrack.Builder(Vec3.ZERO)
//                .linear(0.0, 0, 0, 0).linear(1.5, 0, 720, 0).build();
//        public static final AnimationTrack HEAD_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.4, 0, 6, 0).catmull(1.5, 0, -16, 0).build();
//
//        public static final AnimationTrack RING_UP_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.5, 0, 12, 0).catmull(1.5, 0, -18, 0).build();
//        public static final AnimationTrack RING_UP_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
//                .catmull(0.0, 1, 1, 1).catmull(0.5, 1.6, 1.6, 1.6).catmull(1.5, 0, 0, 0).build();
//
//        public static final AnimationTrack RING_DOWN_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.5, 0, -5, 0).catmull(1.5, 0, -18, 0).build();
//        public static final AnimationTrack RING_DOWN_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
//                .catmull(0.0, 1, 1, 1).catmull(0.5, 1.5, 1.5, 1.5).catmull(1.5, 0, 0, 0).build();
//
//        public static final AnimationTrack RING_CENTER_POS = new AnimationTrack.Builder(Vec3.ZERO)
//                .catmull(0.0, 0, 0, 0).catmull(0.5, 0, 5, 0).catmull(1.5, 0, -20, 0).build();
//        public static final AnimationTrack RING_CENTER_SCALE = new AnimationTrack.Builder(new Vec3(1, 1, 1))
//                .catmull(0.0, 1, 1, 1).catmull(0.5, 1.8, 1.8, 1.8).catmull(1.5, 0, 0, 0).build();
//
//        public static void apply(SoulInfuserModel<?> m, float ticks) {
//            float t = Math.min(ticks, LENGTH);
//            Vec3 ap = ALL_POS.get(t); Vec3 as = ALL_SCALE.get(t);
//            m.all.setPos((float) ap.x, 24.0F + (float) ap.y, (float) ap.z);
//            m.all.xScale = (float) as.x; m.all.yScale = (float) as.y; m.all.zScale = (float) as.z;
//
//            Vec3 hp = HEAD_POS.get(t);
//            m.head.yRot = (float) HEAD_ROT.get(t).y * DEG_TO_RAD;
//            m.head.setPos((float) hp.x, -9.0F + (float) hp.y, (float) hp.z);
//
//            Vec3 rup = RING_UP_POS.get(t); Vec3 rus = RING_UP_SCALE.get(t);
//            m.ring_up.setPos((float) rup.x, -10.0F + (float) rup.y, (float) rup.z);
//            m.ring_up.xScale = (float) rus.x; m.ring_up.yScale = (float) rus.y; m.ring_up.zScale = (float) rus.z;
//
//            Vec3 rdp = RING_DOWN_POS.get(t); Vec3 rds = RING_DOWN_SCALE.get(t);
//            m.ring_down.setPos((float) rdp.x, 5.0F + (float) rdp.y, (float) rdp.z);
//            m.ring_down.xScale = (float) rds.x; m.ring_down.yScale = (float) rds.y; m.ring_down.zScale = (float) rds.z;
//
//            Vec3 rcp = RING_CENTER_POS.get(t); Vec3 rcs = RING_CENTER_SCALE.get(t);
//            m.ring_center.setPos((float) rcp.x, 5.0F + (float) rcp.y, (float) rcp.z);
//            m.ring_center.xScale = (float) rcs.x; m.ring_center.yScale = (float) rcs.y; m.ring_center.zScale = (float) rcs.z;
//        }
//    }
//
//    // =========================================================================
//    // 基础偏移工具方法
//    // =========================================================================
//    private static void applyPart(ModelPart part, Vec3 posOffset, Vec3 rotDeg, float ox, float oy, float oz) {
//        if (posOffset != null) {
//            part.setPos(ox + (float) posOffset.x, oy + (float) posOffset.y, oz + (float) posOffset.z);
//        }
//        if (rotDeg != null) {
//            part.xRot = (float) rotDeg.x * DEG_TO_RAD;
//            part.yRot = (float) rotDeg.y * DEG_TO_RAD;
//            part.zRot = (float) rotDeg.z * DEG_TO_RAD;
//        }
//    }
//}
