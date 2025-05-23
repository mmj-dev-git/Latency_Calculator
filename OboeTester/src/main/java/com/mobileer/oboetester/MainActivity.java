/*
 * Copyright 2017 The Android Open Source Project
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.airbnb.lottie.LottieAnimationView;

/**
 * Select various Audio tests.
 */

public class MainActivity extends BaseOboeTesterActivity implements RoundTripLatencyCallback{

    private long pressedTime;
    private static final String KEY_TEST_NAME = "test";
    public static final String VALUE_TEST_NAME_LATENCY = "latency";
    public static final String VALUE_TEST_NAME_GLITCH = "glitch";

    static {
        // Must match name in CMakeLists.txt
        System.loadLibrary("oboetester");
    }

    private Spinner mModeSpinner;
    private TextView mCallbackSizeEditor;
    protected TextView mDeviceView;
    private TextView mVersionTextView;
    private TextView mBuildTextView;
    private TextView mBluetoothScoStatusView;
    private Bundle mBundleFromIntent;
    private BroadcastReceiver mScoStateReceiver;
    private CheckBox mWorkaroundsCheckBox;
    private CheckBox mBackgroundCheckBox;
    private static String mVersionText;
//    private LottieAnimationView lottieAnimation;

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            System.exit(0);
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        logScreenSize();

        RoundTripLatencyActivity.setRoundTripLatencyCallback(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            setVolumeMax(true);
        }

        mVersionTextView = (TextView) findViewById(R.id.versionText);
        mCallbackSizeEditor = (TextView) findViewById(R.id.callbackSize);

        mDeviceView = (TextView) findViewById(R.id.deviceView);
        updateNativeAudioUI();

