package com.example.fluidsynthandroidhelloworld

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException

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
        main.postDelayed({
            engine.startRecording()
            print("startRecording")
            main.postDelayed({

                try {
                    val tempSoundfontPath = copyAssetToTmpFile("sndfnt.sf2")
                    fluidsynthHelloWorld(tempSoundfontPath)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }

                engine.stopRecording()
                print("stopRecording")
                main.postDelayed({
                    engine.startPlayingRecordedStream()
                    print("startPlayingRecordedStream")
                    main.postDelayed({
                        engine.delete()
                        print("AudioEngine.delete")
                    }, 10000)
                }, 3000)
            }, 15000)
        }, 3000)


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