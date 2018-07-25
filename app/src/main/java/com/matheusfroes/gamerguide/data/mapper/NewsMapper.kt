package com.matheusfroes.gamerguide.data.mapper

import com.matheusfroes.gamerguide.adicionarSchemaUrl
import com.matheusfroes.gamerguide.data.model.News
import com.pkmmte.pkrss.Article

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
                    else -> "IGN"
                }

                News(article.title, adicionarSchemaUrl(imagemNoticia!!), article.source.toString(), article.date, website)
            }
        }
    }
}