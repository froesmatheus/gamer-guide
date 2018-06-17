package com.matheusfroes.gamerguide.ui.gamelists

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.matheusfroes.gamerguide.data.model.GameList

class DetalhesListaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: ListasDAO by lazy {
        ListasDAO(application)
    }

    private var lista = MutableLiveData<GameList>()

    fun getLista(listaId: Int): GameList? = dao.obterLista(listaId)

    fun excluirLista(listaId: Int) {
        dao.excluirLista(listaId)
    }
}