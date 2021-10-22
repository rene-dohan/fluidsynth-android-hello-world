package com.example.fluidsynthandroidhelloworld

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    val main by lazy { Handler(Looper.getMainLooper()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 999
        )

        val engine = AudioEngine()
        engine.create(1L)
        later(1000) {
            print("startRecording")
            val task = object : TimerTask() {
                override fun run() {
                    try {
                        engine.startRecording()
                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    }
                }

            }
            Timer().schedule(task, 0)

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    try {
                        val tempSoundfontPath = copyAssetToTmpFile("sndfnt.sf2")
                        fluidsynthHelloWorld(tempSoundfontPath)
                        engine.stopRecording()

                        engine.startPlayingRecordedStream()
                        print("startPlayingRecordedStream")
                        later(8000) {
                            engine.delete()
                            print("AudioEngine.delete")
                        }
                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    }
                }

            }, 0)
//            print("startRecording")
//            later(1000) {
//
//
//                later(7000) {
//                    engine.stopRecording()
//                    print("stopRecording")
//                    later(1000) {
//                        engine.startPlayingRecordedStream()
//                        print("startPlayingRecordedStream")
//                        later(8000) {
//                            engine.delete()
//                            print("AudioEngine.delete")
//                        }
//                    }
//                }
//            }
        }


    }

    fun later(delayMillis: Long, function: () -> Unit) {
        main.postDelayed(function, delayMillis)
    }

    @Throws(IOException::class)
    private fun copyAssetToTmpFile(fileName: String): String {
        assets.open(fileName).use { `is` ->
            val tempFileName = "tmp_$fileName"
            openFileOutput(tempFileName, MODE_PRIVATE).use { fos ->
                var bytes_read: Int
                val buffer = ByteArray(4096)
                while (`is`.read(buffer).also { bytes_read = it } != -1) {
                    fos.write(buffer, 0, bytes_read)
                }
            }
            return "$filesDir/$tempFileName"
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun fluidsynthHelloWorld(soundfontPath: String?)
}