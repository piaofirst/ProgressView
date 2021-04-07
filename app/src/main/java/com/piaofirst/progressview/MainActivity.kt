package com.piaofirst.progressview

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.piaofirst.progressview.databinding.ActivityMainBinding
import com.piaofirst.progressviewlibrary.ProgressView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.progressView.color = Color.RED
        binding.progressView.borderWidth = 3f;
        binding.progressView.radius = 15f
        binding.progressView.textSize = 30f

        binding.progressView.onStart = {
            startLoadingThread()
        }
        binding.progressView.onPause = {
            loadingThread?.state = ProgressView.ProgressState.Paused
        }
        binding.progressView.onResume = {
            loadingThread?.state = ProgressView.ProgressState.Loading
        }
    }

    var loadingThread: LoadingThread? = null

    private fun startLoadingThread() {
        loadingThread = LoadingThread()
        loadingThread?.onProgress = {
            binding.progressView.progress = it
            if (it >= 1) {
                binding.progressView.state = ProgressView.ProgressState.Finished
            }
        }
        loadingThread?.start()
    }

    inner class LoadingThread : Thread() {
        var state = ProgressView.ProgressState.Loading
        var onProgress: ((Float) -> Unit)? = null
        var progress = 0f

        override fun run() {
            super.run()
            while (state == ProgressView.ProgressState.Loading || state == ProgressView.ProgressState.Paused) {
                if (progress > 1) {
                    break
                }
                if (state != ProgressView.ProgressState.Paused) {
                    progress += Math.random().toFloat() * 0.1f
                    runOnUiThread {
                        onProgress?.invoke(Math.min(progress, 1f))
                    }
                }
                sleep(100)
            }
        }
    }

}
