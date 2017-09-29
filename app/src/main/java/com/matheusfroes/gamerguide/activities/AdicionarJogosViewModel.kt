package com.matheusfroes.gamerguide.activities

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.AppRepository
import com.matheusfroes.gamerguide.models.Jogo
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by matheusfroes on 29/09/2017.
 */
class AdicionarJogosViewModel : ViewModel() {
    var listaPesquisas = MutableLiveData<List<Jogo>>().apply { value = mutableListOf() }
    private val repository = AppRepository()

    fun pesquisarJogos(query: String) {
        doAsync {
            val result = repository.pesquisarJogos(query)

            uiThread { listaPesquisas.value = result }
        }

    }
}
