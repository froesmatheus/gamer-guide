package com.matheusfroes.gamerguide

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.matheusfroes.gamerguide.di.DaggerInjector
import com.matheusfroes.gamerguide.di.Injector
import com.matheusfroes.gamerguide.di.modules.AppModule
import com.matheusfroes.gamerguide.extra.CrashlyticsTree
import timber.log.Timber

class GamerGuideApplication : Application() {
    lateinit var injector: Injector private set

    override fun onCreate() {
        super.onCreate()

        setupDagger()
        setupTimber()
//        Stetho.initializeWithDefaults(this)

        Kotpref.init(this)
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }
    }

    private fun setupDagger() {
        injector = DaggerInjector
                .builder()
                .appModule(AppModule(this))
                .build()
    }
}