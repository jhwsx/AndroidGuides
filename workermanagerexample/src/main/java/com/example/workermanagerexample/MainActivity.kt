package com.example.workermanagerexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.*
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var workRequest: WorkRequest
    private var  count: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            Timber.d("click: ${System.currentTimeMillis()}")
            val constraints = Constraints.Builder()
                .setRequiresDeviceIdle(true)
                .setRequiresCharging(true)
                .build()
            val imageData = workDataOf(Constants.KEY_IMAGE_URL to "http://www.image")
            workRequest = OneTimeWorkRequestBuilder<MyWorker>()
//                .setConstraints(constraints)
//                .setInitialDelay(3, TimeUnit.SECONDS)
//                .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
//                .setInputData(imageData)
                .addTag("wzc")
                .build()
            WorkManager.getInstance(this@MainActivity).enqueue(workRequest)
            val workInfo =
                WorkManager.getInstance(this@MainActivity).getWorkInfoById(workRequest.id)
            val workInfoByIdLiveData =
                WorkManager.getInstance(this@MainActivity).getWorkInfoByIdLiveData(workRequest.id)
            workInfoByIdLiveData.observe(this, Observer {
                Timber.d("workInfo:${it.state}")
            })
        }
        buttonChainingWork.setOnClickListener {
            WorkManager.getInstance(this)
                .beginWith(
                    listOf(
                        OneTimeWorkRequestBuilder<Filter1Worker>().build(),
                        OneTimeWorkRequestBuilder<Filter2Worker>().build(),
                        OneTimeWorkRequestBuilder<Filter3Worker>().build()
                    )
                )
                .then(
                    OneTimeWorkRequestBuilder<CompressWorker>()
                        .setInputMerger(ArrayCreatingInputMerger::class).build()
                )
                .then(OneTimeWorkRequestBuilder<UploadWorker>().build())
                .enqueue()
        }

        buttonCancelWork.setOnClickListener {
            workRequest?.let {
                WorkManager.getInstance(this).cancelWorkById(it.id)
            }
        }

        buttonRecurringWork.setOnClickListener {
            val constraints = Constraints.Builder()
//                .setRequiresCharging(true)
                .build()

            val saveRequest =
                PeriodicWorkRequestBuilder<SaveImageToFileWorker>(15, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .build()

            WorkManager.getInstance(this).enqueue(saveRequest)
        }

        buttonUniqueWork.setOnClickListener {
            count++
            WorkManager.getInstance(this)
                .beginUniqueWork("sync", ExistingWorkPolicy.APPEND, OneTimeWorkRequestBuilder<MyUniqueWorker>().setInputData(
                    workDataOf("data" to count)).build())
                .enqueue()
        }

    }
}
