package com.matheusfroes.gamerguide.di

import com.matheusfroes.gamerguide.di.modules.*
import dagger.Component

@Component(modules = [(AppModule::class), (DataModule::class), (ViewModelModule::class),
    (NetworkModule::class), (GameModule::class), (FeedModule::class)])
interface AppComponent
