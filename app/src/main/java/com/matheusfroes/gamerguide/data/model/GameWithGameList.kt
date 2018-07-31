package com.matheusfroes.gamerguide.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey

@Entity(tableName = "games_lists", primaryKeys = ["gameId", "gameListId"], foreignKeys = [
    ForeignKey(
            entity = Game::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.CASCADE),
    ForeignKey(
            entity = GameList::class,
            parentColumns = ["id"],
            childColumns = ["gameListId"],
            onDelete = ForeignKey.CASCADE)])
data class GameWithGameList(
        val gameId: Long,
        val gameListId: Long
)