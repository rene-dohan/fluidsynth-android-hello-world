#include <jni.h>
#include <string>
#include <fluidsynth.h>
#include <unistd.h>
#include "AudioEngine.h"
#include "logging_macros.h"

const char *TAG = "jni_bridge:: %s";
static AudioEngine *audioEngine = nullptr;
static fluid_settings_t *settings = nullptr;
static fluid_synth_t *synth = nullptr;
//static fluid_audio_driver_t *adriver = nullptr;

extern "C" JNIEXPORT void JNICALL
Java_com_example_fluidsynthandroidhelloworld_MainActivity_fluidsynthHelloWorld(JNIEnv *env, jobject,
                                                                               jstring jSoundfontPath) {
//    // Setup synthesizer
//    fluid_settings_t *settings = new_fluid_settings();
//    fluid_synth_t *synth = new_fluid_synth(settings);
//    fluid_audio_driver_t *adriver = new_fluid_audio_driver(settings, synth);

    // Load sample soundfont
    const char *soundfontPath = env->GetStringUTFChars(jSoundfontPath, nullptr);
    fluid_synth_sfload(synth, soundfontPath, 1);

    fluid_synth_noteon(synth, 0, 60, 127); // play middle C
    sleep(2); // sleep for 1 second
    fluid_synth_noteoff(synth, 0, 60); // stop playing middle C

    fluid_synth_noteon(synth, 0, 62, 127);
    sleep(2);
    fluid_synth_noteoff(synth, 0, 62);

    fluid_synth_noteon(synth, 0, 64, 127);
    sleep(2);
    fluid_synth_noteoff(synth, 0, 64);

    // Clean up
//    delete_fluid_audio_driver(adriver);
    delete_fluid_synth(synth);
    delete_fluid_settings(settings);
}

extern "C" {

JNIEXPORT bool JNICALL
Java_com_example_fluidsynthandroidhelloworld_AudioEngine_create(
        JNIEnv *env, jobject obj, jlong native_value) {

    LOGD(TAG, "create(): ");
    settings = new_fluid_settings();
    fluid_settings_setint(settings, "synth.threadsafe-api", 1);
    fluid_settings_setint(settings, "synth.cpu-cores", 3);
    fluid_settings_setnum(settings, "synth.gain", 1.0);
    fluid_settings_setint(settings, "synth.verbose", 0);
    fluid_settings_setstr(settings, "audio.oboe.sharing-mode", "Exclusive");
    fluid_settings_setstr(settings, "audio.oboe.performance-mode", "LowLatency");
//    fluid_settings_setnum(settings, "synth.sample-rate", 48000.0);
//    fluid_settings_setint(settings, "audio.period-size", 192);

    synth = new_fluid_synth(settings);
//    adriver = new_fluid_audio_driver(settings, synth);

    if (audioEngine == nullptr) {
        audioEngine = new AudioEngine();
    }

    return (audioEngine != nullptr);
}

JNIEXPORT void JNICALL
Java_com_example_fluidsynthandroidhelloworld_AudioEngine_delete(JNIEnv *env, jobject) {

    LOGD(TAG, "delete(): ");

    delete audioEngine;
    audioEngine = nullptr;

}

JNIEXPORT void JNICALL
Java_com_example_fluidsynthandroidhelloworld_AudioEngine_startRecording(JNIEnv *env, jobject obj) {

    LOGD(TAG, "startRecording(): ");

    if (audioEngine == nullptr) {
        LOGE(TAG, "audioEngine is null, you must call create() method before calling this method");
        return;
    }

    audioEngine->startRecording(env, obj, synth, settings);

}

JNIEXPORT void JNICALL
Java_com_example_fluidsynthandroidhelloworld_AudioEngine_stopRecording(JNIEnv *env, jobject) {

    LOGD(TAG, "stopRecording(): ");

    if (audioEngine == nullptr) {
        LOGE(TAG, "audioEngine is null, you must call create() method before calling this method");
        return;
    }

    audioEngine->stopRecording();

}

JNIEXPORT void JNICALL
Java_com_example_fluidsynthandroidhelloworld_AudioEngine_startPlayingRecordedStream(JNIEnv *env,
                                                                                    jobject) {

    LOGD(TAG, "startPlayingRecordedStream(): ");

    if (audioEngine == nullptr) {
        LOGE(TAG, "audioEngine is null, you must call create() method before calling this method");
        return;
    }

    audioEngine->startPlayingRecordedStream();

}

JNIEXPORT void JNICALL
Java_com_example_fluidsynthandroidhelloworld_AudioEngine_stopPlayingRecordedStream(JNIEnv *env,
                                                                                   jobject) {

    LOGD(TAG, "stopPlayingRecordedStream(): ");

    if (audioEngine == nullptr) {
        LOGE(TAG, "audioEngine is null, you must call create() method before calling this method");
        return;
    }

    audioEngine->stopPlayingRecordedStream();

}

JNIEXPORT void JNICALL
Java_com_example_fluidsynthandroidhelloworld_AudioEngine_writeFile(JNIEnv *env, jobject,
                                                                   jstring filePath) {

    LOGD(TAG, "writeFile(): filePath = ");
//    LOGD(TAG, filePath);

    if (audioEngine == nullptr) {
        LOGE(TAG, "audioEngine is null, you must call create() method before calling this method");
        return;
    }

    const char *path;

    path = env->GetStringUTFChars(filePath, nullptr);
    audioEngine->writeToFile(path);
    env->ReleaseStringUTFChars(filePath, path);
}

JNIEXPORT void JNICALL
Java_com_example_fluidsynthandroidhelloworld_AudioEngine_startPlayingFromFile(JNIEnv *env, jobject,
                                                                              jstring filePath) {

    LOGD(TAG, "startPlayingFromFile(): filePath = ");
//    LOGD(TAG, filePath);

    if (audioEngine == nullptr) {
        LOGE(TAG, "audioEngine is null, you must call create() method before calling this method");
        return;
    }

    const char *path;

    path = env->GetStringUTFChars(filePath, nullptr);
    audioEngine->startPlayingFromFile(path);
    env->ReleaseStringUTFChars(filePath, path);
}

JNIEXPORT void JNICALL
Java_com_example_fluidsynthandroidhelloworld_AudioEngine_stopPlayingFromFile(JNIEnv *env,
                                                                             jobject thiz) {

    LOGD(TAG, "stopPlayingFromFile(): ");

    if (audioEngine == nullptr) {
        LOGE(TAG, "audioEngine is null, you must call create() method before calling this method");
        return;
    }

    audioEngine->stopPlayingFromFile();
}

}
