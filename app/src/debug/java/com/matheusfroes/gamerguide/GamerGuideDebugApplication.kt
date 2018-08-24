package com.matheusfroes.gamerguide

import android.app.Application
import com.facebook.stetho.Stetho

class GamerGuideDebugApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }

}