package com.habitarc.app

import android.app.Application
import habitarc.shared.initKmpAndroid

class App : Application() {

    companion object {

        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
        initKmpAndroid(
            application = this,
            build = BuildConfig.VERSION_CODE,
            version = BuildConfig.VERSION_NAME,
            flavor = BuildConfig.FLAVOR,
        )
    }
}
