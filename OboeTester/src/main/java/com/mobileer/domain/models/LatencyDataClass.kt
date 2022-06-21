package com.mobileer.domain.models

data class LatencyDataClass(
    val BrustSize: Double,
    val VoiceRecLatencyCommunication: String,
    val VoiceRecLatencyNormal: String,
    val MMAPValue: Double,
    val flag_45: Double,
    val flag_20: Double,
    val voiceComInput: ArrayList<Double>,
    val voiceComOutput: ArrayList<Double>,
    val finalLatency: String
)