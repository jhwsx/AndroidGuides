package com.example.workermanagerexample

import android.content.Context
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
class Filter3Worker(context: Context, workerParameters: WorkerParameters):Worker(context, workerParameters) {
    override fun doWork(): Result {
        Timber.d("Fileter2Worker doWork()")
        val data = workDataOf(Constants.KEY_IMAGE_3 to "image3")
        return Result.success(data)
    }
}