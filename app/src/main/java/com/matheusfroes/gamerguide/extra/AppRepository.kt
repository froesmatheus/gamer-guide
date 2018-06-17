package com.matheusfroes.gamerguide.extra

import com.matheusfroes.gamerguide.adicionarSchemaUrl
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.network.ApiService
import com.matheusfroes.gamerguide.network.data.GameResponse
import com.pkmmte.pkrss.Article
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppRepository {
    private val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ApiService.URL_BASE)
            .build()

    private val service: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    fun pesquisarJogos(query: String = "", nextPage: String = ""): Pair<MutableList<GameResponse>, String> {
        val listaJogos = mutableListOf<GameResponse>()

        val call = if (nextPage.isEmpty()) {
            service.pesquisarJogos(query)
        } else {
            service.proximaPaginaJogos(nextPage)
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

//    fun atualizarFeed(context: Context): MutableList<News> {
//        val fontesDAO = FonteNoticiasDAO(context)
//        val fontes = fontesDAO.obterFonteNoticias(ativos = true).map { it.website }
//        val noticias = mutableSetOf<News>()
//
//        fontes.map {
//            PkRSS.with(context).load(it).skipCache().safe(true).callback(object : Callback {
//                override fun onLoadFailed() {
//                    Log.d("GAMERGUIDE", "onLoadFailed() $it")
//                }
//
//                override fun onPreload() {
//                    Log.d("GAMERGUIDE", "onPreload() $it")
//                }
//
//                override fun onLoaded(newArticles: MutableList<Article>) {}
//            }).get()
//        }.forEach { noticias.addAll(extrairNoticias(it)) }
//
//        val lista = noticias.toMutableList()
//
//        lista.sortByDescending { it.publishDate }
//
//        return lista
//    }

    private fun extrairNoticias(newArticles: MutableList<Article>): MutableList<News> {
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
        }.toMutableList()
    }

}