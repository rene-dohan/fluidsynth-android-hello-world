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

package com.google.sample.oboe.manualtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.google.sample.oboe.manualtest.R;

/**
 * Test Oboe Capture
 */

public class TestInputActivity  extends TestAudioActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int AUDIO_ECHO_REQUEST = 0;
    private AudioInputTester mAudioInputTester;
    private static final int NUM_VOLUME_BARS = 4;
    private TextView[] mVolumeTexts = new TextView[NUM_VOLUME_BARS];
    private VolumeBarView[] mVolumeBars = new VolumeBarView[NUM_VOLUME_BARS];

    @Override boolean isOutput() { return false; }

    protected void inflateActivity() {
        setContentView(R.layout.activity_test_input);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateActivity();

        mVolumeTexts[0] = (TextView) findViewById(R.id.volumeText0);
        mVolumeBars[0] = (VolumeBarView) findViewById(R.id.volumeBar0);
        mVolumeTexts[1] = (TextView) findViewById(R.id.volumeText1);
        mVolumeBars[1] = (VolumeBarView) findViewById(R.id.volumeBar1);
        mVolumeTexts[2] = (TextView) findViewById(R.id.volumeText2);
        mVolumeBars[2] = (VolumeBarView) findViewById(R.id.volumeBar2);
        mVolumeTexts[3] = (TextView) findViewById(R.id.volumeText3);
        mVolumeBars[3] = (VolumeBarView) findViewById(R.id.volumeBar3);

        findAudioCommon();
        updateEnabledWidgets();

        mAudioStreamTester = mAudioInputTester = AudioInputTester.getInstance();
    }

    void updateStreamDisplay() {
        int numChannels = mAudioInputTester.getCurrentAudioStream().getChannelCount();
        if (numChannels > NUM_VOLUME_BARS) {
            numChannels = NUM_VOLUME_BARS;
        }
        for (int i = 0; i < numChannels; i++) {
            double level = mAudioInputTester.getPeakLevel(i);
            String msg = String.format("level = %8.6f", level);
            mVolumeTexts[i].setText(msg);
            mVolumeBars[i].setVolume((float) level);
        }
    }

    public void openAudio() {
        if (!isRecordPermissionGranted()){
            requestRecordPermission();
            return;
        }
        super.openAudio();
    }

    private boolean isRecordPermissionGranted() {
        return (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestRecordPermission(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                AUDIO_ECHO_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (AUDIO_ECHO_REQUEST != requestCode) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 1 ||
                grantResults[0] != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getApplicationContext(),
                    getString(R.string.need_record_audio_permission),
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            // Permission was granted
            super.openAudio();
        }
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
}
