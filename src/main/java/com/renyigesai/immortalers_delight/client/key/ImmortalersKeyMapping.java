package com.renyigesai.immortalers_delight.client.key;

import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
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
    public static final KeyMapping AUXILIARY = new KeyMapping("key" + ImmortalersDelightMod.MODID + "auxiliary", GLFW.GLFW_KEY_LEFT_SHIFT,"key" + ImmortalersDelightMod.MODID + "bakeries"){
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
    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(AUXILIARY);
    }
}
