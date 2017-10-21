package com.matheusfroes.gamerguide.activities

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.matheusfroes.gamerguide.JogoAdicionadoRemovidoEvent
import com.matheusfroes.gamerguide.db.JogosDAO
import com.matheusfroes.gamerguide.db.ProgressoDAO
import com.matheusfroes.gamerguide.extra.AppRepository
import com.matheusfroes.gamerguide.models.Jogo
import com.matheusfroes.gamerguide.models.Noticia
import com.matheusfroes.gamerguide.models.ProgressoJogo
import org.greenrobot.eventbus.EventBus
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
    var jogos = MutableLiveData<List<Jogo>>()

    fun atualizarListaJogos() {
        jogos.value = jogosDAO.obterJogos()
    }


    fun removerJogo(jogoId: Long) {
        jogosDAO.remover(jogoId)
    }

    fun obterProgressoJogo(jogoId: Long): ProgressoJogo? =
            progressosDAO.obterProgressoPorJogo(jogoId)


    fun atualizarProgressoJogo(jogoId: Long, progressoJogo: ProgressoJogo) {
        progressosDAO.atualizarProgresso(progressoJogo, jogoId)
        atualizarListaJogos()
        EventBus.getDefault().postSticky(JogoAdicionadoRemovidoEvent())
    }

    fun marcarComoZerado(jogoId: Long) {
        val jogo = jogosDAO.obterJogo(jogoId)
        if (jogo?.progresso != null) {
            jogo.progresso.zerado = true
            progressosDAO.atualizarProgresso(jogo.progresso, jogoId)
            atualizarListaJogos()
        }
        EventBus.getDefault().postSticky(JogoAdicionadoRemovidoEvent())
    }

    fun atualizarFeed() {
        doAsync {
            val result = repository.atualizarFeed(app)
            noticias.postValue(result)
        }
    }
}