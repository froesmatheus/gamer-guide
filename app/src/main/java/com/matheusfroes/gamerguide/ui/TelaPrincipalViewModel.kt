package com.matheusfroes.gamerguide.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.matheusfroes.gamerguide.JogoAdicionadoRemovidoEvent
import com.matheusfroes.gamerguide.data.db.JogosDAO
import com.matheusfroes.gamerguide.data.db.ProgressoDAO
import com.matheusfroes.gamerguide.data.models.Jogo
import com.matheusfroes.gamerguide.data.models.Noticia
import com.matheusfroes.gamerguide.data.models.ProgressoJogo
import com.matheusfroes.gamerguide.extra.AppRepository
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync

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

    fun alterarStatusJogo(jogoId: Long, zerado: Boolean) {
        val jogo = jogosDAO.obterJogoPorFormaCadastro(jogoId)
        if (jogo?.progresso != null) {
            jogo.progresso.zerado = zerado
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