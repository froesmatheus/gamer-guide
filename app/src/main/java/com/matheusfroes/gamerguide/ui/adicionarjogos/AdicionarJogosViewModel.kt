package com.matheusfroes.gamerguide.ui.adicionarjogos

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.extra.AppRepository
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AdicionarJogosViewModel(application: Application) : AndroidViewModel(application) {
    var listaPesquisas = MutableLiveData<List<Game>>().apply { value = mutableListOf() }
    var nextPageId = MutableLiveData<String>()

    private val repository = AppRepository()

    fun pesquisarJogos(query: String = "", nextPage: String = "") {
        doAsync {
            val result = repository.pesquisarJogos(query, nextPage)

            val jogos = result.first.map { normalizarDadosJogo(it, PlataformasDAO(getApplication())) }

            uiThread {
                nextPageId.value = result.second
                listaPesquisas.value = jogos
            }
        }
    }
}
