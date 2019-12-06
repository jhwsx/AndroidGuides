package com.example.workermanagerexample

import android.app.Application
import timber.log.Timber

/**
 *
 *
 * @author wangzhichao
 * @since 2019/12/05
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}