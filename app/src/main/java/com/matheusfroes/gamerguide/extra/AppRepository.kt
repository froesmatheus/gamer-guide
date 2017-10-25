package com.matheusfroes.gamerguide.extra

import android.content.Context
import android.util.Log
import com.matheusfroes.gamerguide.adicionarSchemaUrl
import com.matheusfroes.gamerguide.api.ApiService
import com.matheusfroes.gamerguide.db.FonteNoticiasDAO
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

    fun atualizarFeed(context: Context): MutableList<Noticia> {
        val fontesDAO = FonteNoticiasDAO(context)
        val fontes = fontesDAO.obterFonteNoticias(ativos = true).map { it.website }
        val noticias = mutableSetOf<Noticia>()

        fontes.map {
            PkRSS.with(context).load(it).skipCache().safe(true).callback(object : Callback {
                override fun onLoadFailed() {
                    Log.d("GAMERGUIDE", "onLoadFailed() $it")
                }

                override fun onPreload() {
                    Log.d("GAMERGUIDE", "onPreload() $it")
                }

                override fun onLoaded(newArticles: MutableList<Article>) {}
            }).get()
        }.forEach { noticias.addAll(extrairNoticias(it)) }

        val lista = noticias.toMutableList()

        lista.sortByDescending { it.dataPublicacao }

        return lista
    }

    private fun extrairNoticias(newArticles: MutableList<Article>): MutableList<Noticia> {
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

            Noticia(article.title, adicionarSchemaUrl(imagemNoticia!!), article.source.toString(), article.date, website)
        }.toMutableList()
    }

}