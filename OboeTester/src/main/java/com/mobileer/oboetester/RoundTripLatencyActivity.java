/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobileer.oboetester;

import static com.mobileer.core.Constants.LATENCY_CALCULATOR_LOGS;
import static com.mobileer.oboetester.OboeTesterGlobal.StatusList;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.mobileer.core.Constants;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Activity to measure latency on a full duplex stream.
 */
public class RoundTripLatencyActivity extends AnalyzerActivity implements inComFailedCallBack {

    private long pressedTime;
    ConstraintLayout latencyConstraint, DoneConstraint;
    // STATEs defined in LatencyAnalyzer.h
    int voiceRecLatencyCounter = 0;
    private static final int STATE_MEASURE_BACKGROUND = 0;
    private static final int STATE_IN_PULSE = 1;
    private static final int STATE_GOT_DATA = 2;
    private final static String LATENCY_FORMAT = "%4.2f";
    // When I use 5.3g I only get one digit after the decimal point!
    private final static String CONFIDENCE_FORMAT = "%5.3f";
    Handler stuckHandler = null;

    private TextView mAnalyzerView;
    private Button mMeasureButton;
    private Button mAverageButton;
    private Button mCancelButton;
    private Button mShareButton;
    private LottieAnimationView lottieAnimation;
    private boolean mHasRecording = false;
    ArrayList<String> voiceRecLatencyList;

    private boolean mTestRunningByIntent;
    private Bundle mBundleFromIntent;
    private int mBufferBursts = -1;
    private Handler mHandler = new Handler(Looper.getMainLooper()); // UI thread
    private boolean mMeasureAverage;
    ProgressBar voiceComProgress; // initiate the progress bar


    public static OboeTesterDataCollectorCallBack oboeTesterDataCollectorCallBackRef;
    public static RoundTripLatencyCallback roundTripLatencyCallbackRef;

    public static void setOboeTesterDataCollectorCallBack(OboeTesterDataCollectorCallBack oboeTesterDataCollectorCallBack) {
        oboeTesterDataCollectorCallBackRef = oboeTesterDataCollectorCallBack;
    }

    public static void setRoundTripLatencyCallback(RoundTripLatencyCallback roundTripLatencyCallback) {
        roundTripLatencyCallbackRef = roundTripLatencyCallback;
    }

    LatencyAverager mLatencyAverager = new LatencyAverager();

    public ArrayList<Double> voiceComInput = new ArrayList();
    public ArrayList<Double> voiceComOutput = new ArrayList();
    public double burstSize;

