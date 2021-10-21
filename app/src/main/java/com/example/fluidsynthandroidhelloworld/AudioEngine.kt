package com.example.fluidsynthandroidhelloworld

class AudioEngine {

//    init {
//        System.loadLibrary("audioEngine")
//    }

    // Native methods
    external fun create(nativeValue: Long): Boolean

    external fun delete()

    external fun startRecording()

    external fun stopRecording()

    external fun startPlayingRecordedStream()

    external fun stopPlayingRecordedStream()

    external fun writeFile(filePath: String)

    external fun startPlayingFromFile(filePath: String)

    external fun stopPlayingFromFile()

//    external fun getJniString(): String

//    fun messageMe(text: String): String {
//        println(text)
//        return "hfidchasflkjhdklvhakshvjklsahvjklshvjklfh"
//    }
}