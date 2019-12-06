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
class CompressWorker(context: Context, workerParameters: WorkerParameters):Worker(context, workerParameters) {
    override fun doWork(): Result {
//        val image1 = inputData.getString(Constants.KEY_IMAGE_1)
//        val image2 = inputData.getString(Constants.KEY_IMAGE_2)
//        val image3 = inputData.getString(Constants.KEY_IMAGE_3)
//        Timber.d("CompressWorker doWork(): $image1, $image2, $image3")
        val keyValueMap = inputData.keyValueMap
                Timber.d("CompressWorker doWork(): ${keyValueMap}")
        return Result.success()
    }
}