package com.matheusfroes.gamerguide.di

import com.matheusfroes.gamerguide.di.modules.*
import com.matheusfroes.gamerguide.ui.BaseActivity
import com.matheusfroes.gamerguide.ui.addgames.AddGamesActivity
import com.matheusfroes.gamerguide.ui.calendar.CalendarFragment
import com.matheusfroes.gamerguide.ui.gamedetails.GameDetailsActivity
import com.matheusfroes.gamerguide.ui.gamedetails.gameinfo.GameInfoFragment
import com.matheusfroes.gamerguide.ui.gamedetails.livestream.StreamsFragment
import com.matheusfroes.gamerguide.ui.gamedetails.video.VideosFragment
import com.matheusfroes.gamerguide.ui.statistics.StatisticsFragment
import com.matheusfroes.gamerguide.ui.gamelists.GameListDetailsActivity
import com.matheusfroes.gamerguide.ui.gamelists.GameListsFragment
import com.matheusfroes.gamerguide.ui.mygames.JogosTabNaoTerminadosFragment
import com.matheusfroes.gamerguide.ui.mygames.JogosTabZeradosFragment
import com.matheusfroes.gamerguide.ui.mygames.MyGamesFragment
import com.matheusfroes.gamerguide.ui.settings.SettingsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, DataModule::class, NetworkModule::class,
    GameModule::class])
interface Injector {
    fun inject(gameListsFragment: GameListsFragment)
    fun inject(gameListDetails: GameListDetailsActivity)
    fun inject(myGamesFragment: MyGamesFragment)
    fun inject(addGamesActivity: AddGamesActivity)
    fun inject(jogosTabNaoTerminadosFragment: JogosTabNaoTerminadosFragment)
    fun inject(jogosTabZeradosFragment: JogosTabZeradosFragment)
    fun inject(statisticsFragment: StatisticsFragment)
    fun inject(gameDetailsActivity: GameDetailsActivity)
    fun inject(gameInfoFragment: GameInfoFragment)
    fun inject(videosFragment: VideosFragment)
    fun inject(streamsFragment: StreamsFragment)
    fun inject(calendarFragment: CalendarFragment)
    fun inject(settingsActivity: SettingsActivity)
    fun inject(baseActivity: BaseActivity)
}