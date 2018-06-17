package com.matheusfroes.gamerguide.di.modules

import com.matheusfroes.gamerguide.MainActivity
import com.matheusfroes.gamerguide.ui.addgames.AddGamesActivity
import com.matheusfroes.gamerguide.ui.calendar.CalendarFragment
import com.matheusfroes.gamerguide.ui.feed.FeedFragment
import com.matheusfroes.gamerguide.ui.gamelists.GameListFragment
import com.matheusfroes.gamerguide.ui.mygames.MyGamesFragment
import com.matheusfroes.gamerguide.ui.statistics.StatisticsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class AndroidBuilder {

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun addGamesActivity(): AddGamesActivity

    @ContributesAndroidInjector
    abstract fun calendarFragment(): CalendarFragment

    @ContributesAndroidInjector
    abstract fun statisticsFragment(): StatisticsFragment

    @ContributesAndroidInjector
    abstract fun feedFragment(): FeedFragment

    @ContributesAndroidInjector
    abstract fun gameListFragment(): GameListFragment

    @ContributesAndroidInjector
    abstract fun myGamesFragment(): MyGamesFragment
}
