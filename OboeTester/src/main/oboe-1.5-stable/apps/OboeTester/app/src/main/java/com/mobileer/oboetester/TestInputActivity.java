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

import android.content.Intent;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import android.view.View;
import android.widget.RadioButton;

import java.io.File;
import java.io.IOException;

/**
 * Test Oboe Capture
 */

public class TestInputActivity  extends TestAudioActivity {

    protected AudioInputTester mAudioInputTester;
    private static final int NUM_VOLUME_BARS = 4;
    private VolumeBarView[] mVolumeBars = new VolumeBarView[NUM_VOLUME_BARS];
    private InputMarginView mInputMarginView;
    private int mInputMarginBursts = 0;
    private WorkloadView mWorkloadView;

    public native void setMinimumFramesBeforeRead(int frames);
    public native int saveWaveFile(String absolutePath);

    @Override boolean isOutput() { return false; }

    @Override
    protected void inflateActivity() {
        setContentView(R.layout.activity_test_input);

        BufferSizeView bufferSizeView = findViewById(R.id.buffer_size_view);
        bufferSizeView.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVolumeBars[0] = (VolumeBarView) findViewById(R.id.volumeBar0);
        mVolumeBars[1] = (VolumeBarView) findViewById(R.id.volumeBar1);
        mVolumeBars[2] = (VolumeBarView) findViewById(R.id.volumeBar2);
        mVolumeBars[3] = (VolumeBarView) findViewById(R.id.volumeBar3);

        mInputMarginView = (InputMarginView) findViewById(R.id.input_margin_view);

        updateEnabledWidgets();

        mAudioInputTester = addAudioInputTester();

        mWorkloadView = (WorkloadView) findViewById(R.id.workload_view);
        if (mWorkloadView != null) {
            mWorkloadView.setAudioStreamTester(mAudioInputTester);
        }
    }

    @Override
    int getActivityType() {
        return ACTIVITY_TEST_INPUT;
    }

    @Override
    protected void resetConfiguration() {
        super.resetConfiguration();
        mAudioInputTester.reset();
    }

    @Override
    void updateStreamDisplay() {
        int numChannels = mAudioInputTester.getCurrentAudioStream().getChannelCount();
        if (numChannels > NUM_VOLUME_BARS) {
            numChannels = NUM_VOLUME_BARS;
        }
        for (int i = 0; i < numChannels; i++) {
            if (mVolumeBars[i] == null) break;
            double level = mAudioInputTester.getPeakLevel(i);
            mVolumeBars[i].setAmplitude((float) level);
        }
    }

    void resetVolumeBars() {
        for (int i = 0; i < mVolumeBars.length; i++) {
            if (mVolumeBars[i] == null) break;
            mVolumeBars[i].setAmplitude((float) 0.0);
        }
    }

    void setMinimumBurstsBeforeRead(int numBursts) {
        int framesPerBurst = mAudioInputTester.getCurrentAudioStream().getFramesPerBurst();
        if (framesPerBurst > 0) {
            setMinimumFramesBeforeRead(numBursts * framesPerBurst);
        }
    }

    @Override
    public void openAudio(View view) {
        try {
            openAudio();
        } catch (Exception e) {
            showErrorToast(e.getMessage());
        }
    }

    @Override
    public void openAudio() throws IOException {
        super.openAudio();
        setMinimumBurstsBeforeRead(mInputMarginBursts);
        resetVolumeBars();
    }

    @Override
    public void stopAudio() {
        super.stopAudio();
        resetVolumeBars();
    }

    @Override
    protected void toastPauseError(int result) {
        showToast("Pause not implemented. Returned " + result);
    }


    public void setupAGC(int sessionId) {
        AutomaticGainControl effect =  AutomaticGainControl.create(sessionId);
    }

    public void setupAEC(int sessionId) {
        AcousticEchoCanceler effect =  AcousticEchoCanceler.create(sessionId);
    }

    @Override
    public void setupEffects(int sessionId) {
        setupAEC(sessionId);
    }

    protected int saveWaveFile(File file) {
        // Pass filename to native to write WAV file
        int result = saveWaveFile(file.getAbsolutePath());
        if (result < 0) {
            showErrorToast("Save returned " + result);
        } else {
            showToast("Saved " + result + " bytes.");
        }
        return result;
    }

    String getWaveTag() {
        return "input";
    }

    @NonNull
    private File createFileName() {
        // Get directory and filename
        File dir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        return new File(dir, "oboe_" +  getWaveTag() + "_" + AutomatedTestRunner.getTimestampString() + ".wav");
    }

    public void shareWaveFile() {
        // Share WAVE file via GMail, Drive or other method.
        File file = createFileName();
        int result = saveWaveFile(file);
        if (result > 0) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("audio/wav");
            String subjectText = file.getName();
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subjectText);
            Uri uri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(sharingIntent, "Share WAV using:"));
        }
    }

    public void onShareFile(View view) {
        shareWaveFile();
    }

    public void onMarginBoxClicked(View view) {
        RadioButton radioButton = (RadioButton) view;
        String text = (String) radioButton.getText();
        mInputMarginBursts = Integer.parseInt(text);
        setMinimumBurstsBeforeRead(mInputMarginBursts);
    }
}
