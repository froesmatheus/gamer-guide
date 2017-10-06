package com.matheusfroes.gamerguide.activities

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.matheusfroes.gamerguide.db.JogosDAO
import com.matheusfroes.gamerguide.db.ProgressoDAO
import com.matheusfroes.gamerguide.extra.AppRepository
import com.matheusfroes.gamerguide.models.Jogo
import com.matheusfroes.gamerguide.models.Noticia
import org.jetbrains.anko.doAsync

/**
 * Created by matheusfroes on 29/09/2017.
 */
class TelaPrincipalViewModel(val app: Application) : AndroidViewModel(app) {
    private val repository = AppRepository()

    var fragmentAtual = MutableLiveData<Int>().apply { value = 2 }
    var noticias = MutableLiveData<MutableList<Noticia>>()
    private val jogosDAO: JogosDAO by lazy { JogosDAO(app) }
    private val progressosDAO: ProgressoDAO by lazy { ProgressoDAO(app) }

    var jogosNaoTerminados = MutableLiveData<List<Jogo>>()
    var jogosZerados = MutableLiveData<List<Jogo>>()

    fun getJogosNaoTerminados() {
        jogosNaoTerminados.value = jogosDAO.obterJogosPorStatus(zerados = false)
    }

    fun getJogosZerados() {
        jogosZerados.value = jogosDAO.obterJogosPorStatus(zerados = true)
    }

    fun removerJogo(jogoId: Long) {
        jogosDAO.remover(jogoId)
        jogosNaoTerminados.postValue(jogosDAO.obterJogosPorStatus(zerados = false))
    }

    fun marcarComoZerado(jogoId: Long) {
        val jogo = jogosDAO.obterJogo(jogoId)
        jogo?.progresso?.apply {
            jogo.progresso.zerado = true
            progressosDAO.atualizarProgresso(jogo.progresso, jogoId)
            jogosNaoTerminados.postValue(jogosDAO.obterJogosPorStatus(zerados = true))
        }
    }

    fun atualizarFeed() {
        doAsync {
            val result = repository.atualizarFeed(app)
            noticias.postValue(result)
        }
    }
}