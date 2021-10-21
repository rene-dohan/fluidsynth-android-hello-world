#ifndef OBOE_RECORDER_RECORDINGCALLBACK_H
#define OBOE_RECORDER_RECORDINGCALLBACK_H


#include <oboe/Definitions.h>
#include <oboe/AudioStream.h>
#include "SoundRecording.h"
#include "logging_macros.h"

#ifndef MODULE_NAME
#define MODULE_NAME  "RecordingCallback"
#endif


class RecordingCallback : public oboe::AudioStreamCallback {

private:
    const char *TAG = "RecordingCallback:: %s";
    SoundRecording *mSoundRecording = nullptr;

public:
    RecordingCallback() = default;

    explicit RecordingCallback(SoundRecording *recording) {
        mSoundRecording = recording;
//        clazz = (*jniEnv).FindClass("com/sheraz/oboerecorder/AudioEngine");
//        messageMe = (*jniEnv)
//                .GetMethodID(clazz, "messageMe", "(Ljava/lang/String;)Ljava/lang/String;");
//        jniEnv->GetJavaVM(&javaVM);
//        javaObject = object;
//            JNIEnv *env;
//    g_JavaVM->GetEnv((void**)&env, JNI_VERSION_1_6);
    }

    oboe::DataCallbackResult
    onAudioReady(oboe::AudioStream *audioStream, void *audioData, int32_t numFrames);

    oboe::DataCallbackResult
    processRecordingFrames(oboe::AudioStream *audioStream, int16_t *audioData, int32_t numFrames);
};

#endif //OBOE_RECORDER_RECORDINGCALLBACK_H
