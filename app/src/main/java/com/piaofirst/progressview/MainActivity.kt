package com.piaofirst.progressview

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.piaofirst.progressviewlibrary.ProgressView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressView.color = Color.RED
        progressView.borderWidth = 3f;
        progressView.radius = 15f
        progressView.textSize = 30f

        progressView.onStart = {
            startLoadingThread()
        }
    }

    var loadingThread: LoadingThread? = null

    private fun startLoadingThread() {
        loadingThread = LoadingThread()
        loadingThread?.onProgress = {
            progressView.progress = it
            if (it >= 1) {
                progressView.state = ProgressView.ProgressState.Finished
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
                Thread.sleep(100)
            }
        }
    }

}
