package com.matheusfroes.gamerguide.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.OnConflictStrategy

@Entity(tableName = "games_platforms", primaryKeys = ["gameId", "platformId"], foreignKeys = [
    ForeignKey(
            entity = Game::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = OnConflictStrategy.REPLACE),
    ForeignKey(
            entity = Platform::class,
            parentColumns = ["id"],
            childColumns = ["platformId"],
            onDelete = OnConflictStrategy.REPLACE)])
data class GameWithPlatform(
        val gameId: Long,
        val platformId: Long
)