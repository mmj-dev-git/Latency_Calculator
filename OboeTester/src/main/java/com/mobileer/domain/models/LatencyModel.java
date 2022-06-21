package com.mobileer.domain.models;

import java.util.ArrayList;

public class LatencyModel {
    public double getBurstSize() {
        return burstSize;
    }

    public void setBurstSize(double burstSize) {
        this.burstSize = burstSize;
    }

    public double getFinalLatency() {
        return finalLatency;
    }

    public void setFinalLatency(double finalLatency) {
        this.finalLatency = finalLatency;
    }

    public double getFlag_20() {
        return flag_20;
    }

    public void setFlag_20(double flag_20) {
        this.flag_20 = flag_20;
    }

    public double getFlag_45() {
        return flag_45;
    }

    public void setFlag_45(double flag_45) {
        this.flag_45 = flag_45;
    }

    public double getmMapValue() {
        return mMapValue;
    }

    public void setmMapValue(double mMapValue) {
        this.mMapValue = mMapValue;
    }

    public ArrayList<Double> getVoiceComInput() {
        return voiceComInput;
    }

    public void setVoiceComInput(ArrayList<Double> voiceComInput) {
        this.voiceComInput = voiceComInput;
    }

    public ArrayList<Double> getVoiceComOutput() {
        return voiceComOutput;
    }

    public void setVoiceComOutput(ArrayList<Double> voiceComOutput) {
        this.voiceComOutput = voiceComOutput;
    }

    public double getVoiceRecLatencyCommunication() {
        return voiceRecLatencyCommunication;
    }

    public void setVoiceRecLatencyCommunication(double voiceRecLatencyCommunication) {
        this.voiceRecLatencyCommunication = voiceRecLatencyCommunication;
    }

    public double getVoiceRecLatencyNormal() {
        return voiceRecLatencyNormal;
    }

    public void setVoiceRecLatencyNormal(double voiceRecLatencyNormal) {
        this.voiceRecLatencyNormal = voiceRecLatencyNormal;
    }

    double burstSize;
    double finalLatency;
    double flag_20;
    double flag_45;
    double mMapValue;
    ArrayList<Double> voiceComInput;
    ArrayList<Double> voiceComOutput;
    double voiceRecLatencyCommunication;
    double voiceRecLatencyNormal;


}
