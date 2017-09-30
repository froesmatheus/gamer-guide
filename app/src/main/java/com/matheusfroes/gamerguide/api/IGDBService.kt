package com.matheusfroes.gamerguide.api

import com.matheusfroes.gamerguide.models.GameResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Created by matheusfroes on 26/09/2017.
 */
interface IGDBService {
    companion object {
        val URL_BASE = "https://api-2445582011268.apicast.io/"
    }

    @Headers("user-key:81b44dc6a3b4284e6093dcea834aa49c", "Accept:application/json")
    @GET("games/")
    fun pesquisarJogos(
            @Query("search") query: String,
            @Query("fields") fields: String = "name,id,summary,cover.url,time_to_beat,developers,publishers,videos,first_release_date,genres,release_dates.date,release_dates.platform",
            @Query("expand") expands: String = "publishers,developers,genres",
//            @Query("filter[release_dates.platform][eq]") filters: Array<String> = arrayOf("48", "49", "6", "12", "9"),
            @Query("order") popularity: String = "popularity:desc"): Call<List<GameResponse>>
}