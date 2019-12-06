package com.example.workermanagerexample

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import timber.log.Timber

/**
 *
 *
 * @author wangzhichao
 * @since 2019/12/05
 */
class MyWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        handler.post {
            Toast.makeText(applicationContext, "doWork", Toast.LENGTH_SHORT).show()
        }
        Timber.d("tags: ${tags.joinToString()}")
        val imageUrl = inputData.getString(Constants.KEY_IMAGE_URL)
        Timber.d("imageUrl=$imageUrl")
        Timber.d("work start: ${System.currentTimeMillis()}")
        Thread.sleep(5000L)
        Timber.d("work end: ${System.currentTimeMillis()}")
        val data = workDataOf(Constants.KEY_IMAGE_URL to "https://www.image.success")
        return Result.success(data)
    }

    companion object {
        private val handler = Handler(Looper.getMainLooper())
    }
}