package com.matheusfroes.gamerguide.network

import com.matheusfroes.gamerguide.network.data.GameResponse
import com.matheusfroes.gamerguide.network.data.ObterLancamentosResponse
import com.matheusfroes.gamerguide.network.data.ObterStreamsResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url

interface GameService {
    companion object {
        const val BASE_URL = "https://api-endpoint.igdb.com/"
    }

    @Headers("user-key:81b44dc6a3b4284e6093dcea834aa49c", "Accept:application/json")
    @GET("games/")
    fun searchGames(
            @Query("search") query: String,
            @Query("fields") fields: String = "name,id,summary,cover.url,game_engines,time_to_beat,developers,publishers,videos,first_release_date,genres,release_dates.date,release_dates.platform",
            @Query("expand") expands: String = "publishers,developers,genres,game_engines",
            @Query("order") popularity: String = "popularity:desc",
            @Query("scroll") scroll: String = "1"): Observable<Response<List<GameResponse>>>


    @Headers("user-key:81b44dc6a3b4284e6093dcea834aa49c", "Accept:application/json")
    @GET
    fun searchGamesNextPage(@Url scrollUrl: String): Observable<Response<List<GameResponse>>>

    @Headers("user-key:81b44dc6a3b4284e6093dcea834aa49c", "Accept:application/json")
    @GET("release_dates/")
    fun getGameReleases(
            @Query("filter[date][gt]") dataLancamento: Long,
            @Query("fields") fields: String = "game.name,game.cover.url,date,platform,region",
            @Query("expand") expands: String = "game",
            @Query("order") order: String = "date:asc",
            @Query("scroll") scroll: String = "1"): Observable<Response<List<ObterLancamentosResponse>>>

    @Headers("user-key:81b44dc6a3b4284e6093dcea834aa49c", "Accept:application/json")
    @GET
    fun getGamesReleasesNextPage(@Url scrollUrl: String): Observable<Response<List<ObterLancamentosResponse>>>

    @GET("https://api.twitch.tv/kraken/streams")
    @Headers("Client-Id:8xydr1gey20rwhe79m5i328fitovuz")
    fun getLivestreamsByGame(@Query("game") gameName: String, @Query("limit") limit: Int = 10, @Query("offset") offset: Int = 0): Single<ObterStreamsResponse>
}