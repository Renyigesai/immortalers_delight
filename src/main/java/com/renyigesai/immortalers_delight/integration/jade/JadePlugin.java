package com.renyigesai.immortalers_delight.integration.jade;

import com.renyigesai.immortalers_delight.integration.jade.provider.SnifferProvider;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(SnifferProvider.INSTANCE, Sniffer.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(SnifferProvider.INSTANCE, Sniffer.class);
    }
}
