package com.matheusfroes.gamerguide.data.mapper

import com.matheusfroes.gamerguide.network.data.GameEngineResponse

class GameEngineMapper {
    companion object {
        fun map(gameEngineResponse: List<GameEngineResponse>?): String {
            return gameEngineResponse?.joinToString() ?: ""
        }
    }
}