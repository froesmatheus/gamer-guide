package com.matheusfroes.gamerguide.data.mapper

import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.data.source.local.NewsLocalSource
import com.matheusfroes.gamerguide.extra.adicionarSchemaUrl
import com.pkmmte.pkrss.Article
import javax.inject.Inject

class NewsMapper @Inject constructor(
        private val newsLocalSource: NewsLocalSource
) {

    fun map(newArticles: MutableList<Article>): List<News> {
        return newArticles.map { article ->
            val imagemNoticia: String? = if (article.enclosure == null) {
                article.image?.toString()
            } else {
                article.enclosure.url
            }

            val sourceName = extractSourceName(article.source.host)
            if (sourceName == "Xbox Power") {
                print(sourceName)
            }
            val source = newsLocalSource.getNewsSource(sourceName)

            val news = News(
                    title = article.title,
                    image = adicionarSchemaUrl(imagemNoticia!!),
                    url = article.source.toString(),
                    publishDate = article.date,
                    sourceId = source?.id)

            news.source = source

            news
        }
    }


    private fun extractSourceName(host: String) = when {
        host.contains("eurogamer", ignoreCase = true) -> "eurogamer"
        host.contains("xboxpower", ignoreCase = true) -> "xboxpower"
        host.contains("playstation", ignoreCase = true) -> "playstation"
        host.contains("criticalhits", ignoreCase = true) -> "criticalhits"
        else -> "eurogamer"
    }
}