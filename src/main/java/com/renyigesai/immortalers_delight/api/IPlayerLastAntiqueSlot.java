package com.renyigesai.immortalers_delight.api;

import com.renyigesai.immortalers_delight.item.antique.AntiqueBagHandler;

public interface IPlayerLastAntiqueSlot {

    AntiqueBagHandler.BagRecord getLastAntiqueSlot();

    void setLastAntiqueSlot(AntiqueBagHandler.BagRecord record);
}
