package com.matheusfroes.gamerguide.models

import com.google.gson.annotations.SerializedName

/**
 * Created by matheus_froes on 18/10/2017.
 */
data class ObterStreamsResponse(val streams: List<Stream>)

data class Stream(val viewers: Long, val preview: Preview, val channel: Channel)

data class Preview(val medium: String, val large: String)

data class Channel(
        val status: String,
        @SerializedName("display_name")
        val displayName: String,
        val url: String)