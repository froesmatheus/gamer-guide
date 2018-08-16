package com.matheusfroes.gamerguide.ui.addgames

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.matheusfroes.gamerguide.data.db.PlataformasDAO
import com.matheusfroes.gamerguide.data.models.Jogo
import com.matheusfroes.gamerguide.extra.AppRepository
import com.matheusfroes.gamerguide.normalizarDadosJogo
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AdicionarJogosViewModel(application: Application) : AndroidViewModel(application) {
    var listaPesquisas = MutableLiveData<List<Jogo>>().apply { value = mutableListOf() }
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
