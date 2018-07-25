package com.matheusfroes.gamerguide.network.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VideoResponse(
        @SerializedName("name")
        val nome: String,
        @SerializedName("video_id")
        val videoId: String) : Serializable