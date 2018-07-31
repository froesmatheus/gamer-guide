package com.matheusfroes.gamerguide.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.matheusfroes.gamerguide.ui.adicionarjogos.AddGamesViewModel
import com.matheusfroes.gamerguide.ui.estatisticas.EstatisticasViewModel
import com.matheusfroes.gamerguide.ui.gamelists.GameListDetailsViewModel
import com.matheusfroes.gamerguide.ui.gamelists.GameListsViewModel
import com.matheusfroes.gamerguide.ui.mygames.MyGamesViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(GameListsViewModel::class)
    internal abstract fun listasViewModel(viewModel: GameListsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GameListDetailsViewModel::class)
    internal abstract fun detalhesListaViewModel(viewModel: GameListDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyGamesViewModel::class)
    internal abstract fun myGamesViewModel(viewModel: MyGamesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddGamesViewModel::class)
    internal abstract fun addGamesViewModel(viewModel: AddGamesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EstatisticasViewModel::class)
    internal abstract fun estatisticasViewModel(viewModel: EstatisticasViewModel): ViewModel
}