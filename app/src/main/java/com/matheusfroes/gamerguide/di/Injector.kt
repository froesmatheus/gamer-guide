package com.matheusfroes.gamerguide.di

import com.matheusfroes.gamerguide.di.modules.*
import com.matheusfroes.gamerguide.ui.addgames.AddGamesActivity
import com.matheusfroes.gamerguide.ui.calendario.CalendarFragment
import com.matheusfroes.gamerguide.ui.gamedetails.GameDetailsActivity
import com.matheusfroes.gamerguide.ui.gamedetails.gameinfo.InformacoesGeraisJogoFragment
import com.matheusfroes.gamerguide.ui.gamedetails.livestream.StreamsFragment
import com.matheusfroes.gamerguide.ui.gamedetails.video.VideosFragment
import com.matheusfroes.gamerguide.ui.estatisticas.EstatisticasActivity
import com.matheusfroes.gamerguide.ui.gamelists.GameListDetailsActivity
import com.matheusfroes.gamerguide.ui.gamelists.GameListsFragment
import com.matheusfroes.gamerguide.ui.mygames.JogosTabNaoTerminadosFragment
import com.matheusfroes.gamerguide.ui.mygames.JogosTabZeradosFragment
import com.matheusfroes.gamerguide.ui.mygames.MyGamesFragment
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
    fun inject(estatisticasActivity: EstatisticasActivity)
    fun inject(gameDetailsActivity: GameDetailsActivity)
    fun inject(informacoesGeraisJogoFragment: InformacoesGeraisJogoFragment)
    fun inject(videosFragment: VideosFragment)
    fun inject(streamsFragment: StreamsFragment)
    fun inject(calendarFragment: CalendarFragment)
}