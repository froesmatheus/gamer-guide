package com.matheusfroes.gamerguide.extra

import android.content.Context
import com.matheusfroes.gamerguide.adicionarSchemaUrl
import com.matheusfroes.gamerguide.api.IGDBService
import com.matheusfroes.gamerguide.models.GameResponse
import com.matheusfroes.gamerguide.models.Noticia
import com.pkmmte.pkrss.Article
import com.pkmmte.pkrss.Callback
import com.pkmmte.pkrss.PkRSS
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by matheusfroes on 29/09/2017.
 */
class AppRepository {
    private val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(IGDBService.URL_BASE)
            .build()

    private val service: IGDBService by lazy {
        retrofit.create(IGDBService::class.java)
    }

    fun pesquisarJogos(query: String = "", nextPage: String = ""): Pair<MutableList<GameResponse>, String> {
        val listaJogos = mutableListOf<GameResponse>()

        val call = if (nextPage.isEmpty()) {
            service.pesquisar(query)
        } else {
            service.proximaPagina(nextPage)
        }

        val response = call.execute()

        var nextPageId: String? = null
        if (response.isSuccessful) {
            nextPageId = response.headers()["X-Next-Page"]

            val listaResponse = response.body()

            listaResponse?.let { listaJogos.addAll(it) }
        }

        return Pair(listaJogos, nextPageId!!)
    }

    fun atualizarFeed(context: Context): MutableList<Noticia> {
        val fontes = listOf("http://www.eurogamer.pt/?format=rss", "https://criticalhits.com.br/feed/")
        val noticias = mutableListOf<Noticia>()

        fontes.map {
            PkRSS.with(context).load(it).skipCache().callback(object : Callback {
                override fun onLoadFailed() {}

                override fun onPreload() {}

                override fun onLoaded(newArticles: MutableList<Article>) {}
            }).get()
        }.forEach { noticias.addAll(extrairNoticias(it)) }

        noticias.sortByDescending { it.dataPublicacao }

        return noticias
    }

    private fun extrairNoticias(newArticles: MutableList<Article>): MutableList<Noticia> {
        return newArticles.map { article ->
            val imagemNoticia: String? = if (article.enclosure == null) {
                article.image?.toString()
            } else {
                article.enclosure.url
            }

            val website = when {
                article.source.host.contains("kotaku", ignoreCase = true) -> "Kotaku"
                article.source.host.contains("tecmundo", ignoreCase = true) -> "Tecmundo"
                article.source.host.contains("eurogamer", ignoreCase = true) -> "EuroGamer"
                article.source.host.contains("criticalhits", ignoreCase = true) -> "Criticalhits"
                else -> "IGN"
            }

            Noticia(article.title, adicionarSchemaUrl(imagemNoticia!!), article.source.toString(), article.date, website)
        }.toMutableList()
    }

}