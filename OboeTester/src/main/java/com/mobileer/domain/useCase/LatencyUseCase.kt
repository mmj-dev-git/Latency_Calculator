package com.mobileer.domain.useCase

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.confuadhoc.cleanArchitecture.domain.server.FirebaseServer
import com.mobileer.core.CommonMethods
import com.mobileer.core.Constants
import com.mobileer.domain.helpers.SVC
import com.mobileer.domain.helpers.SharedPrefMethods
import com.mobileer.domain.models.LatencyModel
import com.mobileer.oboetester.MainActivity
import com.mobileer.oboetester.OboeTesterDataCollectorCallBack
import com.mobileer.oboetester.RoundTripLatencyActivity
import com.mobileer.domain.inerface.CalculateLatencyCallBack
import javax.inject.Inject

class LatencyUseCase @Inject constructor(private val sharedPrefMethods: SharedPrefMethods) {


    fun isLatencyAvailableLocally(): Boolean {
        return sharedPrefMethods.isLatencySet()
    }

    fun setLatencyLocally(Latency: Double) {
        Log.i(Constants.LATENCY_CALCULATOR_LOGS, "Setting Latency Locally From Firebase Successfully")
         sharedPrefMethods.setLatency(Latency.toString())
    }

    fun getLatencyAvailableLocally(): Double {
        return sharedPrefMethods.getLatency()?.toDouble() ?: -1.0
    }

    fun gatherData(latencyClass: Activity, oboeTesterDataCollectorCallBack: OboeTesterDataCollectorCallBack) {
        Log.i(Constants.LATENCY_CALCULATOR_LOGS, "Starting Calculating Latency")
        Constants.LATENCY_MODEL = LatencyModel()
        RoundTripLatencyActivity.setOboeTesterDataCollectorCallBack(oboeTesterDataCollectorCallBack)
        val intent = Intent(latencyClass, MainActivity::class.java)
        latencyClass.startActivity(intent)
    }

    fun getDeviceName(): String {
        return CommonMethods.getAndroidDeviceName().toString()
    }

    fun calculateLatency(deviceName: String, flag45: Double, flag20: Double,calculateLatencyCallBack: CalculateLatencyCallBack) {

        val svc = SVC(deviceName)
        val latency = svc.predict_latency(Constants.LATENCY_MODEL.burstSize,
            Constants.LATENCY_MODEL.voiceRecLatencyCommunication,
            Constants.LATENCY_MODEL.voiceRecLatencyNormal,
            Constants.LATENCY_MODEL.getmMapValue(),
            flag45,
            flag20,
            Constants.LATENCY_MODEL.voiceComInput,
            Constants.LATENCY_MODEL.voiceComOutput)

        Log.i(Constants.LATENCY_CALCULATOR_LOGS, "Final Latency $latency")

        calculateLatencyCallBack.onLatencyCalculated(latency.toDouble())

    }

    fun setLatencyToFirebase( deviceName: String) {

        val firebaseServer = FirebaseServer()
        firebaseServer.addModelLatency(deviceName, Constants.LATENCY_MODEL as Any)
    }


}