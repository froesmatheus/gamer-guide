package com.matheusfroes.gamerguide.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "videos", foreignKeys = [
    ForeignKey(
            entity = Game::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.CASCADE)])
data class Video(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val name: String,
        val gameId: Long,
        val videoId: String) : Serializable