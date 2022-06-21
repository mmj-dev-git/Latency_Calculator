package com.mobileer.domain.LatencyMain;

import java.lang.System;

@dagger.hilt.android.lifecycle.HiltViewModel()
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004B\u000f\b\u0007\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\b\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\u000bH\u0016J\u000e\u0010\r\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\tJ\b\u0010\u000e\u001a\u00020\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u000fH\u0002J\b\u0010\u0011\u001a\u00020\u000bH\u0016J\u0010\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u000fH\u0016J\u0010\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u000fH\u0016R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/mobileer/domain/LatencyMain/LatencyViewModel;", "Landroidx/lifecycle/ViewModel;", "Lone/younite/feature_firebase/domain/interfaces/LatencyFromFBResponse;", "Lcom/mobileer/oboetester/OboeTesterDataCollectorCallBack;", "Lcom/mobileer/domain/inerface/CalculateLatencyCallBack;", "latencyUseCase", "Lcom/mobileer/domain/useCase/LatencyUseCase;", "(Lcom/mobileer/domain/useCase/LatencyUseCase;)V", "latencyClass", "Landroid/app/Activity;", "OnDataCollectionComplete", "", "OnDataCollectionFailed", "getDeviceLatency", "getFlag20", "", "getFlag45", "latencyNotFoundInFireBase", "onLatencyCalculated", "Latency", "setLatencyFromFireBase", "oboeTester_debug"})
public final class LatencyViewModel extends androidx.lifecycle.ViewModel implements one.younite.feature_firebase.domain.interfaces.LatencyFromFBResponse, com.mobileer.oboetester.OboeTesterDataCollectorCallBack, com.mobileer.domain.inerface.CalculateLatencyCallBack {
    private final com.mobileer.domain.useCase.LatencyUseCase latencyUseCase = null;
    private android.app.Activity latencyClass;
    
    @javax.inject.Inject()
    public LatencyViewModel(@org.jetbrains.annotations.NotNull()
    com.mobileer.domain.useCase.LatencyUseCase latencyUseCase) {
        super();
    }
    
    public final void getDeviceLatency(@org.jetbrains.annotations.NotNull()
    android.app.Activity latencyClass) {
    }
    
    @java.lang.Override()
    public void setLatencyFromFireBase(double Latency) {
    }
    
    @java.lang.Override()
    public void latencyNotFoundInFireBase() {
    }
    
    @java.lang.Override()
    public void OnDataCollectionComplete() {
    }
    
    @java.lang.Override()
    public void OnDataCollectionFailed() {
    }
    
    @java.lang.Override()
    public void onLatencyCalculated(double Latency) {
    }
    
    private final double getFlag45() {
        return 0.0;
    }
    
    private final double getFlag20() {
        return 0.0;
    }
}