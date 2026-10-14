package com.renyigesai.immortalers_delight.client.key;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.message.AntiqueToolKeyC2SPackage;
import com.renyigesai.immortalers_delight.message.KeyAuxiliaryMessage;
import com.renyigesai.immortalers_delight.network.ImmortalersNetwork;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ImmortalersKeyMapping {

    private static final String CATEGORY = "key." + ImmortalersDelightMod.MODID + "." + ImmortalersDelightMod.MODID;

    public static final KeyMapping AUXILIARY = new KeyMapping("key." + ImmortalersDelightMod.MODID + ".auxiliary", GLFW.GLFW_KEY_LEFT_SHIFT,CATEGORY){
        private boolean isDownOld = false;
        @Override
        public void setDown(boolean isDown) {

            if (this.isDownOld == isDown){
                super.setDown(isDown);
                return;
            }
            super.setDown(isDown);
            if (Minecraft.getInstance().getConnection() != null) {
                int type = 1;
                if (isDownOld != isDown && isDown) {
                    type = 0;
                }

                ImmortalersNetwork.sendMSGToServer(new KeyAuxiliaryMessage(type));
                isDownOld = isDown;
            }
        }
    };

    public static final KeyMapping ANTIQUE_TOOL_SLOT_1 = new KeyMapping("key." + ImmortalersDelightMod.MODID + ".antique_tool_slot_1", GLFW.GLFW_KEY_1,CATEGORY){
        private boolean isDownOld = false;
        @Override
        public void setDown(boolean isDown) {

            if (this.isDownOld == isDown){
                super.setDown(isDown);
                return;
            }
            super.setDown(isDown);
            if (Minecraft.getInstance().getConnection() != null) {
                int type = 1;
                if (isDownOld != isDown && isDown) {
                    type = 0;
                }
                //插入发包
                ImmortalersNetwork.sendMSGToServer(new AntiqueToolKeyC2SPackage(0));
                isDownOld = isDown;
            }
        }
    };

    public static final KeyMapping ANTIQUE_TOOL_SLOT_2 = new KeyMapping("key." + ImmortalersDelightMod.MODID + ".antique_tool_slot_2", GLFW.GLFW_KEY_2,CATEGORY){
        private boolean isDownOld = false;
        @Override
        public void setDown(boolean isDown) {

            if (this.isDownOld == isDown){
                super.setDown(isDown);
                return;
            }
            super.setDown(isDown);
            if (Minecraft.getInstance().getConnection() != null) {
                int type = 1;
                if (isDownOld != isDown && isDown) {
                    type = 0;
                }
                //插入发包
                ImmortalersNetwork.sendMSGToServer(new AntiqueToolKeyC2SPackage(1));
                isDownOld = isDown;
            }
        }
    };

    public static final KeyMapping ANTIQUE_TOOL_SLOT_3 = new KeyMapping("key." + ImmortalersDelightMod.MODID + ".antique_tool_slot_3", GLFW.GLFW_KEY_3,CATEGORY){
        private boolean isDownOld = false;
        @Override
        public void setDown(boolean isDown) {

            if (this.isDownOld == isDown){
                super.setDown(isDown);
                return;
            }
            super.setDown(isDown);
            if (Minecraft.getInstance().getConnection() != null) {
                int type = 1;
                if (isDownOld != isDown && isDown) {
                    type = 0;
                }
                //插入发包
                ImmortalersNetwork.sendMSGToServer(new AntiqueToolKeyC2SPackage(2));
                isDownOld = isDown;
            }
        }
    };

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(AUXILIARY);
        event.register(ANTIQUE_TOOL_SLOT_1);
        event.register(ANTIQUE_TOOL_SLOT_2);
        event.register(ANTIQUE_TOOL_SLOT_3);
    }
}
