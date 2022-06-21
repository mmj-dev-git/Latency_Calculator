package com.mobileer.domain.useCase;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J&\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\rJ\u0016\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\u0006\u0010\u0013\u001a\u00020\bJ\u0006\u0010\u0014\u001a\u00020\nJ\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\nJ\u000e\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/mobileer/domain/useCase/LatencyUseCase;", "", "sharedPrefMethods", "Lcom/mobileer/domain/helpers/SharedPrefMethods;", "(Lcom/mobileer/domain/helpers/SharedPrefMethods;)V", "calculateLatency", "", "deviceName", "", "flag45", "", "flag20", "calculateLatencyCallBack", "Lcom/mobileer/domain/inerface/CalculateLatencyCallBack;", "gatherData", "latencyClass", "Landroid/app/Activity;", "oboeTesterDataCollectorCallBack", "Lcom/mobileer/oboetester/OboeTesterDataCollectorCallBack;", "getDeviceName", "getLatencyAvailableLocally", "isLatencyAvailableLocally", "", "setLatencyLocally", "Latency", "setLatencyToFirebase", "oboeTester_debug"})
public final class LatencyUseCase {
    private final com.mobileer.domain.helpers.SharedPrefMethods sharedPrefMethods = null;
    
    @javax.inject.Inject()
    public LatencyUseCase(@org.jetbrains.annotations.NotNull()
    com.mobileer.domain.helpers.SharedPrefMethods sharedPrefMethods) {
        super();
    }
    
    public final boolean isLatencyAvailableLocally() {
        return false;
    }
    
    public final void setLatencyLocally(double Latency) {
    }
    
    public final double getLatencyAvailableLocally() {
        return 0.0;
    }
    
    public final void gatherData(@org.jetbrains.annotations.NotNull()
    android.app.Activity latencyClass, @org.jetbrains.annotations.NotNull()
    com.mobileer.oboetester.OboeTesterDataCollectorCallBack oboeTesterDataCollectorCallBack) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDeviceName() {
        return null;
    }
    
    public final void calculateLatency(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceName, double flag45, double flag20, @org.jetbrains.annotations.NotNull()
    com.mobileer.domain.inerface.CalculateLatencyCallBack calculateLatencyCallBack) {
    }
    
    public final void setLatencyToFirebase(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceName) {
    }
}