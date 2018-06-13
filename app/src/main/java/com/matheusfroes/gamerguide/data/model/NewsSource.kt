package com.matheusfroes.gamerguide.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "news_sources")
data class NewsSource(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val name: String,
        val website: String,
        var enabled: Boolean = true)