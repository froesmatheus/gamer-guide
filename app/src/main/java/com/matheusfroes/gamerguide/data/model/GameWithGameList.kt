package com.matheusfroes.gamerguide.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.OnConflictStrategy

@Entity(tableName = "games_lists", primaryKeys = ["gameId", "gameListId"], foreignKeys = [
    ForeignKey(
            entity = Game::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = OnConflictStrategy.REPLACE),
    ForeignKey(
            entity = GameList::class,
            parentColumns = ["id"],
            childColumns = ["gameListId"],
            onDelete = OnConflictStrategy.REPLACE)])
data class GameWithGameList(
        val gameId: Long,
        val gameListId: Long
)