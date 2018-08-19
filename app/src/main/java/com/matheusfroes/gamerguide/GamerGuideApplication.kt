package com.matheusfroes.gamerguide

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.facebook.stetho.Stetho
import com.matheusfroes.gamerguide.di.DaggerInjector
import com.matheusfroes.gamerguide.di.Injector
import com.matheusfroes.gamerguide.di.modules.AppModule
import timber.log.Timber

class GamerGuideApplication : Application() {
    lateinit var injector: Injector private set

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }

        setupDagger()
        Kotpref.init(this)
    }

    private fun setupDagger() {
        injector = DaggerInjector
                .builder()
                .appModule(AppModule(this))
                .build()
    }
}