package com.matheusfroes.gamerguide.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey

@Entity(tableName = "games_platforms", primaryKeys = ["gameId", "platformId"], foreignKeys = [
    ForeignKey(
            entity = Game::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.CASCADE),
    ForeignKey(
            entity = Platform::class,
            parentColumns = ["id"],
            childColumns = ["platformId"],
            onDelete = ForeignKey.CASCADE)])
data class GameWithPlatform(
        val gameId: Long,
        val platformId: Long
)