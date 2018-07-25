package com.matheusfroes.gamerguide.network.data

import com.matheusfroes.gamerguide.data.model.Platform
import java.util.*

data class ObterLancamentosResponse(
        val id: Int,
        val game: GameResponse,
        val platform: Long,
        val date: Long,
        val region: Int)


data class Lancamento(
        val game: JogoLancamento,
        val platform: Platform,
        val date: Date,
        val region: String
) {

    override fun equals(other: Any?): Boolean {
        return if (other is Lancamento) {
            this.game.nome == other.game.nome
        } else {
            false
        }

    }

    override fun hashCode(): Int {
        var result = game.hashCode()
        result = 31 * result + platform.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + region.hashCode()
        return result
    }
}