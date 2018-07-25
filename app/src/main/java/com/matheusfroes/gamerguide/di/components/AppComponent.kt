package com.matheusfroes.gamerguide.di.components

import com.matheusfroes.gamerguide.GamerGuideApplication
import com.matheusfroes.gamerguide.di.annotation.AppScope
import com.matheusfroes.gamerguide.di.modules.AndroidBuilder
import com.matheusfroes.gamerguide.di.modules.AppModule
import com.matheusfroes.gamerguide.di.modules.DataModule
import com.matheusfroes.gamerguide.di.modules.NetworkModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@AppScope
@Component(modules = [(AndroidSupportInjectionModule::class), (AppModule::class), (AndroidBuilder::class), (DataModule::class), (NetworkModule::class)])
interface AppComponent : AndroidInjector<GamerGuideApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<GamerGuideApplication>()
}
