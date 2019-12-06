package com.example.workermanagerexample

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber

/**
 *
 *
 * @author wangzhichao
 * @since 2019/12/05
 */
class SaveImageToFileWorker(context: Context, workerParameters: WorkerParameters):Worker(context, workerParameters) {
    override fun doWork(): Result {
        Thread.sleep(2000L)
        Timber.d("doWork(): save image to file")
        return Result.success()
    }
}