        mModeSpinner = (Spinner) findViewById(R.id.spinnerAudioMode);
        mModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                long mode = mModeSpinner.getSelectedItemId();
                AudioManager myAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                myAudioMgr.setMode((int)mode);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int oboeVersion = OboeAudioStream.getOboeVersionNumber();
            int oboeMajor = (oboeVersion >> 24) & 0xFF;
            int oboeMinor = (oboeVersion >> 16) & 0xFF;
            int oboePatch = oboeVersion & 0xFF;
            mVersionText = "OboeTester (" + pinfo.versionCode + ") v " + pinfo.versionName
                    + ", Oboe v " + oboeMajor + "." + oboeMinor + "." + oboePatch;
            mVersionTextView.setText(mVersionText);
        } catch (PackageManager.NameNotFoundException e) {
            mVersionTextView.setText(e.getMessage());
        }

        mWorkaroundsCheckBox = (CheckBox) findViewById(R.id.boxEnableWorkarounds);
        NativeEngine.setWorkaroundsEnabled(false);

        mBackgroundCheckBox = (CheckBox) findViewById(R.id.boxEnableBackground);

        mBuildTextView = (TextView) findViewById(R.id.text_build_info);
        mBuildTextView.setText(Build.DISPLAY);

        mBluetoothScoStatusView = (TextView) findViewById(R.id.textBluetoothScoStatus);
        mScoStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int state = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, -1);
                if (state == AudioManager.SCO_AUDIO_STATE_CONNECTING) {
                    mBluetoothScoStatusView.setText("CONNECTING");
                } else if (state == AudioManager.SCO_AUDIO_STATE_CONNECTED) {
                    mBluetoothScoStatusView.setText("CONNECTED");
                } else if (state == AudioManager.SCO_AUDIO_STATE_DISCONNECTED) {
                    mBluetoothScoStatusView.setText("DISCONNECTED");
                }
            }
        };

        saveIntentBundleForLaterProcessing(getIntent());

        onSetSpeakerphoneOn();

        //First time when voice rec is not calculated. Go and calculate voice rec latecny
        // Mohsin was herea
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    launchTestThatDoesRecording(RoundTripLatencyActivity.class);
                }
            }, 2000);
    }



    public static String getVersiontext() {
        return mVersionText;
    }

    private void registerScoStateReceiver() {
        registerReceiver(mScoStateReceiver,
                new IntentFilter(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED));
    }

    private void unregisterScoStateReceiver() {
        unregisterReceiver(mScoStateReceiver);
    }

    private void logScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.i(TestAudioActivity.TAG, "Screen size = " + size.x + " * " + size.y);
    }

    @Override
    public void onNewIntent(Intent intent) {
        saveIntentBundleForLaterProcessing(intent);
    }

    // This will get processed during onResume.
    private void saveIntentBundleForLaterProcessing(Intent intent) {
        mBundleFromIntent = intent.getExtras();
    }


    private void processBundleFromIntent() {
        if (mBundleFromIntent == null) {
            return;
        }



        if (mBundleFromIntent.containsKey(KEY_TEST_NAME)) {
            String testName = mBundleFromIntent.getString(KEY_TEST_NAME);
            if (VALUE_TEST_NAME_LATENCY.equals(testName)) {
                Intent intent = new Intent(this, RoundTripLatencyActivity.class);
                intent.putExtras(mBundleFromIntent);
                startActivity(intent);

            } else if (VALUE_TEST_NAME_GLITCH.equals(testName)) {
                Intent intent = new Intent(this, ManualGlitchActivity.class);
                intent.putExtras(mBundleFromIntent);
                startActivity(intent);
            }
        }
        mBundleFromIntent = null;
    }

    @Override
    public void onResume(){
        super.onResume();
        mWorkaroundsCheckBox.setChecked(NativeEngine.areWorkaroundsEnabled());
        processBundleFromIntent();
        registerScoStateReceiver();


    }

    @Override
    public void onPause(){
        unregisterScoStateReceiver();
        super.onPause();
    }

    private void updateNativeAudioUI() {
        AudioManager myAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        String audioManagerSampleRate = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
        String audioManagerFramesPerBurst = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER);
        mDeviceView.setText("Java AudioManager: rate = " + audioManagerSampleRate +
                ", burst = " + audioManagerFramesPerBurst);
    }

    public void onLaunchTestOutput(View view) {
        launchTestActivity(TestOutputActivity.class);
    }

    public void onLaunchTestInput(View view) {
        launchTestThatDoesRecording(TestInputActivity.class);
    }

    public void onLaunchTapToTone(View view) {
        launchTestThatDoesRecording(TapToToneActivity.class);
    }

    public void onLaunchRecorder(View view) {
        launchTestThatDoesRecording(RecorderActivity.class);
    }

    public void onLaunchEcho(View view) {
        launchTestThatDoesRecording(EchoActivity.class);
    }

    public void onLaunchRoundTripLatency(View view) {
        Log.d("CheckKro","data call maro 1");
        launchTestThatDoesRecording(RoundTripLatencyActivity.class);
    }

    public void onLaunchManualGlitchTest(View view) {
        launchTestThatDoesRecording(ManualGlitchActivity.class);
    }

    public void onLaunchAutoGlitchTest(View view) { launchTestThatDoesRecording(AutomatedGlitchActivity.class); }

    public void onLaunchTestDisconnect(View view) {
        launchTestThatDoesRecording(TestDisconnectActivity.class);
    }

    public void onLaunchTestDataPaths(View view) {
        launchTestThatDoesRecording(TestDataPathsActivity.class);
    }

    public void onLaunchTestDeviceReport(View view)  {
        launchTestActivity(DeviceReportActivity.class);
    }

    public void onLaunchExtratests(View view) {
        launchTestActivity(ExtraTestsActivity.class);
    }

    private void applyUserOptions() {
        updateCallbackSize();

        long mode = mModeSpinner.getSelectedItemId();
        AudioManager myAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        myAudioMgr.setMode((int) mode);

        NativeEngine.setWorkaroundsEnabled(mWorkaroundsCheckBox.isChecked());
        TestAudioActivity.setBackgroundEnabled(mBackgroundCheckBox.isChecked());
    }

    @Override
    protected void launchTestActivity(Class clazz) {
        applyUserOptions();
        super.launchTestActivity(clazz);
    }

    public void onUseCallbackClicked(View view) {
        CheckBox checkBox = (CheckBox) view;
        OboeAudioStream.setUseCallback(checkBox.isChecked());
    }

    private void updateCallbackSize() {
        CharSequence chars = mCallbackSizeEditor.getText();
        String text = chars.toString();
        int callbackSize = 0;
        try {
            callbackSize = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            showErrorToast("Badly formated callback size: " + text);
            mCallbackSizeEditor.setText("0");
        }
        OboeAudioStream.setCallbackSize(callbackSize);
    }

    public void onSetSpeakerphoneOn() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.setSpeakerphoneOn);
        boolean enabled = checkBox.isChecked();
        AudioManager myAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        myAudioMgr.setSpeakerphoneOn(enabled);
    }

    public void onStartStopBluetoothSco(View view) {
        CheckBox checkBox = (CheckBox) view;
        AudioManager myAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (checkBox.isChecked()) {
            myAudioMgr.startBluetoothSco();
        } else {
            myAudioMgr.stopBluetoothSco();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setVolumeMax(boolean volumeMax) {
//        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
//
//        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        AudioManager am =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if(volumeMax) {
            am.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                    0);
        }
        else
        {
            am.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    am.getStreamMinVolume(AudioManager.STREAM_MUSIC),
                    0);
        }
    }

    @Override
    public void voiceRecCompleteCallback() {
        // Mohsin was here
//        if((SharedPref.isVoiceRecLatencyNormalSet(getApplication())) && (!SharedPref.isVoiceComLatencySet(getApplication())))
//        {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("CheckKro","data call maro");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        setVolumeMax(false);
                    }
                    launchTestThatDoesRecording(RoundTripLatencyActivity.class);
                }
            }, 2000);
        }

//    }
}
