package com.matheusfroes.gamerguide.di

import com.matheusfroes.gamerguide.di.modules.AppModule
import com.matheusfroes.gamerguide.di.modules.DataModule
import com.matheusfroes.gamerguide.di.modules.ViewModelModule
import com.matheusfroes.gamerguide.ui.gamelists.GameListDetailsActivity
import com.matheusfroes.gamerguide.ui.gamelists.GameListsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, DataModule::class])
interface Injector {
    fun inject(gameListsFragment: GameListsFragment)
    fun inject(gameListDetails: GameListDetailsActivity)
}