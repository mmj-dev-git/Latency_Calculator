package com.mobileer.core;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\u00020\u0011X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015\u00a8\u0006\u0016"}, d2 = {"Lcom/mobileer/core/Constants;", "", "()V", "DEVICENAME", "", "getDEVICENAME", "()Ljava/lang/String;", "setDEVICENAME", "(Ljava/lang/String;)V", "DEVICE_HARDWARE_LATENCY", "", "getDEVICE_HARDWARE_LATENCY", "()D", "setDEVICE_HARDWARE_LATENCY", "(D)V", "LATENCY_CALCULATOR_LOGS", "LATENCY_MODEL", "Lcom/mobileer/domain/models/LatencyModel;", "getLATENCY_MODEL", "()Lcom/mobileer/domain/models/LatencyModel;", "setLATENCY_MODEL", "(Lcom/mobileer/domain/models/LatencyModel;)V", "oboeTester_debug"})
public final class Constants {
    @org.jetbrains.annotations.NotNull()
    public static final com.mobileer.core.Constants INSTANCE = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String LATENCY_CALCULATOR_LOGS = "LATENCY_CALCULATOR_LOGS";
    public static com.mobileer.domain.models.LatencyModel LATENCY_MODEL;
    private static double DEVICE_HARDWARE_LATENCY = 0.0;
    @org.jetbrains.annotations.NotNull()
    private static java.lang.String DEVICENAME = "";
    
    private Constants() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mobileer.domain.models.LatencyModel getLATENCY_MODEL() {
        return null;
    }
    
    public final void setLATENCY_MODEL(@org.jetbrains.annotations.NotNull()
    com.mobileer.domain.models.LatencyModel p0) {
    }
    
    public final double getDEVICE_HARDWARE_LATENCY() {
        return 0.0;
    }
    
    public final void setDEVICE_HARDWARE_LATENCY(double p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDEVICENAME() {
        return null;
    }
    
    public final void setDEVICENAME(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
}