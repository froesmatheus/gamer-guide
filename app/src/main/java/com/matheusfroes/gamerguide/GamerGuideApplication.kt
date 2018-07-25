package com.matheusfroes.gamerguide

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class GamerGuideApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}