package com.renyigesai.immortalers_delight.util.task;
import net.neoforged.fml.common.EventBusSubscriber;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper.DifficultyModeSaveLoadHelper;
import com.renyigesai.immortalers_delight.util.datautil.datasaveloadhelper.ExitTimeSaveLoadHelper;
import com.renyigesai.immortalers_delight.util.task.ScheduledExecuteTask;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import javax.annotation.Nonnull;

@EventBusSubscriber
public class TimekeepingTask {
    private static Long ImmortalTickTime = 0L;
    public static Long getImmortalTickTime() {return ImmortalTickTime;}
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            if (ExitTimeSaveLoadHelper.loadExitTime(serverLevel) != null) {
                ImmortalTickTime = ExitTimeSaveLoadHelper.loadExitTime(serverLevel);
                //System.out.println("[ImmortalersDelight] 读取存档中信息成功！");
            }
            if (ImmortalTickTime == 0) ImmortalTickTime = System.currentTimeMillis();

        }
    }
    @SubscribeEvent
    public static void onTick(@Nonnull ServerTickEvent.Pre evt) {
        run();
    }
//    @SubscribeEvent
//    public static void clientTick(TickEvent.ClientTickEvent event) {
//        if (event.phase == TickEvent.Phase.START) {
//            run();
//        }
//
//    }
    private static void run() {
        ImmortalTickTime = ImmortalTickTime + 50L;
        //ImmortalersDelightMod.LOGGER.info("现在的TICK时间是：" + ImmortalTickTime);
    }
}
