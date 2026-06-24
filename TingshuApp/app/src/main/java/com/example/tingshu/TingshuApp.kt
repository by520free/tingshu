package com.example.tingshu

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TingshuApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
