package com.matheusfroes.gamerguide.extra

import android.content.Context
import android.util.Log
import com.matheusfroes.gamerguide.adicionarSchemaUrl
import com.matheusfroes.gamerguide.data.db.FonteNoticiasDAO
import com.matheusfroes.gamerguide.data.models.Noticia
import com.pkmmte.pkrss.Article
import com.pkmmte.pkrss.Callback
import com.pkmmte.pkrss.PkRSS

class AppRepository {

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