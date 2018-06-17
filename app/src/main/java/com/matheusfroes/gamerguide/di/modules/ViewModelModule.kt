package com.matheusfroes.gamerguide.di.modules

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.di.annotation.ViewModelKey
import com.matheusfroes.gamerguide.ui.calendar.CalendarViewModel
import com.matheusfroes.gamerguide.ui.statistics.StatisticsViewModel
import com.matheusfroes.gamerguide.ui.feed.FeedViewModel
import com.matheusfroes.gamerguide.ui.gamelists.GameListViewModel
import com.matheusfroes.gamerguide.ui.mygames.MyGamesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MyGamesViewModel::class)
    abstract fun myGamesViewModel(myGamesViewModel: MyGamesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GameListViewModel::class)
    abstract fun gameListViewModel(gameListViewModel: GameListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel::class)
    abstract fun feedViewModel(feedViewModel: FeedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StatisticsViewModel::class)
    abstract fun statisticsViewModel(statisticsViewModel: StatisticsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StatisticsViewModel::class)
    abstract fun calendarViewModel(calendarViewModel: CalendarViewModel): ViewModel
}