    @Override
    public void ignoreInCom() {
        voiceRecLatencyCounter = 0;
        Constants.LATENCY_MODEL.setVoiceRecLatencyCommunication(Constants.LATENCY_MODEL.getVoiceRecLatencyNormal());
//        SharedPref.setVoiceRecLatencyCommunication(SharedPref.getVoiceRecLatencyNormal(getApplicationContext()), getApplicationContext());

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("oboeTester", "starting Test for VoiceComLatency");
                roundTripLatencyCallbackRef.voiceRecCompleteCallback();
                finish();
            }
        }, 2000);
    }

    // Run the test several times and report the acverage latency.
    protected class LatencyAverager {
        private final static int AVERAGE_TEST_DELAY_MSEC = 1000; // arbitrary
        private static final int GOOD_RUNS_REQUIRED = 5; // arbitrary
        private static final int MAX_BAD_RUNS_ALLOWED = 5; // arbitrary
        private int mBadCount = 0; // number of bad measurements
        private int mGoodCount = 0; // number of good measurements

        ArrayList<Double> mLatencies = new ArrayList<Double>(GOOD_RUNS_REQUIRED);
        ArrayList<Double> mConfidences = new ArrayList<Double>(GOOD_RUNS_REQUIRED);
        private double mLatencyMin;
        private double mLatencyMax;
        private double mConfidenceSum;
        private boolean mActive;
        private String mLastReport = "";

        // Called on UI thread.
        String onAnalyserDone() {
            String message = "";
            boolean reschedule = false;
            if (!mActive) {
                message = "";
            } else if (getMeasuredResult() != 0) {
                mBadCount++;
                if (mBadCount > MAX_BAD_RUNS_ALLOWED) {
                    cancel();
                    updateButtons(false);
                    message = "averaging cancelled due to error\n";
                } else {
                    message = "skipping this bad run, "
                            + mBadCount + " of " + MAX_BAD_RUNS_ALLOWED + " max\n";
                    reschedule = true;
                }
            } else {
                mGoodCount++;
                double latency = getMeasuredLatencyMillis();
                double confidence = getMeasuredConfidence();
                mLatencies.add(latency);
                mConfidences.add(confidence);
                mConfidenceSum += confidence;
                mLatencyMin = Math.min(mLatencyMin, latency);
                mLatencyMax = Math.max(mLatencyMax, latency);
                if (mGoodCount < GOOD_RUNS_REQUIRED) {
                    reschedule = true;
                } else {
                    mActive = false;
                    updateButtons(false);
                }
                message = reportAverage();
            }
            if (reschedule) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        measureSingleLatency();
                    }
                }, AVERAGE_TEST_DELAY_MSEC);
            }
            return message;
        }

        private String reportAverage() {
            String message;
            if (mGoodCount == 0 || mConfidenceSum == 0.0) {
                message = "num.iterations = " + mGoodCount + "\n";
            } else {
                final double mAverageConfidence = mConfidenceSum / mGoodCount;
                double meanLatency = calculateMeanLatency();
                double meanAbsoluteDeviation = calculateMeanAbsoluteDeviation(meanLatency);
                message = "average.latency.msec = " + String.format(LATENCY_FORMAT, meanLatency) + "\n"
                        + "mean.absolute.deviation = " + String.format(LATENCY_FORMAT, meanAbsoluteDeviation) + "\n"
                        + "average.confidence = " + String.format(CONFIDENCE_FORMAT, mAverageConfidence) + "\n"
                        + "min.latency.msec = " + String.format(LATENCY_FORMAT, mLatencyMin) + "\n"
                        + "max.latency.msec = " + String.format(LATENCY_FORMAT, mLatencyMax) + "\n"
                        + "num.iterations = " + mGoodCount + "\n";
            }
            message += "num.failed = " + mBadCount + "\n";
            mLastReport = message;
            return message;
        }

        private double calculateMeanAbsoluteDeviation(double meanLatency) {
            double deviationSum = 0.0;
            for (double latency : mLatencies) {
                deviationSum += Math.abs(latency - meanLatency);
            }
            return deviationSum / mLatencies.size();
        }

        private double calculateMeanLatency() {
            double latencySum = 0.0;
            for (double latency : mLatencies) {
                latencySum += latency;
            }
            return latencySum / mLatencies.size();
        }

        // Called on UI thread.
        public void start() {
            mLatencies.clear();
            mConfidences.clear();
            mConfidenceSum = 0.0;
            mLatencyMax = Double.MIN_VALUE;
            mLatencyMin = Double.MAX_VALUE;
            mBadCount = 0;
            mGoodCount = 0;
            mActive = true;
            mLastReport = "";
            measureSingleLatency();
        }

        public void clear() {
            mActive = false;
            mLastReport = "";
        }

        public void cancel() {
            mActive = false;
        }

        public boolean isActive() {
            return mActive;
        }

        public String getLastReport() {
            return mLastReport;
        }
    }

    public void SetTestDoneStatus() {
        latencyConstraint.setVisibility(View.GONE);
        DoneConstraint.setVisibility(View.VISIBLE);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                oboeTesterDataCollectorCallBackRef.OnDataCollectionComplete();
            }
        }, 1000);


    }

    // Periodically query the status of the stream.
    protected class LatencySniffer {
        private int counter = 0;
        public static final int SNIFFER_UPDATE_PERIOD_MSEC = 150;
        public static final int SNIFFER_UPDATE_DELAY_MSEC = 300;


        // Display status info for the stream.
        private final Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                String message = "";


                if (isAnalyzerDone()) {

                    message = mLatencyAverager.onAnalyserDone();
                    message += onAnalyzerDone();

                    if (NativeEngine.isMMapSupported()) {
                        Constants.LATENCY_MODEL.setmMapValue(1.0);
                        // SharedPref.setMMapSupported("1.0", getApplicationContext());
                    }

                    // Mohsin was here
                    if ((Constants.LATENCY_MODEL.getVoiceRecLatencyNormal()==0.0)) {
//                    if ((!SharedPref.isVoiceRecLatencyNormalSet(getApplication()))) {
                        if (message.contains("result.text = OK")) {
                            Constants.LATENCY_MODEL.setVoiceRecLatencyNormal(getVoiceRecLatency(message));
//                            SharedPref.setVoiceRecLatencyNormal(getVoiceRecLatency(message), getApplicationContext());

                            AudioManager myAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                            myAudioMgr.setMode((int) 3);

                            Log.i(LATENCY_CALCULATOR_LOGS, "VoiceRecLatencyNormal Set: " +  Constants.LATENCY_MODEL.getVoiceRecLatencyNormal());
                            voiceRecLatencyCounter = 0;

                            Log.i(LATENCY_CALCULATOR_LOGS, "stuckHandler countdown Started");
                            // handler for stuck case.. remove InCommuncation voiceRec if it is stucked.
                            stuckHandler = new Handler(Looper.getMainLooper());
                            String finalMessage = message;
                            stuckHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Constants.LATENCY_MODEL.setVoiceRecLatencyCommunication(getVoiceRecLatency(finalMessage));
//                                    SharedPref.setVoiceRecLatencyCommunication(getVoiceRecLatency(finalMessage), getApplicationContext());

                                    Log.i(LATENCY_CALCULATOR_LOGS, "VoiceRecLatencyCommunication cancelled due to error. Setting Normal mode value");
                                    Log.i(LATENCY_CALCULATOR_LOGS, "VoiceRecLatencyCommunication Set: " + getVoiceRecLatency(finalMessage));

                                    voiceRecLatencyCounter = 0;

                                    Log.i(LATENCY_CALCULATOR_LOGS, "starting Test for VoiceComLatency");
                                    roundTripLatencyCallbackRef.voiceRecCompleteCallback();
                                    finish();
                                }
                            }, 20000);

                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Log.i(LATENCY_CALCULATOR_LOGS, "starting Test for VoiceRecLatencyCommunication");
                                    onMeasure(null);
                                }
                            }, 2000);


                        }
                        else if (voiceRecLatencyCounter < 2) {
                            Log.i(LATENCY_CALCULATOR_LOGS, "VoiceRecLatencyNormal Test failed trying again: test " + voiceRecLatencyCounter+1);
                            voiceRecLatencyCounter++;
                            onMeasure(null);
                        }
                        else {
                            voiceRecLatencyCounter = 0;
                            Log.i(LATENCY_CALCULATOR_LOGS, "VoiceRecLatencyNormal Test both times failed.. setting -1");
                            Constants.LATENCY_MODEL.setVoiceRecLatencyNormal(-1.0);
//                          SharedPref.setVoiceRecLatencyNormal("-1", getApplicationContext());
                            onMeasure(null);
                        }
                    }
                    else if ((Constants.LATENCY_MODEL.getVoiceRecLatencyCommunication()==0.0)) {
//                    else if ((!SharedPref.isVoiceRecLatencyCommunicationSet(getApplication()))) {
                        if (message.contains("result.text = OK")) {

                            stuckHandler.removeCallbacksAndMessages(null);
                            Log.i(LATENCY_CALCULATOR_LOGS, "stuckHandler countdown cancelled because all going good");

                            Constants.LATENCY_MODEL.setVoiceRecLatencyCommunication(getVoiceRecLatency(message));
//                            SharedPref.setVoiceRecLatencyCommunication(getVoiceRecLatency(message), getApplicationContext());

                            Log.i(LATENCY_CALCULATOR_LOGS, "VoiceRecLatencyCommunication set :" + Constants.LATENCY_MODEL.getVoiceRecLatencyCommunication());
                            voiceRecLatencyCounter = 0;

                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i(LATENCY_CALCULATOR_LOGS, "starting Test for VoiceComLatency");

                                    roundTripLatencyCallbackRef.voiceRecCompleteCallback();
                                    finish();
                                }
                            }, 2000);
                        }
                        else if (voiceRecLatencyCounter < 2) {
                            Log.i(LATENCY_CALCULATOR_LOGS, "VoiceRecLatencyCommunication Test failed trying again");
                            voiceRecLatencyCounter++;
                            onMeasure(null);
                        }
                        else {
                            voiceRecLatencyCounter = 0;
                            Log.i(LATENCY_CALCULATOR_LOGS, "VoiceRecLatencyCommunication Test both times failed.. setting " + Constants.LATENCY_MODEL.getVoiceRecLatencyNormal());
                            Constants.LATENCY_MODEL.setVoiceRecLatencyCommunication(Constants.LATENCY_MODEL.getVoiceRecLatencyNormal());

                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i(LATENCY_CALCULATOR_LOGS, "starting Test for VoiceComLatency");
                                    roundTripLatencyCallbackRef.voiceRecCompleteCallback();
                                    finish();
                                }
                            }, 2000);
                        }
                    }
                    else {
                        AudioManager myAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                        myAudioMgr.setMode((int) 3);

                        try {
                            voiceComProgress.setProgress(voiceComProgress.getProgress() + 1);
                        } catch (Exception e) {
                            Log.i(LATENCY_CALCULATOR_LOGS, e.getLocalizedMessage());
                        }

                        voiceComInput.add(getVoiceComLatency(StatusList.get(StatusList.size() - 2)));
                        voiceComOutput.add(getVoiceComLatency(StatusList.get(StatusList.size() - 1)));

                        Log.i(LATENCY_CALCULATOR_LOGS, "VoiceComLatency in " + (getVoiceComLatency(StatusList.get(StatusList.size() - 2)) + " out " + getVoiceComLatency(StatusList.get(StatusList.size() - 1))));

//
                        if (message.contains("result.text = OK") && voiceComInput.size() >= 5) {

                            Constants.LATENCY_MODEL.setVoiceComInput(voiceComInput);
                            Constants.LATENCY_MODEL.setVoiceComOutput(voiceComOutput);
//                            SharedPref.setVoiceComLatency(getApplication());

                            Log.i(LATENCY_CALCULATOR_LOGS, "VoiceComInput set : "+ voiceComInput);
                            Log.i(LATENCY_CALCULATOR_LOGS, "voiceComOutput set : "+ voiceComOutput);

                            Constants.LATENCY_MODEL.setBurstSize(getBurstSize(StatusList.get(StatusList.size() - 1)));
                            // to get burst size from voiceCom output
//                            burstSize = Double.parseDouble(getBurstSize(StatusList.get(StatusList.size() - 1)));
                            Log.i(LATENCY_CALCULATOR_LOGS, "BurstSize set : "+ Constants.LATENCY_MODEL.getBurstSize());

                            SetTestDoneStatus();
                        }
                        else if (voiceComInput.size() > 5) {
                            Constants.LATENCY_MODEL.setVoiceComInput(voiceComInput);
                            Constants.LATENCY_MODEL.setVoiceComOutput(voiceComOutput);
//                            SharedPref.setVoiceComLatency(getApplication());

                            Log.i(LATENCY_CALCULATOR_LOGS, "VoiceComInput set : "+ voiceComInput);
                            Log.i(LATENCY_CALCULATOR_LOGS, "voiceComOutput set : "+ voiceComOutput);

                            Constants.LATENCY_MODEL.setBurstSize(getBurstSize(StatusList.get(StatusList.size() - 1)));
                            // to get burst size from voiceCom output
//                            burstSize = Double.parseDouble(getBurstSize(StatusList.get(StatusList.size() - 1)));
                            Log.i(LATENCY_CALCULATOR_LOGS, "BurstSize set : "+ Constants.LATENCY_MODEL.getBurstSize());

                            SetTestDoneStatus();
                        }
                    }


                } else {
                    message = getProgressText();
                    message += "please wait... " + counter + "\n";
                    message += convertStateToString(getAnalyzerState());

                    mHandler.postDelayed(runnableCode, SNIFFER_UPDATE_PERIOD_MSEC);

                }
                setAnalyzerText(message);
                counter++;
            }
        };

        public Double getVoiceRecLatency(String msg) {
            try {
                String[] result = msg.split("=");

                String[] ResultOK = result[2].split("\n");
                if (ResultOK[0].trim().equalsIgnoreCase("OK")) {
                    String[] split = result[3].split("\n");
                    return Double.valueOf(split[0]);
                }
                Log.e(LATENCY_CALCULATOR_LOGS, "getVoiceRecLatency Exception -> setting -1");
                return -1.0;
            } catch (Exception e) {
                Log.e(LATENCY_CALCULATOR_LOGS, "getVoiceRecLatency Exception ->" + e.getMessage());
                return -1.0;
            }

        }

        private void startSniffer() {
            counter = 0;
            // Start the initial runnable task by posting through the handler
            mHandler.postDelayed(runnableCode, SNIFFER_UPDATE_DELAY_MSEC);
        }

        private void stopSniffer() {
            if (mHandler != null) {
                mHandler.removeCallbacks(runnableCode);
            }
        }

        public Double getVoiceComLatency(String Value) {
            try {
                String[] split;
                split = Value.split("=");
                String[] inputTimeStamLatency = split[1].split("\n");
                String[] midValue = inputTimeStamLatency[0].split("/");
                return Double.valueOf(midValue[1]);
            } catch (Exception e) {
                Log.e(TAG, "getVoiceComLatency Exception ->" + e.getMessage());
                return -1.0;
            }
        }

        public Double getBurstSize(String value) {
            try {
                String[] result = value.split("\\* ");

                String[] ResultOK = result[1].split("\\)");
                String BurstSize = ResultOK[0];
                Log.d(TAG, "BurstSize " + BurstSize);

                return Double.valueOf(BurstSize.trim());
            } catch (Exception e) {
                Log.e(TAG, "getVoiceComLatency Exception ->" + e.getMessage());
                return -1.0;
            }

        }
    }


    static String convertStateToString(int state) {
        switch (state) {
            case STATE_MEASURE_BACKGROUND:
                return "BACKGROUND";
            case STATE_IN_PULSE:
                return "RECORDING";
            case STATE_GOT_DATA:
                return "ANALYZING";
            default:
                return "DONE";
        }
    }

    private String getProgressText() {
        int progress = getAnalyzerProgress();
        int state = getAnalyzerState();
        int resetCount = getResetCount();
        String message = String.format("progress = %d\nstate = %d\n#resets = %d\n",
                progress, state, resetCount);
        message += mLatencyAverager.getLastReport();
        return message;
    }

    private String onAnalyzerDone() {
        String message = getResultString();
        if (mTestRunningByIntent) {
            String report = getCommonTestReport();
            report += message;
            maybeWriteTestResult(report);
        }
        mTestRunningByIntent = false;
        mHasRecording = true;
        stopAudioTest();
        return message;
    }

    @NonNull
    private String getResultString() {
        int result = getMeasuredResult();
        int resetCount = getResetCount();
        double confidence = getMeasuredConfidence();
        String message = "";

        message += String.format("confidence = " + CONFIDENCE_FORMAT + "\n", confidence);
        message += String.format("result.text = %s\n", resultCodeToString(result));

        // Only report valid latencies.
        if (result == 0) {
            int latencyFrames = getMeasuredLatency();
            double latencyMillis = getMeasuredLatencyMillis();
            int bufferSize = mAudioOutTester.getCurrentAudioStream().getBufferSizeInFrames();
            int latencyEmptyFrames = latencyFrames - bufferSize;
            double latencyEmptyMillis = latencyEmptyFrames * 1000.0 / getSampleRate();
            message += String.format("latency.msec = " + LATENCY_FORMAT + "\n", latencyMillis);
            message += String.format("latency.frames = %d\n", latencyFrames);
            message += String.format("latency.empty.msec = " + LATENCY_FORMAT + "\n", latencyEmptyMillis);
            message += String.format("latency.empty.frames = %d\n", latencyEmptyFrames);
        }

        message += String.format("rms.signal = %7.5f\n", getSignalRMS());
        message += String.format("rms.noise = %7.5f\n", getBackgroundRMS());
        message += String.format("reset.count = %d\n", resetCount);
        message += String.format("result = %d\n", result);

        return message;
    }

    private LatencySniffer mLatencySniffer = new LatencySniffer();

    native int getAnalyzerProgress();

    native int getMeasuredLatency();

    double getMeasuredLatencyMillis() {
        return getMeasuredLatency() * 1000.0 / getSampleRate();
    }

    native double getMeasuredConfidence();

    native double getBackgroundRMS();

    native double getSignalRMS();

    private void setAnalyzerText(String s) {
        mAnalyzerView.setText(s);

//
//        finish();
//        Toast.makeText(getApplicationContext(), "Bye bye", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void inflateActivity() {
        setContentView(R.layout.activity_rt_latency);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        voiceRecLatencyList = new ArrayList<>();
        voiceComProgress = (ProgressBar) findViewById(R.id.voiceComProgress);


        DoneConstraint = findViewById(R.id.DoneConstraint);
        latencyConstraint = findViewById(R.id.latencyConstraint);

        mMeasureButton = (Button) findViewById(R.id.button_measure);
        mAverageButton = (Button) findViewById(R.id.button_average);
        mCancelButton = (Button) findViewById(R.id.button_cancel);
        mShareButton = (Button) findViewById(R.id.button_share);
        lottieAnimation = (LottieAnimationView) findViewById(R.id.iv_wave_lottie);
        mShareButton.setEnabled(false);
        mAnalyzerView = (TextView) findViewById(R.id.text_status);
        mAnalyzerView.setMovementMethod(new ScrollingMovementMethod());
        updateEnabledWidgets();

        hideSettingsViews();

        mBufferSizeView.setFaderNormalizedProgress(0.0); // for lowest latency

        mBundleFromIntent = getIntent().getExtras();


        //         start onmeasure function after given seconds
        if (Constants.LATENCY_MODEL.getVoiceRecLatencyNormal()==0.0) {
//        if ((!SharedPref.isVoiceRecLatencyNormalSet(getApplication()))) {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lottieAnimation.setVisibility(View.VISIBLE);
                    lottieAnimation.loop(true);
                    lottieAnimation.playAnimation();
                    voiceComProgress.setVisibility(View.GONE);
                    mLatencyAverager.clear();
                    measureSingleLatency();

                }
            }, 300);
        } else {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lottieAnimation.setVisibility(View.GONE);
                    voiceComProgress.setVisibility(View.VISIBLE);
                    mLatencyAverager.clear();
                    mLatencyAverager.start();

                }
            }, 300);
        }
    }

    public void configrationForOboeTester() {
        updateEnabledWidgets();
        hideSettingsViews();

        try {
            mBufferSizeView.setFaderNormalizedProgress(0.0);
        } catch (Exception e) {

        }


        mLatencyAverager.clear();
        measureSingleLatency();
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            oboeTesterDataCollectorCallBackRef.OnDataCollectionFailed();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    @Override
    public void onNewIntent(Intent intent) {
        mBundleFromIntent = intent.getExtras();
    }

    @Override
    int getActivityType() {
        return ACTIVITY_RT_LATENCY;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHasRecording = false;
        updateButtons(false);
    }

    private void processBundleFromIntent() {
        if (mBundleFromIntent == null) {
            return;
        }
        if (mTestRunningByIntent) {
            return;
        }

        mResultFileName = null;
        if (mBundleFromIntent.containsKey(KEY_FILE_NAME)) {
            mTestRunningByIntent = true;
            mResultFileName = mBundleFromIntent.getString(KEY_FILE_NAME);
            getFirstInputStreamContext().configurationView.setExclusiveMode(true);
            getFirstOutputStreamContext().configurationView.setExclusiveMode(true);
            mBufferBursts = mBundleFromIntent.getInt(KEY_BUFFER_BURSTS, mBufferBursts);

            // Delay the test start to avoid race conditions.
            Handler handler = new Handler(Looper.getMainLooper()); // UI thread
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startAutomaticTest();
                }
            }, 500); // TODO where is the race, close->open?
        }
    }

    @Override
    public boolean isTestConfiguredUsingBundle() {
        return mBundleFromIntent != null;
    }

    void startAutomaticTest() {
        try {


            configureStreamsFromBundle(mBundleFromIntent);
            onMeasure(null);
        } finally {
            mBundleFromIntent = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        processBundleFromIntent();
//        lottieAnimation.loop(true);
//        lottieAnimation.playAnimation();
    }

    @Override
    protected void onStop() {
        mLatencySniffer.stopSniffer();
        super.onStop();
    }

    public void onMeasure(View view) {
        mLatencyAverager.clear();
        measureSingleLatency();
    }

    void updateButtons(boolean running) {
        boolean busy = running || mLatencyAverager.isActive();
        mMeasureButton.setEnabled(!busy);
        mAverageButton.setEnabled(!busy);
        mCancelButton.setEnabled(running);
        mShareButton.setEnabled(!busy && mHasRecording);
    }

    public void measureSingleLatency() {
        try {
            openAudio();
            if (mBufferBursts >= 0) {
                AudioStreamBase stream = mAudioOutTester.getCurrentAudioStream();
                int framesPerBurst = stream.getFramesPerBurst();
                stream.setBufferSizeInFrames(framesPerBurst * mBufferBursts);
                // override buffer size fader
                mBufferSizeView.setEnabled(false);
                mBufferBursts = -1;
            }
            startAudio(this);
            mLatencySniffer.startSniffer();
//            updateButtons(true);
        } catch (IOException e) {
            showErrorToast(e.getMessage());
        }
    }

    public void onAverage(View view) {
        mLatencyAverager.start();
    }

    public void onCancel(View view) {
        mLatencyAverager.cancel();
        stopAudioTest();
    }

    // Call on UI thread
    public void stopAudioTest() {
        mLatencySniffer.stopSniffer();
        stopAudio();
        closeAudio();
        updateButtons(false);
    }

    @Override
    String getWaveTag() {
        return "rtlatency";
    }

    @Override
    boolean isOutput() {
        return false;
    }

    @Override
    public void setupEffects(int sessionId) {
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setVolumeMax(boolean volumeMax) {
//        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
//
//        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        AudioManager am =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (volumeMax) {
            am.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                    0);
        } else {
            am.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    am.getStreamMinVolume(AudioManager.STREAM_MUSIC),
                    0);
        }
    }


}
