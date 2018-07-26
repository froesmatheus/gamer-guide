package com.matheusfroes.gamerguide.di

import com.matheusfroes.gamerguide.di.modules.AppModule
import com.matheusfroes.gamerguide.di.modules.DataModule
import com.matheusfroes.gamerguide.di.modules.ViewModelModule
import dagger.Component

@Component(modules = [(AppModule::class), (DataModule::class), (ViewModelModule::class)])
interface AppComponent
