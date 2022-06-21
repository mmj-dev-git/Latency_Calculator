package com.mobileer.domain.models;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u001a\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001Bm\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0016\u0010\n\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u000bj\b\u0012\u0004\u0012\u00020\u0003`\f\u0012\u0016\u0010\r\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u000bj\b\u0012\u0004\u0012\u00020\u0003`\f\u0012\u0006\u0010\u000e\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010 \u001a\u00020\u0003H\u00c6\u0003J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\u0019\u0010\"\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u000bj\b\u0012\u0004\u0012\u00020\u0003`\fH\u00c6\u0003J\u0019\u0010#\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u000bj\b\u0012\u0004\u0012\u00020\u0003`\fH\u00c6\u0003J\t\u0010$\u001a\u00020\u0005H\u00c6\u0003J\u0083\u0001\u0010%\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\u0018\b\u0002\u0010\n\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u000bj\b\u0012\u0004\u0012\u00020\u0003`\f2\u0018\b\u0002\u0010\r\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u000bj\b\u0012\u0004\u0012\u00020\u0003`\f2\b\b\u0002\u0010\u000e\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010&\u001a\u00020\'2\b\u0010(\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010)\u001a\u00020*H\u00d6\u0001J\t\u0010+\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014R\u0011\u0010\u000e\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0014R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0011R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0011R!\u0010\n\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u000bj\b\u0012\u0004\u0012\u00020\u0003`\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR!\u0010\r\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u000bj\b\u0012\u0004\u0012\u00020\u0003`\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001a\u00a8\u0006,"}, d2 = {"Lcom/mobileer/domain/models/LatencyDataClass;", "", "BrustSize", "", "VoiceRecLatencyCommunication", "", "VoiceRecLatencyNormal", "MMAPValue", "flag_45", "flag_20", "voiceComInput", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "voiceComOutput", "finalLatency", "(DLjava/lang/String;Ljava/lang/String;DDDLjava/util/ArrayList;Ljava/util/ArrayList;Ljava/lang/String;)V", "getBrustSize", "()D", "getMMAPValue", "getVoiceRecLatencyCommunication", "()Ljava/lang/String;", "getVoiceRecLatencyNormal", "getFinalLatency", "getFlag_20", "getFlag_45", "getVoiceComInput", "()Ljava/util/ArrayList;", "getVoiceComOutput", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "", "toString", "oboeTester_debug"})
public final class LatencyDataClass {
    private final double BrustSize = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String VoiceRecLatencyCommunication = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String VoiceRecLatencyNormal = null;
    private final double MMAPValue = 0.0;
    private final double flag_45 = 0.0;
    private final double flag_20 = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.ArrayList<java.lang.Double> voiceComInput = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.ArrayList<java.lang.Double> voiceComOutput = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String finalLatency = null;
    
    @org.jetbrains.annotations.NotNull()
    public final com.mobileer.domain.models.LatencyDataClass copy(double BrustSize, @org.jetbrains.annotations.NotNull()
    java.lang.String VoiceRecLatencyCommunication, @org.jetbrains.annotations.NotNull()
    java.lang.String VoiceRecLatencyNormal, double MMAPValue, double flag_45, double flag_20, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<java.lang.Double> voiceComInput, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<java.lang.Double> voiceComOutput, @org.jetbrains.annotations.NotNull()
    java.lang.String finalLatency) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String toString() {
        return null;
    }
    
    public LatencyDataClass(double BrustSize, @org.jetbrains.annotations.NotNull()
    java.lang.String VoiceRecLatencyCommunication, @org.jetbrains.annotations.NotNull()
    java.lang.String VoiceRecLatencyNormal, double MMAPValue, double flag_45, double flag_20, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<java.lang.Double> voiceComInput, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<java.lang.Double> voiceComOutput, @org.jetbrains.annotations.NotNull()
    java.lang.String finalLatency) {
        super();
    }
    
    public final double component1() {
        return 0.0;
    }
    
    public final double getBrustSize() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getVoiceRecLatencyCommunication() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getVoiceRecLatencyNormal() {
        return null;
    }
    
    public final double component4() {
        return 0.0;
    }
    
    public final double getMMAPValue() {
        return 0.0;
    }
    
    public final double component5() {
        return 0.0;
    }
    
    public final double getFlag_45() {
        return 0.0;
    }
    
    public final double component6() {
        return 0.0;
    }
    
    public final double getFlag_20() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<java.lang.Double> component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<java.lang.Double> getVoiceComInput() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<java.lang.Double> component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<java.lang.Double> getVoiceComOutput() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFinalLatency() {
        return null;
    }
}