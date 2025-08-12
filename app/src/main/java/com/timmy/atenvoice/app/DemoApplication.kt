package com.timmy.atenvoice.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DemoApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private lateinit var instance: DemoApplication
    }

}