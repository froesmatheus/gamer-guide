package com.matheusfroes.gamerguide.network.data

import com.google.gson.annotations.SerializedName

data class ObterStreamsResponse(val streams: List<Stream>)

data class Stream(val viewers: Long, val preview: Preview, val channel: Channel)

data class Preview(val medium: String, val large: String)

data class Channel(
        val status: String,
        @SerializedName("display_name")
        val displayName: String,
        val logo: String,
        val url: String)