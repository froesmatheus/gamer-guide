package com.matheusfroes.gamerguide.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "videos")
data class Video(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val name: String,
        val videoId: String) : Serializable