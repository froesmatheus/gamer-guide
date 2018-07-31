package com.matheusfroes.gamerguide.data.mapper

import com.matheusfroes.gamerguide.network.data.GenreResponse

class GenreMapper {
    companion object {
        fun map(genresResponse: List<GenreResponse>?): String {
            return genresResponse?.joinToString() ?: ""
        }
    }
}