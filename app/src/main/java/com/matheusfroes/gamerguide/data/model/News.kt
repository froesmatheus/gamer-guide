package com.matheusfroes.gamerguide.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "news", foreignKeys = [
    ForeignKey(
            entity = NewsSource::class,
            parentColumns = ["id"],
            childColumns = ["sourceId"],
            onDelete = ForeignKey.CASCADE)
])
data class News(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val title: String,
        val image: String,
        val url: String,
        val publishDate: Long,
        val sourceId: Int?) {

    @Ignore
    var source: NewsSource? = null
}
