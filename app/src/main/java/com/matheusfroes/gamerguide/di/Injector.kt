package com.matheusfroes.gamerguide.di

import com.matheusfroes.gamerguide.di.modules.AppModule
import com.matheusfroes.gamerguide.di.modules.DataModule
import com.matheusfroes.gamerguide.di.modules.ViewModelModule
import com.matheusfroes.gamerguide.ui.listas.GameListsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, DataModule::class])
interface Injector {
    fun inject(listasFragment: GameListsFragment)
}