package com.matheusfroes.gamerguide.network.data

import com.google.gson.annotations.SerializedName

data class GameResponse(
        val id: Long,
        val name: String? = null,
        val summary: String? = null,
        var developers: List<DeveloperResponse>? = null,
        var publishers: List<PublisherResponse>? = null,
        val genres: List<GenreResponse>? = null,
        @SerializedName("first_release_date")
        val firstReleaseDate: Long,
        @SerializedName("release_dates")
        val releaseDates: List<ReleaseDateResponse>? = null,
        @SerializedName("time_to_beat")
        val timeToBeat: TimeToBeatResponse? = null,
        @SerializedName("game_engines")
        val gameEngines: List<GameEngineResponse>? = null,
        val videos: List<VideoResponse>? = null,
        val cover: CoverResponse?)