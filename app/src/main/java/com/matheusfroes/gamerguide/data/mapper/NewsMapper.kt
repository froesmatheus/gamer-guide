package com.matheusfroes.gamerguide.data.mapper

import com.matheusfroes.gamerguide.extra.adicionarSchemaUrl
import com.matheusfroes.gamerguide.data.model.News
import com.pkmmte.pkrss.Article
import me.toptas.rssconverter.RssItem
import java.text.SimpleDateFormat
import java.util.*

class NewsMapper {

    companion object {
        fun map(newArticles: MutableList<Article>): List<News> {
            return newArticles.map { article ->
                val imagemNoticia: String? = if (article.enclosure == null) {
                    article.image?.toString()
                } else {
                    article.enclosure.url
                }

                val website = when {
                    article.source.host.contains("comboinfinito", ignoreCase = true) -> "Combo Infinito"
                    article.source.host.contains("tecmundo", ignoreCase = true) -> "Tecmundo"
                    article.source.host.contains("eurogamer", ignoreCase = true) -> "EuroGamer"
                    article.source.host.contains("criticalhits", ignoreCase = true) -> "Criticalhits"
                    article.source.host.contains("xboxpower", ignoreCase = true) -> "Xbox Power"
                    article.source.host.contains("playstation", ignoreCase = true) -> "Playstation Blog"
                    article.source.host.contains("uol", ignoreCase = true) -> "UOL Jogos"
                    else -> "IGN"
                }

                News(title = article.title, image = adicionarSchemaUrl(imagemNoticia!!), url = article.source.toString(), publishDate = article.date, website = website)
            }
        }

        fun map2(rssItems: List<RssItem>): List<News> {
            return rssItems.map { rssItem ->
                News(
                        title = rssItem.title,
                        image = rssItem.image,
                        url = rssItem.link,
                        publishDate = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale("pt", "BR")).parse(rssItem.publishDate).time,
                        website = extractNewsWebsite(rssItem.link))
            }
        }

        private fun extractNewsWebsite(link: String): String {
            return when {
                link.contains("comboinfinito", ignoreCase = true) -> "Combo Infinito"
                link.contains("tecmundo", ignoreCase = true) -> "Tecmundo"
                link.contains("eurogamer", ignoreCase = true) -> "EuroGamer"
                link.contains("criticalhits", ignoreCase = true) -> "Criticalhits"
                else -> "IGN"
            }

        }
    }
}