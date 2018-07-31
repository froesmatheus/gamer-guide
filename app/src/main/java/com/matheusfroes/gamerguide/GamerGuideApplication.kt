package com.matheusfroes.gamerguide

import android.app.Application
import com.facebook.stetho.Stetho
import com.matheusfroes.gamerguide.di.DaggerInjector
import com.matheusfroes.gamerguide.di.Injector
import com.matheusfroes.gamerguide.di.modules.AppModule

class GamerGuideApplication : Application() {
    lateinit var injector: Injector private set

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        setupDagger()
    }

    private fun setupDagger() {
        injector = DaggerInjector
                .builder()
                .appModule(AppModule(this))
                .build()
    }
}