package com.matheusfroes.gamerguide.activities

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.matheusfroes.gamerguide.db.PlataformasDAO
import com.matheusfroes.gamerguide.extra.AppRepository
import com.matheusfroes.gamerguide.models.Jogo
import com.matheusfroes.gamerguide.normalizarDadosJogo
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by matheusfroes on 29/09/2017.
 */
class AdicionarJogosViewModel(application: Application) : AndroidViewModel(application) {
    var listaPesquisas = MutableLiveData<List<Jogo>>().apply { value = mutableListOf() }
    private val repository = AppRepository()

    fun pesquisarJogos(query: String) {
        doAsync {
            val result = repository.pesquisarJogos(query)

            val jogos = result.map { normalizarDadosJogo(it, PlataformasDAO(getApplication())) }

            uiThread { listaPesquisas.value = jogos }
        }
    }

    fun obterLancamentos() {
        doAsync {
            val result = repository.obterLancamentos()

            val jogos = result.map { normalizarDadosJogo(it, PlataformasDAO(getApplication())) }

            uiThread { listaPesquisas.value = jogos }
        }
    }
}
