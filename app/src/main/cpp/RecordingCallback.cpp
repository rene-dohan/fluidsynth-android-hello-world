
#include "RecordingCallback.h"
#include "logging_macros.h"

oboe::DataCallbackResult
RecordingCallback::onAudioReady(oboe::AudioStream *audioStream, void *audioData,
                                int32_t numFrames) {
    LOGD(TAG, "processRecordingFrames(): ");

//    if (audioStream->getFormat() == oboe::AudioFormat::Float) {
//        fluid_synth_write_float(synth, numFrames, static_cast<float *>(audioData), 0, 2,
//                                static_cast<float *>(audioData), 1, 2);
//    } else {
        fluid_synth_write_s16(synth, numFrames, static_cast<short *>(audioData), 0, 2,
                              static_cast<short *>(audioData), 1, 2);
//    }

    //    return oboe::DataCallbackResult::Continue;
    return processRecordingFrames(audioStream, static_cast<int16_t *>(audioData),
                                  numFrames * audioStream->getChannelCount());
}

oboe::DataCallbackResult
RecordingCallback::processRecordingFrames(oboe::AudioStream *audioStream, int16_t *audioData,
                                          int32_t numFrames) {
    LOGD(TAG, "processRecordingFrames(): ");
    int32_t framesWritten = mSoundRecording->write(audioData, numFrames);
    return oboe::DataCallbackResult::Continue;

}

//RecordingCallback::Log(std::string s){
//
//    JNIEnv *env;
//    g_JavaVM->GetEnv((void**)&env, JNI_VERSION_1_6);
//
//    jstring jstr1 = env->NewStringUTF(s.c_str());
//
//    jclass clazz = env->FindClass("com/android/gl2jni/GL2JNILib");
//    jmethodID mid = env->GetStaticMethodID(clazz, "log", "(Ljava/lang/String;)V");
//
//    jobject obj = env->CallStaticObjectMethod(clazz, mid, jstr1);
//}
