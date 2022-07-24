package com.playgroundagc.deepltranslator.util

import android.app.Activity
import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by Amadou on 12/07/2022, 09:21
 *
 * Purpose:
 *
 */

@HiltAndroidApp
class GlobalActivity: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}