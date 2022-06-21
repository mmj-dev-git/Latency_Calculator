package com.mobileer.domain.helpers

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefMethods @Inject constructor(private val sharedPreferences: SharedPreferences) {
    private val latencySet = "latencySet"
    private val finalLatency = "finalLatency"

    fun isLatencySet(): Boolean {
        return sharedPreferences.getBoolean(latencySet, false)
    }

    fun setLatency(latency: String) {
        sharedPreferences.edit().putBoolean(latencySet, true).commit()
        sharedPreferences.edit().putString(finalLatency, latency).commit()
    }

    fun getLatency(): String? {
        return sharedPreferences.getString(finalLatency, "-1.0")
    }
}