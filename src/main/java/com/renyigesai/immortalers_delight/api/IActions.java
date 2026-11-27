package com.renyigesai.immortalers_delight.api;

public interface IActions {
    boolean isReady();
    void startAction();
    void actionTick();
    boolean needStop();
    void endAction();
}
