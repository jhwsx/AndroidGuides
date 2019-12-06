package com.example.workermanagerexample

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber

/**
 *
 *
 * @author wangzhichao
 * @since 2019/12/06
 */
class MyUniqueWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val count = inputData.getInt("data", 0)
        Timber.d("doWork() count = $count,  start: ${System.currentTimeMillis()}")
        Thread.sleep(3000L)
        Timber.d("doWork() count = $count,  end: ${System.currentTimeMillis()}")
        return Result.success()
    }
}