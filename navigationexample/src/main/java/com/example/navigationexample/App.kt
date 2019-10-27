package com.example.navigationexample

import android.app.Application
import timber.log.Timber

/**
 * @author wangzhichao
 * @date 2019/10/27
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}