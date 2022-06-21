package com.mobileer.domain.LatencyMain

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.ViewModel
import com.confuadhoc.cleanArchitecture.domain.server.FirebaseServer
import com.mobileer.core.Constants
import com.mobileer.core.Constants.DEVICE_HARDWARE_LATENCY
import com.mobileer.core.Constants.LATENCY_CALCULATOR_LOGS
import com.mobileer.domain.useCase.LatencyUseCase
import com.mobileer.oboetester.OboeTesterDataCollectorCallBack
import dagger.hilt.android.lifecycle.HiltViewModel
import com.mobileer.domain.inerface.CalculateLatencyCallBack
import one.younite.feature_firebase.domain.interfaces.LatencyFromFBResponse
import javax.inject.Inject

@HiltViewModel
class LatencyViewModel @Inject constructor(private val latencyUseCase: LatencyUseCase) : ViewModel(), LatencyFromFBResponse, OboeTesterDataCollectorCallBack,
    CalculateLatencyCallBack {

    private lateinit var latencyClass: Activity

    fun getDeviceLatency(latencyClass: Activity) {
        this.latencyClass = latencyClass
        if (latencyUseCase.isLatencyAvailableLocally()) {
            DEVICE_HARDWARE_LATENCY = latencyUseCase.getLatencyAvailableLocally()
            Log.i(LATENCY_CALCULATOR_LOGS, "Latency Set From Local : $DEVICE_HARDWARE_LATENCY")
        } else {
            Log.i(LATENCY_CALCULATOR_LOGS, "Latency Not Found Locally")
            FirebaseServer().readLatency(this,latencyUseCase.getDeviceName())
//            latencyUseCase.gatherData(latencyClass, this)
        }
    }

    override fun setLatencyFromFireBase(Latency: Double) {
        DEVICE_HARDWARE_LATENCY = Latency
        Log.i(LATENCY_CALCULATOR_LOGS, "Latency available in FireBase : $DEVICE_HARDWARE_LATENCY")
        latencyUseCase.setLatencyLocally(Latency)
    }

    override fun latencyNotFoundInFireBase() {
        Log.i(LATENCY_CALCULATOR_LOGS, "Latency Not Found in FireBase : Calculating")
        latencyUseCase.gatherData(latencyClass, this)

    }

    override fun OnDataCollectionComplete() {
        Log.i(LATENCY_CALCULATOR_LOGS, "OnDataCollectionComplete Successfully")
        latencyUseCase.calculateLatency(latencyUseCase.getDeviceName(), getFlag45(), getFlag20(), this)

    }

    override fun OnDataCollectionFailed() {
        Log.i(LATENCY_CALCULATOR_LOGS, "Latency Calculation Failed")
    }

    override fun onLatencyCalculated(Latency: Double) {
        Log.i(LATENCY_CALCULATOR_LOGS, "Latency Calculated Complete")
        Constants.LATENCY_MODEL.finalLatency = Latency
        latencyUseCase.setLatencyLocally(Latency)
        latencyUseCase.setLatencyToFirebase(latencyUseCase.getDeviceName())
    }

    private fun getFlag45(): Double {
        if (latencyClass.packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_LOW_LATENCY)) {
            return 1.0
        } else {
            return 0.0
        }
    }

    private fun getFlag20(): Double {
        if (latencyClass.getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUDIO_PRO)) {
            return 1.0
        } else {
            return 0.0
        }
    }


}