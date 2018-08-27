package com.matheusfroes.gamerguide.data.model

import android.arch.persistence.room.*

@Entity(tableName = "news", foreignKeys = [
    ForeignKey(
            entity = NewsSource::class,
            parentColumns = ["id"],
            childColumns = ["sourceId"],
            onDelete = ForeignKey.CASCADE)],
        indices = [Index("url", name = "news_url_unique", unique = true)])
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
