package com.matheusfroes.gamerguide.di

import com.matheusfroes.gamerguide.di.modules.*
import com.matheusfroes.gamerguide.ui.BaseActivity
import com.matheusfroes.gamerguide.ui.addgamedialog.AddGameDialog
import com.matheusfroes.gamerguide.ui.addgames.AddGamesActivity
import com.matheusfroes.gamerguide.ui.calendar.CalendarFragment
import com.matheusfroes.gamerguide.ui.feed.FeedFragment
import com.matheusfroes.gamerguide.ui.gamedetails.GameDetailsActivity
import com.matheusfroes.gamerguide.ui.gamedetails.gameinfo.GameInfoFragment
import com.matheusfroes.gamerguide.ui.gamedetails.livestream.StreamsFragment
import com.matheusfroes.gamerguide.ui.gamedetails.video.VideosFragment
import com.matheusfroes.gamerguide.ui.gamelists.GameListsFragment
import com.matheusfroes.gamerguide.ui.gamelists.gamelistdetails.GameListDetailsActivity
import com.matheusfroes.gamerguide.ui.gameprogressdialog.GameProgressDialog
import com.matheusfroes.gamerguide.ui.mygames.MyGamesFragment
import com.matheusfroes.gamerguide.ui.mygames.tabs.BeatenGamesFragment
import com.matheusfroes.gamerguide.ui.mygames.tabs.UnfinishedGamesFragment
import com.matheusfroes.gamerguide.ui.removegamedialog.RemoveGameDialog
import com.matheusfroes.gamerguide.ui.settings.SettingsActivity
import com.matheusfroes.gamerguide.ui.statistics.StatisticsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class, DataModule::class, NetworkModule::class,
    GameModule::class, FeedModule::class])
interface Injector {
    fun inject(gameListsFragment: GameListsFragment)
    fun inject(gameListDetails: GameListDetailsActivity)
    fun inject(myGamesFragment: MyGamesFragment)
    fun inject(addGamesActivity: AddGamesActivity)
    fun inject(unfinishedGamesFragment: UnfinishedGamesFragment)
    fun inject(beatenGamesFragment: BeatenGamesFragment)
    fun inject(statisticsFragment: StatisticsFragment)
    fun inject(gameDetailsActivity: GameDetailsActivity)
    fun inject(gameInfoFragment: GameInfoFragment)
    fun inject(videosFragment: VideosFragment)
    fun inject(streamsFragment: StreamsFragment)
    fun inject(calendarFragment: CalendarFragment)
    fun inject(settingsActivity: SettingsActivity)
    fun inject(baseActivity: BaseActivity)
    fun inject(feedFragment: FeedFragment)
    fun inject(addGameDialog: AddGameDialog)
    fun inject(removeGameDialog: RemoveGameDialog)
    fun inject(gameProgressDialog: GameProgressDialog)
}