package com.matheusfroes.gamerguide.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "news_sources")
data class NewsSource(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val name: String,
        val website: String,
        var enabled: Boolean = true) {


    companion object {
        fun getNewsSources(): List<NewsSource> = listOf(
                NewsSource(name = "Eurogamer", website = "http://www.eurogamer.pt/?format=rss"),
                NewsSource(name = "CriticalHits", website = "https://criticalhits.com.br/feed/"),
                NewsSource(name = "Xbox Power", website = "https://www.xboxpower.com.br/feed/"),
                NewsSource(name = "Playstation Blog", website = "https://blog.br.playstation.com/feed/"),
                NewsSource(name = "IGN Brasil", website = "https://br.ign.com/feed.xml"),
//                NewsSource(name = "Switch Brasil", website = "https://switch-brasil.com/feed/")
                NewsSource(name = "Mundo Nintendo", website = "https://mundonintendo.com.br/feed/")
        )
    }
}