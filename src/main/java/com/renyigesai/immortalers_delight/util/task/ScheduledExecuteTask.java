package com.renyigesai.immortalers_delight.util.task;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Random;

@Mod.EventBusSubscriber
public abstract class ScheduledExecuteTask implements Runnable {
    /*
    这是一个计划任务，在服务端tick执行自身tick
    用于实际执行特殊状态效果的功能
     */
    private final static HashMap<Integer, ScheduledExecuteTask> TASK_LIST = new HashMap<Integer, ScheduledExecuteTask>();
    private volatile boolean isRemoved = false;
    protected final int TASKID;
    private int delay;
    protected boolean cycle = delay >= 0;;
    protected final int INITIALDELAY;
    protected final int DELAY;
    protected boolean start;
    protected boolean first;
    protected int tick;

    public ScheduledExecuteTask() {
        this(0);
    }

    public ScheduledExecuteTask(int initialDelay) {
        this(initialDelay, -1);
    }

    public ScheduledExecuteTask(int initialDelay, int delay) {
        this.TASKID = new Random().nextInt(Short.MAX_VALUE);
        this.first = true;
        this.delay = delay;
        this.tick = 0;
        this.INITIALDELAY = initialDelay;
        this.DELAY = delay;
    }

    public ScheduledExecuteTask(int initialDelay, int delay, int taskID) {
        this.TASKID = taskID;
        this.first = true;
        this.delay = delay;
        this.tick = 0;
        this.INITIALDELAY = initialDelay;
        this.DELAY = delay;
    }

    public synchronized static boolean cancel(int taskID) {
        if (TASK_LIST.containsKey(taskID)) {
            TASK_LIST.get(taskID).cancel();
            return true;
        } else {
            return false;
        }
    }

    public static HashMap<Integer, ScheduledExecuteTask> getTaskMap() {return TASK_LIST;}
    public static ScheduledExecuteTask getTaskFromID(Integer id) {return TASK_LIST.get(id);}

    @SubscribeEvent
    public void onTick(@Nonnull TickEvent.ServerTickEvent evt) {
        if (evt.phase.equals(TickEvent.Phase.START)) {
            tick++;
            if (first) {
                if (tick >= INITIALDELAY) {
                    first = false;
                    tick = 0;
                    this.run();
                }
            } else {
                if (delay >= 0) {
                    if (tick >= DELAY) {
                        tick = 0;
                        this.run();
                    }
                } else {
                    ImmortalersDelightMod.LOGGER.info("孩子们，我已经超时了，但我还在跑");
                    cancel();
                }
            }
        }
    }

    public int getTaskID() {
        return TASKID;
    }

    public synchronized void start() {
        if (!start) {
            start = true;
            TASK_LIST.put(getTaskID(), this);
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public synchronized void cancel() {
        start = false;
        TASK_LIST.remove(this.getTaskID());
        ImmortalersDelightMod.LOGGER.info("我是Task，孩子们我被取消了");
        MinecraftForge.EVENT_BUS.unregister(this);
    }
    public void stop() {
        delay = -1;
    }
}
